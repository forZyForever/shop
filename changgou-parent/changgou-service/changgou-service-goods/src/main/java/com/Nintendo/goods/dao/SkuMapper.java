package com.Nintendo.goods.dao;

import com.Nintendo.goods.pojo.Sku;
import com.Nintendo.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Package: com.Nintendo.goods.dao
 * @Author: ZZM
 * @Date: Created in 2019/8/21 0:50
 * @Address:CN.SZ
 **/
public interface SkuMapper extends Mapper<Sku> {
    @Update(value = "update tb_sku set num=num-#{num},sale_num=sale_num-#{num} where id=#{skuId} and num>=#{num}")
    int decrCount(OrderItem orderItem);
}
