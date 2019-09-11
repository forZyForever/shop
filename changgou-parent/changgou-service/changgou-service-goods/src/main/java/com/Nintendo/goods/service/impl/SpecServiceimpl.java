package com.Nintendo.goods.service.impl;

import com.Nintendo.goods.dao.CategoryMapper;
import com.Nintendo.goods.dao.SpecMapper;
import com.Nintendo.goods.pojo.Category;
import com.Nintendo.goods.pojo.Spec;
import com.Nintendo.goods.service.SpecService;
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
 * @Date: Created in 2019/8/20 17:09
 * @Address:CN.SZ
 **/
@Service
public class SpecServiceimpl implements SpecService {
    @Autowired(required = false)
    private SpecMapper specMapper;
    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Override
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    @Override
    public void add(Spec spec) {
        specMapper.insert(spec);
    }

    @Override
    public List<Spec> searchByTerm(Spec spec) {
        Example example = createExample(spec);
        return specMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Spec> searchPageUp(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(specMapper.selectAll());
    }

    @Override
    public PageInfo<Spec> serachPageUpByTerm(Integer page, Integer size, Spec spec) {
        Example example = createExample(spec);
        PageHelper.startPage(page, size);
        return new PageInfo<>(specMapper.selectByExample(example));
    }

    @Override
    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateById(Spec spec) {
        specMapper.updateByPrimaryKeySelective(spec);
    }

    @Override
    public void deleteById(Integer id) {
        specMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据分类id查询规格列表
     * @param id
     * @return
     */
    @Override
    public List<Spec> findByCategoryId(Integer id) {
        /**
         * SELECT * FROM `tb_spec` WHERE template_id = (SELECT template_id FROM `tb_category` WHERE id=1)
         */
        //根据分类id找到模板id
        Category category = categoryMapper.selectByPrimaryKey(id);
        Spec spec = new Spec();
        spec.setTemplateId(category.getTemplateId());
        //根据模板id找到规格列表
        return specMapper.select(spec);
    }

    public Example createExample(Spec spec) {
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if (null != spec) {
            if (!StringUtils.isEmpty(spec.getName())) {
                criteria.andLike("name", "%" + spec.getName() + "%");
            }
            if (!StringUtils.isEmpty(spec.getOptions())) {
                criteria.andLike("options", "%" + spec.getOptions() + "%");
            }
        }
        return example;
    }
}
