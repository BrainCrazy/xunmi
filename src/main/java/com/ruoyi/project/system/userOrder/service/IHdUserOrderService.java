package com.ruoyi.project.system.userOrder.service;

import com.ruoyi.project.system.userOrder.domain.HdUserOrder;
import java.util.List;

/**
 * 支付订单Service接口
 * 
 * @author ruoyi
 * @date 2019-10-21
 */
public interface IHdUserOrderService 
{
    /**
     * 查询支付订单
     * 
     * @param id 支付订单ID
     * @return 支付订单
     */
    public HdUserOrder selectHdUserOrderById(Long id);

    /**
     * 查询支付订单列表
     * 
     * @param hdUserOrder 支付订单
     * @return 支付订单集合
     */
    public List<HdUserOrder> selectHdUserOrderList(HdUserOrder hdUserOrder);

    /**
     * 新增支付订单
     * 
     * @param hdUserOrder 支付订单
     * @return 结果
     */
    public int insertHdUserOrder(HdUserOrder hdUserOrder);

    /**
     * 修改支付订单
     * 
     * @param hdUserOrder 支付订单
     * @return 结果
     */
    public int updateHdUserOrder(HdUserOrder hdUserOrder);

    /**
     * 批量删除支付订单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdUserOrderByIds(String ids);

    /**
     * 删除支付订单信息
     * 
     * @param id 支付订单ID
     * @return 结果
     */
    public int deleteHdUserOrderById(Long id);

    HdUserOrder selectOrderByOrderNo(String out_trade_no);
}
