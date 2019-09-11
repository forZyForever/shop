package com.Nintendo.goods.service.impl;

import com.Nintendo.goods.dao.AlbumMapper;
import com.Nintendo.goods.pojo.Album;
import com.Nintendo.goods.service.AlbumService;
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
 * @Date: Created in 2019/8/20 11:01
 * @Address:CN.SZ
 **/
@Service
public class AlbumServiceimpl implements AlbumService {
    @Autowired(required = false)
    private AlbumMapper albumMapper;

    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }

    @Override
    public void add(Album album) {
        albumMapper.insert(album);
    }

    @Override
    public List<Album> searchByTerm(Album album) {
        Example example = createExample(album);
        return albumMapper.selectByExample(example);

    }

    @Override
    public PageInfo<Album> searchPageUp(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(albumMapper.selectAll());
    }

    @Override
    public PageInfo<Album> serachPageUpByTerm(Integer page, Integer size, Album album) {
        Example example = createExample(album);
        PageHelper.startPage(page, size);
        return new PageInfo<>(albumMapper.selectByExample(example));
    }

    @Override
    public Album findById(Integer id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateById(Album album) {
        albumMapper.updateByPrimaryKeySelective(album);
    }

    @Override
    public void deleteById(Integer id) {
        albumMapper.deleteByPrimaryKey(id);
    }

    private Example createExample(Album album) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if (null != album) {
            if (!StringUtils.isEmpty(album.getTitle())) {
                criteria.andLike("title", "%" + album.getTitle() + "%");
            }
        }
        return example;
    }


}
