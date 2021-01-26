package com.ruoyi.project.system.StockInfo.service;

import com.ruoyi.project.system.StockInfo.domain.StockInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 库存管理Service接口
 *
 * @author wqf
 * @date 2020-02-18
 */
public interface IStockInfoService {
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
     * @param packageNo
     * @param file
     * @return 结果
     */
    public boolean insertStockInfo(StockInfo stockInfo, String packageNo, MultipartFile file) throws IOException;

    /**
     * 修改库存管理
     *
     * @param stockInfo 库存管理
     * @return 结果
     */
    public int updateStockInfo(StockInfo stockInfo);

    /**
     * 批量删除库存管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStockInfoByIds(String ids);

    /**
     * 删除库存管理信息
     *
     * @param id 库存管理ID
     * @return 结果
     */
    public int deleteStockInfoById(Long id);

    boolean saveAddStock(long id, Long pendigStorageCount, String packageNo);
}
