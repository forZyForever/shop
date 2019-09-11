package com.Nintendo.goods.service.impl;

import com.Nintendo.goods.dao.CategoryMapper;
import com.Nintendo.goods.dao.ParaMapper;
import com.Nintendo.goods.pojo.Category;
import com.Nintendo.goods.pojo.Para;
import com.Nintendo.goods.service.ParaService;
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
 * @Date: Created in 2019/8/20 17:30
 * @Address:CN.SZ
 **/
@Service
public class ParaServiceimpl implements ParaService {
    @Autowired(required = false)
    private ParaMapper paraMapper;
    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }

    @Override
    public void add(Para para) {
        paraMapper.insert(para);
    }

    @Override
    public List<Para> searchByTerm(Para para) {
        Example example = createExample(para);
        return paraMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Para> searchPageUp(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(paraMapper.selectAll());
    }

    @Override
    public PageInfo<Para> serachPageUpByTerm(Integer page, Integer size, Para para) {
        Example example = createExample(para);
        PageHelper.startPage(page, size);
        return new PageInfo<>(paraMapper.selectByExample(example));
    }

    @Override
    public Para findById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateById(Para para) {
        paraMapper.updateByPrimaryKeySelective(para);
    }

    @Override
    public void deleteById(Integer id) {
        paraMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据分类id查询参数列表
     * @param id
     * @return
     */
    @Override
    public List<Para> findByCategoryId(Integer id) {
        /**
         * SELECT * FROM tb_para WHERE template_id = (SELECT template_id FROM tb_category WHERE id = 1)
         */
        //根据分类id找寻模板
        Category category = categoryMapper.selectByPrimaryKey(id);
        Para para = new Para();
        para.setTemplateId(category.getTemplateId());
        //根据模板找寻参数
        return paraMapper.select(para);
    }

    public Example createExample(Para para) {
        Example example = new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if (null != para) {
            if (!StringUtils.isEmpty(para.getName())) {
                criteria.andLike("name", "%" + para.getName() + "%");
            }
            if (!StringUtils.isEmpty(para.getOptions())) {
                criteria.andLike("options", "%" + para.getOptions() + "%");
            }
        }
        return example;
    }
}
