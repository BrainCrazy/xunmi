package com.ruoyi.project.system.item.service;

import com.ruoyi.project.system.item.domain.HdItem;

import java.util.List;

/**
 * 商品明细Service接口
 *
 * @author ruoyi
 * @date 2020-02-15
 */
public interface IHdItemService {
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
     * 批量删除商品明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdItemByIds(String ids);

    /**
     * 删除商品明细信息
     *
     * @param itemId 商品明细ID
     * @return 结果
     */
    public int deleteHdItemById(Long itemId);

    /**
     * 异步更新或新增
     *
     * @param userId 用户id
     * @param shopId 店铺id
     * @param itemId 商品id
     */
    void asyncInsertOrUpdate(Long userId, Long shopId, Long itemId);

    /**
     * 同步更新或查询
     *
     * @param userId 用户id
     * @param shopId 店铺id
     * @param itemId 商品id
     * @return
     */
    boolean syncInsertOrUpdate(Long userId, Long shopId, Long itemId);

    /**
     * 根据商品id更新商品信息
     *
     * @param itemId 商品id
     * @return 成功-true
     */
    boolean updateItemByItemId(Long itemId);

    /**
     * 批量查询商品
     *
     * @param itemIds 商品id集合
     * @return ~
     */
    List<HdItem> selectHdItemByIds(List<Long> itemIds);
}
