package com.ruoyi.project.system.item.mapper;

import com.ruoyi.project.system.item.domain.HdItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品明细Mapper接口
 *
 * @author ruoyi
 * @date 2020-02-15
 */
@Mapper
@Repository
public interface HdItemMapper {
    /**
     * 查询商品明细
     *
     * @param itemId 商品明细ID
     * @return 商品明细
     */
    public HdItem selectHdItemById(Long itemId);

    /**
     * 查询商品明细列表
     *
     * @param hdItem 商品明细
     * @return 商品明细集合
     */
    public List<HdItem> selectHdItemList(HdItem hdItem);

    /**
     * 新增商品明细
     *
     * @param hdItem 商品明细
     * @return 结果
     */
    public int insertHdItem(HdItem hdItem);

    /**
     * 修改商品明细
     *
     * @param hdItem 商品明细
     * @return 结果
     */
    public int updateHdItem(HdItem hdItem);

    /**
     * 删除商品明细
     *
     * @param itemId 商品明细ID
     * @return 结果
     */
    public int deleteHdItemById(Long itemId);

    /**
     * 批量删除商品明细
     *
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdItemByIds(String[] itemIds);

    /**
     * 批量查询商品
     *
     * @param itemIds 商品id集合
     * @return ~
     */
    public List<HdItem> selectByItemIds(List<Long> itemIds);


}
