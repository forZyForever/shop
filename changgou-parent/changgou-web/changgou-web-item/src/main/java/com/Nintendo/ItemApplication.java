package com.Nintendo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Package: com.Nintendo
 * @Author: ZZM
 * @Date: Created in 2019/8/28 10:18
 * @Address:CN.SZ
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.Nintendo.goods.feign"})
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
