package com.Nintendo.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Package: com.Nintendo.search
 * @Author: ZZM
 * @Date: Created in 2019/8/27 9:50
 * @Address:CN.SZ
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.Nintendo.search.feign"})
public class SearchWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchWebApplication.class,args);
    }
}
