package com.Nintendo.goods.service.impl;

import com.Nintendo.goods.dao.CategoryMapper;
import com.Nintendo.goods.pojo.Category;
import com.Nintendo.goods.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.service.impl
 * @Author: ZZM
 * @Date: Created in 2019/8/20 15:25
 * @Address:CN.SZ
 **/
@Service
public class CategoryServiceimpl implements CategoryService {
    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public void delete(Integer id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Category> queryByTerm(Category category) {
        Example example = createExample(category);
        return categoryMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Category> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(categoryMapper.selectAll());
    }

    @Override
    public PageInfo<Category> findSearch(Category category, int page, int size) {
        Example example = createExample(category);
        PageHelper.startPage(page, size);
        return new PageInfo<>(categoryMapper.selectByExample(example));
    }

    /**
     * 根据子节点进行查询
     * @param pid
     * @return
     */
    @Override
    public List<Category> findByPrantId(Integer pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    private Example createExample(Category category) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if (null != category) {
            if (!StringUtils.isEmpty(category.getName())) {
                criteria.andLike("name", "%" + category.getName() + "%");
            }
        }
        return example;
    }
}
