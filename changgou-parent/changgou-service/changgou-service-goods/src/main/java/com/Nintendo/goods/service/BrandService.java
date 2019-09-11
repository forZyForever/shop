package com.Nintendo.goods.service;


import com.Nintendo.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;


import java.util.List;


/**
 * Package: com.Nintendo.goods.service
 * Author: ZZM
 * Date: Created in 2019/8/15 23:47
 **/
/*创建商品微服务，实现对品牌表的增删改查功能。具体包括

（1）查询全部列表数据

（2）根据ID查询实体数据

（3）增加

（4）修改

（5）删除

（6）条件查询

（7）分页查询

（8）分页+条件查询

（9）公共异常处理
*/
public interface BrandService {
    /**
     * 查询全部列表数据
     * @return
     */
    List<Brand> findAll();

    /**
     *根据id查询实体数据
     */
    Brand findById(Integer id);
    /**
     * 增加品牌
     */
    void  addBrand(Brand brand);
    /**
     * 修改品牌
     */
    void updateBrand(Brand brand);
    /**
     * 删除品牌
     */
    void deleteBrand(Integer id);
    /**
     * 条件查询
     */
    List<Brand> queryByTerm(Brand brand);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(int page, int size);

    /**
     * 条件查询和分页查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findSearch(Brand brand, int page, int size);

    /**
     * 根据分类id查询集合成功
     * @param id
     * @return
     */
    List<Brand> findBrandById(Integer id);
}
