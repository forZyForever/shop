package com.Nintendo.pay.service.impl;

import com.Nintendo.entity.HttpClient;
import com.Nintendo.pay.service.WeixinPayService;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package: com.Nintendo.pay.service.impl
 * @Author: ZZM
 * @Date: Created in 2019/9/4 13:54
 * @Address:CN.SZ
 **/
@Service
public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    @Value("${weixin.notifyurl}")
    private String notifyurl;

    /**
     * 使用httpClient发送post请求生成code_url返回给前端
     * @param params
     * @return
     */
    @Override
    public Map<String, String> createNative(Map<String,String> params) {
        try {
            //创建参数对象,用于组合参数
            Map<String, String> paraMap = new HashMap<>();
            //设置需要的参数,根据微信接口需要的文档
            paraMap.put("appid", appid);
            paraMap.put("mch_id", partner);
            //获得随机字符串
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paraMap.put("body", "畅购");
            paraMap.put("out_trade_no", params.get("out_trade_no"));
            //支付总金额,单位是分
            paraMap.put("total_fee", params.get("total_fee"));
            //终端的ip
            paraMap.put("spbill_create_ip", "127.0.0.1");
            paraMap.put("notify_url", notifyurl);
            //扫码支付类型
            paraMap.put("trade_type", "NATIVE");
                //把队列,交换机等信息放到attach中传送过去
            Map<String,String> attach=new HashMap<>();
            attach.put("username",params.get("username"));
            attach.put("queue",params.get("queue"));
            attach.put("routingKey",params.get("routingKey"));
            attach.put("exchange",params.get("exchange"));
            //attach附加信息
            paraMap.put("attach", JSON.toJSONString(attach));
            System.out.println(params);
            //转成xml,自动添加签名
            String paramXml = WXPayUtil.generateSignedXml(paraMap, partnerkey);
            //创建httpclient模拟浏览器发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置https协议
            httpClient.setHttps(true);
            //设置请求体
            httpClient.setXmlParam(paramXml);
            //发送请求
            httpClient.post();
            //获取微信支付系统返回的响应结果(xml格式的字符串)
            String content = httpClient.getContent();
            System.out.println(content);
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("out_trade_no",  params.get("out_trade_no"));
            resultMap.put("total_fee", params.get("total_fee"));
            resultMap.put("code_url", map.get("code_url"));
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据订单号查询订单状态
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public Map<String, String> queryStatus(String out_trade_no) {
        try {
            //创建参数对象
            Map<String, String> map = new HashMap<>();
            //设置参数值
            map.put("appid", appid);
            map.put("mch_id", partner);
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("out_trade_no", out_trade_no);
            //转成xml字符串,自动签名
            String signedXml = WXPayUtil.generateSignedXml(map, partnerkey);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            //设置请求体
            httpClient.setXmlParam(signedXml);
            //发送请求
            httpClient.post();
            //获取内容
            String content = httpClient.getContent();
            System.out.println(content);
            Map<String, String> map1 = WXPayUtil.xmlToMap(content);
            return map1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
