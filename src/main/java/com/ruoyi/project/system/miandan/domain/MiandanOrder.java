package com.ruoyi.project.system.miandan.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 纯贴面单对象 miandan_order
 *
 * @author wqf
 * @date 2020-02-14
 */
public class MiandanOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long id;

    private Date createTime;
    private Date updateTime;

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
     * 状态
     */
    @Excel(name = "状态")
    private Integer status;

    /**
     * 缩略图
     */
    @Excel(name = "缩略图")
    private String goodImage;

    private String items;

    private Date sys_time;
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
    //1-已扣费,0-未扣费
    private Integer feeFlag;

    //10-正常,20-异常
    private int exceptionFlag;

    public int getExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(int exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    public Integer getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(Integer feeFlag) {
        this.feeFlag = feeFlag;
    }

    public Date getSys_time() {
        return sys_time;
    }

    public void setSys_time(Date sys_time) {
        this.sys_time = sys_time;
    }

    /**
     * 运单号
     */
    @Excel(name = "运单号")
    private String packageNo;

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }


    public void setGoodImage(String goodImage) {
        this.goodImage = goodImage;
    }

    public String getGoodImage() {
        return goodImage;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getPackageNo() {
        return packageNo;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

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
    public String toString() {
        return "MiandanOrder{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", userId=" + userId +
                ", shopeeOrderNo='" + shopeeOrderNo + '\'' +
                ", authNumber='" + authNumber + '\'' +
                ", status=" + status +
                ", goodImage='" + goodImage + '\'' +
                ", items='" + items + '\'' +
                ", sys_time=" + sys_time +
                ", amount='" + amount + '\'' +
                ", country='" + country + '\'' +
                ", lastedSendTime=" + lastedSendTime +
                ", shippingCarrier='" + shippingCarrier + '\'' +
                ", remark='" + remark + '\'' +
                ", packageNo='" + packageNo + '\'' +
                '}';
    }
}
