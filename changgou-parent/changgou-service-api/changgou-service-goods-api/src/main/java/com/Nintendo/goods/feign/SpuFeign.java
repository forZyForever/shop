package com.Nintendo.goods.feign;

import com.Nintendo.entity.Result;
import com.Nintendo.goods.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Package: com.Nintendo.goods.feign
 * @Author: ZZM
 * @Date: Created in 2019/8/28 10:25
 * @Address:CN.SZ
 **/
@FeignClient(name = "goods")
@RequestMapping("/spu")
public interface SpuFeign {
    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Spu> findById(@PathVariable Long id);
}
