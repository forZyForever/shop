package com.Nintendo.goods.service.impl;

import com.Nintendo.goods.dao.CategoryMapper;
import com.Nintendo.goods.dao.TemplateMapper;
import com.Nintendo.goods.pojo.Category;
import com.Nintendo.goods.pojo.Template;
import com.Nintendo.goods.service.TemplateService;
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
 * @Date: Created in 2019/8/20 16:47
 * @Address:CN.SZ
 **/
@Service
public class TemplateServiceimpl implements TemplateService {
    @Autowired(required = false)
    private TemplateMapper templateMapper;
    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Override
    public List<Template> findAll() {
        return templateMapper.selectAll();
    }

    @Override
    public void add(Template template) {
        templateMapper.insert(template);
    }

    @Override
    public List<Template> searchByTerm(Template template) {
        Example example = creatExample(template);
        return templateMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Template> searchPageUp(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(templateMapper.selectAll());
    }

    @Override
    public PageInfo<Template> serachPageUpByTerm(Integer page, Integer size, Template template) {
        Example example = creatExample(template);
        PageHelper.startPage(page, size);
        return new PageInfo<>(templateMapper.selectByExample(example));
    }

    @Override
    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateById(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public void deleteById(Integer id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据分类id查询模板
     *
     * @param id
     * @return
     */
    @Override
    public Template findByCategoryId(Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        return templateMapper.selectByPrimaryKey(category.getTemplateId());
    }

    private Example creatExample(Template template) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (null != template) {
            if (!StringUtils.isEmpty(template.getName())) {
                criteria.andLike("name", "%" + template.getName() + "%");
            }
        }
        return example;
    }
}
