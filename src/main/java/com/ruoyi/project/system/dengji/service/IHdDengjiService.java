package com.ruoyi.project.system.dengji.service;

import com.ruoyi.project.system.dengji.domain.HdDengji;
import java.util.List;

/**
 * 等级Service接口
 * 
 * @author ruoyi
 * @date 2019-11-26
 */
public interface IHdDengjiService 
{
    /**
     * 查询等级
     * 
     * @param id 等级ID
     * @return 等级
     */
    public HdDengji selectHdDengjiById(Long id);

    /**
     * 查询等级列表
     * 
     * @param hdDengji 等级
     * @return 等级集合
     */
    public List<HdDengji> selectHdDengjiList(HdDengji hdDengji);

    /**
     * 新增等级
     * 
     * @param hdDengji 等级
     * @return 结果
     */
    public int insertHdDengji(HdDengji hdDengji);

    /**
     * 修改等级
     * 
     * @param hdDengji 等级
     * @return 结果
     */
    public int updateHdDengji(HdDengji hdDengji);

    /**
     * 批量删除等级
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdDengjiByIds(String ids);

    /**
     * 删除等级信息
     * 
     * @param id 等级ID
     * @return 结果
     */
    public int deleteHdDengjiById(Long id);
}
