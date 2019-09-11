package com.Nintendo.seckill.listener;

import com.Nintendo.constant.Constant;
import com.Nintendo.seckill.dao.SeckillOrderMapper;
import com.Nintendo.seckill.pojo.SeckillOrder;
import com.Nintendo.seckill.pojo.SeckillStatus;
import com.Nintendo.seckill.service.SeckillOrderService;
import com.alibaba.fastjson.JSON;;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @Package: com.Nintendo.seckill.listener
 * @Author: ZZM
 * @Date: Created in 2019/9/7 18:22
 * @Address:CN.SZ
 **/
@Component
@RabbitListener(queues = "${mq.pay.queue.seckillorder}")
public class SeckillOrderPayListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillOrderService seckillOrderService;

    //接收秒杀下单信息
    @RabbitHandler
    public void handlerData(String msg) throws Exception {
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        if (null != map) {
            System.out.println("收到rabbitMQ的消息");
            String return_code = map.get("return_code");
            //返回结果
            if ("SUCCESS".equalsIgnoreCase(return_code)) {

                //业务结果
                String result_code = map.get("result_code");
                //从attach中获取用户名
                String username = (String) JSON.parseObject(map.get("attach"), Map.class).get("username");

                //获取排队状态
                SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps(Constant.SEC_KILL_USER_STATUS_KEY).get(username);
                //获取预订单信息
                SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(Constant.SEC_KILL_ORDER_PREFIX).get(username);
                //删除redis预订单
                redisTemplate.boundHashOps(Constant.SEC_KILL_ORDER_PREFIX).delete(username);

                if ("SUCCESS".equalsIgnoreCase(result_code)) {

                    //支付成功,恢复数据库
                    receiveDb(map, seckillOrder);

                    //删除关于用户redis排队信息的相关列表
                    seckillOrderService.deleteUserQueue(username);

                } else {
                    //关闭微信订单
                    Map<String, String> closeMap = seckillOrderService.closeWeixinPay(seckillStatus.getOrderId());
                    //关闭订单业务结果
                    String closeResult = closeMap.get("result_code");
                    //如果关闭成功
                    if ("SUCCESS".equalsIgnoreCase(closeResult)) {

                        //恢复redis列表库存
                       seckillOrderService. receiveRedis(seckillStatus, seckillOrder);

                        //清除排队信息相关队列
                        seckillOrderService.deleteUserQueue(username);

                    } else {
                        //如果关闭微信订单失败,查看错误码详情进行处理
                        String err_code = closeMap.get("err_code");
                        switch (err_code) {
                            //订单已支付，不能发起关单，当作已支付情况处理
                            case "ORDERPAID":
                                receiveDb(map, seckillOrder);
                                break;
                            default:
                                throw new RuntimeException("系统异常,请重新调用该API");
                        }
                    }
                }
            }
        }
    }

    /**
     * 支付成功恢复数据库
     *
     * @param map
     * @param seckillOrder
     * @throws ParseException
     */
    private void receiveDb(Map<String, String> map, SeckillOrder seckillOrder) throws ParseException {

        //支付成功的情况
        //已支付状态
        seckillOrder.setStatus("1");
        //设定支付日期
        seckillOrder.setPayTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(map.get("time_end")));
        //设定交易id
        seckillOrder.setTransactionId(map.get("transaction_id"));

        //同步到数据库
        seckillOrderMapper.insertSelective(seckillOrder);
    }

}