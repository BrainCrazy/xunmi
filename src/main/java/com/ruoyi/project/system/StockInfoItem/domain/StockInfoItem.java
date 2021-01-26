package com.ruoyi.project.system.StockInfoItem.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 库存明细对象 stock_info_item
 *
 * @author wqf
 * @date 2020-02-18
 */
public class StockInfoItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date sysTime;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * stock_info表主键
     */
    private Long stockInfoId;

    /**
     * 虾皮订单号
     */
    @Excel(name = "虾皮订单号")
    private String shopeeOrderNo;

    /**
     * 快递单号
     */
    @Excel(name = "快递单号")
    private String packageNo;

    /**
     * 商品类别
     */
    @Excel(name = "商品类别")
    private String categoryName;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String goodsName;

    /**
     * 缩略图
     */
    @Excel(name = "缩略图")
    private String goodsImageUrl;

    /**
     * 商品数量
     */
    @Excel(name = "商品数量")
    private Long goodsCount;

    /**
     * 操作类型
     */
    @Excel(name = "操作类型")
    private Integer itemType;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer stockItemStatus;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setSysTime(Date sysTime) {
        this.sysTime = sysTime;
    }

    public Date getSysTime() {
        return sysTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setStockInfoId(Long stockInfoId) {
        this.stockInfoId = stockInfoId;
    }

    public Long getStockInfoId() {
        return stockInfoId;
    }

    public void setShopeeOrderNo(String shopeeOrderNo) {
        this.shopeeOrderNo = shopeeOrderNo;
    }

    public String getShopeeOrderNo() {
        return shopeeOrderNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
    }

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsCount(Long goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Long getGoodsCount() {
        return goodsCount;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setStockItemStatus(Integer stockItemStatus) {
        this.stockItemStatus = stockItemStatus;
    }

    public Integer getStockItemStatus() {
        return stockItemStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sysTime", getSysTime())
                .append("userId", getUserId())
                .append("stockInfoId", getStockInfoId())
                .append("shopeeOrderNo", getShopeeOrderNo())
                .append("packageNo", getPackageNo())
                .append("categoryName", getCategoryName())
                .append("goodsName", getGoodsName())
                .append("goodsImageUrl", getGoodsImageUrl())
                .append("goodsCount", getGoodsCount())
                .append("itemType", getItemType())
                .append("stockItemStatus", getStockItemStatus())
                .toString();
    }
}
