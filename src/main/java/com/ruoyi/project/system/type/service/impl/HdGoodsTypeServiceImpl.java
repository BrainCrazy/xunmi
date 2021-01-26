package com.ruoyi.project.system.type.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.type.mapper.HdGoodsTypeMapper;
import com.ruoyi.project.system.type.domain.HdGoodsType;
import com.ruoyi.project.system.type.service.IHdGoodsTypeService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 用户商品类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2019-10-21
 */
@Service
public class HdGoodsTypeServiceImpl implements IHdGoodsTypeService 
{
    @Autowired
    private HdGoodsTypeMapper hdGoodsTypeMapper;

    /**
     * 查询用户商品类型
     * 
     * @param id 用户商品类型ID
     * @return 用户商品类型
     */
    @Override
    public HdGoodsType selectHdGoodsTypeById(Long id)
    {
        return hdGoodsTypeMapper.selectHdGoodsTypeById(id);
    }

    /**
     * 查询用户商品类型列表
     * 
     * @param hdGoodsType 用户商品类型
     * @return 用户商品类型
     */
    @Override
    public List<HdGoodsType> selectHdGoodsTypeList(HdGoodsType hdGoodsType)
    {
        return hdGoodsTypeMapper.selectHdGoodsTypeList(hdGoodsType);
    }

    /**
     * 新增用户商品类型
     * 
     * @param hdGoodsType 用户商品类型
     * @return 结果
     */
    @Override
    public int insertHdGoodsType(HdGoodsType hdGoodsType)
    {
        hdGoodsType.setCreateTime(DateUtils.getNowDate());
        return hdGoodsTypeMapper.insertHdGoodsType(hdGoodsType);
    }

    /**
     * 修改用户商品类型
     * 
     * @param hdGoodsType 用户商品类型
     * @return 结果
     */
    @Override
    public int updateHdGoodsType(HdGoodsType hdGoodsType)
    {
        hdGoodsType.setUpdateTime(DateUtils.getNowDate());
        return hdGoodsTypeMapper.updateHdGoodsType(hdGoodsType);
    }

    /**
     * 删除用户商品类型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHdGoodsTypeByIds(String ids)
    {
        return hdGoodsTypeMapper.deleteHdGoodsTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户商品类型信息
     * 
     * @param id 用户商品类型ID
     * @return 结果
     */
    public int deleteHdGoodsTypeById(Long id)
    {
        return hdGoodsTypeMapper.deleteHdGoodsTypeById(id);
    }
}
