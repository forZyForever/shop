package com.Nintendo.user.controller;

import com.Nintendo.entity.BCrypt;
import com.Nintendo.entity.JwtUtil;
import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.user.pojo.User;
import com.Nintendo.user.service.UserService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /***
     * User分页条件搜索实现
     * @param user
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) User user, @PathVariable int page, @PathVariable int size) {
        //调用UserService实现分页条件查询User
        PageInfo<User> pageInfo = userService.findPage(user, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * User分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size) {
        //调用UserService实现分页查询User
        PageInfo<User> pageInfo = userService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param user
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<User>> findList(@RequestBody(required = false) User user) {
        //调用UserService实现条件查询User
        List<User> list = userService.findList(user);
        return new Result<List<User>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        //调用UserService实现根据主键删除
        userService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改User数据
     * @param user
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody User user, @PathVariable String id) {
        //设置主键值
        user.setUsername(id);
        //调用UserService实现修改User
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增User数据
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        //调用UserService实现添加User
        userService.add(user);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询User数据
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable String id) {
        //调用UserService实现根据主键查询User
        User user = userService.findById(id);
        return new Result<User>(true, StatusCode.OK, "查询成功", user);
    }

    /***
     * 查询User全部数据
     * @return
     */
    @PreAuthorize("hasAuthority('goods_list')")
    @GetMapping
    public Result<List<User>> findAll() {
        //调用UserService实现查询所有User
        List<User> list = userService.findAll();
        return new Result<List<User>>(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 用户登录成功颁发令牌
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findById(username);
        if (null == user) {
            return new Result(false, StatusCode.LOGINERROR, "用户名或密码错误");
        }

        if (BCrypt.checkpw(password, user.getPassword())) {
            //登陆成功颁发令牌
            Map<String, Object> map = new HashMap<>();
            map.put("role", "USER");
            map.put("success", "SUCCESS");
            map.put("username", username);
            //设置id 主题 有限期
            String jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(map), null);
            //放到cookie中去
            Cookie cookie = new Cookie("Authorization", jwt);
            response.addCookie(cookie);
            //设置到头信息中去
            response.setHeader("Authorization", jwt);
            return new Result(true, StatusCode.OK, "登录成功", jwt);
        } else {
            return new Result(true, StatusCode.LOGINERROR, "用户名或密码错误");
        }
    }

    /**
     * 加载用户数据
     *
     * @param id
     * @return
     */
    @GetMapping("/load/{id}")
    public Result<User> findByUsername(@PathVariable(name = "id") String id) {
        User user = userService.findById(id);
        return new Result<User>(true, StatusCode.OK, "查询成功", user);
    }

    /**
     * 添加积分
     * @param points
     * @param username
     * @return
     */
    @GetMapping(value = "/points/add")
    public Result addPoints(@RequestParam(value = "points") Integer points
            , @RequestParam(value = "username") String username) {
        userService.addPoints(points, username);
        return new Result(true, StatusCode.OK, "添加积分成功");
    }
}
