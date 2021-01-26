package com.ruoyi.project.system.userpackage.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import com.ruoyi.project.system.StockInfoItem.mapper.StockInfoItemMapper;
import com.ruoyi.project.system.StockInfoItem.service.IStockInfoItemService;
import com.ruoyi.project.system.miandan.domain.MiandanOrder;
import com.ruoyi.project.system.miandan.mapper.MiandanOrderMapper;
import com.ruoyi.project.system.unpackage.domain.UnpackingOrder;
import com.ruoyi.project.system.unpackage.mapper.UnpackingOrderMapper;
import com.ruoyi.project.system.userOrder.domain.OrderItemInfo;
import com.ruoyi.project.system.userpackage.domain.UserPackage;
import com.ruoyi.project.system.userpackage.mapper.UserPackageMapper;
import com.ruoyi.project.system.userpackage.service.IUserPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户快递Service业务层处理
 *
 * @author wqf
 * @date 2020-02-15
 */
@Service
public class UserPackageServiceImpl implements IUserPackageService {

    @Autowired
    private UserPackageMapper userPackageMapper;

    @Autowired
    private UnpackingOrderMapper unpackingOrderMapper;

    @Autowired
    private MiandanOrderMapper miandanOrderMapper;

    @Autowired
    private StockInfoItemMapper stockInfoItemMapper;

    @Autowired
    private IStockInfoItemService stockInfoItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePackageNos(Long[] ids, String[] packageNos, String orderNo, String remark) {
        int status = 10;
        if (ids.length != packageNos.length) {
            throw new BusinessException("参数不正确！");
        }
        int size = ids.length;
        boolean flag = true;
        for (int i = 0; i < size; i++) {
            if (StringUtils.isEmpty(packageNos[i])) {
                flag = false;
                continue;
            }
            //查询
            UserPackage userPackage = userPackageMapper.selectUserPackageById(ids[i]);
            if (20 == userPackage.getStatus()) {
                //已入库，不能修改
                continue;
            }
            userPackageMapper.updatePackageNoById(ids[i], packageNos[i]);
        }
        if (flag) {
            status = 20;
        }
        if (status == 10 && StringUtils.isEmpty(remark)) {
            return true;
        }
        //更新订单表的状态
        unpackingOrderMapper.updateStatusAndRemarkByShopeeOrderNo(orderNo, status, remark);
        return true;
    }

    @Override
    public List<UserPackage> selectByShopeeOrderNo(String shopeeOrderNo) {
        return userPackageMapper.selectByShopeeOrderNo(shopeeOrderNo);
    }

    @Override
    public List<UserPackage> selectByShopeeOrderNoNotNull(String shopeeOrderNo) {
        return userPackageMapper.selectByShopeeOrderNoNotNull(shopeeOrderNo);
    }

    /**
     * 查询用户快递
     *
     * @param id 用户快递ID
     * @return 用户快递
     */
    @Override
    public UserPackage selectUserPackageById(Long id) {
        return userPackageMapper.selectUserPackageById(id);
    }

    /**
     * 查询用户快递列表
     *
     * @param userPackage 用户快递
     * @return 用户快递
     */
    @Override
    public List<UserPackage> selectUserPackageList(UserPackage userPackage) {
        return userPackageMapper.selectUserPackageList(userPackage);
    }

    /**
     * 新增用户快递
     *
     * @param userPackage 用户快递
     * @return 结果
     */
    @Override
    public int insertUserPackage(UserPackage userPackage, MultipartFile goodsImage) throws IOException {
        if (null != goodsImage) {
            String stockImage = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), goodsImage);
            userPackage.setGoodsImage(stockImage);
        }
        userPackage.setUserId(ShiroUtils.getUserId());
        userPackage.setStatus(10);
        return userPackageMapper.insertUserPackage(userPackage);
    }

    /**
     * 修改用户快递
     *
     * @param userPackage 用户快递
     * @return 结果
     */
    @Override
    public int updateUserPackage(UserPackage userPackage) {
        return userPackageMapper.updateUserPackage(userPackage);
    }

    /**
     * 删除用户快递对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserPackageByIds(String ids) {
        return userPackageMapper.deleteUserPackageByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户快递信息
     *
     * @param id 用户快递ID
     * @return 结果
     */
    public int deleteUserPackageById(Long id) {
        return userPackageMapper.deleteUserPackageById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStockImage(MultipartFile file, long id, int orderType) throws IOException {
        String stockImage = null;
        if (null != file) {
            stockImage = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);
        }
        if (orderType == 10) {
            //拆包订单,
            userPackageMapper.updateStockImage(id, stockImage, 20);
            UserPackage userPackage = userPackageMapper.selectUserPackageById(id);
            //查询快递号非空的快递
            List<UserPackage> list = userPackageMapper.selectByShopeeOrderNoNotNull(userPackage.getShopeeOrderNo());
            boolean flag = true;
            for (UserPackage u : list) {
                if (!StringUtils.isEmpty(u.getPackageNo()) && u.getStatus() == 10) {
                    flag = false;
                    break;
                }
            }
            //查询订单原状态
            UnpackingOrder unpackingOrder = unpackingOrderMapper.selectByShopeeOrderNo(userPackage.getShopeeOrderNo());
            //若用户未提交订单,即使订单全部到了,也不能流转状态 | 用户提交了订单,但是货未全部确认入库也不处理
            if (flag && unpackingOrder.getPackageStatus() == 20) {
                //用户已提交订单,快递已快到货,更细状态为30-已入库
                unpackingOrderMapper.updateStatusByShopeeOrderNo(userPackage.getShopeeOrderNo(), 30);
            }
        } else if (orderType == 20) {
            //纯贴面单,更新为已入库
            miandanOrderMapper.updateStatusById(id, 20, 30);
        } else {
            //库存补货
            stockInfoItemService.confirmStock(id);
        }
        return true;
    }

    /**
     * @param packageNo
     * @param flag,true-查询未入库快递,false-不带状态查询
     * @return
     */
    @Override
    public List<UserPackage> getUserPackageInfo(String packageNo, boolean flag) {
        //查询快递表
        UserPackage userPackage = new UserPackage();
        userPackage.setPackageNo(packageNo);
        List<UserPackage> list = new ArrayList<>();
        if (flag) {
            list = userPackageMapper.selectUserPackageByPackageNo(packageNo, 10);
        } else {
            list = userPackageMapper.selectUserPackageList(userPackage);
        }
        //查询纯贴面单
        List<MiandanOrder> miandanOrders = new ArrayList<>();
        if (flag) {
            miandanOrders = miandanOrderMapper.selectMiandanOrderByPackageNo(packageNo, 20);
        } else {
            MiandanOrder seletMiandan = new MiandanOrder();
            seletMiandan.setPackageNo(packageNo);
            miandanOrders = miandanOrderMapper.selectMiandanOrderList(seletMiandan);
        }
        if (!CollectionUtils.isEmpty(miandanOrders)) {
            for (MiandanOrder miandanOrder : miandanOrders) {
                userPackage.setId(miandanOrder.getId());
                userPackage.setShopeeOrderNo(miandanOrder.getShopeeOrderNo());
                userPackage.setGoodsImage(miandanOrder.getGoodImage());
                List<OrderItemInfo> items = JSON.parseArray(miandanOrder.getItems(), OrderItemInfo.class);
                OrderItemInfo orderItemInfo = items.get(0);
                userPackage.setGoodsName(orderItemInfo.getItem_name());
                userPackage.setVariationName(orderItemInfo.getVariation_name());
                userPackage.setGoodsCount(orderItemInfo.getVariation_quantity_purchased());
                if (miandanOrder.getStatus() == 10 || miandanOrder.getStatus() == 20) {
                    userPackage.setStatus(10);
                } else {
                    userPackage.setStatus(20);
                }
                userPackage.setPhotograph(10);
                userPackage.setOrderType(20);
                list.add(userPackage);
            }
        }
        //查询库存明细表
        List<StockInfoItem> items = new ArrayList<>();
        if (flag) {
            items = stockInfoItemMapper.selectStockInfoItemByPackageNo(packageNo, 10);
        } else {
            StockInfoItem stockInfoItem = new StockInfoItem();
            stockInfoItem.setPackageNo(packageNo);
            items = stockInfoItemMapper.selectStockInfoItemList(stockInfoItem);
        }
        if (!CollectionUtils.isEmpty(items)) {
            for (StockInfoItem item : items) {
                userPackage.setId(item.getId());
                userPackage.setShopeeOrderNo(item.getShopeeOrderNo());
                userPackage.setGoodsImage(item.getGoodsImageUrl());
                userPackage.setGoodsName(item.getGoodsName());
                userPackage.setVariationName(item.getCategoryName());
                userPackage.setGoodsCount(new Long(item.getGoodsCount()).intValue());
                userPackage.setStatus(item.getStockItemStatus());
                userPackage.setPhotograph(10);
                userPackage.setOrderType(30);
                list.add(userPackage);
            }
        }
        return list;
    }

    @Override
    public List<UserPackage> getUserPackageInfoByType(long id, int orderType) {
        List<UserPackage> list = new ArrayList<>();
        UserPackage userPackage = new UserPackage();
        userPackage.setId(id);
        if (10 == orderType) {
            userPackage = userPackageMapper.selectUserPackageById(id);
            list.add(userPackage);
        } else if (20 == orderType) {
            MiandanOrder miandanOrder = miandanOrderMapper.selectMiandanOrderById(id);
            userPackage.setPackageNo(miandanOrder.getPackageNo());
            userPackage.setShopeeOrderNo(miandanOrder.getShopeeOrderNo());
            userPackage.setGoodsImage(miandanOrder.getGoodImage());
            List<OrderItemInfo> items = JSON.parseArray(miandanOrder.getItems(), OrderItemInfo.class);
            OrderItemInfo orderItemInfo = items.get(0);
            userPackage.setGoodsName(orderItemInfo.getItem_name());
            userPackage.setVariationName(orderItemInfo.getVariation_name());
            userPackage.setGoodsCount(orderItemInfo.getVariation_quantity_purchased());
            if (miandanOrder.getStatus() == 10 || miandanOrder.getStatus() == 20) {
                userPackage.setStatus(10);
            } else {
                userPackage.setStatus(20);
            }
            userPackage.setPhotograph(10);
            userPackage.setOrderType(20);
            list.add(userPackage);
        } else {
            StockInfoItem item = stockInfoItemMapper.selectStockInfoItemById(id);
            userPackage.setShopeeOrderNo(item.getShopeeOrderNo());
            userPackage.setGoodsImage(item.getGoodsImageUrl());
            userPackage.setGoodsName(item.getGoodsName());
            userPackage.setVariationName(item.getCategoryName());
            userPackage.setGoodsCount(new Long(item.getGoodsCount()).intValue());
            userPackage.setStatus(item.getStockItemStatus());
            userPackage.setPhotograph(10);
            userPackage.setOrderType(30);
            list.add(userPackage);
        }
        return list;
    }
}
