package com.ruoyi.project.system.StockInfo.service.impl;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.system.StockInfo.domain.StockInfo;
import com.ruoyi.project.system.StockInfo.mapper.StockInfoMapper;
import com.ruoyi.project.system.StockInfo.service.IStockInfoService;
import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import com.ruoyi.project.system.StockInfoItem.mapper.StockInfoItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 库存管理Service业务层处理
 *
 * @author wqf
 * @date 2020-02-18
 */
@Service
public class StockInfoServiceImpl implements IStockInfoService {

    @Autowired
    private StockInfoMapper stockInfoMapper;

    @Autowired
    private StockInfoItemMapper stockInfoItemMapper;

    /**
     * 查询库存管理
     *
     * @param id 库存管理ID
     * @return 库存管理
     */
    @Override
    public StockInfo selectStockInfoById(Long id) {
        return stockInfoMapper.selectStockInfoById(id);
    }

    /**
     * 查询库存管理列表
     *
     * @param stockInfo 库存管理
     * @return 库存管理
     */
    @Override
    public List<StockInfo> selectStockInfoList(StockInfo stockInfo) {
        return stockInfoMapper.selectStockInfoList(stockInfo);
    }

    /**
     * 新增库存管理
     *
     * @param stockInfo 库存管理
     * @param packageNo
     * @param file
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertStockInfo(StockInfo stockInfo, String packageNo, MultipartFile file) throws IOException {
        stockInfo.setUserId(ShiroUtils.getUserId());
        stockInfo.setGoodsCount(0L);
        if (null != file) {
            String stockImage = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);
            stockInfo.setGoodsImageUrl(stockImage);
        }
        stockInfoMapper.insertStockInfo(stockInfo);
        StockInfoItem item = new StockInfoItem();
        item.setUserId(ShiroUtils.getUserId());
        item.setSysTime(new Date());
        item.setStockInfoId(stockInfo.getId());
        item.setPackageNo(packageNo);
        item.setCategoryName(stockInfo.getCategoryName());
        item.setGoodsCount(stockInfo.getPendigStorageCount());
        item.setGoodsName(stockInfo.getGoodsName());
        item.setGoodsImageUrl(stockInfo.getGoodsImageUrl());
        item.setItemType(20);
        item.setStockItemStatus(10);
        stockInfoItemMapper.insertStockInfoItem(item);
        return true;
    }

    /**
     * 修改库存管理
     *
     * @param stockInfo 库存管理
     * @return 结果
     */
    @Override
    public int updateStockInfo(StockInfo stockInfo) {
        return stockInfoMapper.updateStockInfo(stockInfo);
    }

    /**
     * 删除库存管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStockInfoByIds(String ids) {
        return stockInfoMapper.deleteStockInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除库存管理信息
     *
     * @param id 库存管理ID
     * @return 结果
     */
    public int deleteStockInfoById(Long id) {
        return stockInfoMapper.deleteStockInfoById(id);
    }

    /**
     * 补货处理
     *
     * @param id
     * @param pendigStorageCount
     * @param packageNo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAddStock(long id, Long pendigStorageCount, String packageNo) {
        StockInfo stockInfo = stockInfoMapper.selectStockInfoById(id);
        stockInfo.setPendigStorageCount(stockInfo.getPendigStorageCount() + pendigStorageCount);
        stockInfoMapper.updateStockInfo(stockInfo);
        //入库明细表
        StockInfoItem item = new StockInfoItem();
        item.setUserId(ShiroUtils.getUserId());
        item.setSysTime(new Date());
        item.setStockInfoId(stockInfo.getId());
        item.setPackageNo(packageNo);
        item.setCategoryName(stockInfo.getCategoryName());
        item.setGoodsCount(pendigStorageCount);
        item.setGoodsName(stockInfo.getGoodsName());
        item.setGoodsImageUrl(stockInfo.getGoodsImageUrl());
        item.setItemType(20);
        item.setStockItemStatus(10);
        stockInfoItemMapper.insertStockInfoItem(item);
        return true;
    }
}
