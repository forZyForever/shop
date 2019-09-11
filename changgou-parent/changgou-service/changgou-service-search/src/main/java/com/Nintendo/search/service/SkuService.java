package com.Nintendo.search.service;

import java.util.Map;

/**
 * @Package: com.Nintendo.search.service
 * @Author: ZZM
 * @Date: Created in 2019/8/24 10:36
 * @Address:CN.SZ
 **/
public interface SkuService {
    /***
     * 导入SKU数据
     */
    void importSku();

    /**
     * 查询索引库数据
     * @param searchMap
     * @return
     */
    Map searchByEs(Map<String,String> searchMap);
}
