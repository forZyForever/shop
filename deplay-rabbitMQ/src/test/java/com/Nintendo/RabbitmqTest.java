package com.Nintendo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Package: com.Nintendo
 * @Author: ZZM
 * @Date: Created in 2019/9/8 0:12
 * @Address:CN.SZ
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitmqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void Test() {
        System.out.println("开始发送"+new Date());
        rabbitTemplate.convertAndSend("queue.message.delay", (Object) "hello world", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //延时10秒
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
