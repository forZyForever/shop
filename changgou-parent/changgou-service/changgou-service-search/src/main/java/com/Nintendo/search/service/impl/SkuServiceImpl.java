package com.Nintendo.search.service.impl;


import com.Nintendo.goods.pojo.Sku;
import com.Nintendo.search.dao.SkuEsMapper;
import com.Nintendo.goods.feign.SkuFeign;
import com.Nintendo.search.pojo.SkuInfo;
import com.Nintendo.search.service.SkuService;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * @Package: com.Nintendo.search.service.impl
 * @Author: ZZM
 * @Date: Created in 2019/8/24 10:36
 * @Address:CN.SZ
 **/
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired(required = false)
    private SkuFeign skuFeign;
    @Autowired
    private SkuEsMapper skuEsMapper;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 把从数据库获得的sku列表
     * 通过feign的调用
     * 保存到es中去
     */
    @Override
    public void importSku() {
        //已通过审核,获得sku的列表
        List<Sku> skuList = skuFeign.findByStatus("1").getData();
        //List<Sku>转化为list<skuInfo>
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuList), SkuInfo.class);
        //spec:{"电视音响效果":"立体声","电视屏幕尺寸":"20英寸","尺码":"165"}
        //转成map设置specMap<String,object>属性,
        for (SkuInfo skuInfo : skuInfoList) {
            Map map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }
        //使用springData存入到es中去
        skuEsMapper.saveAll(skuInfoList);
    }

    /**
     * 根据name属性匹配
     * 关键字查询(keyword=关键字)
     * 商品分类显示(categoryName=商品名)
     * 品牌分类显示(brandName=品牌名)
     * 规格分类显示(spec.keyword=规格名)
     * category分类过滤
     * brand品牌过滤
     * spec_开头为规格过滤条件
     * price价格过滤
     * pageNum 当前页
     * pageSize 每页数据
     * sortField 要进行排序的字段
     * sortRule 排序的规则
     *
     * @param searchMap
     * @return
     */
    @Override
    public Map searchByEs(Map<String, String> searchMap) {
        //获取关键字
        String keyword = searchMap.get("keyword");
        //如果为空,设置默认值
        if (StringUtils.isEmpty(keyword)) {
            keyword = "华为";
        }
        //创建查询构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //商品分类
        categoryQuery(nativeSearchQueryBuilder, "skuCategoryGroup", "categoryName", 50);
        //品牌分类
        categoryQuery(nativeSearchQueryBuilder, "skuBrandGroup", "brandName", 50);
        //规格分类
        categoryQuery(nativeSearchQueryBuilder, "skuSpecGroup", "spec.keyword", 50);
        /**
         * 高亮显示
         */
        //设置高亮字段
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder().preTags("<em style=\"color:red\">").postTags("</em>"));

        /**匹配查询
         * matchQuery先分词在查询,关键字查询
         参数分别为要查询的字段,对应的值*/
        //nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", keyword));
        //搜索关键字的时候从这这些字段中去查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keyword, "name", "categoryName", "brandName"));
        /**过滤查询
         * 前端category=分类;brand=品牌
         * 分类品牌的过滤
         */
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //商品分类过滤
        filterQuery(searchMap, boolQuery, "categoryName", "category");
        //品牌分类过滤
        filterQuery(searchMap, boolQuery, "brandName", "brand");
        //规格过滤
        for (String key : searchMap.keySet()) {
            if (key.startsWith("spec_")) {
                boolQuery.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key)));
            }
        }
        //价钱区间过滤
        String price = searchMap.get("price");
        if (!StringUtils.isEmpty(price)) {
            String[] split = price.split("-");
            if (!split[1].equals("*")) {//价格区间0-500
                boolQuery.filter(QueryBuilders.rangeQuery("price").from(split[0], true).to(split[1], true));
            } else {//价格区间>=500
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }
        }
        nativeSearchQueryBuilder.withFilter(boolQuery);
        /**
         * 分页查询
         */
        String pageNum1 = searchMap.get("pageNum");
        Integer pageNum = Integer.valueOf(pageNum1);
        Integer pageSize = 30;
        //arg0:当前页的页码, 第一页为0
        //arg1:指定当前页显示的行
        nativeSearchQueryBuilder.withPageable(PageRequest.of((pageNum - 1), pageSize));
        /**
         * 排序查询
         */
        //指定要排序的字段和排序规则
        String sortRule = searchMap.get("sortRule");
        String sortField = searchMap.get("sortField");
        if (!StringUtils.isEmpty(sortField) && (!StringUtils.isEmpty(sortRule))) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        //添加自定义结果集映射
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(nativeSearchQuery, SkuInfo.class, new SearchResultMapperImpl());

        //获取聚合结果,商品分类的列表
        List<String> categoryList = getBucketsResult(getStringTerms(skuInfos, "skuCategoryGroup"));
        //获取聚合结果,品牌分类的列表
        List<String> brandList = getBucketsResult(getStringTerms(skuInfos, "skuBrandGroup"));
        //获取聚合结果,规格分类的列表
        Map<String, Set<String>> specList = getMapList(getStringTerms(skuInfos, "skuSpecGroup"));
        Map map = getMap(skuInfos, categoryList, brandList, specList, pageNum);
        return map;
    }

    /**
     * 结果集封装
     *
     * @param skuInfos
     * @param categoryList
     * @param brandList
     * @param specList
     * @return
     */
    private Map getMap(AggregatedPage<SkuInfo> skuInfos, List<String> categoryList, List<String> brandList, Map<String, Set<String>> specList, Integer pageNum) {
        //关键字查询出来的skuInfo列表
        List<SkuInfo> skuInfoList = skuInfos.getContent();
        //总页数
        int totalPages = skuInfos.getTotalPages();
        //总条数
        long totalElements = skuInfos.getTotalElements();

        //获取结果,返回map
        Map map = new HashMap();
        map.put("totalPages", totalPages);
        map.put("total", totalElements);
        map.put("rows", skuInfoList);
        map.put("categoryList", categoryList);
        map.put("brandList", brandList);
        map.put("specList", specList);
        map.put("pageNum", pageNum);
        map.put("pageSize", 30);
        return map;
    }

    /**
     * 获得聚合函数
     *
     * @param skuInfos
     * @param groupName
     * @return
     */
    private StringTerms getStringTerms(AggregatedPage<SkuInfo> skuInfos, String groupName) {
        return (StringTerms) skuInfos.getAggregation(groupName);
    }

    /**
     * 过滤查询
     *
     * @param searchMap 前端参数map
     * @param boolQuery 过滤查询
     * @param field     进行过滤的字段
     * @param fieldKey  map的键(要进行过滤的值所对应的key)
     */
    private void filterQuery(Map<String, String> searchMap, BoolQueryBuilder boolQuery, String field, String fieldKey) {
        //通过map中的键获得值
        String value = searchMap.get(fieldKey);
        if (!StringUtils.isEmpty(value)) {
            //过滤字段,过滤的key值
            boolQuery.filter(QueryBuilders.termQuery(field, value));
        }
    }

    /**
     * 聚合查询
     *
     * @param nativeSearchQueryBuilder 构建查询对象
     * @param name                     要进行聚合分组后的别名
     * @param field                    要聚合的字段名
     * @param size                     指定查询结果的数量
     */
    private void categoryQuery(NativeSearchQueryBuilder nativeSearchQueryBuilder, String name, String field, int size) {
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(name).field(field).size(size));
    }

    /**
     * 传入聚合结果封装到map中去
     *
     * @param skuSpecGroup
     */
    private Map<String, Set<String>> getMapList(StringTerms skuSpecGroup) {
        Map<String, Set<String>> specMap = new HashMap<>();
        Set<String> setSpecList = new HashSet<>();
        if (null != skuSpecGroup) {
            for (StringTerms.Bucket bucket : skuSpecGroup.getBuckets()) {
                //获得每条结果集{"手机屏幕尺寸":"5寸","网络":"联通2G","颜色":"红"}
                String keyAsString = bucket.getKeyAsString();
                //转成map
                Map<String, String> map = JSON.parseObject(keyAsString, Map.class);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    //转成map<string,set<string>>
                    //key为规格名,value为set集合{"手机屏幕尺寸":"5寸"."6.6寸"}
                    //specMap中去获取一个规格名称,再去找该规格对应的值如:手机屏幕尺寸=5寸
                    setSpecList = specMap.get(key);
                    if (null == specMap.get(key)) {
                        setSpecList = new HashSet<>();
                    }
                    setSpecList.add(value);
                    specMap.put(key, setSpecList);
                }
            }
        }
        return specMap;
    }

    /**
     * 传入聚合结果封装到list里面去
     *
     * @param StringTerms
     * @return
     */
    private List<String> getBucketsResult(StringTerms StringTerms) {
        List<String> categoryList = new ArrayList<>();
        if (null != StringTerms) {
            for (StringTerms.Bucket bucket : StringTerms.getBuckets()) {
                //商品分类的数据
                String keyAsString = bucket.getKeyAsString();
                categoryList.add(keyAsString);
            }
        }
        return categoryList;
    }

}
