package com.ruoyi.project.system.userOrder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.item.mapper.HdItemMapper;
import com.ruoyi.project.system.miandan.domain.MiandanOrder;
import com.ruoyi.project.system.miandan.mapper.MiandanOrderMapper;
import com.ruoyi.project.system.ship.domain.HdOrderShip;
import com.ruoyi.project.system.ship.service.IHdOrderShipService;
import com.ruoyi.project.system.stockOrder.domain.StockOrder;
import com.ruoyi.project.system.stockOrder.mapper.StockOrderMapper;
import com.ruoyi.project.system.unpackage.domain.UnpackingOrder;
import com.ruoyi.project.system.unpackage.mapper.UnpackingOrderMapper;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import com.ruoyi.project.system.userpackage.domain.UserPackage;
import com.ruoyi.project.system.userpackage.mapper.UserPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserOrderService {

    private final static long MIANDAN_FEE = 150;
    private final static long PACKAGE_FEE = 300;

    @Autowired
    private IHdOrderShipService hdOrderShipService;

    @Autowired
    private MiandanOrderMapper miandanOrderMapper;

    @Autowired
    private UnpackingOrderMapper unpackingOrderMapper;

    @Autowired
    private StockOrderMapper stockOrderMapper;

    @Autowired
    private HdItemMapper hdItemMapper;

    @Autowired
    private UserPackageMapper userPackageMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * @param orderIds 虾皮订单号数组集合
     * @return Set集合，存储虾皮订单号，返回的集合中都是已经存在的OrderId
     */
    public Set<String> orderExist(String[] orderIds) {
        List<MiandanOrder> miandanList = miandanOrderMapper.selectByShopeeOrderNos(orderIds);
        List<UnpackingOrder> unpackingList = unpackingOrderMapper.selectByShopeeOrderNos(orderIds);
        List<StockOrder> stockList = stockOrderMapper.selectByShopeeOrderNos(orderIds);
        Set<String> resultSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(miandanList)) {
            for (MiandanOrder miandanOrder : miandanList) {
                resultSet.add(miandanOrder.getShopeeOrderNo());
            }
        }
        if (!CollectionUtils.isEmpty(unpackingList)) {
            for (UnpackingOrder unpackingOrder : unpackingList) {
                resultSet.add(unpackingOrder.getShopeeOrderNo());
            }
        }
        if (!CollectionUtils.isEmpty(stockList)) {
            for (StockOrder stockOrder : stockList) {
                resultSet.add(stockOrder.getShopeeOrderNo());
            }
        }
        return resultSet;
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean createOrder(String[] ids, int orderType, int photograph) {
        if (null == ids || ids.length < 1) {
            throw new BusinessException("请选择订单！");
        }
        if (orderType == 10) {
            //纯贴面单
            createMiandanOrder(ids);
        } else if (orderType == 20) {
            //拆包订单
            createUnpackageOrder(ids, photograph, 20);
        } else {
            throw new BusinessException("订单类型不正确！");
        }
        return true;
    }


    /**
     * weightFlag 10-称重,20-不称重,前端默认不称重
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean createOrder(String[] ids, int orderType, int photograph, int weightFlag) {
        if (null == ids || ids.length < 1) {
            throw new BusinessException("请选择订单！");
        }
        if (orderType == 10) {
            //纯贴面单
            createMiandanOrder(ids);
        } else if (orderType == 20) {
            //拆包订单
            createUnpackageOrder(ids, photograph, weightFlag);
        } else {
            throw new BusinessException("订单类型不正确！");
        }
        return true;
    }

    private void createUnpackageOrder(String[] ids, int photograph, int weightFlag) {
        HdOrderShip hdOrderShip;
        UnpackingOrder unpackingOrder;
        for (int i = 0; i < ids.length; i++) {
            //查询待处理订单
            hdOrderShip = hdOrderShipService.selectHdOrderShipById(ids[i]);
            unpackingOrder = new UnpackingOrder();
            unpackingOrder.setUserId(ShiroUtils.getUserId());
            unpackingOrder.setShopeeOrderNo(hdOrderShip.getOrderId());
            unpackingOrder.setAuthNumber(String.valueOf(hdOrderShip.getShopId()));
            unpackingOrder.setCreateTime(new Date());
            unpackingOrder.setPackageStatus(10);
            unpackingOrder.setPhotograph(photograph);
            unpackingOrder.setAmount(hdOrderShip.getTotalAmount());
            unpackingOrder.setCountry(hdOrderShip.getCountry());
            unpackingOrder.setLastedSendTime(hdOrderShip.getLastDeliverTime());
            unpackingOrder.setShippingCarrier(hdOrderShip.getShippingCarrier());
            unpackingOrder.setWeightFlag(weightFlag);
            JSONArray jsonArray = JSONArray.parseArray(hdOrderShip.getItems());
            UserPackage userPackage;
            for (int j = 0; j < jsonArray.size(); j++) {
                userPackage = new UserPackage();
                JSONObject itemJson = jsonArray.getJSONObject(j);
                Long itemId = itemJson.getLong("item_id");
                //查询item表
                HdItem hdItem = hdItemMapper.selectHdItemById(itemId);
                userPackage.setUserId(hdItem.getUserId());
                userPackage.setShopeeOrderNo(ids[i]);
                userPackage.setCategoryName(hdItem.getCategoryName());
                userPackage.setGoodsCount(itemJson.getInteger("variation_quantity_purchased"));
                userPackage.setGoodsName(hdItem.getName());
                userPackage.setVariationName(itemJson.getString("variation_name"));
                userPackage.setItemSku(itemJson.getString("variation_sku"));
                userPackage.setPrice(String.valueOf(hdItem.getPrice()));
                String imageUrl = JSONArray.parseArray(hdItem.getImages()).getString(0);
                userPackage.setGoodsImage(imageUrl);
                if (j == 0) {
                    unpackingOrder.setGoodsImage(imageUrl);
                }
                userPackage.setPhotograph(unpackingOrder.getPhotograph());
                userPackage.setStatus(10);
                userPackageMapper.insertUserPackage(userPackage);
            }
            //入库
            unpackingOrderMapper.insertUnpackingOrder(unpackingOrder);
            //删除待处理表中的订单
            hdOrderShipService.deleteHdOrderShipById(ids[i]);
        }
        //扣款
        long fee = PACKAGE_FEE * ids.length;
        User user = userMapper.selectUserById(ShiroUtils.getUserId());
        //预扣费处理
        long avaBalance = user.getMoney() - user.getPreMoney();
        if (avaBalance < fee) {
            throw new BusinessException("减掉预扣费金额,实际可用余额不足,请先充值");
        }
        user.setPreMoney(user.getPreMoney() + fee);
        //更新user表
        userMapper.updateUser(user);
        ShiroUtils.setSysUser(user);
    }

    private void createMiandanOrder(String[] ids) {
        HdOrderShip hdOrderShip;
        MiandanOrder miandanOrder;
        for (int i = 0; i < ids.length; i++) {
            //查询待处理订单
            hdOrderShip = hdOrderShipService.selectHdOrderShipById(ids[i]);
            miandanOrder = new MiandanOrder();
            miandanOrder.setUserId(ShiroUtils.getUserId());
            miandanOrder.setShopeeOrderNo(hdOrderShip.getOrderId());
            miandanOrder.setAuthNumber(String.valueOf(hdOrderShip.getShopId()));
            miandanOrder.setCreateTime(new Date());
            miandanOrder.setStatus(10);
            miandanOrder.setItems(hdOrderShip.getItems());
            miandanOrder.setAmount(hdOrderShip.getTotalAmount());
            miandanOrder.setCountry(hdOrderShip.getCountry());
            miandanOrder.setLastedSendTime(hdOrderShip.getLastDeliverTime());
            miandanOrder.setShippingCarrier(hdOrderShip.getShippingCarrier());
            JSONArray jsonArray = JSONArray.parseArray(hdOrderShip.getItems());
            JSONObject itemJson = jsonArray.getJSONObject(0);
            Long itemId = itemJson.getLong("item_id");
            //查询item表
            HdItem hdItem = hdItemMapper.selectHdItemById(itemId);
            JSONArray imgJsonArray = JSONArray.parseArray(hdItem.getImages());
            miandanOrder.setGoodImage(imgJsonArray.getString(0));
            //入库
            miandanOrderMapper.insertMiandanOrder(miandanOrder);
            //删除待处理表中的订单
            hdOrderShipService.deleteHdOrderShipById(ids[i]);
        }
        //扣款
        long fee = MIANDAN_FEE * ids.length;
        User user = userMapper.selectUserById(ShiroUtils.getUserId());
        //预扣费处理
        long avaBalance = user.getMoney() - user.getPreMoney();
        if (avaBalance < fee) {
            throw new BusinessException("可用余额不足,请先充值");
        }
        user.setPreMoney(user.getPreMoney() + fee);
        //更新user表
        userMapper.updateUser(user);
        ShiroUtils.setSysUser(user);
    }

}
