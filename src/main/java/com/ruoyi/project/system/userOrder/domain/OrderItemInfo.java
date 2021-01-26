package com.ruoyi.project.system.userOrder.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

public class OrderItemInfo extends BaseEntity {
    private String promotion_type;
    private String variation_original_price;
    private String variation_sku;
    private String item_sku;
    private Long item_id;
    private String variation_name;
    private String weight;
    private String item_name;
    private Integer variation_quantity_purchased;

    public Integer getVariation_quantity_purchased() {
        return variation_quantity_purchased;
    }

    public void setVariation_quantity_purchased(Integer variation_quantity_purchased) {
        this.variation_quantity_purchased = variation_quantity_purchased;
    }

    public String getPromotion_type() {
        return promotion_type;
    }

    public void setPromotion_type(String promotion_type) {
        this.promotion_type = promotion_type;
    }

    public String getVariation_original_price() {
        return variation_original_price;
    }

    public void setVariation_original_price(String variation_original_price) {
        this.variation_original_price = variation_original_price;
    }

    public String getVariation_sku() {
        return variation_sku;
    }

    public void setVariation_sku(String variation_sku) {
        this.variation_sku = variation_sku;
    }

    public String getItem_sku() {
        return item_sku;
    }

    public void setItem_sku(String item_sku) {
        this.item_sku = item_sku;
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public String getVariation_name() {
        return variation_name;
    }

    public void setVariation_name(String variation_name) {
        this.variation_name = variation_name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
