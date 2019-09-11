package com.Nintendo.seckill.task;

import com.Nintendo.constant.Constant;
import com.Nintendo.entity.IdWorker;
import com.Nintendo.seckill.dao.SeckillGoodsMapper;
import com.Nintendo.seckill.pojo.SeckillGoods;
import com.Nintendo.seckill.pojo.SeckillOrder;
import com.Nintendo.seckill.pojo.SeckillStatus;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Package: com.Nintendo.seckill.task
 * @Author: ZZM
 * @Date: Created in 2019/9/6 9:24
 * @Address:CN.SZ
 **/
@Component
public class MultiThreadingCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired(required = false)
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Async
    public void createOrder() {
        //取出队列中的消息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(Constant.SEC_KILL_USER_QUEUE_KEY).rightPop();
        if (null != seckillStatus) {

            //获取用户名,商品id,时间段
            String time = seckillStatus.getTime();
            String name = seckillStatus.getUsername();
            Long id = seckillStatus.getGoodsId();

            //从商品队列中取出
            Object o = redisTemplate.boundListOps(Constant.SEC_KILL_GOODS_COUNT_PREFIX + id).rightPop();
            if (null == o) {
                //队列中的商品已获取完
                //清除掉用户排队次数统计
                redisTemplate.boundHashOps(Constant.SEC_KILL_USER_QUEUE_COUNT).delete(name);
                //清除掉用户排队状态列表
                redisTemplate.boundHashOps(Constant.SEC_KILL_USER_STATUS_KEY).delete(name);
                return;
            }
            //查询出商品
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_PREFIX + time).get(id);
            //判断是否库存是否大于0,
           /* if (null == seckillGoods && seckillGoods.getStockCount() <= 0) {
                throw new RuntimeException("商品已出售完");
            }*/


            //redis预订单
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            //秒杀商品id
            seckillOrder.setSeckillId(seckillGoods.getId());
            seckillOrder.setUserId(name);
            //花费
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setStatus("0");
            //建立redis预订单
            redisTemplate.boundHashOps(Constant.SEC_KILL_ORDER_PREFIX).put(name, seckillOrder);
            //基于内存(数据库)减库存
            //seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
            //在redis队列(单线程)减库存
            Long increment = redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_STACK_COUNT).increment(id, -1L);
            seckillGoods.setStockCount(increment.intValue());

            //减库存之后库存等于0
            if (seckillGoods.getStockCount() <= 0) {
                //redis商品列表数据进行更新
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                //redis中删除该商品
                redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_PREFIX + time).delete(id);
            } else {
                //更新redis中该商品的库存
                redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_PREFIX + time).put(id, seckillGoods);
            }


            //多线程测试
            System.out.println("用户" + Thread.currentThread().getName() + "正在下单" + System.currentTimeMillis());


            //创建订单成功,改变排队队列状态信息
            seckillStatus.setStatus(2);
            //订单id
            seckillStatus.setOrderId(seckillOrder.getId());
            System.out.println("订单号为=="+seckillStatus.getOrderId());
            //档单金额
            seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
            //更新排队信息状态
            redisTemplate.boundHashOps(Constant.SEC_KILL_USER_STATUS_KEY).put(name, seckillStatus);
            //调用发送延时消息
            sendTimerMessage(seckillStatus);
        }

    }
    //发送redis排队状态的延时消息
    public void sendTimerMessage(SeckillStatus seckillStatus){

        rabbitTemplate.convertAndSend(env.getProperty("mq.pay.queue.seckillordertimerdelay"), seckillStatus, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("30000");
                System.out.println("延时消息发送");
                return message;
            }
        });
    }
}
