package com.ruoyi.project.system.ship.controller;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.ship.domain.HdOrderShip;
import com.ruoyi.project.system.ship.service.IHdOrderShipService;
import com.ruoyi.project.system.ship.service.impl.PullOrderService;
import com.ruoyi.project.system.userOrder.UserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 待处理订单Controller
 *
 * @author ruoyi
 * @date 2020-02-15
 */
@Controller
@RequestMapping("/system/ship")
@Slf4j
public class HdOrderShipController extends BaseController {
    private String prefix = "system/ship";

    @Autowired
    private IHdOrderShipService hdOrderShipService;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private PullOrderService pullOrderService;

    @RequiresPermissions("system:ship:view")
    @GetMapping()
    public String ship() {
        return prefix + "/ship";
    }

    /**
     * 查询待处理订单列表
     */
    @RequiresPermissions("system:ship:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HdOrderShip hdOrderShip) {
        startPage();
        List<HdOrderShip> list = hdOrderShipService.selectHdOrderShipList(hdOrderShip);
        return getDataTable(list);
    }

    /**
     * 导出待处理订单列表
     */
    @RequiresPermissions("system:ship:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdOrderShip hdOrderShip) {
        List<HdOrderShip> list = hdOrderShipService.selectHdOrderShipList(hdOrderShip);
        ExcelUtil<HdOrderShip> util = new ExcelUtil<HdOrderShip>(HdOrderShip.class);
        return util.exportExcel(list, "ship");
    }

    /**
     * 新增待处理订单
     */
    @GetMapping("/add")
    public String add(String orderType, String ids) {
        return prefix + "/add?orderType=" + orderType + "&ids=" + ids;
    }

    /**
     * 新增保存待处理订单
     */
    @RequiresPermissions("system:ship:add")
    @Log(title = "待处理订单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdOrderShip hdOrderShip) {
        return toAjax(hdOrderShipService.insertHdOrderShip(hdOrderShip));
    }

    /**
     * 修改待处理订单
     */
    @GetMapping("/edit/{orderId}")
    public String edit(@PathVariable("orderId") String orderId, ModelMap mmap) {
        HdOrderShip hdOrderShip = hdOrderShipService.selectHdOrderShipById(orderId);
        mmap.put("hdOrderShip", hdOrderShip);
        return prefix + "/edit";
    }

    /**
     * 修改保存待处理订单
     */
    @RequiresPermissions("system:ship:edit")
    @Log(title = "待处理订单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdOrderShip hdOrderShip) {
        return toAjax(hdOrderShipService.updateHdOrderShip(hdOrderShip));
    }

    /**
     * 拉取订单
     *
     * @return ~
     */
    @RequiresPermissions("system:ship:pull")
    @Log(title = "拉取订单", businessType = BusinessType.OTHER)
    @PostMapping("/pull")
    @ResponseBody
    public AjaxResult pullOrder(String begin, String end) {
        Long userId = ShiroUtils.getUserId();
        int i = hdOrderShipService.deleteByUserId(userId);
        log.info("删除订单数量,userId-->{},数量-->{}", userId, i);
        Integer count;
        try {
            count = pullOrderService.pullOrderByUserId(userId, begin, end);
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("拉取订单异常,userId-->{},begin-->{},end-->{},e-->", userId, begin, end, e);
            return AjaxResult.error("拉取订单异常");
        }
        if (count == null) {
            return AjaxResult.error("订单拉取失败");
        }
        // 批量获取物流信息
//        List<HdOrderShip> list = hdOrderShipService.selectHdOrderShipByUserIdAndTrackingNo(userId, null);
//        if (!list.isEmpty()) {
//            AtomicInteger j = new AtomicInteger(0);
//            list.parallelStream().forEach(order -> {
//                try {
//                    boolean b = hdOrderShipService.applySingleTrackingNo(order.getShopId(), order.getOrderId());
//                    if (b) {
//                        j.incrementAndGet();
//                    }
//                } catch (Exception e) {
//                    log.error("批量申请物流信息异常,order-->{},e-->", order, e);
//                }
//            });
//            log.info("批量获取物流信息,待获取数量-->{},修改数量-->{}", list.size(), j);
//        }
        return AjaxResult.success("订单拉取成功,数量:" + count);
    }


    /**
     * 删除待处理订单
     */
    @RequiresPermissions("system:ship:remove")
    @Log(title = "待处理订单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hdOrderShipService.deleteHdOrderShipByIds(ids));
    }

    @Log(title = "更新商品OrderId", businessType = BusinessType.OTHER)
    @PostMapping("/updateItem")
    @ResponseBody
    public AjaxResult updateItemByOrderId(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return AjaxResult.error("更新商品信息-未获取到订单id");
        }
        boolean b = hdOrderShipService.updateItemByOrderId(orderId);
        if (b) {
            return AjaxResult.success("更新商品信息-成功");
        } else {
            return AjaxResult.error("更新商品信息-失败");
        }
    }

    @Log(title = "推送订单", businessType = BusinessType.OTHER)
    @PostMapping("/pushOrder")
    @ResponseBody
    public AjaxResult pushOrder(String ids, Integer orderType, Integer photograph, Integer weightFlag) {
        if (StringUtils.isBlank(ids)) {
            return AjaxResult.error("无法获取订单id");
        }
        // 判断订单是否需要获取物流编号
        List<HdOrderShip> list = hdOrderShipService.selectHdOrderShipByIdAndTrackingNo(ids.split(","), null);
        if (!list.isEmpty()) {
            List<String> collect = list.stream().map(HdOrderShip::getOrderId).collect(Collectors.toList());
            return AjaxResult.error("订单id:" + collect.toString() + "没有获取到物流信息,请先获取物流信息");
        }
        // 推送
        boolean b = userOrderService.createOrder(ids.split(","), orderType == null ? 0 : orderType,
                photograph == null ? 10 : photograph,
                weightFlag == null ? 20 : weightFlag);
        if (!b) {
            return AjaxResult.error("推送失败");
        }
        return AjaxResult.success("推送成功");
    }

    @Log(title = "申请物流id", businessType = BusinessType.OTHER)
    @PostMapping("/apply/trackingNo")
    @ResponseBody
    public AjaxResult applyTrackingNo(String orderIds) {
        if (StringUtils.isBlank(orderIds)) {
            return AjaxResult.error("无法获取订单id");
        }
        String[] split = orderIds.split(",");
        StringBuffer errorMsg = new StringBuffer();

        // 判断订单是否需要获取物流编号
        List<HdOrderShip> list = hdOrderShipService.selectHdOrderShipByIdAndTrackingNo(split, null);
        AtomicInteger j = new AtomicInteger(0);
        if (!list.isEmpty()) {
            list.parallelStream().forEach(order -> {
                try {
                    boolean b = hdOrderShipService.applySingleTrackingNo(order.getShopId(), order.getOrderId());
                    if (b) {
                        j.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.error("批量申请物流信息异常,order-->{},e-->", order, e);
                    if (errorMsg.length() != 0) {
                        errorMsg.append(",");
                    }
                    errorMsg.append(order.getOrderId());
                }
            });
        }
        log.info("批量获取物流信息,申请修改数量-->{},待获取数量-->{},修改数量-->{}", split.length, list.size(), j);
        if (errorMsg.length() != 0) {
            return AjaxResult.error("申请物流id失败，订单id-->" + errorMsg.toString());
        }
        return AjaxResult.success("申请物流id全部成功");
    }

    @GetMapping("/pull/html")
    public String getPullModel() {
        return prefix + "/pullParam";
    }
}
