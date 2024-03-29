package com.changgou.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Package: com.Nintendo.order.controller
 * @Author: ZZM
 * @Date: Created in 2019/9/2 9:51
 * @Address:CN.SZ
 **/
@Controller
@RequestMapping("/oauth")
public class LoginRedirect {
    /**
     * 跳转到登录页面
     *
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "FROM", required = false, defaultValue = "") String from, Model model) {
        model.addAttribute("from", from);
        return "login";
    }
}
