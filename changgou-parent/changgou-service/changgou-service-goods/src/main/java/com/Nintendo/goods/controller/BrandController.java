package com.Nintendo.goods.controller;


import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.goods.pojo.Brand;
import com.Nintendo.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Package: com.Nintendo.goods
 * Author: ZZM
 * Date: Created in 2019/8/15 23:50
 **/
@RestController
@RequestMapping("/brand")
@CrossOrigin
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌列表
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Brand> brands = brandService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",brands);
    }
    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable(value = "id")Integer id){
        Brand brand = brandService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",brand);
    }
    /**
     * 添加品牌
     */
    @PostMapping
    public Result addBrand(@RequestBody Brand brand){
        brandService.addBrand(brand);
        return new Result(true,StatusCode.OK,"添加成功");
    }
    /**
     * 更新品牌
     */
    @PutMapping("/{id}")
    public Result updateBrand(@PathVariable(value = "id")Integer id,@RequestBody Brand brand){
        brand.setId(id);
        brandService.updateBrand(brand);
        return new Result(true,StatusCode.OK,"更新成功");
    }
    /**
     * 删除品牌
     */
    @DeleteMapping("/{id}")
    public Result deleteBrand(@PathVariable(value = "id") Integer id){
        brandService.deleteBrand(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }
    /**
     * 条件查询查询
     */
    @PostMapping("/search")
    public Result queryByTerm(Brand brand){
        List<Brand> brands = brandService.queryByTerm(brand);
        return new Result(true,StatusCode.OK,"查询成功",brands);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result findPage(@PathVariable(value="page") int page,@PathVariable(value="size") int size){
        PageInfo<Brand>  brand=brandService.findPage(page,size);
        return new Result(true,StatusCode.OK,"查询成功",brand);
    }

    /**
     * 分页和条件查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result findSearch(@RequestBody Brand brand,@PathVariable(value="page") int page,@PathVariable(value="size") int size){
      PageInfo<Brand>  brands= brandService.findSearch(brand,page,size);
      return new Result(true,StatusCode.OK,"查询成功",brands);
    }

    /**
     * 根据分类id查询brand集合成功
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result findBrandById(@PathVariable(value = "id") Integer id){
      List<Brand> brands=  brandService.findBrandById(id);
      return new Result(true,StatusCode.OK,"根据分类id查询品牌集合成功",brands);
    }
}
