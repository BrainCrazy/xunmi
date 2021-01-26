package com.ruoyi.project.system.shop.service;

import com.ruoyi.project.system.shop.domain.HdShop;
import java.util.List;

/**
 * 授权店铺Service接口
 * 
 * @author ruoyi
 * @date 2020-02-13
 */
public interface IHdShopService 
{
    /**
     * 查询授权店铺
     * 
     * @param shopId 授权店铺ID
     * @return 授权店铺
     */
    public HdShop selectHdShopById(Long shopId);

    /**
     * 查询授权店铺列表
     * 
     * @param hdShop 授权店铺
     * @return 授权店铺集合
     */
    public List<HdShop> selectHdShopList(HdShop hdShop);

    /**
     * 新增授权店铺
     * 
     * @param hdShop 授权店铺
     * @return 结果
     */
    public int insertHdShop(HdShop hdShop);

    /**
     * 修改授权店铺
     * 
     * @param hdShop 授权店铺
     * @return 结果
     */
    public int updateHdShop(HdShop hdShop);

    /**
     * 批量删除授权店铺
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdShopByIds(String ids);

    /**
     * 删除授权店铺信息
     * 
     * @param shopId 授权店铺ID
     * @return 结果
     */
    public int deleteHdShopById(Long shopId);

    /**
     * 创建授权店铺信息
     * @param shopId 店铺id
     * @param userId 用户id
     */
    void createShopInfo(Long shopId, Long userId);
}
