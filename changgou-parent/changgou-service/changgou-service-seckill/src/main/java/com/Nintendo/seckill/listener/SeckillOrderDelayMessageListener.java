package com.Nintendo.seckill.listener;

import com.Nintendo.constant.Constant;
import com.Nintendo.seckill.pojo.SeckillOrder;
import com.Nintendo.seckill.pojo.SeckillStatus;
import com.Nintendo.seckill.service.SeckillOrderService;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;



/**
 * @Package: com.Nintendo.seckill.listener
 * @Author: ZZM
 * @Date: Created in 2019/9/8 10:46
 * @Address:CN.SZ
 **/

/**
 * 延时监听秒杀订单消息
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.seckillordertimer}")
public class SeckillOrderDelayMessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderService seckillOrderService;

    //接收延时秒杀下单信息
    @RabbitHandler
    public void handlerData(@Payload SeckillStatus seckillStatus) throws Exception {

        //查看预订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(Constant.SEC_KILL_ORDER_PREFIX).get(seckillStatus.getUsername());
        if (null != seckillOrder) {
            System.out.println("延时消息收到");

            //关闭微信订单
            seckillOrderService.closeWeixinPay(seckillStatus.getOrderId());

            //删除redis预订单
            redisTemplate.boundHashOps(Constant.SEC_KILL_ORDER_PREFIX).delete(seckillStatus.getUsername());

            //恢复库存
            seckillOrderService.receiveRedis(seckillStatus,seckillOrder);

            //删除排队列表标识
            seckillOrderService.deleteUserQueue(seckillStatus.getUsername());

            System.out.println("延时任务完成");
        }
    }
}
