package com.Nintendo.goods.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.goods.pojo.Para;
import com.Nintendo.goods.service.ParaService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/20 17:29
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/para")
@CrossOrigin
public class ParaController {
    @Autowired(required = false)
    private ParaService paraService;

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Para> paras = paraService.findAll();
        return new Result(true, StatusCode.OK, "查询规格列表成功", paras);
    }

    /**
     * 添加模板
     *
     * @param para
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Para para) {
        paraService.add(para);
        return new Result(true, StatusCode.OK, "新建规格成功");
    }

    /**
     * 条件插询
     *
     * @param para
     * @return
     */
    @PostMapping("/search")
    public Result searchByTerm(@RequestBody Para para) {
        List<Para> paras = paraService.searchByTerm(para);
        return new Result(true, StatusCode.OK, "条件查询成功", paras);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result searchPageUp(@PathVariable(value = "page") Integer page, @PathVariable(value = "size") Integer size) {
        PageInfo<Para> pageInfo = paraService.searchPageUp(page, size);
        return new Result(true, StatusCode.OK, "分页查询成功", pageInfo);
    }

    /**
     * 分页查询和条件查询
     *
     * @param page
     * @param size
     * @param para
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result serachPageUpByTerm(@PathVariable(value = "page") Integer page, @PathVariable(value = "size") Integer size, @RequestBody Para para) {
        PageInfo<Para> pageInfo = paraService.serachPageUpByTerm(page, size, para);
        return new Result(true, StatusCode.OK, "分页条件查询成功", pageInfo);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable(value = "id") Integer id) {
        Para para = paraService.findById(id);
        return new Result(true, StatusCode.OK, "根据id查询规格成功", para);
    }

    /**
     * 根据id进行修改
     *
     * @param id
     * @param para
     * @return
     */
    @PutMapping("/{id}")
    public Result updateById(@PathVariable(value = "id") Integer id, @RequestBody Para para) {
        para.setId(id);
        paraService.updateById(para);
        return new Result(true, StatusCode.OK, "根据id修改规格数据成功");
    }

    /**
     * 根据id进行删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable(value = "id") Integer id) {
        paraService.deleteById(id);
        return new Result(true, StatusCode.OK, "根据id删除成功");
    }

    /**
     * 根据分类id查询参数列表
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result findByCategoryId(@PathVariable(value = "id") Integer id){
        List<Para> paras=paraService.findByCategoryId(id);
        return new Result(true,StatusCode.OK,"根据分类id查询参数列表成功",paras);
    }
}
