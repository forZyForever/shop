package com.Nintendo.goods.service;

import com.Nintendo.goods.pojo.Template;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.service
 * @Author: ZZM
 * @Date: Created in 2019/8/20 16:43
 * @Address:CN.SZ
 **/
public interface TemplateService {
   List<Template> findAll() ;

   void add(Template template);

   List<Template> searchByTerm(Template template);

   PageInfo<Template> searchPageUp(Integer page, Integer size);

   PageInfo<Template> serachPageUpByTerm(Integer page, Integer size, Template template);

   Template findById(Integer id);

   void updateById(Template template);

   void deleteById(Integer id);

    Template findByCategoryId(Integer id);
}
