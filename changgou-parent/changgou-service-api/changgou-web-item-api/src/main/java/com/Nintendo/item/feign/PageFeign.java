package com.Nintendo.item.feign;

import com.Nintendo.entity.Result;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Package: com.Nintendo.item.feign
 * @Author: ZZM
 * @Date: Created in 2019/8/29 18:43
 * @Address:CN.SZ
 **/
@FeignClient(name = "item")
@RequestMapping("/page")
public interface PageFeign {
    /**
     * 生成静态页面
     * @param id
     * @return
     */
    @GetMapping("/createHtml/{id}")
    Result createHtml(@PathVariable(name = "id") Long id);
    /**
     * 删除静态页面
     * @param id
     * @return
     */
    @DeleteMapping("/deleteHtml/{id}")
    Result deleteHtml(@PathVariable(name = "id") Long id);
}
