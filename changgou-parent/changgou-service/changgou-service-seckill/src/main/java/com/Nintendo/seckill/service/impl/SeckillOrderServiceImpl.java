package com.Nintendo.seckill.service.impl;

import com.Nintendo.constant.Constant;
import com.Nintendo.entity.HttpClient;
import com.Nintendo.entity.IdWorker;
import com.Nintendo.entity.StatusCode;
import com.Nintendo.seckill.dao.SeckillGoodsMapper;
import com.Nintendo.seckill.dao.SeckillOrderMapper;;
import com.Nintendo.seckill.pojo.SeckillGoods;
import com.Nintendo.seckill.pojo.SeckillOrder;
import com.Nintendo.seckill.pojo.SeckillStatus;
import com.Nintendo.seckill.service.SeckillGoodsService;
import com.Nintendo.seckill.service.SeckillOrderService;
import com.Nintendo.seckill.task.MultiThreadingCreateOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 * @Author:admin
 * @Description:SeckillOrder业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    @Value("${weixin.notifyurl}")
    private String notifyurl;

    @Autowired(required = false)
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired(required = false)
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    /**
     * SeckillOrder条件+分页查询
     *
     * @param seckillOrder 查询条件
     * @param page         页码
     * @param size         页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    /**
     * SeckillOrder分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    /**
     * SeckillOrder条件查询
     *
     * @param seckillOrder
     * @return
     */
    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder) {
        //构建查询条件
        Example example = createExample(seckillOrder);
        //根据构建的条件查询数据
        return seckillOrderMapper.selectByExample(example);
    }


    /**
     * SeckillOrder构建查询对象
     *
     * @param seckillOrder
     * @return
     */
    public Example createExample(SeckillOrder seckillOrder) {
        Example example = new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if (seckillOrder != null) {
            // 主键
            if (!StringUtils.isEmpty(seckillOrder.getId())) {
                criteria.andEqualTo("id", seckillOrder.getId());
            }
            // 秒杀商品ID
            if (!StringUtils.isEmpty(seckillOrder.getSeckillId())) {
                criteria.andEqualTo("seckillId", seckillOrder.getSeckillId());
            }
            // 支付金额
            if (!StringUtils.isEmpty(seckillOrder.getMoney())) {
                criteria.andEqualTo("money", seckillOrder.getMoney());
            }
            // 用户
            if (!StringUtils.isEmpty(seckillOrder.getUserId())) {
                criteria.andEqualTo("userId", seckillOrder.getUserId());
            }
            // 创建时间
            if (!StringUtils.isEmpty(seckillOrder.getCreateTime())) {
                criteria.andEqualTo("createTime", seckillOrder.getCreateTime());
            }
            // 支付时间
            if (!StringUtils.isEmpty(seckillOrder.getPayTime())) {
                criteria.andEqualTo("payTime", seckillOrder.getPayTime());
            }
            // 状态，0未支付，1已支付
            if (!StringUtils.isEmpty(seckillOrder.getStatus())) {
                criteria.andEqualTo("status", seckillOrder.getStatus());
            }
            // 收货人地址
            if (!StringUtils.isEmpty(seckillOrder.getReceiverAddress())) {
                criteria.andEqualTo("receiverAddress", seckillOrder.getReceiverAddress());
            }
            // 收货人电话
            if (!StringUtils.isEmpty(seckillOrder.getReceiverMobile())) {
                criteria.andEqualTo("receiverMobile", seckillOrder.getReceiverMobile());
            }
            // 收货人
            if (!StringUtils.isEmpty(seckillOrder.getReceiver())) {
                criteria.andEqualTo("receiver", seckillOrder.getReceiver());
            }
            // 交易流水
            if (!StringUtils.isEmpty(seckillOrder.getTransactionId())) {
                criteria.andEqualTo("transactionId", seckillOrder.getTransactionId());
            }
        }
        return example;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillOrder
     *
     * @param seckillOrder
     */
    @Override
    public void update(SeckillOrder seckillOrder) {
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 增加SeckillOrder
     *
     * @param seckillOrder
     */
    @Override
    public void add(SeckillOrder seckillOrder) {

        seckillOrderMapper.insert(seckillOrder);
    }

    /**
     * 根据ID查询SeckillOrder
     *
     * @param id
     * @return
     */
    @Override
    public SeckillOrder findById(Long id) {
        return seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询SeckillOrder全部数据
     *
     * @return
     */
    @Override
    public List<SeckillOrder> findAll() {

        return seckillOrderMapper.selectAll();
    }

    /**
     * 秒杀商品下单任务
     *
     * @param id
     * @param name
     * @param time
     * @return
     */
    @Override
    public boolean addOrder(Long id, String name, String time) {

        //每个人的排队次数统计
        Long count = redisTemplate.boundHashOps(Constant.SEC_KILL_USER_QUEUE_COUNT).increment(name, 1);
        if (count>1){
            //重复排队
            throw new RuntimeException(String.valueOf(StatusCode.REPERROR));
        }
        //排队信息进行封装,存入redis
        SeckillStatus seckillStatus = new SeckillStatus(name, new Date(), 1, id, time);
        //放入排队队列
        redisTemplate.boundListOps(Constant.SEC_KILL_USER_QUEUE_KEY).leftPush(seckillStatus);
        //设定排队状态
        redisTemplate.boundHashOps(Constant.SEC_KILL_USER_STATUS_KEY).put(name, seckillStatus);
        //多线程抢单
        multiThreadingCreateOrder.createOrder();
        return true;
    }

    /**
     * 用户查询订单任务成功
     *
     * @param username
     * @return
     */
    @Override
    public SeckillStatus queryStatus(String username) {
        return (SeckillStatus) redisTemplate.boundHashOps(Constant.SEC_KILL_USER_STATUS_KEY).get(username);

    }

    /**
     * 关闭微信订单,返回结果集
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> closeWeixinPay(Long out_trade_no) throws Exception {
        //关闭微信订单
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("appid", appid);
        paraMap.put("mch_id", partner);
        //获得随机字符串
        paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paraMap.put("out_trade_no",String.valueOf(out_trade_no) );
        //转成xml,自动添加签名
        String paramXml = WXPayUtil.generateSignedXml(paraMap, partnerkey);
        //创建httpclient模拟浏览器发送关闭微信订单请求
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
        httpClient.setHttps(true);
        httpClient.setXmlParam(paramXml);
        httpClient.post();
        //获取微信支付系统返回的响应结果(xml格式的字符串)
        Map<String, String> closeMap = WXPayUtil.xmlToMap(httpClient.getContent());
        return closeMap;
    }
    /**
     * 删除关于用户redis排队信息的相关列表
     *
     * @param username
     */
    @Override
    public void deleteUserQueue(String username) {

        //删除用户排队次数统计标识
        redisTemplate.boundHashOps(Constant.SEC_KILL_USER_QUEUE_COUNT).delete(username);
        //删除排队状态信息
        redisTemplate.boundHashOps(Constant.SEC_KILL_USER_STATUS_KEY).delete(username);

    }
    /**
     * 恢复redis库存
     * @param seckillStatus
     * @param seckillOrder
     */
    @Override
    public void receiveRedis(SeckillStatus seckillStatus, SeckillOrder seckillOrder) {

        // 支付失败,恢复数据库信息
        // 1.压入商品库存队列
        redisTemplate.boundListOps(Constant.SEC_KILL_GOODS_COUNT_PREFIX + seckillOrder.getSeckillId()).leftPush(seckillOrder.getSeckillId());
        //2.恢复时间段列表中该商品的库存
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_PREFIX + seckillStatus.getTime())
                .get(seckillOrder.getSeckillId());

        //如果购买商品为last,此时redis已清除列表
        //从数据中查询出该商品,恢复其库存
        if (null == seckillGoods) {
            seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillOrder.getSeckillId());
        }
        //3.压入商品库存计数队列
        Long increment = redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_STACK_COUNT)
                .increment(seckillOrder.getSeckillId(), 1L);

        seckillGoods.setStockCount(increment.intValue());
    }
}
