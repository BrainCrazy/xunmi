package com.ruoyi.project.system.userpackage.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户快递对象 user_package
 *
 * @author wqf
 * @date 2020-02-15
 */
public class UserPackage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 快递单号
     */
    @Excel(name = "快递单号")
    private String packageNo;

    /**
     * 虾皮订单号
     */
    private String shopeeOrderNo;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String goodsName;

    private String categoryName;

    private int goodsCount;
    /**
     * 缩略图
     */
    @Excel(name = "缩略图")
    private String goodsImage;

    /**
     * 仓库拍图
     */
    @Excel(name = "仓库拍图")
    private String stockImage;

    private String variationName;

    private Integer photograph;

    private Integer status;

    private String itemSku;
    private String price;
    //类型字段,适配,数据库中不存在
    private int orderType = 10;


    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPhotograph() {
        return photograph;
    }

    public void setPhotograph(Integer photograph) {
        this.photograph = photograph;
    }

    public String getVariationName() {
        return variationName;
    }

    public void setVariationName(String variationName) {
        this.variationName = variationName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setShopeeOrderNo(String shopeeOrderNo) {
        this.shopeeOrderNo = shopeeOrderNo;
    }

    public String getShopeeOrderNo() {
        return shopeeOrderNo;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setStockImage(String stockImage) {
        this.stockImage = stockImage;
    }

    public String getStockImage() {
        return stockImage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("packageNo", getPackageNo())
                .append("shopeeOrderNo", getShopeeOrderNo())
                .append("goodsName", getGoodsName())
                .append("goodsImage", getGoodsImage())
                .append("stockImage", getStockImage())
                .toString();
    }
}
