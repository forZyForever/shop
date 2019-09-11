package com.Nintendo.goods.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.goods.pojo.Album;
import com.Nintendo.goods.service.AlbumService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Package: com.Nintendo.goods.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/20 10:51
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/album")
@CrossOrigin
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    /**
     * 查询所有相册
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Album> album = albumService.findAll();
        return new Result(true, StatusCode.OK, "查询相册列表成功", album);
    }

    /**
     * 添加相册
     *
     * @param album
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Album album) {
        albumService.add(album);
        return new Result(true, StatusCode.OK, "新建相册成功");
    }

    /**
     * 条件插询
     *
     * @param album
     * @return
     */
    @PostMapping("/search")
    public Result searchByTerm(@RequestBody Album album) {
        List<Album> albums = albumService.searchByTerm(album);
        return new Result(true, StatusCode.OK, "条件查询成功", albums);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result searchPageUp(@PathVariable(value = "page") Integer page, @PathVariable(value = "size") Integer size) {
        PageInfo<Album> pageInfo = albumService.searchPageUp(page, size);
        return new Result(true, StatusCode.OK, "条件查询成功", pageInfo);
    }

    /**
     * 分页查询和条件查询
     *
     * @param page
     * @param size
     * @param album
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result serachPageUpByTerm(@PathVariable(value = "page") Integer page, @PathVariable(value = "size") Integer size, @RequestBody Album album) {
        PageInfo<Album> pageInfo = albumService.serachPageUpByTerm(page, size, album);
        return new Result(true, StatusCode.OK, "分页查询成功", pageInfo);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable(value = "id") Integer id) {
        Album album = albumService.findById(id);
        return new Result(true, StatusCode.OK, "根据id查询相册成功", album);
    }

    /**
     * 根据id进行修改
     *
     * @param id
     * @param album
     * @return
     */
    @PutMapping("/{id}")
    public Result updateById(@PathVariable(value = "id") long id, @RequestBody Album album) {
        album.setId(id);
        albumService.updateById(album);
        return new Result(true, StatusCode.OK, "根据id修改相册数据成功");
    }

    /**
     * 根据id进行删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable(value = "id") Integer id) {
        albumService.deleteById(id);
        return new Result(true, StatusCode.OK, "根据id删除成功");
    }
}
