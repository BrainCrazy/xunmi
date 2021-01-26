package com.ruoyi.project.system.userOrder.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.system.userOrder.domain.HdUserOrder;
import com.ruoyi.project.system.userOrder.mapper.HdUserOrderMapper;
import com.ruoyi.project.system.userOrder.service.IHdUserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2019-10-21
 */
@Service
public class HdUserOrderServiceImpl implements IHdUserOrderService 
{
    @Autowired
    private HdUserOrderMapper hdUserOrderMapper;

    /**
     * 查询支付订单
     * 
     * @param id 支付订单ID
     * @return 支付订单
     */
    @Override
    public HdUserOrder selectHdUserOrderById(Long id)
    {
        return hdUserOrderMapper.selectHdUserOrderById(id);
    }

    /**
     * 查询支付订单列表
     * 
     * @param hdUserOrder 支付订单
     * @return 支付订单
     */
    @Override
    public List<HdUserOrder> selectHdUserOrderList(HdUserOrder hdUserOrder)
    {
        return hdUserOrderMapper.selectHdUserOrderList(hdUserOrder);
    }

    /**
     * 新增支付订单
     * 
     * @param hdUserOrder 支付订单
     * @return 结果
     */
    @Override
    public int insertHdUserOrder(HdUserOrder hdUserOrder)
    {
        hdUserOrder.setCreateTime(DateUtils.getNowDate());
        return hdUserOrderMapper.insertHdUserOrder(hdUserOrder);
    }

    /**
     * 修改支付订单
     * 
     * @param hdUserOrder 支付订单
     * @return 结果
     */
    @Override
    public int updateHdUserOrder(HdUserOrder hdUserOrder)
    {
        hdUserOrder.setUpdateTime(DateUtils.getNowDate());
        return hdUserOrderMapper.updateHdUserOrder(hdUserOrder);
    }

    /**
     * 删除支付订单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHdUserOrderByIds(String ids)
    {
        return hdUserOrderMapper.deleteHdUserOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付订单信息
     * 
     * @param id 支付订单ID
     * @return 结果
     */
    public int deleteHdUserOrderById(Long id)
    {
        return hdUserOrderMapper.deleteHdUserOrderById(id);
    }

    @Override
    public HdUserOrder selectOrderByOrderNo(String out_trade_no) {
        return hdUserOrderMapper.selectOrderByOrderNo(out_trade_no);
    }
}
