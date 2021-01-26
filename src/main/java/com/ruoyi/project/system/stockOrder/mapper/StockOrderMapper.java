package com.ruoyi.project.system.stockOrder.mapper;

import com.ruoyi.project.system.stockOrder.domain.StockOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存订单Mapper接口
 *
 * @author wqf
 * @date 2020-02-19
 */
public interface StockOrderMapper {
    /**
     * 查询库存订单
     *
     * @param id 库存订单ID
     * @return 库存订单
     */
    public StockOrder selectStockOrderById(Long id);

    /**
     * 查询库存订单列表
     *
     * @param stockOrder 库存订单
     * @return 库存订单集合
     */
    public List<StockOrder> selectStockOrderList(StockOrder stockOrder);

    /**
     * 新增库存订单
     *
     * @param stockOrder 库存订单
     * @return 结果
     */
    public int insertStockOrder(StockOrder stockOrder);

    /**
     * 修改库存订单
     *
     * @param stockOrder 库存订单
     * @return 结果
     */
    public int updateStockOrder(StockOrder stockOrder);

    /**
     * 删除库存订单
     *
     * @param id 库存订单ID
     * @return 结果
     */
    public int deleteStockOrderById(Long id);

    /**
     * 批量删除库存订单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStockOrderByIds(String[] ids);

    int updateStatusByShopeeOrderNo(@Param("shopeeOrderNo") String shopeeOrderNo, @Param("status") int status);

    List<StockOrder> selectByShopeeOrderNos(String[] orderIds);
}
