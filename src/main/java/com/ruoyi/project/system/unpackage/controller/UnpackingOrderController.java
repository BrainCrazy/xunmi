package com.ruoyi.project.system.unpackage.controller;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.waybill.SfPrintOrderParam;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import com.ruoyi.project.system.StockInfoItem.service.IStockInfoItemService;
import com.ruoyi.project.system.ship.service.IHdOrderShipService;
import com.ruoyi.project.system.stockOrder.service.IStockOrderService;
import com.ruoyi.project.system.unpackage.domain.UnpackingOrder;
import com.ruoyi.project.system.unpackage.service.IUnpackingOrderService;
import com.ruoyi.project.system.userpackage.domain.UserPackage;
import com.ruoyi.project.system.userpackage.service.IUserPackageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 拆包订单Controller
 *
 * @author wqf
 * @date 2020-02-15
 */
@Controller
@RequestMapping("/system/unpackage")
@Slf4j
public class UnpackingOrderController extends BaseController {
    private String prefix = "system/unpackage";

    @Autowired
    private IUnpackingOrderService unpackingOrderService;

    @Autowired
    private IUserPackageService userPackageService;

    @Autowired
    private IHdOrderShipService orderShipService;

    @Autowired
    private IStockOrderService stockOrderService;

    @Autowired
    private IStockInfoItemService stockInfoItemService;

    @RequestMapping("/userSubmitOrder")
    @ResponseBody
    public AjaxResult userSubmitOrder(String shopeeOrderNo) {
        try {
            unpackingOrderService.userSubmitOrder(shopeeOrderNo);
            return AjaxResult.success();
        } catch (BusinessException be) {
            return AjaxResult.error(be.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/orderDetail")
    public TableDataInfo getOrderDetail(String shopeeOrderNo) {
        List<UserPackage> list = userPackageService.selectByShopeeOrderNoNotNull(shopeeOrderNo);
        List<StockInfoItem> items = stockInfoItemService.selectStockInfoItemByShopeeOrderNo(shopeeOrderNo);
        if (!CollectionUtils.isEmpty(items)) {
            UserPackage userPackage;
            for (StockInfoItem stockInfoItem : items) {
                userPackage = new UserPackage();
                userPackage.setId(stockInfoItem.getId());
                userPackage.setUserId(stockInfoItem.getUserId());
                userPackage.setCategoryName(stockInfoItem.getCategoryName());
                userPackage.setGoodsCount(new Long(stockInfoItem.getGoodsCount()).intValue());
                userPackage.setGoodsName(stockInfoItem.getGoodsName());
                userPackage.setGoodsImage(stockInfoItem.getGoodsImageUrl());
                userPackage.setPackageNo("库存商品");
                list.add(userPackage);
            }
        }
        return getDataTable(list);
    }

    @RequestMapping("/submitOrder/{id}")
    public String submitOrder(@PathVariable("id") String id, ModelMap mmap) {
        List<UserPackage> list = userPackageService.selectByShopeeOrderNoNotNull(id);
        List<StockInfoItem> items = stockInfoItemService.selectStockInfoItemByShopeeOrderNo(id);
        if (!CollectionUtils.isEmpty(items)) {
            UserPackage userPackage;
            for (StockInfoItem stockInfoItem : items) {
                userPackage = new UserPackage();
                userPackage.setId(stockInfoItem.getId());
                userPackage.setUserId(stockInfoItem.getUserId());
                userPackage.setCategoryName(stockInfoItem.getCategoryName());
                userPackage.setGoodsCount(new Long(stockInfoItem.getGoodsCount()).intValue());
                userPackage.setGoodsName(stockInfoItem.getGoodsName());
                userPackage.setGoodsImage(stockInfoItem.getGoodsImageUrl());
                userPackage.setPackageNo("库存商品");
                list.add(userPackage);
            }
        }
        mmap.put("userPackages", list);
        mmap.put("shopeeOrderNo", id);
        return prefix + "/showOrderDetail";
    }

    @RequestMapping("/toAddGoodsPage/{id}")
    public String toAddNewGoodsPage(@PathVariable("id") Long id, ModelMap mmap) {
        UnpackingOrder unpackingOrder = unpackingOrderService.selectUnpackingOrderById(id);
        mmap.put("shopeeOrderNo", unpackingOrder.getShopeeOrderNo());
        mmap.put("photograph", unpackingOrder.getPhotograph());
        return prefix + "/addOtherGoods";
    }

    @RequestMapping("/download")
    @RequiresPermissions("system:unpackage:outStock")
    public ResponseEntity<byte[]> downloadMiandan(long id) throws Exception {
        //查询订单信息
        UnpackingOrder unpackingOrder = unpackingOrderService.selectUnpackingOrderById(id);
        List<SfPrintOrderParam.Item> items = new ArrayList<>();
        List<UserPackage> list = userPackageService.selectByShopeeOrderNo(unpackingOrder.getShopeeOrderNo());
        SfPrintOrderParam.Item item;
        for (UserPackage userPackage : list) {
            item = new SfPrintOrderParam.Item();
            item.setCount(userPackage.getGoodsCount());
            item.setCategoryName(userPackage.getCategoryName());
            items.add(item);
        }
        byte[] file = orderShipService.createLogisticsPDF(Long.parseLong(unpackingOrder.getAuthNumber()), unpackingOrder.getShopeeOrderNo());
        if (file == null || file.length == 0) {
            file = orderShipService.createLogisticsImg(Long.parseLong(unpackingOrder.getAuthNumber()), unpackingOrder.getShopeeOrderNo(), unpackingOrder.getCountry(), items);

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + unpackingOrder.getShopeeOrderNo() + ".pdf");
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(file, headers, statusCode);
        return entity;
    }

    @RequestMapping("/queryCount")
    @ResponseBody
    public AjaxResult queryCount() {
        UnpackingOrder unpackingOrder = new UnpackingOrder();
        unpackingOrder.setUserId(ShiroUtils.getUserId());
        unpackingOrder.setPackageStatus(10);
        List<UnpackingOrder> list = unpackingOrderService.selectUnpackingOrderList(unpackingOrder);
        String res = list.size() + "-";
        unpackingOrder.setPackageStatus(30);
        list = unpackingOrderService.selectUnpackingOrderList(unpackingOrder);
        int tmp = list.size();
        unpackingOrder.setPackageStatus(40);
        list = unpackingOrderService.selectUnpackingOrderList(unpackingOrder);
        tmp += list.size();
        res += tmp + "-";
        unpackingOrder.setPackageStatus(null);
        unpackingOrder.setExceptionFlag(20);
        list = unpackingOrderService.selectUnpackingOrderList(unpackingOrder);
        res += list.size();
        return AjaxResult.success(res);
    }

    @RequiresPermissions("system:unpackage:view")
    @GetMapping()
    public String unpackage() {
        return prefix + "/unpackage";
    }

    /**
     * 用户确认仓库图片
     */
    @RequiresPermissions("system:unpackage:userConfirm")
    @Log(title = "用户确认仓库图片", businessType = BusinessType.UPDATE)
    @PostMapping("/userConfirm")
    @ResponseBody
    public AjaxResult userConfirm(String shopeeOrderNo) {
        return toAjax(unpackingOrderService.updateStatusByShopeeOrderNo(shopeeOrderNo, 50));
    }


    /**
     * 前往确认发货页面
     */
    @RequiresPermissions("system:unpackage:outStock")
    @Log(title = "拆包订单确认已发货", businessType = BusinessType.UPDATE)
    @RequestMapping("/outStock/{id}")
    public String confirmOutStock(@PathVariable("id") Long id, ModelMap mmp) {
        UnpackingOrder unpackingOrder = unpackingOrderService.selectUnpackingOrderById(id);
        mmp.put("shopeeOrderNo", unpackingOrder.getShopeeOrderNo());
        mmp.put("id", unpackingOrder.getId());
        mmp.put("weightFlag", unpackingOrder.getWeightFlag());
        return prefix + "/confirmOutStock";
    }

    /**
     * 确认已发货
     */
    @RequiresPermissions("system:unpackage:outStock")
    @Log(title = "拆包订单确认已发货", businessType = BusinessType.UPDATE)
    @RequestMapping("/saveoutStock")
    @ResponseBody
    public AjaxResult saveoutStock(String shopeeOrderNo, String weight, MultipartFile stockImage) {
        try {
            unpackingOrderService.confirmOutStock(shopeeOrderNo, stockImage, weight);
            return AjaxResult.success();
        } catch (Exception e) {
            log.error("确认发货失败", e);
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 查询拆包订单列表
     */
    @RequiresPermissions("system:unpackage:list")
    @RequestMapping("/list")
    @DataScope(deptAlias = "d", userAlias = "u")
    @ResponseBody
    public TableDataInfo list(UnpackingOrder unpackingOrder) {
        startPage();
        return getDataTable(unpackingOrderService.selectUnpackingOrderList(unpackingOrder));
    }


    /**
     * 新增拆包订单
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存拆包订单
     */
    @RequiresPermissions("system:unpackage:add")
    @Log(title = "拆包订单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(UnpackingOrder unpackingOrder) {
        return toAjax(unpackingOrderService.insertUnpackingOrder(unpackingOrder));
    }

    /**
     * 修改拆包订单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        UnpackingOrder unpackingOrder = unpackingOrderService.selectUnpackingOrderById(id);
        mmap.put("unpackingOrder", unpackingOrder);
        return prefix + "/edit";
    }

    /**
     * 修改保存拆包订单
     */
    @RequiresPermissions("system:unpackage:edit")
    @Log(title = "拆包订单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(UnpackingOrder unpackingOrder) {
        return toAjax(unpackingOrderService.updateUnpackingOrder(unpackingOrder));
    }

    /**
     * 删除拆包订单
     */
    @Log(title = "拆包订单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(unpackingOrderService.deleteUnpackingOrderByIds(ids));
    }
}
