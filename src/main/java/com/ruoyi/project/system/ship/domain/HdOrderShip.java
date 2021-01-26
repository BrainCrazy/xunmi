package com.ruoyi.project.system.ship.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.system.item.domain.HdItem;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 待处理订单对象 hd_order_ship
 *
 * @author ruoyi
 * @date 2020-02-15
 */
public class HdOrderShip extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private String orderId;

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
     * 订单创建时间
     */
    @Excel(name = "订单创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderCreateTime;

    /**
     * 订单修改时间
     */
    @Excel(name = "订单修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderUpdateTime;

    /**
     * 下单国家数字代码
     */
    @Excel(name = "下单国家数字代码")
    private String country;

    /**
     * 货币单位，3位数代码
     */
    @Excel(name = "货币单位，3位数代码")
    private String currency;

    /**
     * 是否为货到付款订单，0-false,1-true
     */
    @Excel(name = "是否为货到付款订单，0-false,1-true")
    private Integer cod;

    /**
     * 买方选择为订单付款的付款方式
     */
    @Excel(name = "买方选择为订单付款的付款方式")
    private String paymentMethod;

    /**
     * 是否需要在海关申报，0-false,1-true
     */
    @Excel(name = "是否需要在海关申报，0-false,1-true")
    private Integer goodsToDeclare;

    /**
     * 给卖方的消息
     */
    @Excel(name = "给卖方的消息")
    private String messageToSeller;

    /**
     * 订单商品详情，jsonArray
     */
    @Excel(name = "订单商品详情，jsonArray")
    private String items;

    /**
     * 支付时间
     */
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 买方名称
     */
    @Excel(name = "买方名称")
    private String buyerUsername;

    @Excel(name = "物流id")
    private String trackingNo;

    /**
     * 是否为拆分订单,0-false,1-true
     */
    @Excel(name = "是否为拆分订单,0-false,1-true")
    private Integer isSplitUp;

    @Excel(name = "总金额")
    private String totalAmount;

    @Excel(name = "最晚发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastDeliverTime;

    /**
     * 第一件商品id
     */
    private Long firstItemId;

    /**
     * 物流服务提供商
     */
    private String shippingCarrier;

    private List<HdItem> itemList;

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
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

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderUpdateTime(Date orderUpdateTime) {
        this.orderUpdateTime = orderUpdateTime;
    }

    public Date getOrderUpdateTime() {
        return orderUpdateTime;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCod(Boolean cod) {
        if (cod == null) {
            this.cod = null;
        } else {
            this.cod = cod ? 1 : 0;
        }
    }

    public Boolean getCod() {
        if (cod != null) {
            return cod != 0;
        }
        return null;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setGoodsToDeclare(Boolean goodsToDeclare) {
        if (goodsToDeclare == null) {
            this.goodsToDeclare = null;
        } else {
            this.goodsToDeclare = goodsToDeclare ? 1 : 0;
        }
    }

    public Boolean getGoodsToDeclare() {
        if (goodsToDeclare == null) {
            return null;
        } else {
            return goodsToDeclare != 0;
        }
    }

    public void setMessageToSeller(String messageToSeller) {
        this.messageToSeller = messageToSeller;
    }

    public String getMessageToSeller() {
        return messageToSeller;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getItems() {
        return items;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public void setIsSplitUp(Boolean isSplitUp) {
        if (isSplitUp == null) {
            this.isSplitUp = null;
        } else {
            this.isSplitUp = isSplitUp ? 1 : 0;
        }
    }

    public Boolean getIsSplitUp() {
        if (isSplitUp == null) {
            return null;
        } else {
            return isSplitUp != 0;
        }
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getLastDeliverTime() {
        return lastDeliverTime;
    }

    public void setLastDeliverTime(Date lastDeliverTime) {
        this.lastDeliverTime = lastDeliverTime;
    }

    public Long getFirstItemId() {
        return firstItemId;
    }

    public void setFirstItemId(Long firstItemId) {
        this.firstItemId = firstItemId;
    }

    public String getShippingCarrier() {
        return shippingCarrier;
    }

    public void setShippingCarrier(String shippingCarrier) {
        this.shippingCarrier = shippingCarrier;
    }

    public List<HdItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<HdItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("orderId", getOrderId())
                .append("shopId", getShopId())
                .append("userId", getUserId())
                .append("orderCreateTime", getOrderCreateTime())
                .append("orderUpdateTime", getOrderUpdateTime())
                .append("country", getCountry())
                .append("currency", getCurrency())
                .append("cod", getCod())
                .append("paymentMethod", getPaymentMethod())
                .append("goodsToDeclare", getGoodsToDeclare())
                .append("messageToSeller", getMessageToSeller())
                .append("items", getItems())
                .append("payTime", getPayTime())
                .append("buyerUsername", getBuyerUsername())
                .append("isSplitUp", getIsSplitUp())
                .append("trackingNo", getTrackingNo())
                .append("lastDeliverTime", getLastDeliverTime())
                .append("totalAmount", getTotalAmount())
                .append("createTime", getCreateTime())
                .append("shippingCarrier", getShippingCarrier())
                .toString();
    }
}
