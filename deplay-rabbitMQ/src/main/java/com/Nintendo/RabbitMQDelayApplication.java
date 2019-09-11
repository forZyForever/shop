package com.Nintendo;

import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @Package: com.Nintendo
 * @Author: ZZM
 * @Date: Created in 2019/9/7 23:46
 * @Address:CN.SZ
 **/
@SpringBootApplication
public class RabbitMQDelayApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQDelayApplication.class, args);
    }
    //创建队列1用于生产者发送,默认交换机,默认路由,消息体=>死信队列,参数指定的交换机和路由器到queue中去
    // =>队列2绑定指定的交换机接收消息

    @Bean
    public Queue createQueue01() {
        return QueueBuilder.durable("queue.message.delay")
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "queue.message")
                .build();
    }

    //创建队列2
    @Bean
    public Queue createQueue02() {
        return new Queue("queue.message");
    }
        //交换机
    @Bean
    public DirectExchange CreatedirectExchange() {
        return new DirectExchange("dlx.exchange");
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(createQueue02()).to(CreatedirectExchange()).with("queue.message");
    }

}
