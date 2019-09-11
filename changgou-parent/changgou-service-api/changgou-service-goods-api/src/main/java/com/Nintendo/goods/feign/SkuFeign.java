package com.Nintendo.goods.feign;

import com.Nintendo.entity.Result;
import com.Nintendo.goods.pojo.Sku;
import com.Nintendo.order.pojo.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.feign
 * @Author: ZZM
 * @Date: Created in 2019/8/28 23:36
 * @Address:CN.SZ
 **/
@FeignClient(value = "goods")
@RequestMapping("/sku")
public interface SkuFeign {
    /**
     * 查询符合条件的状态的SKU的列表
     *
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable(name = "status") String status);

    /***
     * 多条件搜索品牌数据
     * @param sku
     * @return
     */
    @PostMapping(value = "/search")
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable Long id);

    /**
     * 减库存
     *
     * @param orderItem
     * @return
     */
    @PostMapping(value = "/decr/count")
    Result decrCount(@RequestBody OrderItem orderItem);
}
