package com.Nintendo.goods.feign;

import com.Nintendo.entity.Result;
import com.Nintendo.goods.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Package: com.Nintendo.goods.feign
 * @Author: ZZM
 * @Date: Created in 2019/8/28 10:22
 * @Address:CN.SZ
 **/
@FeignClient(name = "goods")
@RequestMapping("/category")
public interface CategoryFeign {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Category> findById(@PathVariable(value = "id")Integer id);
}
