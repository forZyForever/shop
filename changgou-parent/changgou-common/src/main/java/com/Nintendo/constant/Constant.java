package com.Nintendo.constant;

/**
 * @Package: com.Nintendo.constant
 * @Author: ZZM
 * @Date: Created in 2019/9/5 22:04
 * @Address:CN.SZ
 **/
public class Constant {
    /**
     * 秒杀商品存储到前缀的KEY
     */
    public static final String SEC_KILL_GOODS_PREFIX="SeckillGoods_";
    /**
     * 预订单前缀key
     */
    public static final String SEC_KILL_ORDER_PREFIX="SeckillOrder";
    /**
     * 抢单队列封装
     */
    public static final String SEC_KILL_USER_QUEUE_KEY="SeckillOrderQueue";
    /**
     * 抢单队列状态
     */
    public static final String SEC_KILL_USER_STATUS_KEY="UserQueueStatus";
    /**
     * 用户排队统计
     */
    public static final  String SEC_KILL_USER_QUEUE_COUNT="UserQueueCount";
    /**
     * 商品库存队列
     */
    public static final String SEC_KILL_GOODS_COUNT_PREFIX="SeckillGoodsCountList_";
    /**
     * 商品库存计数队列
     */
    public static final  String SEC_KILL_GOODS_STACK_COUNT="SeckillGoodsCount";
}
