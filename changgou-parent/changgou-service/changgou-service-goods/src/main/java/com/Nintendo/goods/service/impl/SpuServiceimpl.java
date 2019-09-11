package com.Nintendo.goods.service.impl;

import com.Nintendo.entity.IdWorker;
import com.Nintendo.goods.dao.*;
import com.Nintendo.goods.pojo.CategoryBrand;
import com.Nintendo.goods.pojo.Goods;
import com.Nintendo.goods.pojo.Sku;
import com.Nintendo.goods.pojo.Spu;
import com.Nintendo.goods.service.SpuService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Package: com.Nintendo.goods.service.impl
 * @Author: ZZM
 * @Date: Created in 2019/8/20 22:04
 * @Address:CN.SZ
 **/
@Service
public class SpuServiceimpl implements SpuService {
    @Autowired
    private IdWorker idWorker;
    @Autowired(required = false)
    private SpuMapper spuMapper;
    @Autowired(required = false)
    private CategoryMapper categoryMapper;
    @Autowired(required = false)
    private BrandMapper brandMapper;
    @Autowired(required = false)
    private SkuMapper skuMapper;
    @Autowired(required = false)
    private CategoryBrandMapper categoryBrandMapper;
    /**
     * Spu条件+分页查询
     * @param spu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(spu);
        //执行搜索
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     * @param spu
     * @return
     */
    @Override
    public List<Spu> findList(Spu spu){
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     * @param spu
     * @return
     */
    public Example createExample(Spu spu){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //只显示未删除的
        criteria.andEqualTo("isDelete",0);
        if(spu!=null){
            // 主键
            if(!StringUtils.isEmpty(spu.getId())){
                criteria.andEqualTo("id",spu.getId());
            }
            // 货号
            if(!StringUtils.isEmpty(spu.getSn())){
                criteria.andEqualTo("sn",spu.getSn());
            }
            // SPU名
            if(!StringUtils.isEmpty(spu.getName())){
                criteria.andLike("name","%"+spu.getName()+"%");
            }
            // 副标题
            if(!StringUtils.isEmpty(spu.getCaption())){
                criteria.andEqualTo("caption",spu.getCaption());
            }
            // 品牌ID
            if(!StringUtils.isEmpty(spu.getBrandId())){
                criteria.andEqualTo("brandId",spu.getBrandId());
            }
            // 一级分类
            if(!StringUtils.isEmpty(spu.getCategory1Id())){
                criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            // 二级分类
            if(!StringUtils.isEmpty(spu.getCategory2Id())){
                criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            // 三级分类
            if(!StringUtils.isEmpty(spu.getCategory3Id())){
                criteria.andEqualTo("category3Id",spu.getCategory3Id());
            }
            // 模板ID
            if(!StringUtils.isEmpty(spu.getTemplateId())){
                criteria.andEqualTo("templateId",spu.getTemplateId());
            }
            // 运费模板id
            if(!StringUtils.isEmpty(spu.getFreightId())){
                criteria.andEqualTo("freightId",spu.getFreightId());
            }
            // 图片
            if(!StringUtils.isEmpty(spu.getImage())){
                criteria.andEqualTo("image",spu.getImage());
            }
            // 图片列表
            if(!StringUtils.isEmpty(spu.getImages())){
                criteria.andEqualTo("images",spu.getImages());
            }
            // 售后服务
            if(!StringUtils.isEmpty(spu.getSaleService())){
                criteria.andEqualTo("saleService",spu.getSaleService());
            }
            // 介绍
            if(!StringUtils.isEmpty(spu.getIntroduction())){
                criteria.andEqualTo("introduction",spu.getIntroduction());
            }
            // 规格列表
            if(!StringUtils.isEmpty(spu.getSpecItems())){
                criteria.andEqualTo("specItems",spu.getSpecItems());
            }
            // 参数列表
            if(!StringUtils.isEmpty(spu.getParaItems())){
                criteria.andEqualTo("paraItems",spu.getParaItems());
            }
            // 销量
            if(!StringUtils.isEmpty(spu.getSaleNum())){
                criteria.andEqualTo("saleNum",spu.getSaleNum());
            }
            // 评论数
            if(!StringUtils.isEmpty(spu.getCommentNum())){
                criteria.andEqualTo("commentNum",spu.getCommentNum());
            }
            // 是否上架
            if(!StringUtils.isEmpty(spu.getIsMarketable())){
                criteria.andEqualTo("isMarketable",spu.getIsMarketable());
            }
            // 是否启用规格
            if(!StringUtils.isEmpty(spu.getIsEnableSpec())){
                criteria.andEqualTo("isEnableSpec",spu.getIsEnableSpec());
            }
            // 是否删除
            if(!StringUtils.isEmpty(spu.getIsDelete())){
                criteria.andEqualTo("isDelete",spu.getIsDelete());
            }
            // 审核状态
            if(!StringUtils.isEmpty(spu.getStatus())){
                criteria.andEqualTo("status",spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if ("0".equals(spu.getIsDelete())){
            throw new RuntimeException("此商品不能被删除");
        }
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Spu
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
    @Override
    public Spu findById(Long id){
        return  spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }
    /**
     * 添加商品/修改商品
     * spu+sku保存
     * @param goods
     */
    @Override
    @Transactional
    public void saveGoods(Goods goods) {
        Spu spu = goods.getSpu();
        //如果主键为空,则进行插入
        if (null == spu.getId()) {
            //雪花算法生产id
            spu.setId(idWorker.nextId());
            /**
             * 新增spu
             */
            spuMapper.insertSelective(spu);
        } else {//不为空,进行修改前先删除原sku表关系
            /**
             * 修改spu
             */
            spuMapper.updateByPrimaryKeySelective(spu);
            /**
             * 删除sku
             */
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            //修改时先删除原有sku表
            skuMapper.delete(sku);
        }
        /**
         * 新增sku和修改sku
         */
        String name = spu.getName();
        Date date = new Date();
        Integer category3Id = spu.getCategory3Id();
        List<Sku> skuList = goods.getSkuList();
            for (Sku sku : skuList) {
                //设置sku的id
                sku.setId(idWorker.nextId());
                //如果sku的规格参数列表为空
                if (StringUtils.isEmpty(sku.getSpec())) {
                    sku.setSpec("{}");
                }
                /**设置sku的名称
                 sku的名字是由spu的名字和sku通过getSpec获得value再转化为map遍历name+value拼接组成*/
                Map<String, String> map = JSON.parseObject(sku.getSpec(), Map.class);
                for (String value : map.values()) {
                    name += " " + value;
                }
                //设置名字
                sku.setName(name);
                //创建日期
                sku.setCreateTime(date);
                //更新日期
                sku.setUpdateTime(date);
                //设置spuId
                sku.setSpuId(spu.getId());
                //设置分类id
                sku.setCategoryId(category3Id);
                //设置分类名
                sku.setCategoryName(categoryMapper.selectByPrimaryKey(category3Id).getName());
                //设置品牌名
                sku.setBrandName(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
                skuMapper.insertSelective(sku);
            }
        /**
         * 关联品牌表与分类表
         */
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setCategoryId(spu.getCategory3Id());
        categoryBrand.setBrandId(spu.getBrandId());
        //判断中间表之间是否关联
        if (0 == categoryBrandMapper.selectCount(categoryBrand)) {
            categoryBrandMapper.insertSelective(categoryBrand);
        }
    }
    /**
     * 根据id查询商品(spu+sku)
     *
     * @param id
     * @return
     */
    @Override
    public Goods findGoodsById(long id) {
        //按照id查询到spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //再用查到的spu的id去sku表里找spuId对应的sku集合
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skus);
        return goods;
    }

    /**
     * 商品审核
     *
     * @param id
     */
    @Override
    public void audit(long id) {
        /**
         *  update tb_spu set status=1,is_marketable=1 where is_delete=0 and id = ?
         */
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //商品不存在
        if (null == spu) {
            throw new RuntimeException("商品不存在");
        }
        //商品已被删除
        if ("1".equals(spu.getIsDelete())) {
            throw new RuntimeException("商品已删除");
        }
        //审核通过,
        spu.setStatus("1");
        //进行上架
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品下架
     *
     * @param id
     */
    @Override
    public void pull(long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (null == spu) {
            throw new RuntimeException("商品不存在");
        }
        if ("1".equals(spu.getIsDelete())) {
            throw new RuntimeException("商品已被删除");
        }
        if ("0".equals(spu.getStatus()) || "0".equals(spu.getIsMarketable())) {
            throw new RuntimeException("商品未上架");
        }
        //下架
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品上架
     *
     * @param id
     */
    @Override
    public void put(long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //未被删除和通过审核且未上架
        if (null != spu && "0".equals(spu.getIsDelete()) && "1".equals(spu.getStatus()) && "0".equals(spu.getIsMarketable())) {
            spu.setIsMarketable("1");
            spuMapper.updateByPrimaryKeySelective(spu);
        } else {
            throw new RuntimeException("此物品不能上架");
        }
    }

    /**
     * 批量上架
     *
     * @param ids
     */
    @Override
    public int putMany(long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("1");
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //数组转化为list
        criteria.andIn("id", Arrays.asList(ids));
        //未被删除
        criteria.andEqualTo("isDelete", "0");
        //通过审核
        criteria.andEqualTo("status", "1");
        //物品未上架
        criteria.andEqualTo("isMarketable", "0");
        //上架
        spu.setIsMarketable("1");
        //更新
        return spuMapper.updateByExampleSelective(spu, example);
    }

    /**
     * 批量下架
     *
     * @param ids
     */
    @Override
    public int pullMany(long[] ids) {
        Spu spu = new Spu();
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        //商品已上架
        criteria.andEqualTo("isMarketable", "1");
        //商品未被删除
        criteria.andEqualTo("isDelete", "0");
        //商品已被审核
        criteria.andEqualTo("status", "1");
        //下架
        spu.setIsMarketable("0");
        return spuMapper.updateByExampleSelective(spu, example);
    }

    /**
     * 逻辑删除
     *
     * @param id
     */
    @Override
    public void logicDelete(long id) {
        //delete改为1,where delete =0 and id=?
        Spu spu = new Spu();
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        //未被删除
        criteria.andEqualTo("isDelete", "0");
        //已下架
        criteria.andEqualTo("isMarketable", "0");
        //逻辑删除
        spu.setIsDelete("1");
        //改为未审核
        spu.setStatus("0");
        spuMapper.updateByExampleSelective(spu, example);
    }

    /**
     * 数据恢复
     *
     * @param id
     */
    @Override
    public void restore(long id) {
        Spu spu = new Spu();
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        criteria.andEqualTo("isDelete", "1");
        //改为未被删除
        spu.setIsDelete("0");
        spu.setStatus("0");
        spuMapper.updateByExampleSelective(spu, example);
    }
}
