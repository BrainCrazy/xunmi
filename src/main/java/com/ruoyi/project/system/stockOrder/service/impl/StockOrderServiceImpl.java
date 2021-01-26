package com.ruoyi.project.system.stockOrder.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.system.StockInfo.domain.StockInfo;
import com.ruoyi.project.system.StockInfo.mapper.StockInfoMapper;
import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import com.ruoyi.project.system.StockInfoItem.mapper.StockInfoItemMapper;
import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.item.mapper.HdItemMapper;
import com.ruoyi.project.system.stockOrder.domain.StockOrder;
import com.ruoyi.project.system.stockOrder.mapper.StockOrderMapper;
import com.ruoyi.project.system.stockOrder.service.IStockOrderService;
import com.ruoyi.project.system.userOrder.domain.OrderItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 库存订单Service业务层处理
 *
 * @author wqf
 * @date 2020-02-19
 */
@Service
public class StockOrderServiceImpl implements IStockOrderService {
    @Autowired
    private StockOrderMapper stockOrderMapper;

    @Autowired
    private StockInfoMapper stockInfoMapper;

    @Autowired
    private StockInfoItemMapper stockInfoItemMapper;

    @Autowired
    private HdItemMapper hdItemMapper;

    /**
     * 查询库存订单
     *
     * @param id 库存订单ID
     * @return 库存订单
     */
    @Override
    public StockOrder selectStockOrderById(Long id) {
        return stockOrderMapper.selectStockOrderById(id);
    }

    /**
     * 查询库存订单列表
     *
     * @param stockOrder 库存订单
     * @return 库存订单
     */
    @Override
    public List<StockOrder> selectStockOrderList(StockOrder stockOrder) {
        return stockOrderMapper.selectStockOrderList(stockOrder);
    }

    /**
     * 新增库存订单
     *
     * @param stockOrder 库存订单
     * @return 结果
     */
    @Override
    public int insertStockOrder(StockOrder stockOrder) {
        stockOrder.setCreateTime(DateUtils.getNowDate());
        return stockOrderMapper.insertStockOrder(stockOrder);
    }

    /**
     * 修改库存订单
     *
     * @param stockOrder 库存订单
     * @return 结果
     */
    @Override
    public int updateStockOrder(StockOrder stockOrder) {
        stockOrder.setUpdateTime(DateUtils.getNowDate());
        return stockOrderMapper.updateStockOrder(stockOrder);
    }

    /**
     * 删除库存订单对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStockOrderByIds(String ids) {
        return stockOrderMapper.deleteStockOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除库存订单信息
     *
     * @param id 库存订单ID
     * @return 结果
     */
    public int deleteStockOrderById(Long id) {
        return stockOrderMapper.deleteStockOrderById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveGoodsCount(Long id, String shopeeOrderNo, Long goodsCount) {
        //查询库存表
        StockInfo stockInfo = stockInfoMapper.selectStockInfoById(id);
        stockInfo.setGoodsCount(stockInfo.getGoodsCount() - goodsCount);
        stockInfo.setOutStorageCount(stockInfo.getOutStorageCount() + goodsCount);
        stockInfoMapper.updateStockInfo(stockInfo);
        //根据虾皮订单号和StockInfoId查询item表，
        StockInfoItem item = stockInfoItemMapper.selectByShopeeOrderNoAndStockInfoId(shopeeOrderNo, id);
        if (null != item) {
            item.setGoodsCount(item.getGoodsCount() + goodsCount);
            stockInfoItemMapper.updateStockInfoItem(item);
        } else {
            //生成出库记录
            item = new StockInfoItem();
            item.setSysTime(new Date());
            item.setUserId(ShiroUtils.getUserId());
            item.setStockInfoId(stockInfo.getId());
            item.setShopeeOrderNo(shopeeOrderNo);
            item.setCategoryName(stockInfo.getCategoryName());
            item.setGoodsCount(goodsCount);
            item.setGoodsName(stockInfo.getGoodsName());
            item.setGoodsImageUrl(stockInfo.getGoodsImageUrl());
            item.setItemType(10);
            item.setStockItemStatus(10);
            stockInfoItemMapper.insertStockInfoItem(item);
        }
        return true;
    }

    @Override
    public int updateOrderStatus(String shopeeOrderNo, int status) {
        return stockOrderMapper.updateStatusByShopeeOrderNo(shopeeOrderNo, status);
    }

    /**
     * 确认发货
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmOutStock(long id) {
      return false;
    }

    @Override
    public List<HdItem> getorderDetail(Long id) {
        StockOrder stockOrder = stockOrderMapper.selectStockOrderById(id);
        List<OrderItemInfo> list = JSON.parseArray(stockOrder.getItems(), OrderItemInfo.class);
        List<Long> itemIds = new ArrayList<>();
        for (OrderItemInfo info : list) {
            itemIds.add(info.getItem_id());
        }
        List<HdItem> res = hdItemMapper.selectByItemIds(itemIds);
        JSONArray imgJsonArray;
        for (HdItem item : res) {
            imgJsonArray = JSONArray.parseArray(item.getImages());
            item.setImages(imgJsonArray.getString(0));
        }
        return res;
    }

}
