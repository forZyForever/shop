package com.Nintendo.controller;


import com.Nintendo.Service.PageService;
import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Package: com.Nintendo.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/28 10:19
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired(required = false)
    private PageService pageService;

    /**
     * 生成静态页面
     * @param id
     * @return
     */
    @GetMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(name = "id") Long id) {
        pageService.createHtml(id);
        return new Result(true, StatusCode.OK, "生成静态页面成功");
    }
    /**
     * 删除静态页面
     * @param id
     * @return
     */
    @DeleteMapping("/deleteHtml/{id}")
    public Result deleteHtml(@PathVariable(name = "id") Long id) {
        pageService.deleteHtml(id);
        return new Result(true, StatusCode.OK, "删除静态页面成功");
    }
}
