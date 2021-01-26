package com.ruoyi.project.system.ship.service;

import com.ruoyi.common.utils.waybill.SfPrintOrderParam;
import com.ruoyi.project.system.ship.domain.HdOrderShip;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 待处理订单Service接口
 *
 * @author ruoyi
 * @date 2020-02-15
 */
public interface IHdOrderShipService {
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    int insertOrderNewTransactional(HdOrderShip hdOrderShip);

    /**
     * 修改待处理订单
     *
     * @param hdOrderShip 待处理订单
     * @return 结果
     */
    public int updateHdOrderShip(HdOrderShip hdOrderShip);

    /**
     * 批量删除待处理订单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdOrderShipByIds(String ids);

    /**
     * 删除待处理订单信息
     *
     * @param orderId 待处理订单ID
     * @return 结果
     */
    public int deleteHdOrderShipById(String orderId);


    /**
     * 根据用户id删除订单
     *
     * @param userId 用户id
     * @return 删除数量
     */
    int deleteByUserId(Long userId);

    /**
     * 生成面单
     *
     * @param shopId  店铺id
     * @param orderId 订单id
     * @return pdf文件
     * @throws Exception ~
     */
    byte[] createLogisticsPDF(Long shopId, String orderId) throws Exception;

    /**
     * 生成面单
     *
     * @param shopId  店铺id
     * @param orderId 订单id
     * @param country 国家
     * @param items   商品信息
     * @return 面单byte[]
     * @exception ~
     */
    byte[] createLogisticsImg(Long shopId, String orderId, String country, List<SfPrintOrderParam.Item> items) throws Exception;

    /**
     * 根据订单id更新商品信息
     *
     * @param orderId 订单id
     * @return 成功-true
     */
    boolean updateItemByOrderId(String orderId);

    /**
     * 申请物流编号
     *
     * @param orderId 订单id
     * @throws Exception ~
     */
    void applyTrackingNo(String orderId) throws Exception;

    /**
     * 根据订单id和物流id查询订单
     *
     * @param ids        订单id
     *                   格式 'orderId,orderId,orderId'
     * @param trackingNo 物流id
     * @return ~
     */
    List<HdOrderShip> selectHdOrderShipByIdAndTrackingNo(String[] ids, String trackingNo);

    /**
     * 根据用户id和物流id查询订单
     *
     * @param userId     用户id
     * @param trackingNo 物流id
     * @return ~
     */
    List<HdOrderShip> selectHdOrderShipByUserIdAndTrackingNo(Long userId, String trackingNo);

    /**
     * 申请物流信息
     *
     * @param shopId  店铺id
     * @param orderId 订单id
     * @return true-成功
     * @throws Exception ~
     */
    boolean applySingleTrackingNo(Long shopId, String orderId) throws Exception;
}
