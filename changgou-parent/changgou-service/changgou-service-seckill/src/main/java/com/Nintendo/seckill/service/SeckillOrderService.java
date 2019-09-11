package com.Nintendo.seckill.service;

import com.Nintendo.seckill.pojo.SeckillOrder;
import com.Nintendo.seckill.pojo.SeckillStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/****
 * @Author:admin
 * @Description:SeckillOrder业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SeckillOrderService {

    /***
     * SeckillOrder多条件分页查询
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size);

    /***
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(int page, int size);

    /***
     * SeckillOrder多条件搜索方法
     * @param seckillOrder
     * @return
     */
    List<SeckillOrder> findList(SeckillOrder seckillOrder);

    /***
     * 删除SeckillOrder
     * @param id
     */
    void delete(Long id);

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     */
    void update(SeckillOrder seckillOrder);

    /***
     * 新增SeckillOrder
     * @param seckillOrder
     */
    void add(SeckillOrder seckillOrder);

    /**
     * 根据ID查询SeckillOrder
     * @param id
     * @return
     */
     SeckillOrder findById(Long id);

    /***
     * 查询所有SeckillOrder
     * @return
     */
    List<SeckillOrder> findAll();

    /**
     * 秒杀商品下单
     * @param id
     * @param name
     * @param time
     * @return
     */
    boolean addOrder(Long id, String name, String time);

    /**
     * 查询秒杀订单成功
     * @param username
     * @return
     */
    SeckillStatus queryStatus(String username);
    /**
     * 关闭微信订单
     */
    Map<String, String> closeWeixinPay(Long out_trade_no) throws Exception;

    /**
     * 删除关于用户redis排队信息的相关列表
     *
     * @param username
     */
   void deleteUserQueue(String username);
    /**
     * 恢复redis库存
     * @param seckillStatus
     * @param seckillOrder
     */
    void receiveRedis(SeckillStatus seckillStatus, SeckillOrder seckillOrder);
}
