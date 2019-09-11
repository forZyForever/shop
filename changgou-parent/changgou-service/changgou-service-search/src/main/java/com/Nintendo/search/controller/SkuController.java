package com.Nintendo.search.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Package: com.Nintendo.search.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/24 10:39
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuController {
    @Autowired
    private SkuService skuService;

    /**
     * 导入数据到索引库中去
     * @return
     */
    @GetMapping("/import")
    public Result search() {
        skuService.importSku();
        return new Result(true, StatusCode.OK, "导入数据到索引库成功");
    }

    /**
     * 查询es数据(标签分类和列表)
     * @param searchMap
     * @return
     */
    @GetMapping
    public Map searchByEs(@RequestParam(required = false) Map searchMap){
        Object pageNum = searchMap.get("pageNum");
        if (null==pageNum){
            searchMap.put("pageNum","1");
        }
        if (pageNum instanceof Integer){
            searchMap.put("pageNum",pageNum.toString());
        }
        return skuService.searchByEs(searchMap);
    }
}
