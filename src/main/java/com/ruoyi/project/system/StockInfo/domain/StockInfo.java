package com.ruoyi.project.system.StockInfo.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 库存管理对象 stock_info
 *
 * @author wqf
 * @date 2020-02-18
 */
public class StockInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

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
     * 商品缩略图
     */
    private String goodsImageUrl;

    /**
     * 库存量
     */
    @Excel(name = "库存量")
    private Long goodsCount;

    /**
     * 待入库数量
     */
    @Excel(name = "待入库数量")
    private Long pendigStorageCount;

    /**
     * 待出库数量
     */
    @Excel(name = "待出库数量")
    private Long outStorageCount;

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

    public void setPendigStorageCount(Long pendigStorageCount) {
        this.pendigStorageCount = pendigStorageCount;
    }

    public Long getPendigStorageCount() {
        return pendigStorageCount;
    }

    public void setOutStorageCount(Long outStorageCount) {
        this.outStorageCount = outStorageCount;
    }

    public Long getOutStorageCount() {
        return outStorageCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("categoryName", getCategoryName())
                .append("goodsName", getGoodsName())
                .append("goodsImageUrl", getGoodsImageUrl())
                .append("goodsCount", getGoodsCount())
                .append("pendigStorageCount", getPendigStorageCount())
                .append("outStorageCount", getOutStorageCount())
                .toString();
    }
}
