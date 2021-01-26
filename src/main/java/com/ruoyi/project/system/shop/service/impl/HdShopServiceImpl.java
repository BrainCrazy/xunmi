package com.ruoyi.project.system.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.system.shop.domain.HdShop;
import com.ruoyi.project.system.shop.mapper.HdShopMapper;
import com.ruoyi.project.system.shop.remote.ShopInterface;
import com.ruoyi.project.system.shop.service.IHdShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 授权店铺Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-13
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HdShopServiceImpl implements IHdShopService {
    @Autowired
    private HdShopMapper hdShopMapper;

    /**
     * 查询授权店铺
     *
     * @param shopId 授权店铺ID
     * @return 授权店铺
     */
    @Override
    public HdShop selectHdShopById(Long shopId) {
        return hdShopMapper.selectHdShopById(shopId);
    }

    /**
     * 查询授权店铺列表
     *
     * @param hdShop 授权店铺
     * @return 授权店铺
     */
    @Override
    public List<HdShop> selectHdShopList(HdShop hdShop) {
        Long userId = ShiroUtils.getUserId();
        hdShop.setUserId(userId);
        return hdShopMapper.selectHdShopList(hdShop);
    }

    /**
     * 新增授权店铺
     *
     * @param hdShop 授权店铺
     * @return 结果
     */
    @Override
    public int insertHdShop(HdShop hdShop) {
        hdShop.setCreateTime(DateUtils.getNowDate());
        return hdShopMapper.insertHdShop(hdShop);
    }

    /**
     * 修改授权店铺
     *
     * @param hdShop 授权店铺
     * @return 结果
     */
    @Override
    public int updateHdShop(HdShop hdShop) {
        hdShop.setUpdateTime(DateUtils.getNowDate());
        return hdShopMapper.updateHdShop(hdShop);
    }

    /**
     * 删除授权店铺对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHdShopByIds(String ids) {
        return hdShopMapper.deleteHdShopByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除授权店铺信息
     *
     * @param shopId 授权店铺ID
     * @return 结果
     */
    @Override
    public int deleteHdShopById(Long shopId) {
        return hdShopMapper.deleteHdShopById(shopId);
    }

    /**
     * 创建授权店铺信息
     *
     * @param shopId 店铺id
     * @param userId 用户id
     */
    @Override
    public void createShopInfo(Long shopId, Long userId) {
        HdShop hdShop = new HdShop();
        hdShop.setShopId(shopId);
        hdShop.setUserId(userId);
        // 获取店铺信息
        String shopInfo = null;
        try {
            shopInfo = ShopInterface.getShopInfo(shopId);
        } catch (Exception e) {
            log.error("创建授权店铺信息,请求失败-->", e);
            throw new BusinessException(e.getMessage());
        }
        JSONObject json = JSON.parseObject(shopInfo);
        String shopName = json.getString("shop_name");
        String country = json.getString("country");
        if (StringUtils.isEmpty(shopName) && StringUtils.isEmpty(country)) {
            log.error("获取店铺信息错误,远程接口请求失败,shopId-->{},shopInfo-->{}", shopId, shopInfo);
            throw new BusinessException("获取店铺信息错误,远程接口请求失败");
        }
        hdShop.setCountryCode(country);
        hdShop.setShopName(shopName);
        hdShop.setCreateTime(new Date());
        JSONArray jsonArray = json.getJSONArray("sip_a_shops");
        if (jsonArray != null && !jsonArray.isEmpty()) {
            for (Object o : jsonArray) {
                JSONObject obj = (JSONObject) o;
                Long aShopId = obj.getLong("a_shop_id");
                String otherCountry = obj.getString("country");
                if (aShopId != null) {
                    HdShop hdShopById = hdShopMapper.selectHdShopById(aShopId);
                    if (hdShopById == null) {
                        HdShop otherShop = new HdShop();
                        otherShop.setShopId(aShopId);
                        otherShop.setUserId(userId);
                        otherShop.setShopName(shopName);
                        otherShop.setCountryCode(otherCountry);
                        otherShop.setCreateTime(new Date());
                        int i = hdShopMapper.insertHdShop(otherShop);
                        if (1 != i) {
                            log.error("插入数据失败,与预期不符,实际-->{},otherShop-->{}", i, otherShop);
                            throw new BusinessException("插入数据失败,与预期不符");
                        }
                    }
                }
            }
        }
        HdShop hdShopById = hdShopMapper.selectHdShopById(shopId);
        if (hdShopById != null) {
            return;
        }
        int i = hdShopMapper.insertHdShop(hdShop);
        if (1 != i) {
            log.error("插入数据失败,与预期不符,实际-->{},hdShop-->{}", i, hdShop);
            throw new BusinessException("插入数据失败,与预期不符");
        }
    }
}
