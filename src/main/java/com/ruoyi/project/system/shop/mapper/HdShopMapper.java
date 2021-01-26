package com.ruoyi.project.system.shop.mapper;

import com.ruoyi.project.system.shop.domain.HdShop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 授权店铺Mapper接口
 *
 * @author ruoyi
 * @date 2020-02-13
 */
@Mapper
public interface HdShopMapper {
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
     * 删除授权店铺
     *
     * @param shopId 授权店铺ID
     * @return 结果
     */
    public int deleteHdShopById(Long shopId);

    /**
     * 批量删除授权店铺
     *
     * @param shopIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteHdShopByIds(String[] shopIds);

    /**
     * 根据用户id授权店铺
     *
     * @param userId 用户id
     * @return ~
     */
    List<HdShop> findByUserId(Long userId);

    /**
     * 查询所有
     * @return
     */
    List<HdShop> findAll();
}
