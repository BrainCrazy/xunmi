package com.ruoyi.project.system.aboutme.service;

import com.ruoyi.project.system.aboutme.domain.HdAboutme;
import java.util.List;

/**
 * 关于我们Service接口
 * 
 * @author ruoyi
 * @date 2019-11-26
 */
public interface IHdAboutmeService 
{
    /**
     * 查询关于我们
     * 
     * @param id 关于我们ID
     * @return 关于我们
     */
    public HdAboutme selectHdAboutmeById(Long id);

    /**
     * 查询关于我们列表
     * 
     * @param hdAboutme 关于我们
     * @return 关于我们集合
     */
    public List<HdAboutme> selectHdAboutmeList(HdAboutme hdAboutme);

    /**
     * 新增关于我们
     * 
     * @param hdAboutme 关于我们
     * @return 结果
     */
    public int insertHdAboutme(HdAboutme hdAboutme);

    /**
     * 修改关于我们
     * 
     * @param hdAboutme 关于我们
     * @return 结果
     */
    public int updateHdAboutme(HdAboutme hdAboutme);

    /**
     * 批量删除关于我们
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdAboutmeByIds(String ids);

    /**
     * 删除关于我们信息
     * 
     * @param id 关于我们ID
     * @return 结果
     */
    public int deleteHdAboutmeById(Long id);
}
