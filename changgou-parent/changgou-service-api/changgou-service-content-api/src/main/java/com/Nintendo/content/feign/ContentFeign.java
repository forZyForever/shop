package com.Nintendo.content.feign;

import com.Nintendo.content.pojo.Content;
import com.Nintendo.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/18 13:58
 *****/
@FeignClient(name="content")
@RequestMapping("/content")
public interface ContentFeign {
    /**
     * 根据分类id查询广告列表
     * @param id
     * @return
     */
    @GetMapping("/list/category/{id}")
   Result<List<Content>> findCategoryById(@PathVariable(value = "id") long id );

}