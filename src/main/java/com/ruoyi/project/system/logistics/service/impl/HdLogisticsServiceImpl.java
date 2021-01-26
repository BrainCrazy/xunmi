package com.ruoyi.project.system.logistics.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.logistics.mapper.HdLogisticsMapper;
import com.ruoyi.project.system.logistics.domain.HdLogistics;
import com.ruoyi.project.system.logistics.service.IHdLogisticsService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 物流公司Service业务层处理
 * 
 * @author ruoyi
 * @date 2019-10-22
 */
@Service
public class HdLogisticsServiceImpl implements IHdLogisticsService 
{
    @Autowired
    private HdLogisticsMapper hdLogisticsMapper;

    /**
     * 查询物流公司
     * 
     * @param id 物流公司ID
     * @return 物流公司
     */
    @Override
    public HdLogistics selectHdLogisticsById(Long id)
    {
        return hdLogisticsMapper.selectHdLogisticsById(id);
    }

    /**
     * 查询物流公司列表
     * 
     * @param hdLogistics 物流公司
     * @return 物流公司
     */
    @Override
    public List<HdLogistics> selectHdLogisticsList(HdLogistics hdLogistics)
    {
        return hdLogisticsMapper.selectHdLogisticsList(hdLogistics);
    }

    /**
     * 新增物流公司
     * 
     * @param hdLogistics 物流公司
     * @return 结果
     */
    @Override
    public int insertHdLogistics(HdLogistics hdLogistics)
    {
        hdLogistics.setCreateTime(DateUtils.getNowDate());
        return hdLogisticsMapper.insertHdLogistics(hdLogistics);
    }

    /**
     * 修改物流公司
     * 
     * @param hdLogistics 物流公司
     * @return 结果
     */
    @Override
    public int updateHdLogistics(HdLogistics hdLogistics)
    {
        hdLogistics.setUpdateTime(DateUtils.getNowDate());
        return hdLogisticsMapper.updateHdLogistics(hdLogistics);
    }

    /**
     * 删除物流公司对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHdLogisticsByIds(String ids)
    {
        return hdLogisticsMapper.deleteHdLogisticsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除物流公司信息
     * 
     * @param id 物流公司ID
     * @return 结果
     */
    public int deleteHdLogisticsById(Long id)
    {
        return hdLogisticsMapper.deleteHdLogisticsById(id);
    }
}
