package com.Nintendo.search.controller;

import com.Nintendo.entity.Page;
import com.Nintendo.search.feign.SkuFeign;
import com.Nintendo.search.pojo.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Map;

/**
 * @Package: com.Nintendo.search.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/27 9:46
 * @Address:CN.SZ
 **/
@Controller
@RequestMapping("/search")
public class SkuController {
    @Autowired
    private SkuFeign skuFeign;

    @GetMapping("/list")
    public String searchByEs(@RequestParam(required = false) Map<String, String> searchMap, Model model) {
        Map<String, Object> resultMap = skuFeign.searchByEs(searchMap);
        //搜索数据结果
        model.addAttribute("result", resultMap);
        //搜索关键字
        model.addAttribute("searchMap", searchMap);
        //请求地址结果
        String url = url(searchMap);
        model.addAttribute("url", url);
        //分页
        Page<SkuInfo> page = new Page<SkuInfo>(    //总记录数
                Long.valueOf(resultMap.get("total").toString()),
                //当前页
                Integer.valueOf(resultMap.get("pageNum").toString()),
                //每页条数
                Integer.valueOf(resultMap.get("pageSize").toString()));

        model.addAttribute("page", page);
        return "search";
    }

    //请求url的拼接用于条件搜索的实现
    private String url(Map<String, String> searchMap) {
        String url = "/search/list";
        if (null != searchMap && searchMap.size() > 0) {
            url += "?";
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                //如果是排序请求则跳出
                if (key.equals("sortField") || key.equals("sortRule")) {
                    continue;
                }
                //分页
                if (key.equals("pageNum")) {
                    continue;
                }
                url += key + "=" + value + "&";
            }
            //去掉最后的&
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }
}
