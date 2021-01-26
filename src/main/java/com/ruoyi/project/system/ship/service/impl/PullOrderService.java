package com.ruoyi.project.system.ship.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.system.item.service.IHdItemService;
import com.ruoyi.project.system.ship.domain.HdOrderShip;
import com.ruoyi.project.system.ship.service.IHdOrderShipService;
import com.ruoyi.project.system.shop.domain.HdShop;
import com.ruoyi.project.system.shop.mapper.HdShopMapper;
import com.ruoyi.project.system.shop.remote.OrderInterface;
import com.ruoyi.project.system.userOrder.UserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yibo.su
 * @version V1.0
 * @date 2020/5/10 12:56 下午
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class PullOrderService {

    @Autowired
    private HdShopMapper hdShopMapper;

    @Autowired
    private IHdOrderShipService hdOrderShipService;

    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private IHdItemService hdItemService;

    /**
     * 初始订单size
     */
    private static final int INIT_ORDER_SIZE = 50;

    /**
     * 根据用户id拉取订单
     *
     * @param userId       用户id
     * @param beginDateStr 开始时间
     * @param endDateStr   结束时间
     * @return 拉取条数
     */
    public Integer pullOrderByUserId(Long userId, String beginDateStr, String endDateStr) {
        // 查询该用户的shopId
        List<HdShop> shops = hdShopMapper.findByUserId(userId);
        if (shops.isEmpty()) {
            log.error("根据该userId没有获取有效店铺,userId-->{}", userId);
            return null;
        }
        AtomicInteger count = new AtomicInteger();
        shops.forEach(shop -> {
            try {
                Date beginDate = null;
                if (StringUtils.isNotBlank(beginDateStr)) {
                    beginDate = DateUtils.parseDate(beginDateStr, "yyyy-MM-dd");
                }
                Date endDate = null;
                if (StringUtils.isNotBlank(endDateStr)) {
                    endDate = DateUtils.parseDate(endDateStr, "yyyy-MM-dd");
                }
                if (beginDate != null && endDate != null) {
                    if (endDate.getTime() - beginDate.getTime() > 15L * 24L * 3600L * 1000L) {
                        throw new BusinessException("时间间隔不能超过15天");
                    }
                }
                // 是否翻页
                boolean next = true;
                int page = 0;
                while (next) {
                    int start = page * INIT_ORDER_SIZE;
                    String ordersByStatus = OrderInterface.getOrdersByStatus(shop.getShopId(), "READY_TO_SHIP", beginDate, endDate, start, INIT_ORDER_SIZE);
                    JSONObject orders = JSON.parseObject(ordersByStatus);
                    next = orders.getBooleanValue("more");
                    if (next) {
                        page++;
                    }
                    JSONArray orderArray = orders.getJSONArray("orders");
                    if (orderArray == null || orderArray.isEmpty()) {
                        log.info("shopId-->{},该店铺没有需要拉取的订单或者请求失败,rsp-->{}", shop, ordersByStatus);
                        return;
                    }
                    ArrayList<String> orderIds = new ArrayList<>();
                    for (Object o : orderArray) {
                        JSONObject order = (JSONObject) o;
                        String orderId = order.getString("ordersn");
                        orderIds.add(orderId);
                    }
                    if (orderIds.isEmpty()) {
                        log.warn("没有待拉取的订单，shop-->{}", shop);
                        return;
                    }
                    // 获取已存在订单
                    Set<String> orderExist = userOrderService.orderExist(orderIds.toArray(new String[0]));
                    orderIds.removeAll(orderExist);
                    // 拉取订单详情
                    String orderDetails = OrderInterface.getOrderDetails(shop.getShopId(), orderIds.toArray(new String[0]));
                    JSONObject orderDetailsJson = JSON.parseObject(orderDetails);
                    JSONArray orderDetailArray = orderDetailsJson.getJSONArray("orders");
                    if (orderDetailArray == null || orderDetailArray.isEmpty()) {
                        log.error("没有拉取该订单详情,shop-->{},orderIds-->{},rsp-->{}", shop, orderIds, orderDetails);
                        return;
                    }
                    orderDetailArray.parallelStream().forEach(o -> {
                        JSONObject orderDetail = (JSONObject) o;
                        HdOrderShip hdOrderShip = new HdOrderShip();
                        hdOrderShip.setOrderId(orderDetail.getString("ordersn"));
                        hdOrderShip.setBuyerUsername(orderDetail.getString("buyer_username"));
                        hdOrderShip.setCod(orderDetail.getBoolean("cod"));
                        hdOrderShip.setCountry(orderDetail.getString("country"));
                        hdOrderShip.setCurrency(orderDetail.getString("currency"));
                        hdOrderShip.setTotalAmount(orderDetail.getString("total_amount"));
                        hdOrderShip.setGoodsToDeclare(orderDetail.getBoolean("goods_to_declare"));
                        hdOrderShip.setIsSplitUp(orderDetail.getBoolean("is_split_up"));
                        String trackingNo = orderDetail.getString("tracking_no");
                        hdOrderShip.setTrackingNo(trackingNo);
                        String shippingCarrier = orderDetail.getString("shipping_carrier");
                        try {
                            hdOrderShip.setShippingCarrier(StringUtils.isEmpty(shippingCarrier) ? null : URLDecoder.decode(shippingCarrier, "UTF-8"));
                        } catch (Exception e) {
                            log.error("转换url异常");
                            hdOrderShip.setShippingCarrier(shippingCarrier);
                        }
                        hdOrderShip.setItems(orderDetail.getString("items"));
                        JSONArray itemArray = orderDetail.getJSONArray("items");
                        if (itemArray != null && !itemArray.isEmpty()) {
                            JSONObject itemObj = (JSONObject) itemArray.get(0);
                            hdOrderShip.setFirstItemId(itemObj == null ? null : itemObj.getLong("item_id"));
                        }
                        JSONArray items = orderDetail.getJSONArray("items");
                        if (items != null && !items.isEmpty()) {
                            for (Object obj : items) {
                                JSONObject item = (JSONObject) obj;
                                // 异步拉取并判断商品信息
                                hdItemService.asyncInsertOrUpdate(userId, shop.getShopId(), item.getLong("item_id"));
                            }
                        }
                        hdOrderShip.setMessageToSeller(orderDetail.getString("message_to_seller"));
                        Long create_time = orderDetail.getLong("create_time");
                        hdOrderShip.setOrderCreateTime(create_time == null ? null : new Date(create_time * 1000L));
                        Long update_time = orderDetail.getLong("update_time");
                        hdOrderShip.setOrderUpdateTime(update_time == null ? null : new Date(update_time * 1000L));
                        hdOrderShip.setPaymentMethod(orderDetail.getString("payment_method"));
                        Long pay_time = orderDetail.getLong("pay_time");
                        hdOrderShip.setPayTime(pay_time == null ? null : new Date(pay_time * 1000L));
                        Long ship_by_date = orderDetail.getLong("ship_by_date");
                        hdOrderShip.setLastDeliverTime(ship_by_date == null ? null : new Date(ship_by_date * 1000L));
                        hdOrderShip.setShopId(shop.getShopId());
                        hdOrderShip.setUserId(shop.getUserId());
                        int i = hdOrderShipService.insertOrderNewTransactional(hdOrderShip);
                        if (1 != i) {
                            log.error("插入失败,hdOrderShip-->{}", hdOrderShip);
                            return;
                        }
                        count.incrementAndGet();
                    });
                }
            } catch (Exception e) {
                log.error("拉取该店铺订单失败,shop-->{},e-->{}", shop, e);
            }
        });
        return count.get();
    }


}
