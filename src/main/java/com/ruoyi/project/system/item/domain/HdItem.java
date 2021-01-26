package com.ruoyi.project.system.item.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 商品明细对象 hd_item
 *
 * @author ruoyi
 * @date 2020-02-15
 */
public class HdItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long itemId;

    /**
     * 店铺id
     */
    @Excel(name = "店铺id")
    private Long shopId;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long userId;

    /**
     * 商品sku
     */
    @Excel(name = "商品sku")
    private String itemSku;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String name;

    /**
     * 商品描述
     */
    @Excel(name = "商品描述")
    private String description;

    /**
     * 图片集合jsonArray
     */
    @Excel(name = "图片集合jsonArray")
    private String images;

    /**
     * 货币单位，三位代码
     */
    @Excel(name = "货币单位，三位代码")
    private String currency;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private Double price;

    /**
     * 商品创建时间
     */
    @Excel(name = "商品创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date itemCreateTime;

    /**
     * 商品修改时间
     */
    @Excel(name = "商品修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date itemUpdateTime;

    /**
     * 分类id
     */
    @Excel(name = "分类id")
    private Long categoryId;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String categoryName;

    private String variationSku;
    private String variationName;
    private String variationQuantityPurchased;

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setItemCreateTime(Date itemCreateTime) {
        this.itemCreateTime = itemCreateTime;
    }

    public Date getItemCreateTime() {
        return itemCreateTime;
    }

    public void setItemUpdateTime(Date itemUpdateTime) {
        this.itemUpdateTime = itemUpdateTime;
    }

    public Date getItemUpdateTime() {
        return itemUpdateTime;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getVariationSku() {
        return variationSku;
    }

    public void setVariationSku(String variationSku) {
        this.variationSku = variationSku;
    }

    public String getVariationName() {
        return variationName;
    }

    public void setVariationName(String variationName) {
        this.variationName = variationName;
    }

    public String getVariationQuantityPurchased() {
        return variationQuantityPurchased;
    }

    public void setVariationQuantityPurchased(String variationQuantityPurchased) {
        this.variationQuantityPurchased = variationQuantityPurchased;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("itemId", getItemId())
                .append("shopId", getShopId())
                .append("userId", getUserId())
                .append("itemSku", getItemSku())
                .append("status", getStatus())
                .append("name", getName())
                .append("description", getDescription())
                .append("images", getImages())
                .append("currency", getCurrency())
                .append("price", getPrice())
                .append("itemCreateTime", getItemCreateTime())
                .append("itemUpdateTime", getItemUpdateTime())
                .append("categoryId", getCategoryId())
                .append("categoryName", getCategoryName())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
