package com.ruoyi.project.system.unpackage.mapper;

import com.ruoyi.project.system.unpackage.domain.UnpackingOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 拆包订单Mapper接口
 *
 * @author wqf
 * @date 2020-02-15
 */
public interface UnpackingOrderMapper {

    int updateStatusById(@Param("id") long id, @Param("targetStatus") int targetStatus);

    UnpackingOrder selectByShopeeOrderNo(@Param("shopeeOrderNo") String shopeeOrderNo);

    int updateStatusAndRemarkByShopeeOrderNo(@Param("shopeeOrderNo") String shopeeOrderNo, @Param("packageStatus") int packageStatus, @Param("remark") String remark);

    int updateStatusByShopeeOrderNo(@Param("shopeeOrderNo") String shopeeOrderNo, @Param("packageStatus") int packageStatus);

    /**
     * 查询拆包订单
     *
     * @param id 拆包订单ID
     * @return 拆包订单
     */
    public UnpackingOrder selectUnpackingOrderById(Long id);

    /**
     * 查询拆包订单列表
     *
     * @param unpackingOrder 拆包订单
     * @return 拆包订单集合
     */
    public List<UnpackingOrder> selectUnpackingOrderList(UnpackingOrder unpackingOrder);


    /**
     * 新增拆包订单
     *
     * @param unpackingOrder 拆包订单
     * @return 结果
     */
    public int insertUnpackingOrder(UnpackingOrder unpackingOrder);

    /**
     * 修改拆包订单
     *
     * @param unpackingOrder 拆包订单
     * @return 结果
     */
    public int updateUnpackingOrder(UnpackingOrder unpackingOrder);

    public int updateUnpackingOrderByShopeeOrderNo(UnpackingOrder unpackingOrder);

    /**
     * 删除拆包订单
     *
     * @param id 拆包订单ID
     * @return 结果
     */
    public int deleteUnpackingOrderById(Long id);

    /**
     * 批量删除拆包订单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUnpackingOrderByIds(String[] ids);

    int queryCount(@Param("userId") Long userId);

    List<UnpackingOrder> selectByShopeeOrderNos(String[] orderIds);

    public int updateExeFlag(@Param("updateTime") Date updateTime);
}
