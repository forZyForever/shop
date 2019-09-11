package com.Nintendo;

import com.Nintendo.entity.IdWorker;
import com.Nintendo.entity.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Package: com.Nintendo
 * @Author: ZZM
 * @Date: Created in 2019/9/1 16:53
 * @Address:CN.SZ
 **/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@MapperScan("com.Nintendo.order.dao")
@EnableFeignClients(basePackages = {"com.Nintendo.goods.feign","com.Nintendo.user.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
    @Bean
    public TokenDecode decode(){
        return new TokenDecode();
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,1);
    }
}
