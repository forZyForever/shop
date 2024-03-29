package com.Nintendo.goods.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.goods.pojo.Goods;
import com.Nintendo.goods.pojo.Spu;
import com.Nintendo.goods.service.SpuService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/20 22:26
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController {
    @Autowired
    private SpuService spuService;

    /***
     * Spu分页条件搜索实现
     * @param spu
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Spu spu, @PathVariable  int page, @PathVariable  int size){
        //调用SpuService实现分页条件查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * Spu分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SpuService实现分页查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param spu
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Spu>> findList(@RequestBody(required = false)  Spu spu){
        //调用SpuService实现条件查询Spu
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SpuService实现根据主键删除
        spuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改Spu数据
     * @param spu
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Spu spu,@PathVariable Long id){
        //设置主键值
        spu.setId(id);
        //调用SpuService实现修改Spu
        spuService.update(spu);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增Spu数据
     * @param spu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody   Spu spu){
        //调用SpuService实现添加Spu
        spuService.add(spu);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable Long id){
        //调用SpuService实现根据主键查询Spu
        Spu spu = spuService.findById(id);
        return new Result<Spu>(true,StatusCode.OK,"查询成功",spu);
    }

    /***
     * 查询Spu全部数据
     * @return
     */
    @GetMapping
    public Result<List<Spu>> findAll(){
        //调用SpuService实现查询所有Spu
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK,"查询成功",list) ;
    }
    /**
     * (goods)spu+sku进行保存
     *
     * @param goods
     * @return
     */
    @PostMapping("/save")
    public Result saveGoods(@RequestBody Goods goods) {
        spuService.saveGoods(goods);
        return new Result(true, StatusCode.OK, "新增商品成功");
    }

    /**
     * 按照id查询商品
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/goods/{id}")
    public Result findGoodsById(@PathVariable(value = "id") long id) {
        Goods goods = spuService.findGoodsById(id);
        return new Result(true, StatusCode.OK, "按照id查询商品成功", goods);
    }

    /**
     * 审核商品
     *
     * @param id
     * @return
     */
    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable(value = "id") long id) {
        spuService.audit(id);
        return new Result(true, StatusCode.OK, "审核成功");
    }

    /**
     * 下架商品
     *
     * @param id
     * @return
     */
    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable(value = "id") long id) {
        spuService.pull(id);
        return new Result(true, StatusCode.OK, "下架成功");
    }

    /**
     * 商品上架
     *
     * @param id
     * @return
     */
    @PutMapping("/put/{id}")
    public Result put(@PathVariable(value = "id") long id) {
        spuService.put(id);
        return new Result(true, StatusCode.OK, "上架成功");
    }

    /**
     * 商品批量上架
     * @param ids
     * @return
     */
    @PutMapping("/put/many")
    public Result put(@RequestBody long[] ids) {
        int count = spuService.putMany(ids);
        return new Result(true, StatusCode.OK, "批量上架"+count+"个商品");
    }

    /**
     * 商品批量下架
     * @param ids
     * @return
     */
    @PutMapping("/pull/many")
    public Result pullMany(@RequestBody long[] ids){
        int count = spuService.pullMany(ids);
        return new Result(true,StatusCode.OK,"批量下架"+count+"个商品");
    }

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    @DeleteMapping("/logic/delete/{id}")
    public Result logicDelete(@PathVariable (value = "id")long id){
        spuService.logicDelete(id);
        return new Result(true,StatusCode.OK,"逻辑删除成功");
    }

    /**
     * 数据恢复
     * @param id
     * @return
     */
    @PutMapping("/restore/{id}")
    public Result restore(@PathVariable (value = "id") long id){
        spuService.restore(id);
        return new Result(true,StatusCode.OK,"数据恢复成功");
    }

}
