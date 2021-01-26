package com.ruoyi.project.system.money.mapper;

import com.ruoyi.project.system.money.domain.HdUserMoney;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户余额变动Mapper接口
 * 
 * @author ruoyi
 * @date 2019-10-21
 */
public interface HdUserMoneyMapper 
{
    /**
     * 查询用户余额变动
     * 
     * @param id 用户余额变动ID
     * @return 用户余额变动
     */
    public HdUserMoney selectHdUserMoneyLasted(@Param("userId") Long userId);

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


    public HdUserMoney selectHdUserMoneyById(@Param("id") Long id);


    int updateHdUserMoney(HdUserMoney hdUserMoney);
}
