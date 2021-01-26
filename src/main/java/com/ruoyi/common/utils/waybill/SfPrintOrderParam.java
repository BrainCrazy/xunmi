package com.ruoyi.common.utils.waybill;


import com.ruoyi.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 打印订单所需要的参数
 *
 * @author yibo.su
 * @version V1.0
 * @date 2020/1/16 5:28 下午
 */
@Data
public class SfPrintOrderParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 运单流水号
     */
    private String trackingNo;

    /**
     * 承运人的名称跨境运输
     */
    private String firstMileName;

    /**
     * 承运人的名称会在当地国家/地区运送包裹
     */
    private String lastMileName;

    /**
     * 是否需要海关申报
     * true-T
     * false-P
     */
    private String goodsToDeclare;

    /**
     * 用于运单打印
     */
    private String laneCode;

    /**
     * 买方名称
     * 长度过长进行截取
     */
    private String buyerUsername;

    /**
     * 收件地址
     */
    private String fullAddress;

    /**
     * 买方手机号
     */
    private String phone;

    /**
     * shopee地址
     * 固定值 China Guangdong Shenzhen
     */
    private String shopeeAddress = "China Guangdong Shenzhen";

    /**
     * 仓库地址(寄出地址)
     */
    private String warehouseAddress = "China Guangdong Shenzhen\n" +
            "1/F, Building 8,Tangtou industrial District ,Shiyan street ,baoan district";

    /**
     * 商品详情
     */
    private List<Item> items = new ArrayList<>();

    /**
     * 服务code,适用于跨境订单
     */
    private String serviceCode;

    /**
     * 国家
     */
    private String country;

    public String getGoodsToDeclare() {
        return goodsToDeclare;
    }

    public void setGoodsToDeclare(Boolean goodsToDeclare) {
        if (goodsToDeclare == null) {
            this.goodsToDeclare = null;
        } else {
            this.goodsToDeclare = goodsToDeclare ? "T" : "P";
        }
    }

    @Data
    public static class Item {
        private Long itemId;
        private Long categoryId;


        private String categoryName;

        private Integer count;

    }

    /**
     * 校验参数
     *
     * @return 没有问题-true,不满足-false
     */
    public boolean checkParam() {
        if (StringUtils.isEmpty(orderId)) {
            return false;
        }
        if (StringUtils.isEmpty(trackingNo)) {
            return false;
        }
        if (StringUtils.isEmpty(firstMileName)) {
            return false;
        }
        if (StringUtils.isEmpty(lastMileName)) {
            return false;
        }
        if (StringUtils.isEmpty(goodsToDeclare)) {
            return false;
        }
        if (StringUtils.isEmpty(laneCode)) {
            return false;
        }
        if (StringUtils.isEmpty(buyerUsername)) {
            return false;
        }
        if (StringUtils.isEmpty(fullAddress)) {
            return false;
        }
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        if (StringUtils.isEmpty(warehouseAddress)) {
            return false;
        }
        if (StringUtils.isEmpty(serviceCode)) {
            return false;
        }
        if (items == null || items.isEmpty()) {
            return false;
        } else {
            for (Item item : items) {
                if (StringUtils.isEmpty(item.getCategoryName())) {
                    return false;
                }
                if (item.getCount() == null) {
                    return false;
                }
            }
        }
        return true;
    }
}


