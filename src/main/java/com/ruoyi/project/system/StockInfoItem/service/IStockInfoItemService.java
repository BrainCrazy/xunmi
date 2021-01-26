package com.ruoyi.project.system.StockInfoItem.service;

import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import java.util.List;

/**
 * 库存明细Service接口
 *
 * @author wqf
 * @date 2020-02-18
 */
public interface IStockInfoItemService {
    /**
     * 查询库存明细
     *
     * @param id 库存明细ID
     * @return 库存明细
     */
    public StockInfoItem selectStockInfoItemById(Long id);

    /**
     * 查询库存明细列表
     *
     * @param stockInfoItem 库存明细
     * @return 库存明细集合
     */
    public List<StockInfoItem> selectStockInfoItemList(StockInfoItem stockInfoItem);

    /**
     * 新增库存明细
     *
     * @param stockInfoItem 库存明细
     * @return 结果
     */
    public int insertStockInfoItem(StockInfoItem stockInfoItem);

    /**
     * 修改库存明细
     *
     * @param stockInfoItem 库存明细
     * @return 结果
     */
    public int updateStockInfoItem(StockInfoItem stockInfoItem);

    /**
     * 批量删除库存明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStockInfoItemByIds(String ids);

    /**
     * 删除库存明细信息
     *
     * @param id 库存明细ID
     * @return 结果
     */
    public int deleteStockInfoItemById(Long id);

    boolean confirmStock(Long id);
    boolean confirmStock4Page(String id);
    boolean confirmOutStock(String shopeeOrderNo);

    List<StockInfoItem> selectStockInfoItemByShopeeOrderNo(String id);
}
