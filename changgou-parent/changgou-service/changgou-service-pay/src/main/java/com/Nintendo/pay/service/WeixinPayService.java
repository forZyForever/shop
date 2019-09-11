package com.Nintendo.pay.service;

import java.util.Map;

/**
 * @Package: com.Nintendo.pay.service
 * @Author: ZZM
 * @Date: Created in 2019/9/4 13:52
 * @Address:CN.SZ
 **/
public interface WeixinPayService {
    /**
     * 创建二维码地址并返回前端生成二维码图片
     *
     * @param params
     * @return
     */
    Map<String, String> createNative(Map<String,String> params);

    /**
     * 根据订单号查询订单状态成功
     * @param out_trade_no
     * @return
     */
    Map<String, String> queryStatus(String out_trade_no);
}
