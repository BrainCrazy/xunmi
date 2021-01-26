package com.ruoyi.project.system.stockOrder.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.waybill.SfPrintOrderParam;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.StockInfo.domain.StockInfo;
import com.ruoyi.project.system.StockInfo.service.IStockInfoService;
import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.item.mapper.HdItemMapper;
import com.ruoyi.project.system.ship.service.IHdOrderShipService;
import com.ruoyi.project.system.stockOrder.domain.StockOrder;
import com.ruoyi.project.system.stockOrder.service.IStockOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存订单Controller
 *
 * @author wqf
 * @date 2020-02-19
 */
@Controller
@RequestMapping("/system/stockOrder")
public class StockOrderController extends BaseController {
    private String prefix = "system/stockOrder";

    @Autowired
    private IStockOrderService stockOrderService;

    @Autowired
    private IStockInfoService stockInfoService;

    @Autowired
    private HdItemMapper hdItemMapper;

    @Autowired
    private IHdOrderShipService orderShipService;

    @RequestMapping("/orderDetail")
    @ResponseBody
    public TableDataInfo orderDetail(Long id) {
        List<HdItem> list = stockOrderService.getorderDetail(id);
        return getDataTable(list);
    }

    @RequestMapping("/download")
    @RequiresPermissions("system:stockOrder:outStock")
    public ResponseEntity<byte[]> downloadMiandan(long id) throws Exception {
        //查询订单信息
        StockOrder stockOrder = stockOrderService.selectStockOrderById(id);
        List<SfPrintOrderParam.Item> items = new ArrayList<>();
        String itemJson = stockOrder.getItems();
        JSONArray jsonArray = JSONArray.parseArray(itemJson);
        JSONObject json;
        SfPrintOrderParam.Item item;
        //解析json串
        for (int i = 0; i < jsonArray.size(); i++) {
            json = jsonArray.getJSONObject(i);
            item = new SfPrintOrderParam.Item();
            Long itemId = json.getLong("item_id");
            //查询item表
            HdItem hdItem = hdItemMapper.selectHdItemById(itemId);
            item.setCategoryName(hdItem.getCategoryName());
            item.setCount(json.getInteger("variation_quantity_purchased"));
            items.add(item);
        }
        byte[] file = orderShipService.createLogisticsPDF(Long.parseLong(stockOrder.getAuthNumber()), stockOrder.getShopeeOrderNo());
        if (file == null || file.length == 0) {
            file = orderShipService.createLogisticsImg(Long.parseLong(stockOrder.getAuthNumber()), stockOrder.getShopeeOrderNo(), stockOrder.getCountry(), items);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + stockOrder.getShopeeOrderNo() + ".pdf");
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(file, headers, statusCode);
        return entity;
    }

    @ResponseBody
    @RequestMapping("/outStock")
    @RequiresPermissions("system:stockOrder:outStock")
    public AjaxResult outStock(long id) {
        return toAjax(stockOrderService.confirmOutStock(id));
    }

    @RequestMapping("/saveConfirmOrder")
    @ResponseBody
    public AjaxResult saveConfirmOrder(String shopeeOrderNo) {
        return toAjax(stockOrderService.updateOrderStatus(shopeeOrderNo, 20));
    }

    /**
     * 跳转确认订单页面
     */
    @GetMapping("/confirmOrder/{id}")
    public String confirmOrder(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("shopeeOrderNo", id);
        return prefix + "/confirmOrder";
    }

    /**
     * 跳转确认发货页面
     */
    @GetMapping("/confirmOutStock/{id}")
    public String confirmOutStock(@PathVariable("id") Long id, ModelMap mmap) {
        StockOrder stockOrder = stockOrderService.selectStockOrderById(id);
        mmap.put("id", stockOrder.getId());
        mmap.put("shopeeOrderNo", stockOrder.getShopeeOrderNo());
        return prefix + "/confirmOutStock";
    }

    @RequiresPermissions("system:stockOrder:view")
    @GetMapping()
    public String stockOrder() {
        return prefix + "/stockOrder";
    }


    @ResponseBody
    @RequestMapping("/saveGoodsCount")
    public AjaxResult saveGoodsCount(Long id, String shopeeOrderNo, Long goodsCount) {
        return toAjax(stockOrderService.saveGoodsCount(id, shopeeOrderNo, goodsCount));
    }

    /**
     * 修改库存管理
     */
    @GetMapping("/editGoodsCount/{id}")
    public String editGoodsCount(@PathVariable("id") String id, ModelMap mmap) {
        String[] arr = id.split("-");
        Long stockInfoId = Long.parseLong(arr[0]);
        String shopeeOrderNo = arr[1];
        StockInfo stockInfo = stockInfoService.selectStockInfoById(stockInfoId);
        mmap.put("stockInfo", stockInfo);
        mmap.put("shopeeOrderNo", shopeeOrderNo);
        return prefix + "/countEdit";
    }

    /**
     * 查询库存订单列表
     */
    @RequiresPermissions("system:stockOrder:list")
    @PostMapping("/list")
    @ResponseBody
    @DataScope(deptAlias = "d", userAlias = "u")
    public TableDataInfo list(StockOrder stockOrder) {
        startPage();
        List<StockOrder> list = stockOrderService.selectStockOrderList(stockOrder);
        return getDataTable(list);
    }

    /**
     * 导出库存订单列表
     */
    @RequiresPermissions("system:stockOrder:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StockOrder stockOrder) {
        List<StockOrder> list = stockOrderService.selectStockOrderList(stockOrder);
        ExcelUtil<StockOrder> util = new ExcelUtil<StockOrder>(StockOrder.class);
        return util.exportExcel(list, "stockOrder");
    }

    /**
     * 新增库存订单
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存库存订单
     */
    @RequiresPermissions("system:stockOrder:add")
    @Log(title = "库存订单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StockOrder stockOrder) {
        return toAjax(stockOrderService.insertStockOrder(stockOrder));
    }

    /**
     * 修改库存订单
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:stockOrder:edit")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        // StockOrder stockOrder = stockOrderService.selectStockOrderById(id);
        mmap.put("shopeeOrderNo", id);
        return prefix + "/StockInfo";
    }

    /**
     * 修改保存库存订单
     */
    @RequiresPermissions("system:stockOrder:edit")
    @Log(title = "库存订单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StockOrder stockOrder) {
        return toAjax(stockOrderService.updateStockOrder(stockOrder));
    }

    /**
     * 删除库存订单
     */
    @RequiresPermissions("system:stockOrder:remove")
    @Log(title = "库存订单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(stockOrderService.deleteStockOrderByIds(ids));
    }
}
