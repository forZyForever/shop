package com.Nintendo;


import com.Nintendo.entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Package: com.Nintendo.goods
 * Author: ZZM
 * Date: Created in 2019/8/14 11:40
 **/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@MapperScan(basePackages= {"com.Nintendo.goods.dao"})
//tk.mybatis.spring.annotation包下的，用于扫描Mapper接口
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class,args);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }
}
