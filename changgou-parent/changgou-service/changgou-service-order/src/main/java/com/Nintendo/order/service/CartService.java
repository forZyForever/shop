package com.Nintendo.order.service;

import com.Nintendo.order.pojo.OrderItem;

import java.util.List;

/**
 * @Package: com.Nintendo.order.service
 * @Author: ZZM
 * @Date: Created in 2019/9/1 17:44
 * @Address:CN.SZ
 **/
public interface CartService {
    /**
     * 添加购物车
     * @param id  sku的商品id
     * @param num  商品的数量
     * @param username 用户名
     */
    void add(Long id, Integer num, String username);

    /**
     * 展示购物车
     * @param username 用户名
     * @return
     */
    List<OrderItem> list(String username);
}
