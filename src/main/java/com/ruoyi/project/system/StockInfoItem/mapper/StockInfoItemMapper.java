package com.ruoyi.project.system.StockInfoItem.mapper;

import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存明细Mapper接口
 *
 * @author wqf
 * @date 2020-02-18
 */
public interface StockInfoItemMapper {
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
    public List<StockInfoItem> selectStockInfoItemByPackageNo(@Param("packageNo") String packageNo, @Param("stockItemStatus") int stockItemStatus);

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
     * 删除库存明细
     *
     * @param id 库存明细ID
     * @return 结果
     */
    public int deleteStockInfoItemById(Long id);

    /**
     * 批量删除库存明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStockInfoItemByIds(String[] ids);

    int updateStatusById(@Param("id") Long id, @Param("stockItemStatus") int stockItemStatus);

    StockInfoItem selectByShopeeOrderNoAndStockInfoId(@Param("shopeeOrderNo") String shopeeOrderNo, @Param("stockInfoId") Long stockInfoId);
}
