package com.ruoyi.project.system.aboutme.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.aboutme.mapper.HdAboutmeMapper;
import com.ruoyi.project.system.aboutme.domain.HdAboutme;
import com.ruoyi.project.system.aboutme.service.IHdAboutmeService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 关于我们Service业务层处理
 * 
 * @author ruoyi
 * @date 2019-11-26
 */
@Service
public class HdAboutmeServiceImpl implements IHdAboutmeService 
{
    @Autowired
    private HdAboutmeMapper hdAboutmeMapper;

    /**
     * 查询关于我们
     * 
     * @param id 关于我们ID
     * @return 关于我们
     */
    @Override
    public HdAboutme selectHdAboutmeById(Long id)
    {
        return hdAboutmeMapper.selectHdAboutmeById(id);
    }

    /**
     * 查询关于我们列表
     * 
     * @param hdAboutme 关于我们
     * @return 关于我们
     */
    @Override
    public List<HdAboutme> selectHdAboutmeList(HdAboutme hdAboutme)
    {
        return hdAboutmeMapper.selectHdAboutmeList(hdAboutme);
    }

    /**
     * 新增关于我们
     * 
     * @param hdAboutme 关于我们
     * @return 结果
     */
    @Override
    public int insertHdAboutme(HdAboutme hdAboutme)
    {
        return hdAboutmeMapper.insertHdAboutme(hdAboutme);
    }

    /**
     * 修改关于我们
     * 
     * @param hdAboutme 关于我们
     * @return 结果
     */
    @Override
    public int updateHdAboutme(HdAboutme hdAboutme)
    {
        return hdAboutmeMapper.updateHdAboutme(hdAboutme);
    }

    /**
     * 删除关于我们对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHdAboutmeByIds(String ids)
    {
        return hdAboutmeMapper.deleteHdAboutmeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除关于我们信息
     * 
     * @param id 关于我们ID
     * @return 结果
     */
    public int deleteHdAboutmeById(Long id)
    {
        return hdAboutmeMapper.deleteHdAboutmeById(id);
    }
}
