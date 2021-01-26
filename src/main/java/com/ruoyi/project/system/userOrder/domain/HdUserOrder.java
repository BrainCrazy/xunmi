package com.ruoyi.project.system.userOrder.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 支付订单对象 hd_user_order
 * 
 * @author ruoyi
 * @date 2019-10-21
 */
public class HdUserOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 面单包裹订单id */
    @Excel(name = "面单包裹订单id")
    private Long orderId;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 0 充值 1支付订单 */
    @Excel(name = "0 充值 1支付订单")
    private Long type;

    /** 金额 */
    @Excel(name = "金额")
    private String money;

    /** 支付方式 0微信 1支付宝 */
    @Excel(name = "支付方式 0微信 1支付宝")
    private String payType;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private String orderStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }
    public void setMoney(String money) 
    {
        this.money = money;
    }

    public String getMoney() 
    {
        return money;
    }
    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getPayType()
    {
        return payType;
    }
    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("type", getType())
            .append("money", getMoney())
            .append("payType", getPayType())
            .append("orderStatus", getOrderStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
