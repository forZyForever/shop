package com.Nintendo.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @Package: com.Nintendo.search.feign
 * @Author: ZZM
 * @Date: Created in 2019/8/27 9:38
 * @Address:CN.SZ
 **/

@RequestMapping("/search")
@FeignClient(name = "search")
public interface SkuFeign {
    /**
     * 查询es数据(标签分类和列表)
     * @param searchMap
     * @return
     */
   @GetMapping
    Map searchByEs(@RequestParam(required = false) Map searchMap);
}
