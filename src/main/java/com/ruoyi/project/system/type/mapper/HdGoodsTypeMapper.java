package com.ruoyi.project.system.type.mapper;

import com.ruoyi.project.system.type.domain.HdGoodsType;
import java.util.List;

/**
 * 用户商品类型Mapper接口
 * 
 * @author ruoyi
 * @date 2019-10-21
 */
public interface HdGoodsTypeMapper 
{
    /**
     * 查询用户商品类型
     * 
     * @param id 用户商品类型ID
     * @return 用户商品类型
     */
    public HdGoodsType selectHdGoodsTypeById(Long id);

    /**
     * 查询用户商品类型列表
     * 
     * @param hdGoodsType 用户商品类型
     * @return 用户商品类型集合
     */
    public List<HdGoodsType> selectHdGoodsTypeList(HdGoodsType hdGoodsType);

    /**
     * 新增用户商品类型
     * 
     * @param hdGoodsType 用户商品类型
     * @return 结果
     */
    public int insertHdGoodsType(HdGoodsType hdGoodsType);

    /**
     * 修改用户商品类型
     * 
     * @param hdGoodsType 用户商品类型
     * @return 结果
     */
    public int updateHdGoodsType(HdGoodsType hdGoodsType);

    /**
     * 删除用户商品类型
     * 
     * @param id 用户商品类型ID
     * @return 结果
     */
    public int deleteHdGoodsTypeById(Long id);

    /**
     * 批量删除用户商品类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdGoodsTypeByIds(String[] ids);
}
