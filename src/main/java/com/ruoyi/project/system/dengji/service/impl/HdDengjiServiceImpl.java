package com.ruoyi.project.system.dengji.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.dengji.mapper.HdDengjiMapper;
import com.ruoyi.project.system.dengji.domain.HdDengji;
import com.ruoyi.project.system.dengji.service.IHdDengjiService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 等级Service业务层处理
 * 
 * @author ruoyi
 * @date 2019-11-26
 */
@Service
public class HdDengjiServiceImpl implements IHdDengjiService 
{
    @Autowired
    private HdDengjiMapper hdDengjiMapper;

    /**
     * 查询等级
     * 
     * @param id 等级ID
     * @return 等级
     */
    @Override
    public HdDengji selectHdDengjiById(Long id)
    {
        return hdDengjiMapper.selectHdDengjiById(id);
    }

    /**
     * 查询等级列表
     * 
     * @param hdDengji 等级
     * @return 等级
     */
    @Override
    public List<HdDengji> selectHdDengjiList(HdDengji hdDengji)
    {
        return hdDengjiMapper.selectHdDengjiList(hdDengji);
    }

    /**
     * 新增等级
     * 
     * @param hdDengji 等级
     * @return 结果
     */
    @Override
    public int insertHdDengji(HdDengji hdDengji)
    {
        hdDengji.setCreateTime(DateUtils.getNowDate());
        return hdDengjiMapper.insertHdDengji(hdDengji);
    }

    /**
     * 修改等级
     * 
     * @param hdDengji 等级
     * @return 结果
     */
    @Override
    public int updateHdDengji(HdDengji hdDengji)
    {
        hdDengji.setUpdateTime(DateUtils.getNowDate());
        return hdDengjiMapper.updateHdDengji(hdDengji);
    }

    /**
     * 删除等级对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHdDengjiByIds(String ids)
    {
        return hdDengjiMapper.deleteHdDengjiByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除等级信息
     * 
     * @param id 等级ID
     * @return 结果
     */
    public int deleteHdDengjiById(Long id)
    {
        return hdDengjiMapper.deleteHdDengjiById(id);
    }
}
