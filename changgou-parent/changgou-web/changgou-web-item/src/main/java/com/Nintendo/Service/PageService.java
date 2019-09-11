package com.Nintendo.Service;

/**
 * @Package: com.Nintendo.Service
 * @Author: ZZM
 * @Date: Created in 2019/8/28 10:43
 * @Address:CN.SZ
 **/
public interface PageService {
    /**
     * 根据商品的spuId 生成静态页
     * @param spuId
     */
    void createHtml(Long spuId);

    /**
     * 根据商品的spuId删除静态页
     * @param spuId
     */
    void deleteHtml(Long spuId);
}
