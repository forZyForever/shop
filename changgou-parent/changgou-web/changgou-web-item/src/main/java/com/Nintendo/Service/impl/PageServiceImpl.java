package com.Nintendo.Service.impl;

import com.Nintendo.Service.PageService;
import com.Nintendo.entity.Result;
import com.Nintendo.goods.feign.CategoryFeign;
import com.Nintendo.goods.feign.SkuFeign;
import com.Nintendo.goods.feign.SpuFeign;
import com.Nintendo.goods.pojo.Sku;
import com.Nintendo.goods.pojo.Spu;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Package: com.Nintendo.Service.impl
 * @Author: ZZM
 * @Date: Created in 2019/8/28 10:46
 * @Address:CN.SZ
 **/
@Service
public class PageServiceImpl implements PageService {
    @Autowired(required = false)
    private CategoryFeign categoryFeign;
    @Autowired(required = false)
    private SpuFeign spuFeign;
    @Autowired(required = false)
    private SkuFeign skuFeign;
    @Autowired
    private TemplateEngine templateEngine;
    //生成静态文件路径
    @Value("${pagepath}")
    private String pagepath;

    /**
     * 构建数据模型
     * 查询分类
     * sku的标题和价格
     * sku的规格信息
     * spu的信息(图片列表)
     * @param spuId
     */

    public Map<String, Object> buildDataModel(Long spuId) {
        //构建数据模型
        Map<String,Object> dataMap = new HashMap<>();
        //获取spu 和SKU列表
        Result<Spu> result = spuFeign.findById(spuId);
        Spu spu = result.getData();

        //获取分类信息
        dataMap.put("category1",categoryFeign.findById(spu.getCategory1Id()).getData());
        dataMap.put("category2",categoryFeign.findById(spu.getCategory2Id()).getData());
        dataMap.put("category3",categoryFeign.findById(spu.getCategory3Id()).getData());
        if(spu.getImages()!=null) {
            dataMap.put("imageList", spu.getImages().split(","));
        }

        dataMap.put("specificationList", JSON.parseObject(spu.getSpecItems(),Map.class));
        dataMap.put("spu",spu);

        //根据spuId查询Sku集合
        Sku skuCondition = new Sku();
        skuCondition.setSpuId(spu.getId());
        Result<List<Sku>> resultSku = skuFeign.findList(skuCondition);
        dataMap.put("skuList",resultSku.getData());
        return dataMap;
    }

    /**
     * 生成静态页面
     * @param spuId
     */
    @Override
    public void createHtml(Long spuId) {
        // 上下文=模板+数据集
        Context context = new Context();
        Map<String, Object> model = buildDataModel(spuId);
        //设置模板
        context.setVariables(model);
        //准备文件
        File dir=new File(pagepath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File dest=new File(dir,spuId+".html");
        //生成页面
        try (PrintWriter writer=new PrintWriter(dest,"UTF-8")){
            templateEngine.process("item",context,writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除静态页面
     * @param spuId
     */
    @Override
    public void deleteHtml(Long spuId){
        String fileName=spuId+".html";
        File file=new File(pagepath);
        for (File file1 : file.listFiles()) {
            //如果文件名一样且是文件进行删除
                if (file1.isFile()&&file1.getName().equals(fileName)){
                        file1.delete();
                }
        }
    }

}
