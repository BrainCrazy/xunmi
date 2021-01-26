package com.ruoyi.project.system.logistics.mapper;

import com.ruoyi.project.system.logistics.domain.HdLogistics;
import java.util.List;

/**
 * 物流公司Mapper接口
 * 
 * @author ruoyi
 * @date 2019-10-22
 */
public interface HdLogisticsMapper 
{
    /**
     * 查询物流公司
     * 
     * @param id 物流公司ID
     * @return 物流公司
     */
    public HdLogistics selectHdLogisticsById(Long id);

    /**
     * 查询物流公司列表
     * 
     * @param hdLogistics 物流公司
     * @return 物流公司集合
     */
    public List<HdLogistics> selectHdLogisticsList(HdLogistics hdLogistics);

    /**
     * 新增物流公司
     * 
     * @param hdLogistics 物流公司
     * @return 结果
     */
    public int insertHdLogistics(HdLogistics hdLogistics);

    /**
     * 修改物流公司
     * 
     * @param hdLogistics 物流公司
     * @return 结果
     */
    public int updateHdLogistics(HdLogistics hdLogistics);

    /**
     * 删除物流公司
     * 
     * @param id 物流公司ID
     * @return 结果
     */
    public int deleteHdLogisticsById(Long id);

    /**
     * 批量删除物流公司
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdLogisticsByIds(String[] ids);
}
