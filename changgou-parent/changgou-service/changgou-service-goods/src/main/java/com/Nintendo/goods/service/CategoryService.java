package com.Nintendo.goods.service;


import com.Nintendo.goods.pojo.Category;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.service
 * @Author: ZZM
 * @Date: Created in 2019/8/20 15:15
 * @Address:CN.SZ
 **/
public interface CategoryService {

    List<Category> findAll();

    Category findById(Integer id);

    void add(Category category);

    void updateCategory(Category category);

    void delete(Integer id);

    List<Category> queryByTerm(Category category);

    PageInfo<Category> findPage(int page, int size);

    PageInfo<Category> findSearch(Category category, int page, int size);

    List<Category> findByPrantId(Integer pid);
}
