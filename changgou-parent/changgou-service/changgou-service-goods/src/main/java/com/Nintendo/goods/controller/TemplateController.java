package com.Nintendo.goods.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;;
import com.Nintendo.goods.pojo.Template;
import com.Nintendo.goods.service.TemplateService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/20 16:20
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/template")
@CrossOrigin
public class TemplateController {
    @Autowired(required = false)
    private TemplateService templateService;
    /**
     * 查询所有模板
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Template> templates = templateService.findAll();
        return new Result(true, StatusCode.OK, "查询模板列表成功", templates);
    }

    /**
     * 添加模板
     *
     * @param template
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Template template) {
        templateService.add(template);
        return new Result(true, StatusCode.OK, "新建模板成功");
    }

    /**
     * 条件插询
     *
     * @param template
     * @return
     */
    @PostMapping("/search")
    public Result searchByTerm(@RequestBody Template template) {
        List<Template> templates = templateService.searchByTerm(template);
        return new Result(true, StatusCode.OK, "条件查询成功", templates);
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
        PageInfo<Template> pageInfo = templateService.searchPageUp(page, size);
        return new Result(true, StatusCode.OK, "条件查询成功", pageInfo);
    }

    /**
     * 分页查询和条件查询
     *
     * @param page
     * @param size
     * @param template
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result serachPageUpByTerm(@PathVariable(value = "page") Integer page, @PathVariable(value = "size") Integer size, @RequestBody Template template) {
        PageInfo<Template> pageInfo = templateService.serachPageUpByTerm(page, size, template);
        return new Result(true, StatusCode.OK, "分页查询成功", pageInfo);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable(value = "id") Integer id) {
       Template template = templateService.findById(id);
        return new Result(true, StatusCode.OK, "根据id查询模板成功", template);
    }

    /**
     * 根据id进行修改
     *
     * @param id
     * @param template
     * @return
     */
    @PutMapping("/{id}")
    public Result updateById(@PathVariable(value = "id") Integer id, @RequestBody Template template) {
        template.setId(id);
        templateService.updateById(template);
        return new Result(true, StatusCode.OK, "根据id修改模板数据成功");
    }

    /**
     * 根据id进行删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable(value = "id") Integer id) {
        templateService.deleteById(id);
        return new Result(true, StatusCode.OK, "根据id删除成功");
    }

    /**
     * 根据分类id查询模板
     * @param id 分类id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result findByCategoryId(@PathVariable(value = "id")Integer id){
       Template template= templateService.findByCategoryId(id);
       return new Result(true,StatusCode.OK,"根据分类id查询模板成功",template);
    }
}
