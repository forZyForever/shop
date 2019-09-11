package com.Nintendo.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Package: com.Nintendo.listener
 * @Author: ZZM
 * @Date: Created in 2019/9/7 23:47
 * @Address:CN.SZ
 **/
@Component
@RabbitListener(queues = "queue.message")
public class consumerDelay {

    @RabbitHandler
    public void consumer(String message){
        System.out.println("收到消息:"+new Date());
        System.out.println(message);
    }
}
