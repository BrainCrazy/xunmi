package com.ruoyi.project.system.shop.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.shopee.ShopeeConstants;
import com.ruoyi.project.system.shop.HttpClientSender2Shopee;

import java.util.Date;

/**
 * @author yibo.su
 * @version V1.0
 * @date 2020/2/15 7:05 下午
 */
public class OrderInterface {

    /**
     * 获取指定状态订单
     *
     * @param shopId      店铺id
     * @param orderStatus 订单状态
     * @return ~
     * @throws Exception ~
     */
    public static String getOrdersByStatus(Long shopId, String orderStatus, Date createTimeFrom, Date createTimeTo) throws Exception {
        String url = "/api/v1/orders/get";
        JSONObject json = new JSONObject();
        json.put("order_status", orderStatus);
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        if (createTimeFrom != null) {
            json.put("create_time_from", createTimeFrom.getTime() / 1000L);
        }
        if (createTimeTo != null) {
            json.put("create_time_to", (createTimeTo.getTime() + (24L * 3600L * 1000L)) / 1000L);
        }
        json.put("shopid", shopId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 分页获取指定状态订单
     *
     * @param shopId      店铺id
     * @param orderStatus 订单状态
     * @return ~
     * @throws Exception ~
     */
    public static String getOrdersByStatus(Long shopId, String orderStatus, Date createTimeFrom, Date createTimeTo, Integer start, Integer size) throws Exception {
        String url = "/api/v1/orders/get";
        JSONObject json = new JSONObject();
        json.put("order_status", orderStatus);
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        if (createTimeFrom != null) {
            json.put("create_time_from", createTimeFrom.getTime() / 1000L);
        }
        if (createTimeTo != null) {
            json.put("create_time_to", (createTimeTo.getTime() + (24L * 3600L * 1000L)) / 1000L);
        }
        json.put("shopid", shopId);
        json.put("pagination_entries_per_page", size);
        json.put("pagination_offset", start);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }


    /**
     * 拉取订单相求
     *
     * @param shopId  店铺id
     * @param orderSn orderId集合
     * @return ~
     * @throws Exception ~
     */
    public static String getOrderDetails(Long shopId, String... orderSn) throws Exception {
        String url = "/api/v1/orders/detail";
        JSONObject json = new JSONObject();
        json.put("ordersn_list", orderSn);
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }


    /**
     * 获取物流信息
     *
     * @param shopId  店铺id
     * @param orderId 订单id
     * @return ~
     * @throws Exception ~
     */
    public static String getOrderLogistics(Long shopId, String orderId) throws Exception {
        String url = "/api/v1/logistics/order/get";
        JSONObject json = new JSONObject();
        json.put("ordersn", orderId);
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 申请物流编号
     *
     * @param shopId   店铺id
     * @param orderIds 订单id集合
     * @return ~
     * @throws Exception ~
     */
    public static String getTrackingNo(Long shopId, String... orderIds) throws Exception {
        String url = "/api/v1/logistics/tracking_number/get_mass";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("ordersn_list", orderIds);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }


    /**
     * 获取订单物流跟踪信息
     */
    public static String getLogisticsMessage(Long shopId, String orderId) throws Exception {
        String url = "/api/v1/logistics/tracking";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("ordersn", orderId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 获取空运订单
     */
    public static String getAirwayBill(Long shopId, String... orderIds) throws Exception {
        String url = "/api/v1/logistics/airway_bill/get_mass";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("ordersn_list", orderIds);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 获取空运订单的订单
     */
    public static String getForderWaybill(Long shopId, String... orderIds) throws Exception {
        String url = "/api/v1/logistics/forder_waybill/get_mass";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        JSONArray jsonArray = new JSONArray();
        for (String orderId : orderIds) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ordern", orderId);
            jsonArray.add(jsonObject);
        }
        json.put("orders_list", jsonArray);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 获取物流信息
     */
    public static String getLogisticInfo(Long shopId, String orderId) throws Exception {
        String url = "/api/v1/logistics/init_info/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("ordersn", orderId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 初始化物流
     */
    public static String initLogistics(Long shopId, String orderId) throws Exception {
        String url = "/api/v1/logistics/init";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        JSONObject pickup = new JSONObject();
//        pickup.put("address_id", 20027773);
//        json.put("pickup", pickup);
        JSONObject dropoff = new JSONObject();
//        dropoff.put("sender_real_name","anyamahiwaga");
        json.put("dropoff", dropoff);
        JSONObject nonIntegrated = new JSONObject();
//        nonIntegrated.put("dropoff",new JSONArray());
//        json.put("non_integrated",nonIntegrated);
        json.put("ordersn", orderId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }


    /**
     * 初始化物流
     */
    public static String initLogistics(Long shopId, String orderId, JSONObject pickup, JSONObject dropoff, JSONObject nonIntegrated) throws Exception {
        String url = "/api/v1/logistics/init";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        if (!pickup.isEmpty()) {
            json.put("pickup", pickup);
        }
        json.put("dropoff", dropoff);
        if (!nonIntegrated.isEmpty()) {
            JSONArray array = nonIntegrated.getJSONArray("dropoff");
            if (!array.isEmpty()) {
                json.put("non_integrated", nonIntegrated);
            }
        }
        json.put("ordersn", orderId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 获取初始化物流信息
     */
    public static String getParameterForInit(Long shopId, String orderId) throws Exception {
        String url = "/api/v1/logistics/init_parameter/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("ordersn", orderId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    public static String getAddress(Long shopId) throws Exception {
        String url = "/api/v1/logistics/address/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    public static String getTimeSlot(Long shopId, String orderId, Long addressId) throws Exception {
        String url = "/api/v1/logistics/timeslot/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("ordersn", orderId);
        json.put("address_id", addressId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    public static String getBranch(Long shopId, String orderId) throws Exception {
        String url = "/api/v1/logistics/branch/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("ordersn", orderId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    public static String getCategoryItems(Long shopId, Long categoryId) throws Exception {
        String url = "/api/v1/shop_category/get/items";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("shop_category_id", categoryId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

}
