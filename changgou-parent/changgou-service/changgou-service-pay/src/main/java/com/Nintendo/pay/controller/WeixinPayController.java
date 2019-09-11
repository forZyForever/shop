package com.Nintendo.pay.controller;

import com.Nintendo.entity.Result;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.pay.service.WeixinPayService;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package: com.Nintendo.pay.controller
 * @Author: ZZM
 * @Date: Created in 2019/9/4 13:41
 * @Address:CN.SZ
 **/
@RestController
@RequestMapping("/weixin/pay")
public class WeixinPayController {

    @Autowired(required = false)
    private WeixinPayService weixinPayService;

    @Autowired
    private Environment env;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 创建二维码链接地址并返回给前端,生成二维码图片
     *
     *
     * @param params
     * @return
     */
    @GetMapping("/create/native")
    public Result createNative(@RequestParam Map<String,String> params) {
        Map<String, String> map = weixinPayService.createNative(params);
        return new Result(true, StatusCode.OK, "二维码链接地址生成成功", map);
    }

    /**
     * 根据订单号查询订单状态
     *
     * @param out_trade_no
     * @return
     */
    @GetMapping("/status/query")
    public Result<Map> queryStatus(String out_trade_no) {
        Map<String, String> map = weixinPayService.queryStatus(out_trade_no);
        return new Result<Map>(true, StatusCode.OK, "查询订单状态成功", map);
    }

    /**
     * 接收 微信支付通知的结果  结果(以流的形式传递过来)
     *
     * @param request
     * @return
     */
    @RequestMapping("/notify/url")
    public String receiveResult(HttpServletRequest request) {

        ServletInputStream is = null;
        ByteArrayOutputStream os = null;
        try {

            is = request.getInputStream();
            os = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            //转换成xml字符串
            byte[] bytes1 = os.toByteArray();

            //微信支付系统传递过来的xml的字符串
            String resultXml = new String(bytes1, "utf-8");
            System.out.println("===========");
            System.out.println(resultXml);

            //  接收到微信通知的结果后转成map
            Map<String, String> map = WXPayUtil.xmlToMap(resultXml);
            //发送消息给rabbitMq
            String attach = map.get("attach");
            //{username=zhangsan, out_trade_no=1168904209937072150, total_fee=1, queue=queue.seckillorder, routingkey=queue.seckillorder, exchange=exchange.seckillorder}
            Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
            System.out.println("获得的attach数据"+attachMap);
            String jsonString = JSON.toJSONString(map);
            rabbitTemplate.convertAndSend(attachMap.get("exchange"),attachMap.get("routingKey"),jsonString);
            System.out.println("rabbitMq发送消息开始");
            //接收到通知后,给微信返回通知结果
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("return_code", "SUCCESS");
            resultMap.put("return_msg", "OK");
            return WXPayUtil.mapToXml(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
