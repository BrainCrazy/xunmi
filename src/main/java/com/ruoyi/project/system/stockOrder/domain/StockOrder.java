package com.ruoyi.project.system.stockOrder.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 库存订单对象 stock_order
 *
 * @author wqf
 * @date 2020-02-19
 */
public class StockOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 虾皮订单号
     */
    @Excel(name = "虾皮订单号")
    private String shopeeOrderNo;

    /**
     * 授权账号
     */
    @Excel(name = "授权账号")
    private String authNumber;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态")
    private Integer orderStatus;

    /**
     * 缩略图
     */
    @Excel(name = "缩略图")
    private String goodImage;

    /**
     * 运单号
     */
    private String items;

    private Date createTime;

    //总金额
    private String amount;

    //国家
    private String country;

    //最晚发货时间
    private Date lastedSendTime;

    //物流提供方
    private String shippingCarrier;

    //备注
    private String remark;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getLastedSendTime() {
        return lastedSendTime;
    }

    public void setLastedSendTime(Date lastedSendTime) {
        this.lastedSendTime = lastedSendTime;
    }

    public String getShippingCarrier() {
        return shippingCarrier;
    }

    public void setShippingCarrier(String shippingCarrier) {
        this.shippingCarrier = shippingCarrier;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }



    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public void setShopeeOrderNo(String shopeeOrderNo) {
        this.shopeeOrderNo = shopeeOrderNo;
    }

    public String getShopeeOrderNo() {
        return shopeeOrderNo;
    }

    public void setAuthNumber(String authNumber) {
        this.authNumber = authNumber;
    }

    public String getAuthNumber() {
        return authNumber;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setGoodImage(String goodImage) {
        this.goodImage = goodImage;
    }

    public String getGoodImage() {
        return goodImage;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "StockOrder{" +
                "id=" + id +
                ", userId=" + userId +
                ", shopeeOrderNo='" + shopeeOrderNo + '\'' +
                ", authNumber='" + authNumber + '\'' +
                ", orderStatus=" + orderStatus +
                ", goodImage='" + goodImage + '\'' +
                ", items='" + items + '\'' +
                ", createTime=" + createTime +
                ", amount='" + amount + '\'' +
                ", country='" + country + '\'' +
                ", lastedSendTime=" + lastedSendTime +
                ", shippingCarrier='" + shippingCarrier + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
