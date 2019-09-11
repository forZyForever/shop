package com.Nintendo.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Package: com.Nintendo.content
 * @Author: ZZM
 * @Date: Created in 2019/8/23 16:36
 * @Address:CN.SZ
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.Nintendo.content.dao"})
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class,args);
    }
}
