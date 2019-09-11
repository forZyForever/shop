package com.Nintendo.order.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.entity.TokenDecode;
import com.Nintendo.order.pojo.OrderItem;
import com.Nintendo.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * @Package: com.Nintendo.order.controller
 * @Author: ZZM
 * @Date: Created in 2019/9/1 17:34
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private TokenDecode tokenDecode;
    /**
     *展示购物车
     * @param id 购买的sku的Id
     * @param num 购买的数量
     * @return
     */
    @GetMapping("/add")
    public Result add(Long id, Integer num) {

        String username = tokenDecode.getUserInfo().get("username");
        System.out.println(username);
        cartService.add(id,num,username);
        return new Result(true, StatusCode.OK, "添加购物车成功");
    }

    /**
     * 查询购物车
     * @return
     */
    @GetMapping("/list")
    public Result<List<OrderItem>> list(){
        String username = tokenDecode.getUserInfo().get("username");
        List<OrderItem>   orderItems= cartService.list(username);
        return new Result<List<OrderItem>>(true,StatusCode.OK,"展示购物车成功",orderItems);
    }
}
