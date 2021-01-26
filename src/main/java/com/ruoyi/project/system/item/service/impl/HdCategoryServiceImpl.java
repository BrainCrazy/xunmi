package com.ruoyi.project.system.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.project.system.item.domain.HdCategory;
import com.ruoyi.project.system.item.mapper.HdCategoryMapper;
import com.ruoyi.project.system.item.service.HdCategoryService;
import com.ruoyi.project.system.shop.domain.HdShop;
import com.ruoyi.project.system.shop.mapper.HdShopMapper;
import com.ruoyi.project.system.shop.remote.ShopInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author yibo.su
 * @version V1.0
 * @date 2020/3/19 1:37 下午
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class HdCategoryServiceImpl implements HdCategoryService {

    @Autowired
    private HdCategoryMapper hdCategoryMapper;
    @Autowired
    private HdShopMapper hdShopMapper;

    private static Set<Long> shopSet = ConcurrentHashMap.newKeySet();

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void refreshCategory(Long shopId) {
        if (shopSet.contains(shopId)) {
            return;
        }
        try {
            shopSet.add(shopId);
            String categories = ShopInterface.getCategories(shopId);
            JSONObject json = JSON.parseObject(categories);
            JSONArray jsonArray = json.getJSONArray("categories");
            if (jsonArray != null && !jsonArray.isEmpty()) {
                ArrayList<HdCategory> list = new ArrayList<>();
                for (Object o : jsonArray) {
                    HdCategory hdCategory = new HdCategory();
                    JSONObject obj = (JSONObject) o;
                    hdCategory.setCategoryId(obj.getLong("category_id"));
                    hdCategory.setCategoryName(obj.getString("category_name"));
                    hdCategory.setHasChildren(obj.getBoolean("has_children"));
                    JSONObject daysToShipLimits = obj.getJSONObject("days_to_ship_limits");
                    if (daysToShipLimits != null) {
                        hdCategory.setMaxLimit(daysToShipLimits.getInteger("max_limit"));
                        hdCategory.setMinLimit(daysToShipLimits.getInteger("min_limit"));
                    }
                    hdCategory.setParentId(obj.getLong("parent_id"));
                    list.add(hdCategory);
                }
                // 判断是新增还是修改
                List<Long> collect = list.stream().map(HdCategory::getCategoryId).collect(Collectors.toList());
//                Map<Long, List<HdCategory>> map = list.stream().collect(Collectors.groupingBy(HdCategory::getCategoryId));
                List<Long> ids = hdCategoryMapper.selectIdByIds(collect);
                for (HdCategory hdCategory : list) {
                    HashSet<Long> idSet = new HashSet<>(ids);
                    if (idSet.contains(hdCategory.getCategoryId())) {
                        hdCategoryMapper.update(hdCategory);
                    } else {
                        hdCategoryMapper.insert(hdCategory);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取店铺分类错误shopId-->{},e-->", shopId, e);
        } finally {
            shopSet.remove(shopId);
        }
    }

    @Scheduled(cron = "0 0 1  * * ?")
    public void syncCategoryId() throws Exception {
        log.info("同步店铺分类开始-->");
        List<HdShop> shops = hdShopMapper.findAll();
        log.info("同步店铺数量-->{}", shops.size());
        HashMap<Long, Integer> map = new HashMap<>(50000);
        for (HdShop shop : shops) {
            log.info("更新店铺分类信息,shop-->{}", shop);
            String categories = ShopInterface.getCategories(shop.getShopId());
            JSONObject json = JSON.parseObject(categories);
            JSONArray jsonArray = json.getJSONArray("categories");
            if (jsonArray != null && !jsonArray.isEmpty()) {
                log.info("更新店铺分类信息,shopId-->{},更新数量-->{}", shop.getShopId(), jsonArray.size());
                List<HdCategory> list = new ArrayList<>();
                for (Object o : jsonArray) {
                    HdCategory hdCategory = new HdCategory();
                    JSONObject obj = (JSONObject) o;
                    hdCategory.setCategoryId(obj.getLong("category_id"));
                    hdCategory.setCategoryName(obj.getString("category_name"));
                    hdCategory.setHasChildren(obj.getBoolean("has_children"));
                    JSONObject daysToShipLimits = obj.getJSONObject("days_to_ship_limits");
                    if (daysToShipLimits != null) {
                        hdCategory.setMaxLimit(daysToShipLimits.getInteger("max_limit"));
                        hdCategory.setMinLimit(daysToShipLimits.getInteger("min_limit"));
                    }
                    hdCategory.setParentId(obj.getLong("parent_id"));
                    list.add(hdCategory);
                }
                // 判断是新增还是修改
                List<Long> collect = list.stream().map(HdCategory::getCategoryId).collect(Collectors.toList());
                List<Long> ids = hdCategoryMapper.selectIdByIds(collect);
                for (HdCategory hdCategory : list) {
                    Long categoryId = hdCategory.getCategoryId();
                    Integer hashCode = map.get(categoryId);
                    if (hashCode != null && hashCode.equals(hdCategory.hashCode())) {
                        // 相同不进行修改
                        continue;
                    }
                    HashSet<Long> idSet = new HashSet<>(ids);
                    if (idSet.contains(categoryId)) {
                        hdCategoryMapper.update(hdCategory);
                    } else {
                        hdCategoryMapper.insert(hdCategory);
                    }
                    map.put(categoryId, hdCategory.hashCode());
                }
            }
        }
        log.info("同步店铺分类,最终同步数量-->{}", map.size());
    }
}
