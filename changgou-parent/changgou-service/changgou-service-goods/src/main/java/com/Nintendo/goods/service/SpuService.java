package com.Nintendo.goods.service;

import com.Nintendo.goods.pojo.Goods;
import com.Nintendo.goods.pojo.Spu;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.service
 * @Author: ZZM
 * @Date: Created in 2019/8/20 22:01
 * @Address:CN.SZ
 **/
public interface SpuService  {
    /***
     * Spu多条件分页查询
     * @param spu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(Spu spu, int page, int size);

    /***
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(int page, int size);

    /***
     * Spu多条件搜索方法
     * @param spu
     * @return
     */
    List<Spu> findList(Spu spu);

    /***
     * 删除Spu
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Spu数据
     * @param spu
     */
    void update(Spu spu);

    /***
     * 新增Spu
     * @param spu
     */
    void add(Spu spu);

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
    Spu findById(Long id);

    /***
     * 查询所有Spu
     * @return
     */
    List<Spu> findAll();
    /**
     * 保存商品
     * @param goods
     */
    void saveGoods(Goods goods);

    /**
     * 按照id查询商品
     * @param id
     * @return
     */
    Goods findGoodsById(long id);

    /**
     * 商品审核
     * @param id
     */
    void audit(long id);

    /**
     * 商品下架
     * @param id
     */
    void pull(long id);

    /**
     * 商品上架
     * @param id
     */
    void put(long id);

    /**
     * 商品批量上架
     * @param ids
     */
    int putMany(long[] ids);

    /**
     * 商品批量下架
     * @param ids
     */
    int pullMany(long[] ids);

    /**
     * 逻辑删除
     * @param id
     */
    void logicDelete(long id);

    /**
     * 数据恢复
     * @param id
     */
    void restore(long id);
}
