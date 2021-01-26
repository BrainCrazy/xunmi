package com.ruoyi.project.system.money.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.system.money.domain.HdUserMoney;
import com.ruoyi.project.system.money.mapper.HdUserMoneyMapper;
import com.ruoyi.project.system.money.service.IHdUserMoneyService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import com.ruoyi.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户余额变动Service业务层处理
 *
 * @author ruoyi
 * @date 2019-10-21
 */
@Service
public class HdUserMoneyServiceImpl implements IHdUserMoneyService {
    @Autowired
    private HdUserMoneyMapper hdUserMoneyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;


    @Override
    public HdUserMoney selectHdUserMoneyById(Long id) {
        return null;
    }

    /**
     * 查询用户余额变动列表
     *
     * @param hdUserMoney 用户余额变动
     * @return 用户余额变动
     */
    @Override
    public List<HdUserMoney> selectHdUserMoneyList(HdUserMoney hdUserMoney) {
        return hdUserMoneyMapper.selectHdUserMoneyList(hdUserMoney);
    }

    /**
     * 新增用户余额变动
     *
     * @param hdUserMoney 用户余额变动
     * @return 结果
     */
    @Override
    public int insertHdUserMoney(HdUserMoney hdUserMoney) {
        hdUserMoney.setCreateTime(DateUtils.getNowDate());
        return hdUserMoneyMapper.insertHdUserMoney(hdUserMoney);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateHdUserMoney(HdUserMoney hdUserMoney) {
        //查询user表
        User user = userService.selectUserById(hdUserMoney.getUserId());
        hdUserMoney.setBalance(user.getMoney() + hdUserMoney.getAmount());
        hdUserMoneyMapper.updateHdUserMoney(hdUserMoney);
        user.setMoney(hdUserMoney.getBalance());
        //更新用户表金额字段
        userMapper.updateUser(user);
        return 1;
    }

    @Override
    public int deleteHdUserMoneyByIds(String ids) {
        return 0;
    }

    @Override
    public int deleteHdUserMoneyById(Long id) {
        return 0;
    }

}
