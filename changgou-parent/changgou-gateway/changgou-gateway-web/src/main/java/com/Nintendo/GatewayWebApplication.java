package com.Nintendo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Package: com.Nintendo
 * @Author: ZZM
 * @Date: Created in 2019/8/29 22:57
 * @Address:CN.SZ
 **/
@SpringBootApplication
@EnableEurekaClient
public class GatewayWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayWebApplication.class,args);
    }

    /**
     * 创建ipKey指定用户的ip
     * @return
     */
    @Bean(name = "ipKeyResolver")
    public KeyResolver userKeyResolver(){
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //获取request对象
                ServerHttpRequest request = exchange.getRequest();
                //从request中获取ip地址
                String hostString = request.getRemoteAddress().getHostString();
                return Mono.just(hostString);
            }
        };
    }
}
