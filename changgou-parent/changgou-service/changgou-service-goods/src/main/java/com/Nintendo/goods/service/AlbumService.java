package com.Nintendo.goods.service;

import com.Nintendo.goods.pojo.Album;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Package: com.Nintendo.goods.service
 * @Author: ZZM
 * @Date: Created in 2019/8/20 11:00
 * @Address:CN.SZ
 **/
public interface AlbumService {
    List<Album> findAll();

    void add(Album album);

    List<Album> searchByTerm(Album album);

    PageInfo<Album> searchPageUp(Integer page, Integer size);

    PageInfo<Album> serachPageUpByTerm(Integer page, Integer size, Album album);

    Album findById(Integer id);

    void updateById( Album album);

    void deleteById(Integer id);
}
