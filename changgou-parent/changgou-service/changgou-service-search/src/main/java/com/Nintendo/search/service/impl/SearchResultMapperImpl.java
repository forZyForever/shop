package com.Nintendo.search.service.impl;

import com.Nintendo.search.pojo.SkuInfo;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义映射结果集
 * @Package: com.Nintendo.goods.service.impl
 * @Author: ZZM
 * @Date: Created in 2019/8/26 15:16
 * @Address:CN.SZ
 **/
public class SearchResultMapperImpl implements SearchResultMapper {
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse Response, Class<T> aClass, Pageable pageable) {

        //创建当前页的记录集合对象
        List<T> content=new ArrayList<T>();
        //如果结果集为空
        if (null==Response.getHits()&&Response.getHits().getTotalHits()<=0){
        return new AggregatedPageImpl<T>(content);
        }
        SearchHits hits = Response.getHits();
        for (SearchHit searchHit : hits) {
            //获取每一行的数据
            String sourceAsString = searchHit.getSourceAsString();
            SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);
            //key为高亮的字段名,value为高亮的字段对应的数据集合,高亮字段集合
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField nameHigh = highlightFields.get("name");
            if (null!=nameHigh){
                StringBuffer stringBuffer=new StringBuffer();
                for (Text text : nameHigh.getFragments()) {
                    String string = text.string();
                    stringBuffer.append(string);
                }
                //设置名字中高亮的标签进去
                skuInfo.setName(stringBuffer.toString());
            }
            content.add((T) skuInfo);
        }
        //创建分页的对象
        //获取总记录数
        long totalHits = Response.getHits().getTotalHits();
        //获取所有聚合函数的结果
        Aggregations aggregations = Response.getAggregations();
        //深度分页的id
        String scrollId = Response.getScrollId();
        return new AggregatedPageImpl<T>(content,pageable, totalHits,aggregations,scrollId);
    }
}
