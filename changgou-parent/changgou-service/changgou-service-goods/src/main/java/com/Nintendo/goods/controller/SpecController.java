package com.Nintendo.goods.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.goods.pojo.Spec;
import com.Nintendo.goods.service.SpecService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/20 17:07
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/spec")
@CrossOrigin
public class SpecController {
    @Autowired(required = false)
    private SpecService specService;
    @GetMapping
    public Result findAll() {
        List<Spec> specs = specService.findAll();
        return new Result(true, StatusCode.OK, "查询规格列表成功", specs);
    }

    /**
     * 添加模板
     *
     * @param spec
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Spec spec) {
        specService.add(spec);
        return new Result(true, StatusCode.OK, "新建规格成功");
    }

    /**
     * 条件插询
     *
     * @param spec
     * @return
     */
    @PostMapping("/search")
    public Result searchByTerm(@RequestBody Spec spec) {
        List<Spec> specs = specService.searchByTerm(spec);
        return new Result(true, StatusCode.OK, "条件查询成功", specs);
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
        PageInfo<Spec> pageInfo = specService.searchPageUp(page, size);
        return new Result(true, StatusCode.OK, "分页查询成功", pageInfo);
    }

    /**
     * 分页查询和条件查询
     *
     * @param page
     * @param size
     * @param spec
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result serachPageUpByTerm(@PathVariable(value = "page") Integer page, @PathVariable(value = "size") Integer size, @RequestBody Spec spec) {
        PageInfo<Spec> pageInfo = specService.serachPageUpByTerm(page, size, spec);
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
     Spec spec = specService.findById(id);
        return new Result(true, StatusCode.OK, "根据id查询规格成功", spec);
    }

    /**
     * 根据id进行修改
     *
     * @param id
     * @param spec
     * @return
     */
    @PutMapping("/{id}")
    public Result updateById(@PathVariable(value = "id") Integer id, @RequestBody Spec spec) {
        spec.setId(id);
        specService.updateById(spec);
        return new Result(true, StatusCode.OK, "根据id修改规格数据成功");
    }

    /**
     * 根据id进行删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable(value = "id") Integer id) {
        specService.deleteById(id);
        return new Result(true, StatusCode.OK, "根据id删除成功");
    }

    /**
     * 根据分类id查询规格列表
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result findByCategoryId(@PathVariable(value = "id")Integer id){
       List<Spec> specs= specService.findByCategoryId(id);
       return new Result(true,StatusCode.OK,"根据分类id查询规格列表成功",specs);
    }

}
