package com.Nintendo.search.dao;

import com.Nintendo.goods.pojo.Sku;
import com.Nintendo.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Package: com.Nintendo.search.dao
 * @Author: ZZM
 * @Date: Created in 2019/8/24 10:34
 * @Address:CN.SZ
 **/
//接口主要用于索引数据操作，主要使用它来实现将数据导入到ES索引库中
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
