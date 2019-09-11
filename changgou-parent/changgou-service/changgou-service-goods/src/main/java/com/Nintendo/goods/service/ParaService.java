package com.Nintendo.goods.service;

import com.Nintendo.goods.pojo.Para;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.service
 * @Author: ZZM
 * @Date: Created in 2019/8/20 17:29
 * @Address:CN.SZ
 **/
public interface ParaService {
    List<Para> findAll();

    void add(Para para);

    List<Para> searchByTerm(Para para);

    PageInfo<Para> searchPageUp(Integer page, Integer size);

    PageInfo<Para> serachPageUpByTerm(Integer page, Integer size, Para para);

    Para findById(Integer id);

    void updateById(Para para);

    void deleteById(Integer id);

    List<Para> findByCategoryId(Integer id);
}
