package com.Nintendo.goods.service;

import com.Nintendo.goods.pojo.Spec;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.service
 * @Author: ZZM
 * @Date: Created in 2019/8/20 17:09
 * @Address:CN.SZ
 **/
public interface SpecService {
    List<Spec> findAll();

    void add(Spec spec);

    List<Spec> searchByTerm(Spec spec);

    PageInfo<Spec> searchPageUp(Integer page, Integer size);

    PageInfo<Spec> serachPageUpByTerm(Integer page, Integer size, Spec spec);

    Spec findById(Integer id);

    void updateById(Spec spec);

    void deleteById(Integer id);

    List<Spec> findByCategoryId(Integer id);
}
