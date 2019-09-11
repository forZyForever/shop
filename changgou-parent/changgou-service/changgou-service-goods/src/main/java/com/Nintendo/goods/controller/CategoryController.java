package com.Nintendo.goods.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.goods.pojo.Category;
import com.Nintendo.goods.service.CategoryService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/20 15:10
 * @Address:CN.SZ
 **/
@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {
    @Autowired(required = false)
    private CategoryService categoryService;

    /**
     * 查询所有分类
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Category> categories = categoryService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",categories);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable(value = "id")Integer id){
        Category category = categoryService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",category);
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Category category){
        categoryService.add(category);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 更新分类
     * @param id
     * @param category
     * @return
     */
    @PutMapping("/{id}")
    public Result updateBrand(@PathVariable(value = "id")Integer id,@RequestBody Category category){
        category.setId(id);
        categoryService.updateCategory(category);
        return new Result(true,StatusCode.OK,"更新成功");
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value = "id") Integer id){
        categoryService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 条件查询
     * @param category
     * @return
     */
    @PostMapping("/search")
    public Result queryByTerm(@RequestBody Category category){
        List<Category> categories = categoryService.queryByTerm(category);
        return new Result(true, StatusCode.OK,"查询成功",categories);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result findPage(@PathVariable(value="page") int page, @PathVariable(value="size") int size){
        PageInfo<Category> pageInfo=categoryService.findPage(page,size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /**
     * 分页和条件查询
     * @param category
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result findSearch(@RequestBody Category category,@PathVariable(value="page") int page,@PathVariable(value="size") int size){
        PageInfo<Category>  pageInfo= categoryService.findSearch(category,page,size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /**
     *根据父节点查询子节点
     * @param pid
     * @return
     */
    @GetMapping("/list/{pid}")
    public Result findByPrantId(@PathVariable(value = "pid")Integer pid){
       List<Category> categories= categoryService.findByPrantId(pid);
       return new Result(true,StatusCode.OK,"查询父节点成功",categories);
    }

}
