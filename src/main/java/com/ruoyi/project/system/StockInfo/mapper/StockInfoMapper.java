package com.ruoyi.project.system.StockInfo.mapper;

import com.ruoyi.project.system.StockInfo.domain.StockInfo;
import java.util.List;

/**
 * 库存管理Mapper接口
 *
 * @author wqf
 * @date 2020-02-18
 */
public interface StockInfoMapper {
    /**
     * 查询库存管理
     *
     * @param id 库存管理ID
     * @return 库存管理
     */
    public StockInfo selectStockInfoById(Long id);

    /**
     * 查询库存管理列表
     *
     * @param stockInfo 库存管理
     * @return 库存管理集合
     */
    public List<StockInfo> selectStockInfoList(StockInfo stockInfo);

    /**
     * 新增库存管理
     *
     * @param stockInfo 库存管理
     * @return 结果
     */
    public Long insertStockInfo(StockInfo stockInfo);

    /**
     * 修改库存管理
     *
     * @param stockInfo 库存管理
     * @return 结果
     */
    public int updateStockInfo(StockInfo stockInfo);

    /**
     * 删除库存管理
     *
     * @param id 库存管理ID
     * @return 结果
     */
    public int deleteStockInfoById(Long id);

    /**
     * 批量删除库存管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStockInfoByIds(String[] ids);
}
