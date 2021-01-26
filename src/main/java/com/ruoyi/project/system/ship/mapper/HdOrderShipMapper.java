package com.ruoyi.project.system.ship.mapper;

import com.ruoyi.project.system.ship.domain.HdOrderShip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 待处理订单Mapper接口
 *
 * @author ruoyi
 * @date 2020-02-15
 */
@Mapper
public interface HdOrderShipMapper {
    /**
     * 查询待处理订单
     *
     * @param orderId 待处理订单ID
     * @return 待处理订单
     */
    public HdOrderShip selectHdOrderShipById(String orderId);

    /**
     * 查询待处理订单列表
     *
     * @param hdOrderShip 待处理订单
     * @return 待处理订单集合
     */
    public List<HdOrderShip> selectHdOrderShipList(HdOrderShip hdOrderShip);

    /**
     * 新增待处理订单
     *
     * @param hdOrderShip 待处理订单
     * @return 结果
     */
    public int insertHdOrderShip(HdOrderShip hdOrderShip);

    /**
     * 修改待处理订单
     *
     * @param hdOrderShip 待处理订单
     * @return 结果
     */
    public int updateHdOrderShip(HdOrderShip hdOrderShip);

    /**
     * 删除待处理订单
     *
     * @param orderId 待处理订单ID
     * @return 结果
     */
    public int deleteHdOrderShipById(String orderId);

    /**
     * 批量删除待处理订单
     *
     * @param orderIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdOrderShipByIds(String[] orderIds);

    /**
     * 删除指定用户订单
     *
     * @param userId 用户id
     * @return ~
     */
    int deleteByUserId(Long userId);

    /**
     * 根据订单id和物流id查询订单
     *
     * @param orderIds   订单id集合
     * @param trackingNo 物流id
     * @return ~
     */
    List<HdOrderShip> selectHdOrderShipByIdAndTrackingNo(@Param("orderIds") Collection<String> orderIds, @Param("trackingNo") String trackingNo);

    /**
     * 根据用户id和物流id查询订单
     *
     * @param userId     用户id
     * @param trackingNo 物流id
     * @return ~
     */
    List<HdOrderShip> selectHdOrderShipByUserIdAndTrackingNo(@Param("userId") Long userId, @Param("trackingNo") String trackingNo);
}
