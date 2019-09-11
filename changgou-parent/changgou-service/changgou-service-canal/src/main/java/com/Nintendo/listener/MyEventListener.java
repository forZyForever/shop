package com.Nintendo.listener;

import com.Nintendo.content.feign.ContentFeign;
import com.Nintendo.content.pojo.Content;
import com.Nintendo.entity.Result;
import com.Nintendo.item.feign.PageFeign;
import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @Package: com.Nintendo.listener
 * @Author: ZZM
 * @Date: Created in 2019/8/23 15:07
 * @Address:CN.SZ
 **/
//canal监听数据库的变化
@CanalEventListener
public class MyEventListener {
    /* //进行添加的时候触发
     //eventType监听到的操作类型:监听到的操作的类型  INSERT  UPDATE ,DELETE ,CREATE INDEX ,GRAND
     //rowData 监听到被修改的数据
     @InsertListenPoint
     public void onEvent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
         List<CanalEntry.Column> list = rowData.getAfterColumnsList();
         for (CanalEntry.Column column : list) {
             System.out.println(column.getName()+":"+column.getValue());
         }
     }
     //进行更改时触发
     @UpdateListenPoint
     public void onEvent1(CanalEntry.RowData rowData) {
         List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
         for (CanalEntry.Column column : afterColumnsList) {
             System.out.println(column.getName()+":"+column.getValue());
         }
     }
     //进行删除时触发
     @DeleteListenPoint
     public void onEvent3(CanalEntry.EventType eventType,CanalEntry.RowData rowData) {
         List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
         for (CanalEntry.Column column : afterColumnsList) {
             System.out.println(column.getName()+":"+column.getValue());
         }
     }
     //自定义触发类型
     //destination表示表示一个目的地(和配置文件保持一致,# /home/admin/canal-server/conf/example
     //schema 要监听的数据库
     //table 要进行监听的数据库的表
     //eventType 要进行监听的类型
     @ListenPoint(destination = "example", schema = "canal-test", table = {"t_user", "test_table"}, eventType = CanalEntry.EventType.UPDATE)
     public void onEvent4(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
         List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
         for (CanalEntry.Column column : afterColumnsList) {
             System.out.println(column.getName()+":"+column.getValue());
         }
     }*/
    @Autowired(required = false)
    private ContentFeign contentFeign;
    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    @ListenPoint(destination = "example", schema = "changgou_content", table = {"tb_content"}, eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.DELETE, CanalEntry.EventType.INSERT})
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //获取到被修改的categoryId
        String categoryId = getColumnValue(eventType, rowData);
        //调用feign获取数据
        Result<List<Content>> contents = contentFeign.findCategoryById(Long.valueOf(categoryId));
        //存储到redis中去
        List<Content> data = contents.getData();
        stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(data));
    }

    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId = "";
        //如果是删除获取之前的数据,是修改或者新增获取之后的数据
        if (CanalEntry.EventType.DELETE == eventType) {
            List<CanalEntry.Column> list = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : list) {
                if (column.getName().equalsIgnoreCase("category_id")) {
                    categoryId = column.getValue();
                    return categoryId;
                }
            }
        } else {
            List<CanalEntry.Column> list = rowData.getAfterColumnsList();
            for (CanalEntry.Column column : list) {
                if (column.getName().equalsIgnoreCase("category_id")) {
                    categoryId = column.getValue();
                    return categoryId;
                }
            }
        }
        return categoryId;
    }

    //监听spu的数据
    @Autowired
    private PageFeign PageFeign;

    @ListenPoint(destination = "example", schema = "changgou_goods", table = {"tb_spu"}, eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.DELETE, CanalEntry.EventType.INSERT})
    public void onEventCustomSpu(CanalEntry.EventType entryType, CanalEntry.RowData rowData) {
        //删除操作
        if (CanalEntry.EventType.DELETE == entryType) {
            String spuId = "";
            List<CanalEntry.Column> ColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : ColumnsList) {
                if (column.getName().equals("id")) {
                    spuId = column.getValue();
                    System.out.println(column.getName()+":"+column.getValue());
                    break;
                }
            }
            //删除静态页
            PageFeign.deleteHtml(Long.valueOf(spuId));
        } else {
            //新增或者修改操作
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            String spuId = "";
            for (CanalEntry.Column column : afterColumnsList) {
                if (column.getName().equals("id")) {
                    spuId = column.getValue();
                    System.out.println(column.getName()+":"+column.getValue());
                    break;
                }
            }
            //调用feign生成静态页
            PageFeign.createHtml(Long.valueOf(spuId));
        }
    }
}