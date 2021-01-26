package com.ruoyi.project.system.ship.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.common.utils.waybill.CreatePdf;
import com.ruoyi.common.utils.waybill.SfPrintOrderParam;
import com.ruoyi.project.system.item.domain.HdCategory;
import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.item.mapper.HdCategoryMapper;
import com.ruoyi.project.system.item.mapper.HdItemMapper;
import com.ruoyi.project.system.item.service.IHdItemService;
import com.ruoyi.project.system.ship.domain.HdOrderShip;
import com.ruoyi.project.system.ship.mapper.HdOrderShipMapper;
import com.ruoyi.project.system.ship.service.IHdOrderShipService;
import com.ruoyi.project.system.shop.remote.OrderInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 待处理订单Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-15
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HdOrderShipServiceImpl implements IHdOrderShipService {
    @Autowired
    private HdOrderShipMapper hdOrderShipMapper;
    @Autowired
    private IHdItemService hdItemService;
    @Autowired
    private HdCategoryMapper categoryMapper;


    /**
     * 查询待处理订单
     *
     * @param orderId 待处理订单ID
     * @return 待处理订单
     */
    @Override
    public HdOrderShip selectHdOrderShipById(String orderId) {
        return hdOrderShipMapper.selectHdOrderShipById(orderId);
    }

    /**
     * 查询待处理订单列表
     *
     * @param hdOrderShip 待处理订单
     * @return 待处理订单
     */
    @Override
    public List<HdOrderShip> selectHdOrderShipList(HdOrderShip hdOrderShip) {
        Long userId = ShiroUtils.getUserId();
        hdOrderShip.setUserId(userId);
        List<HdOrderShip> hdOrderShips = hdOrderShipMapper.selectHdOrderShipList(hdOrderShip);
        if (hdOrderShips != null && !hdOrderShips.isEmpty()) {
            for (HdOrderShip orderShip : hdOrderShips) {
                String items = orderShip.getItems();
                Map<Long, JSONObject> itemIds = new HashMap<>();
                if (StringUtils.isNotEmpty(items)) {
                    JSONArray jsonArray = JSON.parseArray(items);
                    for (Object o : jsonArray) {
                        JSONObject itemObj = (JSONObject) o;
                        itemIds.put(itemObj.getLong("item_id"), itemObj);
                    }
                }
                if (!itemIds.isEmpty()) {
                    List<HdItem> itemList = hdItemService.selectHdItemByIds(new ArrayList<>(itemIds.keySet()));
                    for (HdItem hdItem : itemList) {
                        JSONObject jsonObject = itemIds.get(hdItem.getItemId());
                        hdItem.setVariationSku(jsonObject.getString("variation_sku"));
                        hdItem.setVariationQuantityPurchased(jsonObject.getString("variation_quantity_purchased"));
                        hdItem.setVariationName(jsonObject.getString("variation_name"));
                    }
                    orderShip.setItemList(itemList);
                }
            }
        }
        return hdOrderShips;
    }

    /**
     * 新增待处理订单
     *
     * @param hdOrderShip 待处理订单
     * @return 结果
     */
    @Override
    public int insertHdOrderShip(HdOrderShip hdOrderShip) {
        hdOrderShip.setCreateTime(DateUtils.getNowDate());
        return hdOrderShipMapper.insertHdOrderShip(hdOrderShip);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertOrderNewTransactional(HdOrderShip hdOrderShip) {
        return insertHdOrderShip(hdOrderShip);
    }

    /**
     * 修改待处理订单
     *
     * @param hdOrderShip 待处理订单
     * @return 结果
     */
    @Override
    public int updateHdOrderShip(HdOrderShip hdOrderShip) {
        hdOrderShip.setUpdateTime(DateUtils.getNowDate());
        return hdOrderShipMapper.updateHdOrderShip(hdOrderShip);
    }

    /**
     * 删除待处理订单对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHdOrderShipByIds(String ids) {
        return hdOrderShipMapper.deleteHdOrderShipByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除待处理订单信息
     *
     * @param orderId 待处理订单ID
     * @return 结果
     */
    @Override
    public int deleteHdOrderShipById(String orderId) {
        return hdOrderShipMapper.deleteHdOrderShipById(orderId);
    }

    @Override
    public int deleteByUserId(Long userId) {
        return hdOrderShipMapper.deleteByUserId(userId);
    }


    /**
     * 生成面单
     *
     * @param shopId  店铺id
     * @param orderId 订单id
     * @return pdf文件
     * @throws Exception ~
     */
    @Override
    public byte[] createLogisticsPDF(Long shopId, String orderId) throws Exception {
//        if (StringUtils.isBlank(trackingNo)) {
//            log.error("当前订单没有获取物流id;shopId-->{},orderId-->{},trackingNo-->{}", shopId, orderId, trackingNo);
//            return null;
//        }
        try {
            String airwayBill = OrderInterface.getAirwayBill(shopId, orderId);
            JSONObject json = JSON.parseObject(airwayBill);
            JSONObject result = json.getJSONObject("result");
            JSONArray airwayBills = result.getJSONArray("airway_bills");
            if (airwayBills == null || airwayBills.isEmpty()) {
                log.warn("没有获取到pdf下载地址,shopId-->{},orderId-->{},响应-->{}", shopId, orderId, airwayBill);
                return null;
            }
            JSONObject bill = airwayBills.getJSONObject(0);
            String airwayBillUrl = bill.getString("airway_bill");
            URL url = new URL(airwayBillUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream is = conn.getInputStream();
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            log.error("拉取pdf异常;shopId-->{},orderId-->{},异常-->", shopId, orderId, e);
            return null;
        }
    }

    /**
     * 生成面单
     *
     * @param shopId  店铺id
     * @param orderId 订单id
     * @param country 国家
     * @param items   商品信息
     * @return 面单byte[]
     */
    @Override
    @Deprecated
    public byte[] createLogisticsImg(Long shopId, String orderId, String country, List<SfPrintOrderParam.Item> items) throws Exception {
        String orderLogistics;
        try {
            orderLogistics = OrderInterface.getOrderLogistics(shopId, orderId);
        } catch (Exception e) {
            log.error("请求接口异常-拉取物流信息,shopId-->{},orderId-->{},e-->", shopId, orderId, e);
            throw new BusinessException("拉取物流信息异常");
        }
        JSONObject json = JSON.parseObject(orderLogistics);
        JSONObject logistics = json.getJSONObject("logistics");
        if (logistics == null || logistics.isEmpty()) {
            log.error("无法获取订单物流信息,shopId-->{},orderId-->{},响应-->{}", shopId, orderId, orderLogistics);
            throw new BusinessException("无法获取订单物流信息");
        }
        SfPrintOrderParam param = new SfPrintOrderParam();
        param.setOrderId(orderId);
        param.setTrackingNo(logistics.getString("tracking_no"));
        param.setFirstMileName(logistics.getString("first_mile_name"));
        param.setLastMileName(logistics.getString("last_mile_name"));
        param.setGoodsToDeclare(logistics.getBoolean("goods_to_declare"));
        param.setLaneCode(logistics.getString("lane_code"));
        JSONObject recipientAddress = logistics.getJSONObject("recipient_address");
        param.setBuyerUsername(recipientAddress.getString("name"));
        param.setFullAddress(recipientAddress.getString("full_address"));
        param.setPhone(recipientAddress.getString("phone"));
        param.setCountry(country);
//        param.setWarehouseAddress(logistics.getString("warehouse_address"));
        param.setServiceCode(logistics.getString("service_code"));
        param.setItems(items);
        for (SfPrintOrderParam.Item item : items) {
            if (StringUtils.isBlank(item.getCategoryName())) {
                if (item.getCategoryId() != null) {
                    HdCategory hdCategory = categoryMapper.selectByPrimaryKey(item.getCategoryId());
                    item.setCategoryName(hdCategory != null ? hdCategory.getCategoryName() : null);
                }
            }
        }
        if (!param.checkParam()) {
            log.error("参数缺失,param-->{}", param);
            throw new BusinessException("参数校验未通过,参数缺失");
        }
        return CreatePdf.createPdf(param);
    }

    @Override
    public boolean updateItemByOrderId(String orderId) {
        HdOrderShip hdOrderShip = hdOrderShipMapper.selectHdOrderShipById(orderId);
        if (hdOrderShip == null) {
            return false;
        }
        String items = hdOrderShip.getItems();
        JSONArray array = JSON.parseArray(items);
        if (StringUtils.isBlank(items) || array == null || array.isEmpty()) {
            log.error("错误的订单信息,无法获取订单商品信息,orderId-->{},items-->{}", orderId, items);
            return false;
        }
        for (Object o : array) {
            JSONObject item = (JSONObject) o;
            Long itemId = item.getLong("item_id");
            if (itemId == null) {
                log.error("错误的订单信息,无法获取订单商品信息Id,orderId-->{},items-->{}", orderId, items);
            }
            return hdItemService.syncInsertOrUpdate(hdOrderShip.getUserId(), hdOrderShip.getShopId(), itemId);
        }
        return false;
    }

    /**
     * 申请物流编号
     *
     * @param orderId 订单id
     * @throws Exception ~
     */
//    @Override
//    @Deprecated
//    public void applyTrackingNo(String orderId) throws Exception {
//        HdOrderShip orderShip = hdOrderShipMapper.selectHdOrderShipById(orderId);
//        if (orderShip == null) {
//            throw new BusinessException("没有该订单信息");
//        }
//        String trackingNoRsp = OrderInterface.getTrackingNo(orderShip.getShopId(), orderId);
//        JSONObject json = JSON.parseObject(trackingNoRsp);
//        JSONArray errors = json.getJSONArray("error");
//        if (errors != null && !errors.isEmpty()) {
//            log.error("申请物流编号失败,rsp-->{}", trackingNoRsp);
//            throw new BusinessException("申请物流编号失败");
//        }
//        JSONArray orders = json.getJSONObject("result").getJSONArray("orders");
//        for (Object order : orders) {
//            JSONObject orderJson = (JSONObject) order;
//            if (orderId.equals(orderJson.getString("ordersn"))) {
//                orderShip.setTrackingNo(orderJson.getString("tracking_no"));
//                orderShip.setShippingCarrier(orderJson.getString("shipping_carrier"));
//                hdOrderShipMapper.updateHdOrderShip(orderShip);
//                return;
//            }
//        }
//    }

    /**
     * 申请物流编号
     *
     * @param orderId 订单id
     * @throws Exception ~
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyTrackingNo(String orderId) throws Exception {
        HdOrderShip orderShip = hdOrderShipMapper.selectHdOrderShipById(orderId);
        if (orderShip == null) {
            throw new BusinessException("没有该订单信息");
        }
        applySingleTrackingNo(orderShip.getShopId(), orderId);
    }

    @Override
    public List<HdOrderShip> selectHdOrderShipByIdAndTrackingNo(String[] ids, String trackingNo) {
        return hdOrderShipMapper.selectHdOrderShipByIdAndTrackingNo(Arrays.asList(ids), trackingNo);
    }

    @Override
    public List<HdOrderShip> selectHdOrderShipByUserIdAndTrackingNo(Long userId, String trackingNo) {
        return hdOrderShipMapper.selectHdOrderShipByUserIdAndTrackingNo(userId, trackingNo);
    }

//    @Override
//    @Deprecated
//    public int batchApplyTrackingNo(List<HdOrderShip> list) {
//        if (list == null || list.isEmpty()) {
//            return 0;
//        }
//        Map<Long, List<HdOrderShip>> map = list.stream().collect(Collectors.groupingBy(HdOrderShip::getShopId));
//        List<HdOrderShip> updateOrder = new ArrayList<>();
//        map.forEach((shopId, value) -> {
//            String[] orderIds = value.stream().map(HdOrderShip::getOrderId).toArray(String[]::new);
//            String trackingNoRsp = null;
//            try {
//                trackingNoRsp = OrderInterface.getTrackingNo(shopId, orderIds);
//            } catch (Exception e) {
//                log.error("批量申请物流信息");
//            }
//            JSONObject json = JSON.parseObject(trackingNoRsp);
//            JSONArray errors = json.getJSONArray("error");
//            if (errors != null && !errors.isEmpty()) {
//                log.error("申请物流编号失败,rsp-->{}", trackingNoRsp);
//                throw new BusinessException("申请物流编号失败");
//            }
//            JSONArray orders = json.getJSONObject("result").getJSONArray("orders");
//            for (Object order : orders) {
//                JSONObject orderJson = (JSONObject) order;
//                HdOrderShip orderShip = new HdOrderShip();
//                orderShip.setOrderId(orderJson.getString("ordersn"));
//                orderShip.setTrackingNo(orderJson.getString("tracking_no"));
//                orderShip.setShippingCarrier(orderJson.getString("shipping_carrier"));
//                updateOrder.add(orderShip);
//            }
//        });
//        AtomicInteger count = new AtomicInteger(0);
//        if (!updateOrder.isEmpty()) {
//            updateOrder.forEach(order -> {
//                count.addAndGet(hdOrderShipMapper.updateHdOrderShip(order));
//            });
//        }
//        return count.get();
//    }

    /**
     * 申请物流信息
     *
     * @param shopId  店铺id
     * @param orderId 订单id
     * @return true-成功
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean applySingleTrackingNo(Long shopId, String orderId) throws Exception {
        String logisticInfo = OrderInterface.getLogisticInfo(shopId, orderId);
        JSONObject logisticInfoObj = JSON.parseObject(logisticInfo);
        if (logisticInfoObj.containsKey("error")) {
            log.error("获取店铺订单的物流配置信息,shopId-->{},orderId-->{},响应-->{}", shopId, orderId, logisticInfo);
            return false;
        }
        String initLogistics = OrderInterface.initLogistics(shopId, orderId, logisticInfoObj.getJSONObject("pickup"), logisticInfoObj.getJSONObject("dropoff"), logisticInfoObj.getJSONObject("info_needed"));
        JSONObject initLogisticsObj = JSON.parseObject(initLogistics);
        if (initLogisticsObj.containsKey("error")) {
            log.error("申请物流信息init失败,shopId-->{},orderId-->{},请求参数-->{},响应-->{}", shopId, orderId, logisticInfo, initLogistics);
            return false;
        }
        // 获取物流信息
        String orderLogistics = OrderInterface.getOrderLogistics(shopId, orderId);
        JSONObject orderLogisticsObj = JSON.parseObject(orderLogistics);
        if (orderLogisticsObj.containsKey("error")) {
            log.error("从物流信息里获取物流id失败,shopId-->{},orderId-->{},响应-->{}", shopId, orderId, orderLogistics);
            return false;
        }
        HdOrderShip orderShip = new HdOrderShip();
        orderShip.setOrderId(orderId);
        JSONObject logistics = orderLogisticsObj.getJSONObject("logistics");
        orderShip.setTrackingNo(logistics.getString("tracking_no"));
        orderShip.setShippingCarrier(logistics.getString("shipping_carrier"));
        int i = hdOrderShipMapper.updateHdOrderShip(orderShip);
        return 1 == i;
    }
}
