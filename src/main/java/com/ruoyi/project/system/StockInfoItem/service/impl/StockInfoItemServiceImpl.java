package com.ruoyi.project.system.StockInfoItem.service.impl;

import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.system.StockInfo.domain.StockInfo;
import com.ruoyi.project.system.StockInfo.mapper.StockInfoMapper;
import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import com.ruoyi.project.system.StockInfoItem.mapper.StockInfoItemMapper;
import com.ruoyi.project.system.StockInfoItem.service.IStockInfoItemService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 库存明细Service业务层处理
 *
 * @author wqf
 * @date 2020-02-18
 */
@Service
public class StockInfoItemServiceImpl implements IStockInfoItemService {
    @Autowired
    private StockInfoItemMapper stockInfoItemMapper;

    @Autowired
    private StockInfoMapper stockInfoMapper;

    /**
     * 查询库存明细
     *
     * @param id 库存明细ID
     * @return 库存明细
     */
    @Override
    public StockInfoItem selectStockInfoItemById(Long id) {
        return stockInfoItemMapper.selectStockInfoItemById(id);
    }

    /**
     * 查询库存明细列表
     *
     * @param stockInfoItem 库存明细
     * @return 库存明细
     */
    @Override
    public List<StockInfoItem> selectStockInfoItemList(StockInfoItem stockInfoItem) {
        return stockInfoItemMapper.selectStockInfoItemList(stockInfoItem);
    }

    /**
     * 新增库存明细
     *
     * @param stockInfoItem 库存明细
     * @return 结果
     */
    @Override
    public int insertStockInfoItem(StockInfoItem stockInfoItem) {
        return stockInfoItemMapper.insertStockInfoItem(stockInfoItem);
    }

    /**
     * 修改库存明细
     *
     * @param stockInfoItem 库存明细
     * @return 结果
     */
    @Override
    public int updateStockInfoItem(StockInfoItem stockInfoItem) {
        //查询库存表
        StockInfoItem oldItem = stockInfoItemMapper.selectStockInfoItemById(stockInfoItem.getId());
        StockInfo stockInfo = stockInfoMapper.selectStockInfoById(stockInfoItem.getStockInfoId());
        stockInfo.setCategoryName(stockInfoItem.getCategoryName());
        stockInfo.setGoodsName(stockInfoItem.getGoodsName());
        stockInfo.setPendigStorageCount(stockInfo.getPendigStorageCount() - oldItem.getGoodsCount() + stockInfoItem.getGoodsCount());
        stockInfoMapper.updateStockInfo(stockInfo);
        return stockInfoItemMapper.updateStockInfoItem(stockInfoItem);
    }

    /**
     * 删除库存明细对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStockInfoItemByIds(String ids) {
        return stockInfoItemMapper.deleteStockInfoItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除库存明细信息
     *
     * @param id 库存明细ID
     * @return 结果
     */
    public int deleteStockInfoItemById(Long id) {
        //查询
        StockInfoItem stockInfoItem = stockInfoItemMapper.selectStockInfoItemById(id);
        //删除待入库数据
        stockInfoItemMapper.deleteStockInfoItemById(id);
        StockInfo stockInfo = stockInfoMapper.selectStockInfoById(stockInfoItem.getStockInfoId());
        if (stockInfoItem.getItemType() == 10) {
            //出库
            stockInfo.setOutStorageCount(stockInfo.getOutStorageCount() - stockInfoItem.getGoodsCount());
            //加库存
            stockInfo.setGoodsCount(stockInfo.getGoodsCount() + stockInfoItem.getGoodsCount());
        } else {
            //入库
            stockInfo.setPendigStorageCount(stockInfo.getPendigStorageCount() - stockInfoItem.getGoodsCount());
        }
        stockInfoMapper.updateStockInfo(stockInfo);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmStock(Long id) {
        StockInfoItem item = stockInfoItemMapper.selectStockInfoItemById(id);
        //更新明细表
        stockInfoItemMapper.updateStatusById(id, 20);
        //更新库存表
        StockInfo stockInfo = stockInfoMapper.selectStockInfoById(item.getStockInfoId());
        stockInfo.setGoodsCount(stockInfo.getGoodsCount() + item.getGoodsCount());
        stockInfo.setPendigStorageCount(stockInfo.getPendigStorageCount() - item.getGoodsCount());
        stockInfoMapper.updateStockInfo(stockInfo);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmStock4Page(String id) {
        String[] arr = id.split("-");
        long itemId = Long.parseLong(arr[0]);
        int itemType = Integer.parseInt(arr[1]);
        StockInfoItem item = stockInfoItemMapper.selectStockInfoItemById(itemId);
        //更新明细表
        stockInfoItemMapper.updateStatusById(itemId, 20);
        //更新库存表
        StockInfo stockInfo = stockInfoMapper.selectStockInfoById(item.getStockInfoId());
        if (20 == itemType) {
            //入库
            stockInfo.setGoodsCount(stockInfo.getGoodsCount() + item.getGoodsCount());
            stockInfo.setPendigStorageCount(stockInfo.getPendigStorageCount() - item.getGoodsCount());
        } else {
            //出库
            stockInfo.setOutStorageCount(stockInfo.getOutStorageCount() - item.getGoodsCount());
        }
        stockInfoMapper.updateStockInfo(stockInfo);
        return true;
    }

    /**
     * 确认出库操作
     *
     * @param shopeeOrderNo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmOutStock(String shopeeOrderNo) {
        //更新库存明细
        StockInfoItem infoItem = new StockInfoItem();
        infoItem.setShopeeOrderNo(shopeeOrderNo);
        List<StockInfoItem> list = stockInfoItemMapper.selectStockInfoItemList(infoItem);
        if (!CollectionUtils.isEmpty(list)) {
            StockInfo stockInfo;
            for (StockInfoItem item : list) {
                stockInfo = stockInfoMapper.selectStockInfoById(item.getStockInfoId());
                stockInfo.setOutStorageCount(stockInfo.getOutStorageCount() - item.getGoodsCount());
                stockInfoMapper.updateStockInfo(stockInfo);
                stockInfoItemMapper.updateStatusById(item.getId(), 20);
            }
        }
        return true;
    }

    @Override
    public List<StockInfoItem> selectStockInfoItemByShopeeOrderNo(String shopeeOrderNo) {
        StockInfoItem item = new StockInfoItem();
        item.setShopeeOrderNo(shopeeOrderNo);
        return stockInfoItemMapper.selectStockInfoItemList(item);
    }
}
