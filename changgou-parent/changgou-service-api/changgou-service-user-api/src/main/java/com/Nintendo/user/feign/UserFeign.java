package com.Nintendo.user.feign;

import com.Nintendo.entity.Result;
import com.Nintendo.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Package: com.Nintendo.user.feign
 * @Author: ZZM
 * @Date: Created in 2019/9/1 14:05
 * @Address:CN.SZ
 **/
@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {

    /**
     * 加载用户数据
     *
     * @param id
     * @return
     */
    @GetMapping("/load/{id}")
    Result<User> findByUsername(@PathVariable(name = "id") String id);

    /**
     * 添加积分
     *
     * @param points
     * @param username
     * @return
     */
    @GetMapping(value = "/points/add")
    Result addPoints(@RequestParam(value = "points") Integer points
            , @RequestParam(value = "username") String username);
}
