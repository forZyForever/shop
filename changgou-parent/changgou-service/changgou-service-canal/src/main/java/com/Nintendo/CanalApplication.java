package com.Nintendo;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Package: com.Nintendo
 * @Author: ZZM
 * @Date: Created in 2019/8/23 15:04
 * @Address:CN.SZ
 **/
//canal客户端监听canal服务端的数据变化
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableCanalClient
@EnableFeignClients(basePackages = {"com.Nintendo.content.feign","com.Nintendo.item.feign"})
public class CanalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class,args);
    }
}
