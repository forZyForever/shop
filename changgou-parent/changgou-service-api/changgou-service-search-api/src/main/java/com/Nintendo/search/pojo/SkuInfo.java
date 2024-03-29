package com.Nintendo.search.pojo;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Package: com.Nintendo.search.pojo
 * @Author: ZZM
 * @Date: Created in 2019/8/24 10:09
 * @Address:CN.SZ
 * indexName 指定创建的索引的名称
 * type :指定索引中的类型
 **/
@Document(indexName = "skuinfo",type = "docs")
public class SkuInfo implements Serializable {
    //商品主键
    //表示文档的唯一标识
    private Long id;
    //商品名称
    //@Field字段的映射
    //analyzer指定索引使用的分词器 searchAnalyzer 搜索的时候使用的分词器
    //type指定的数据类型
    @Field(type = FieldType.Text,analyzer = "ik_smart")
    private String name;
    //商品价格，单位为：元
    @Field(type = FieldType.Double)
    private Long price;
    //库存数量
    private Integer num;

    //商品图片
    private String image;

    //商品状态，1-正常，2-下架，3-删除
    private String status;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //是否默认
    private String isDefault;

    //SPUID
    private Long spuId;

    //类目ID
    private Long categoryId;

    //类目名称
    //keyword表示关键字,表示不分词
    @Field(type = FieldType.Keyword)
    private String categoryName;

    //品牌名称
    @Field(type = FieldType.Keyword)
    private String brandName;

    //规格
    private String spec;

    //规格参数
    //动态的域添加与变化
    //@Field(type = FieldType.Object)
    private Map<String,Object> specMap;

    public SkuInfo() {
    }

    public SkuInfo(Long id, String name, Long price, Integer num, String image, String status, Date createTime, Date updateTime, String isDefault, Long spuId, Long categoryId, String categoryName, String brandName, String spec, Map<String, Object> specMap) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.num = num;
        this.image = image;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDefault = isDefault;
        this.spuId = spuId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.spec = spec;
        this.specMap = specMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Map<String, Object> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, Object> specMap) {
        this.specMap = specMap;
    }

    @Override
    public String toString() {
        return "SkuInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDefault='" + isDefault + '\'' +
                ", spuId=" + spuId +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", spec='" + spec + '\'' +
                ", specMap=" + specMap +
                '}';
    }
}
