package com.Nintendo.seckill;

import com.Nintendo.entity.IdWorker;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Package: com.Nintendo.seckill
 * @Author: ZZM
 * @Date: Created in 2019/9/5 20:50
 * @Address:CN.SZ
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.Nintendo.seckill.dao"})
@EnableScheduling
@EnableAsync
public class SeckillApplication {
    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

    //设置redisTemplate的序列化类型
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //采用普通的key 为 字符串
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }

    /**
     * 到期队列
     *
     * @return
     */
    @Bean
    public Queue seckillOrderTimerQueue() {
        return new Queue(env.getProperty("mq.pay.queue.seckillordertimer"));
    }

    /**
     * 延时队列
     *
     * @return
     */
    @Bean
    public Queue seckillOrderTimerDelayQueue() {

        return QueueBuilder.durable(env.getProperty("mq.pay.queue.seckillordertimerdelay"))
                .withArgument("x-dead-letter-exchange", env.getProperty("mq.pay.exchange.order"))
                .withArgument("x-dead-letter-routing-key", env.getProperty("mq.pay.queue.seckillordertimer"))
                .build();
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(env.getProperty("mq.pay.exchange.order"));
    }

    //绑定
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(seckillOrderTimerQueue()).to(directExchange()).with(env.getProperty("mq.pay.queue.seckillordertimer"));
    }

}
