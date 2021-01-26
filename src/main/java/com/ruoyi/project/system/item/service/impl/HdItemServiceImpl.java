package com.ruoyi.project.system.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.system.item.domain.HdCategory;
import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.item.mapper.HdCategoryMapper;
import com.ruoyi.project.system.item.mapper.HdItemMapper;
import com.ruoyi.project.system.item.service.HdCategoryService;
import com.ruoyi.project.system.item.service.IHdItemService;
import com.ruoyi.project.system.shop.remote.ShopInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商品明细Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class HdItemServiceImpl implements IHdItemService {
    @Autowired
    private HdItemMapper hdItemMapper;
    @Autowired
    private HdCategoryMapper hdCategoryMapper;
    @Autowired
    private HdCategoryService hdCategoryService;

    private Set<Long> itemIdSet = ConcurrentHashMap.newKeySet();

    /**
     * 查询商品明细
     *
     * @param itemId 商品明细ID
     * @return 商品明细
     */
    @Override
    public HdItem selectHdItemById(Long itemId) {
        return hdItemMapper.selectHdItemById(itemId);
    }

    /**
     * 查询商品明细列表
     *
     * @param hdItem 商品明细
     * @return 商品明细
     */
    @Override
    public List<HdItem> selectHdItemList(HdItem hdItem) {
        return hdItemMapper.selectHdItemList(hdItem);
    }

    /**
     * 新增商品明细
     *
     * @param hdItem 商品明细
     * @return 结果
     */
    @Override
    public int insertHdItem(HdItem hdItem) {
        hdItem.setCreateTime(DateUtils.getNowDate());
        return hdItemMapper.insertHdItem(hdItem);
    }

    /**
     * 修改商品明细
     *
     * @param hdItem 商品明细
     * @return 结果
     */
    @Override
    public int updateHdItem(HdItem hdItem) {
        hdItem.setUpdateTime(DateUtils.getNowDate());
        return hdItemMapper.updateHdItem(hdItem);
    }

    /**
     * 删除商品明细对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHdItemByIds(String ids) {
        return hdItemMapper.deleteHdItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品明细信息
     *
     * @param itemId 商品明细ID
     * @return 结果
     */
    @Override
    public int deleteHdItemById(Long itemId) {
        return hdItemMapper.deleteHdItemById(itemId);
    }

    @Override
    @Async("asyncServiceExecutor")
    public void asyncInsertOrUpdate(Long userId, Long shopId, Long itemId) {
        if (itemIdSet.contains(itemId)) {
            return;
        }
        HdItem hdItem = hdItemMapper.selectHdItemById(itemId);
        if (hdItem == null) {
            try {
                itemIdSet.add(itemId);
                syncInsertOrUpdate(userId, shopId, itemId);
            } finally {
                itemIdSet.remove(itemId);
            }
        }
    }

    @Override
    public boolean syncInsertOrUpdate(Long userId, Long shopId, Long itemId) {
        String itemDetail;
        try {
            itemDetail = ShopInterface.getItemDetail(shopId, itemId);
        } catch (Exception e) {
            log.error("拉取商品信息错误shopId-->{},itemId-->{},e-->", shopId, itemId, e);
            return false;
        }
        JSONObject json = JSON.parseObject(itemDetail);
        JSONObject itemJson = json.getJSONObject("item");
        if (itemJson == null || itemJson.isEmpty()) {
            log.error("没有查询到该商品信息,userId-->{},shopId-->{},itemId-->{},响应-->{}", userId, shopId, itemId, itemDetail);
            return false;
        }
        HdItem item = new HdItem();
        item.setItemId(itemJson.getLong("item_id"));
        item.setShopId(shopId);
        item.setUserId(userId);
        item.setItemSku(itemJson.getString("item_sku"));
        item.setStatus(itemJson.getString("status"));
        item.setName(itemJson.getString("name"));
        item.setDescription(itemJson.getString("description"));
        item.setImages(itemJson.getString("images"));
        item.setCurrency(itemJson.getString("currency"));
        item.setPrice(itemJson.getDouble("price"));
        Long create_time = itemJson.getLong("create_time");
        item.setItemCreateTime(create_time == null ? null : new Date(create_time * 1000L));
        Long update_time = itemJson.getLong("update_time");
        item.setItemUpdateTime(update_time == null ? null : new Date(update_time * 1000L));
        item.setCategoryId(itemJson.getLong("category_id"));
        if (item.getCategoryId() != null) {
            HdCategory hdCategory = hdCategoryMapper.selectByPrimaryKey(item.getCategoryId());
//            if (hdCategory == null) {
//                // 批量更新类别
//                hdCategoryService.refreshCategory(shopId);
//                hdCategory = hdCategoryMapper.selectByPrimaryKey(item.getCategoryId());
//            }
            item.setCategoryName(hdCategory == null ? null : hdCategory.getCategoryName());
        }
        HdItem hdItem = hdItemMapper.selectHdItemById(itemId);
        if (hdItem == null) {
            item.setCreateTime(new Date());
            hdItemMapper.insertHdItem(item);
        } else {
            hdItemMapper.updateHdItem(item);
        }
        return true;

    }

    @Override
    public boolean updateItemByItemId(Long itemId) {
        HdItem hdItem = hdItemMapper.selectHdItemById(itemId);
        if (hdItem == null) {
            return false;
        } else {
            return syncInsertOrUpdate(hdItem.getUserId(), hdItem.getShopId(), itemId);
        }
    }

    @Override
    public List<HdItem> selectHdItemByIds(List<Long> itemIds) {
        return hdItemMapper.selectByItemIds(itemIds);
    }


}
