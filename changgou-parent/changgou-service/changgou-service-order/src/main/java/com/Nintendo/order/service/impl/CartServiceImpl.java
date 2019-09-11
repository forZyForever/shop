package com.Nintendo.order.service.impl;


import com.Nintendo.goods.feign.SkuFeign;
import com.Nintendo.goods.feign.SpuFeign;
import com.Nintendo.goods.pojo.Sku;
import com.Nintendo.goods.pojo.Spu;
import com.Nintendo.order.pojo.OrderItem;
import com.Nintendo.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Package: com.Nintendo.order.service.impl
 * @Author: ZZM
 * @Date: Created in 2019/9/1 17:45
 * @Address:CN.SZ
 **/
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加购物车
     *
     * @param id       sku的商品id
     * @param num      商品的数量
     * @param username 用户名
     */
    @Override
    public void add(Long id, Integer num, String username) {
        //当选择的商品小于1时,删除商品订单
        if (num <= 0) {
            redisTemplate.boundHashOps("Cart_" + username).delete(id);
            return;
        }
        OrderItem orderItem = new OrderItem();
        //根据sku的id查询spu的信息
        Sku sku = skuFeign.findById(id).getData();
        if (null != sku) {
            //封装order-item
            Long spuId = sku.getSpuId();
            Spu spu = spuFeign.findById(spuId).getData();
            orderItem.setCategoryId1(spu.getCategory1Id());
            orderItem.setCategoryId2(spu.getCategory2Id());
            orderItem.setCategoryId3(spu.getCategory3Id());
            orderItem.setSpuId(spuId);
            orderItem.setSkuId(id);
            orderItem.setName(sku.getName());
            orderItem.setPrice(sku.getPrice());
            orderItem.setNum(num);
            orderItem.setMoney(sku.getPrice() * num);
            orderItem.setPayMoney(sku.getPrice() * num);
            orderItem.setImage(sku.getImage());
            //存入redis,hash格式,key为用户名,field为商品id,orderItem为商品详情
            redisTemplate.boundHashOps("Cart_" + username).put(id, orderItem);
        }

    }

    /**
     * 查询购物车
     *
     * @param username 用户名
     * @return
     */
    @Override
    public List<OrderItem> list(String username) {
        List<OrderItem> orderItemList = redisTemplate.boundHashOps("Cart_" + username).values();
        return orderItemList;
    }
}
