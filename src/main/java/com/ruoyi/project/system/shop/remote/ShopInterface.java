package com.ruoyi.project.system.shop.remote;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.shopee.ShopeeConstants;
import com.ruoyi.project.system.shop.HttpClientSender2Shopee;

/**
 * 店铺相关接口
 *
 * @author yibo.su
 * @version V1.0
 * @date 2020/2/15 5:17 下午
 */
public class ShopInterface {

    /**
     * 获取商铺信息
     *
     * @param shopId 店铺id
     * @return
     * @throws Exception
     */
    public static String getShopInfo(Long shopId) throws Exception {
        String url = "/api/v1/shop/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 获取商品信息
     *
     * @param itemId 商品id
     * @return ~
     * @throws Exception ~
     */
    public static String getItemDetail(Long shopId, Long itemId) throws Exception {
        String url = "/api/v1/item/get";
        JSONObject json = new JSONObject();
        json.put("item_id", itemId);
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    /**
     * 获取店铺类别
     *
     * @param shopId 店铺id
     * @return ~
     * @throws Exception ~
     */
    public static String getCategories(Long shopId) throws Exception {
        String url = "/api/v1/item/categories/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("language", "en");
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }


    public static String getShopCategories(Long shopId, long paginationOffset) throws Exception {
        String url = "/api/v1/shop_categorys/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("pagination_offset", paginationOffset);
        json.put("pagination_entries_per_page", 100);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }

    public static String getItemAttributes(Long shopId, Long categoryId) throws Exception {
        String url = "/api/v1/item/attributes/get";
        JSONObject json = new JSONObject();
        json.put("partner_id", ShopeeConstants.PARTNER_ID);
        json.put("shopid", shopId);
        json.put("category_id", categoryId);
        json.put("timestamp", System.currentTimeMillis() / 1000);
        return HttpClientSender2Shopee.sendHttpPost(ShopeeConstants.HOST, url, ShopeeConstants.KEY, json.toString());
    }
}
