package com.ruoyi.project.system.miandan.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.item.mapper.HdItemMapper;
import com.ruoyi.project.system.miandan.domain.MiandanOrder;
import com.ruoyi.project.system.miandan.mapper.MiandanOrderMapper;
import com.ruoyi.project.system.miandan.service.IMiandanOrderService;
import com.ruoyi.project.system.money.domain.HdUserMoney;
import com.ruoyi.project.system.money.mapper.HdUserMoneyMapper;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import com.ruoyi.project.system.userOrder.domain.OrderItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 纯贴面单Service业务层处理
 *
 * @author wqf
 * @date 2020-02-14
 */
@Service
public class MiandanOrderServiceImpl implements IMiandanOrderService {

    @Autowired
    private MiandanOrderMapper miandanOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HdItemMapper hdItemMapper;

    @Autowired
    private HdUserMoneyMapper hdUserMoneyMapper;

    @Override
    public int updateStatusById(long id, int sourceStatus, int targetStatus) {
        return miandanOrderMapper.updateStatusById(id, sourceStatus, targetStatus);
    }

    /**
     * 查询纯贴面单
     *
     * @param id 纯贴面单ID
     * @return 纯贴面单
     */
    @Override
    public MiandanOrder selectMiandanOrderById(Long id) {
        return miandanOrderMapper.selectMiandanOrderById(id);
    }

    /**
     * 查询纯贴面单列表
     *
     * @param miandanOrder 纯贴面单
     * @return 纯贴面单
     */
    @Override
    public List<MiandanOrder> selectMiandanOrderList(MiandanOrder miandanOrder) {
        return miandanOrderMapper.selectMiandanOrderList(miandanOrder);
    }

    /**
     * 新增纯贴面单
     *
     * @param miandanOrder 纯贴面单
     * @return 结果
     */
    @Override
    public int insertMiandanOrder(MiandanOrder miandanOrder) {
        miandanOrder.setCreateTime(DateUtils.getNowDate());
        return miandanOrderMapper.insertMiandanOrder(miandanOrder);
    }

    /**
     * 修改纯贴面单
     *
     * @param miandanOrder 纯贴面单
     * @return 结果
     */
    @Override
    public int updateMiandanOrder(MiandanOrder miandanOrder, int status) {
        miandanOrder.setUpdateTime(DateUtils.getNowDate());
        miandanOrder.setStatus(status);
        return miandanOrderMapper.updateMiandanOrder(miandanOrder);
    }

    /**
     * 删除纯贴面单对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int deleteMiandanOrderByIds(String ids) {
        //扣减预扣金额
        MiandanOrder miandanOrder = miandanOrderMapper.selectMiandanOrderById(Long.parseLong(ids));
        User user = userMapper.selectUserById(miandanOrder.getUserId());
        user.setPreMoney(user.getPreMoney() - 150);
        userMapper.updateUser(user);
        return miandanOrderMapper.deleteMiandanOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除纯贴面单信息
     *
     * @param id 纯贴面单ID
     * @return 结果
     */
    public int deleteMiandanOrderById(Long id) {
        return miandanOrderMapper.deleteMiandanOrderById(id);
    }

    @Override
    public int queryCount(Long userId) {
        return miandanOrderMapper.queryCount(userId);
    }

    @Override
    public List<HdItem> getorderDetail(Long id) {
        MiandanOrder miandan = miandanOrderMapper.selectMiandanOrderById(id);
        List<OrderItemInfo> list = JSON.parseArray(miandan.getItems(), OrderItemInfo.class);
        List<Long> itemIds = new ArrayList<>();
        for (OrderItemInfo info : list) {
            itemIds.add(info.getItem_id());
        }
        List<HdItem> res = hdItemMapper.selectByItemIds(itemIds);
        JSONArray imgJsonArray;
        for (HdItem item : res) {
            imgJsonArray = JSONArray.parseArray(item.getImages());
            item.setImages(imgJsonArray.getString(0));
        }
        return res;
    }

    /**
     * 确认发货处理
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void confirmOutStock(Long id) {
        //执行扣费处理
        long fee = 150;
        MiandanOrder miandanOrder = miandanOrderMapper.selectMiandanOrderById(id);
        if (miandanOrder.getFeeFlag() == 10) {
            User user = userMapper.selectUserById(miandanOrder.getUserId());
            //新订单,执行扣费,过滤已扣费的历史订单
            user.setPreMoney(user.getPreMoney() - fee);
            user.setMoney(user.getMoney() - fee);
            //更新user表
            userMapper.updateUser(user);
            HdUserMoney hdUserMoney = new HdUserMoney();
            hdUserMoney.setAmount(fee);
            hdUserMoney.setCreateTime(DateUtils.getNowDate());
            hdUserMoney.setBalance(user.getMoney());
            hdUserMoney.setUserId(user.getUserId());
            hdUserMoney.setUserName(user.getUserName());
            //10-下单消费
            hdUserMoney.setChangeType(10);
            hdUserMoney.setRemark("纯贴面单确认发货,扣款1.5元");
            //入库余额变动
            hdUserMoneyMapper.insertHdUserMoney(hdUserMoney);
        }
        //更新订单表状态
        miandanOrderMapper.updateStatusById(id, 30, 40);

    }
}
