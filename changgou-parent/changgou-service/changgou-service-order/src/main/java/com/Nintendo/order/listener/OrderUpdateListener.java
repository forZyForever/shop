package com.Nintendo.order.listener;

import com.Nintendo.order.service.OrderService;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Package: com.Nintendo.order.listener
 * @Author: ZZM
 * @Date: Created in 2019/9/4 17:59
 * @Address:CN.SZ
 **/
@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class OrderUpdateListener {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void handlerData(String msg) {
            Map<String, String> map = JSON.parseObject(msg, Map.class);
            if (null != map) {
                if (map.get("return_code").equalsIgnoreCase("success")) {
                    orderService.updateStatus(map.get("out_trade_no"), map.get("transaction_id"),map.get("time_end"));
                }
            }
    }
}
