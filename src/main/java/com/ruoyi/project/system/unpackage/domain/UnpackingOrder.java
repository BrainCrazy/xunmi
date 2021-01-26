package com.ruoyi.project.system.unpackage.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 拆包订单对象 unpacking_order
 *
 * @author wqf
 * @date 2020-02-15
 */
public class UnpackingOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long id;

    /**
     * 用户ID
     */
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

    private Date createTime;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer packageStatus;

    /**
     * 缩略图
     */
    @Excel(name = "缩略图")
    private String goodsImage;

    /**
     * 是否拍照确认
     */
    @Excel(name = "是否拍照确认")
    private Integer photograph;

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

    private String outstockImage;

    private Date outstockTime;

    private int weightFlag;

    private String weight;

    //10-未扣费,20-已扣费
    private int feeFlag;

    private Date updateTime;
    //10-正常,20-异常
    private int exceptionFlag;

    public int getExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(int exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getWeightFlag() {
        return weightFlag;
    }

    public void setWeightFlag(int weightFlag) {
        this.weightFlag = weightFlag;
    }

    public int getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(int feeFlag) {
        this.feeFlag = feeFlag;
    }

    public Date getOutstockTime() {
        return outstockTime;
    }

    public void setOutstockTime(Date outstockTime) {
        this.outstockTime = outstockTime;
    }

    public String getOutstockImage() {
        return outstockImage;
    }

    public void setOutstockImage(String outstockImage) {
        this.outstockImage = outstockImage;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getShopeeOrderNo() {
        return shopeeOrderNo;
    }

    public void setShopeeOrderNo(String shopeeOrderNo) {
        this.shopeeOrderNo = shopeeOrderNo;
    }

    public String getAuthNumber() {
        return authNumber;
    }

    public void setAuthNumber(String authNumber) {
        this.authNumber = authNumber;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(Integer packageStatus) {
        this.packageStatus = packageStatus;
    }

    public Integer getPhotograph() {
        return photograph;
    }

    public void setPhotograph(Integer photograph) {
        this.photograph = photograph;
    }
}
