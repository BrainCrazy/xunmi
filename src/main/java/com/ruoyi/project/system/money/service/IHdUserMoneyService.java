package com.ruoyi.project.system.money.service;

import com.ruoyi.project.system.money.domain.HdUserMoney;

import java.util.List;

/**
 * 用户余额变动Service接口
 *
 * @author ruoyi
 * @date 2019-10-21
 */
public interface IHdUserMoneyService {



    /**
     * 查询用户余额变动
     *
     * @param id 用户余额变动ID
     * @return 用户余额变动
     */
    public HdUserMoney selectHdUserMoneyById(Long id);

    /**
     * 查询用户余额变动列表
     *
     * @param hdUserMoney 用户余额变动
     * @return 用户余额变动集合
     */
    public List<HdUserMoney> selectHdUserMoneyList(HdUserMoney hdUserMoney);

    /**
     * 新增用户余额变动
     *
     * @param hdUserMoney 用户余额变动
     * @return 结果
     */
    public int insertHdUserMoney(HdUserMoney hdUserMoney);

    /**
     * 修改用户余额变动
     *
     * @param hdUserMoney 用户余额变动
     * @return 结果
     */
    public int updateHdUserMoney(HdUserMoney hdUserMoney);

    /**
     * 批量删除用户余额变动
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdUserMoneyByIds(String ids);

    /**
     * 删除用户余额变动信息
     *
     * @param id 用户余额变动ID
     * @return 结果
     */
    public int deleteHdUserMoneyById(Long id);
}
