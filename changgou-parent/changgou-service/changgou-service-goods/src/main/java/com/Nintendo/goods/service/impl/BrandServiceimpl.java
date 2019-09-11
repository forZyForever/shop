package com.Nintendo.goods.service.impl;


import com.Nintendo.goods.dao.BrandMapper;
import com.Nintendo.goods.dao.CategoryBrandMapper;
import com.Nintendo.goods.pojo.Brand;
import com.Nintendo.goods.pojo.CategoryBrand;
import com.Nintendo.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Package: com.Nintendo.goods.service.impl
 * Author: ZZM
 * Date: Created in 2019/8/15 23:47
 **/
@Service
public class BrandServiceimpl implements BrandService {
    @Autowired(required = false)
    private BrandMapper brandMapper;
    @Autowired(required = false)
    private CategoryBrandMapper categoryBrandMapper;

    /**
     * 查询所有列表数据
     *
     * @return
     */
    @Override
    public List<Brand> findAll() {
        try {
            System.out.println("线程开始睡");
            Thread.sleep(3000);
            System.out.println("线程醒了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return brandMapper.selectAll();
    }

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    @Override
    public Brand findById(Integer id) {

        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加品牌数据
     *
     * @param brand
     */
    @Override
    public void addBrand(Brand brand) {
        int i = brandMapper.insert(brand);

    }

    /**
     * 更新品牌数据
     *
     * @param brand
     */
    @Override
    public void updateBrand(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 删除品牌数据
     *
     * @param id
     */
    @Override
    public void deleteBrand(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 条件查询
     *
     * @param brand
     * @return
     */
    @Override
    public List<Brand> queryByTerm(Brand brand) {
        Example example = createExample(brand);
        return brandMapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Brand>(brandMapper.selectAll());
    }

    /**
     * 条件+分页查询
     *
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findSearch(Brand brand, int page, int size) {
        //分页开启,第一条查询会被分页,后面的不会
        PageHelper.startPage(page, size);
        Example example = createExample(brand);
        return new PageInfo<Brand>(brandMapper.selectByExample(example));
    }

    /**
     * 根据分类id查询brand
     *
     * @param id
     * @return
     */
    @Override
    public List<Brand> findBrandById(Integer id) {
        /**接口方法定义注解sql
        SELECT * FROM `tb_brand` WHERE id IN (SELECT brand_id FROM `tb_category_brand` WHERE category_id=id)
        SELECT b.* FROM `tb_category_brand` a,`tb_brand` b WHERE a.brand_id=b.id AND a.category_id=76**/
        //根据分类id查询品牌id集合
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setCategoryId(id);
        List<CategoryBrand> categoryBrands = categoryBrandMapper.select(categoryBrand);
        List<Brand> list=new ArrayList<>();
        for (CategoryBrand categoryBrand1: categoryBrands) {
            //根据品牌id查询品牌名
            Brand brand = brandMapper.selectByPrimaryKey(categoryBrand1.getBrandId());
            list.add(brand);
        }
        return list;
    }

    public Example createExample(Brand brand) {

        Example example = new Example(Brand.class);
        //封装结果相当于动态sql
        Example.Criteria criteria = example.createCriteria();
        if (brand != null) {
            if (!StringUtils.isEmpty(brand.getName())) {
                //第一个参数指定要比较的属性值pojo,第二个参数为要比较的值
                criteria.andLike("name", "%" + brand.getName() + "%");
            }
            if (!StringUtils.isEmpty(brand.getImage())) {
                criteria.andLike("image", "%" + brand.getImage() + "%");
            }
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andLike("letter", "%" + brand.getLetter() + "%");
            }
            if (!StringUtils.isEmpty(brand.getSeq())) {
                criteria.andEqualTo(brand.getSeq());
            }
        }
        return example;
    }
}
