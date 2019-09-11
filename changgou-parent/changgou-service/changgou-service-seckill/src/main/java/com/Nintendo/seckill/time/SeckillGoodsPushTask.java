package com.Nintendo.seckill.time;

import com.Nintendo.constant.Constant;
import com.Nintendo.entity.DateUtil;
import com.Nintendo.seckill.dao.SeckillGoodsMapper;
import com.Nintendo.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Package: com.Nintendo.seckill.time
 * @Author: ZZM
 * @Date: Created in 2019/9/5 20:58
 * @Address:CN.SZ
 **/
//spring task 定时任务(多线程)
@Component
public class SeckillGoodsPushTask {

    @Autowired(required = false)
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void loadGoodsPushRedis() {
        /**
         * select * from tb_seckill_goods where
         stock_count>0
         and `status`='1'
         and start_time > 开始时间段
         and end_time < 开始时间段+2hour  and id  not in (redis中已有的id)
         */
        //获得当前时间对应的5个时间段
        List<Date> dateMenus = DateUtil.getDateMenus();

        for (Date dateTime : dateMenus) {
            //转化日期格式
            String date = DateUtil.data2str(dateTime, DateUtil.PATTERN_YYYYMMDDHH);

            //将循环到的时间段作为条件 从数据库中执行查询 得出数据集
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            //通过审核
            criteria.andEqualTo("status", 1);
            //库存数量大于0
            criteria.andGreaterThan("stockCount", 0);
            //当前时间大于或等于开始时间
            criteria.andGreaterThanOrEqualTo("startTime", dateTime);
            //小于结束时间
            criteria.andLessThan("endTime", DateUtil.addDateHour(dateTime, 2));

            //移除掉redis中已有的,避免重复
            Set key = redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_PREFIX + date).keys();
            if (null != key && key.size() > 0) {
                criteria.andNotIn("id", key);
            }

            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

            //将商品存储到redis中去,key为时间段YYYYMMDDHH field为商品id value为商品详情
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_PREFIX + date).put(seckillGood.getId(), seckillGood);
                //设置有效期
                redisTemplate.expireAt(Constant.SEC_KILL_GOODS_PREFIX + date, DateUtil.addDateHour(dateTime, 2));
                //实时商品库存队列
                pushGoods(seckillGood);
                //添加一个商品库存计数器(key为商品id,value为库存id)
                redisTemplate.boundHashOps(Constant.SEC_KILL_GOODS_STACK_COUNT).increment(seckillGood.getId(),seckillGood.getStockCount());

            }
        }
    }
        //按照商品实时库存存入队列中
        public void pushGoods(SeckillGoods seckillGoods){
            for (int i = 0; i < seckillGoods.getStockCount(); i++) {
                redisTemplate.boundListOps(Constant.SEC_KILL_GOODS_COUNT_PREFIX+seckillGoods.getId()).leftPush(seckillGoods.getId());

            }
        }
}
