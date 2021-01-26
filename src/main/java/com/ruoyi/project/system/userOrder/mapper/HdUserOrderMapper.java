package com.ruoyi.project.system.userOrder.mapper;

import com.ruoyi.project.system.userOrder.domain.HdUserOrder;
import java.util.List;

/**
 * 支付订单Mapper接口
 * 
 * @author ruoyi
 * @date 2019-10-21
 */
public interface HdUserOrderMapper 
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
     * 删除支付订单
     * 
     * @param id 支付订单ID
     * @return 结果
     */
    public int deleteHdUserOrderById(Long id);

    /**
     * 批量删除支付订单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdUserOrderByIds(String[] ids);

    HdUserOrder selectOrderByOrderNo(String orderNo);
}
