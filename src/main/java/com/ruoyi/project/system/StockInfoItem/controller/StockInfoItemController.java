package com.ruoyi.project.system.StockInfoItem.controller;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import com.ruoyi.project.system.StockInfoItem.service.IStockInfoItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存明细Controller
 *
 * @author wqf
 * @date 2020-02-18
 */
@Controller
@RequestMapping("/system/StockInfoItem")
public class StockInfoItemController extends BaseController {
    private String prefix = "system/StockInfoItem";

    @Autowired
    private IStockInfoItemService stockInfoItemService;


    /**
     * 查询库存明细列表
     */
    @PostMapping("/itemList/{shopeeOrderNo}")
    @ResponseBody
    public TableDataInfo itemList(@PathVariable("shopeeOrderNo") String shopeeOrderNo) {
        startPage();
        StockInfoItem item = new StockInfoItem();
        item.setShopeeOrderNo(shopeeOrderNo);
        List<StockInfoItem> list = stockInfoItemService.selectStockInfoItemByShopeeOrderNo(shopeeOrderNo);
        if (null == list) {
            list = new ArrayList<>();
        }
        return getDataTable(list);
    }

    @RequiresPermissions("system:StockInfoItem:view")
    @GetMapping()
    public String StockInfoItem() {
        return prefix + "/StockInfoItem";
    }

    /**
     * 修改库存明细
     */
    @RequestMapping("/confirmStock/{id}")
    @RequiresPermissions("system:StockInfoItem:confirmStock")
    @ResponseBody
    public AjaxResult confirmStock(@PathVariable("id") String  id) {
        return toAjax(stockInfoItemService.confirmStock4Page(id));
    }

    /**
     * 查询库存明细列表
     */
    @RequiresPermissions("system:StockInfoItem:list")
    @PostMapping("/list")
    @DataScope(deptAlias = "d", userAlias = "u")
    @ResponseBody
    public TableDataInfo list(StockInfoItem stockInfoItem) {
        startPage();
        List<StockInfoItem> list = stockInfoItemService.selectStockInfoItemList(stockInfoItem);
        return getDataTable(list);
    }

    /**
     * 导出库存明细列表
     */
    @RequiresPermissions("system:StockInfoItem:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StockInfoItem stockInfoItem) {
        List<StockInfoItem> list = stockInfoItemService.selectStockInfoItemList(stockInfoItem);
        ExcelUtil<StockInfoItem> util = new ExcelUtil<StockInfoItem>(StockInfoItem.class);
        return util.exportExcel(list, "StockInfoItem");
    }

    /**
     * 新增库存明细
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存库存明细
     */
    @RequiresPermissions("system:StockInfoItem:add")
    @Log(title = "库存明细", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StockInfoItem stockInfoItem) {
        return toAjax(stockInfoItemService.insertStockInfoItem(stockInfoItem));
    }

    /**
     * 修改库存明细
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StockInfoItem stockInfoItem = stockInfoItemService.selectStockInfoItemById(id);
        mmap.put("stockInfoItem", stockInfoItem);
        return prefix + "/edit";
    }

    /**
     * 修改保存库存明细
     */
    @Log(title = "库存明细", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StockInfoItem stockInfoItem) {
        return toAjax(stockInfoItemService.updateStockInfoItem(stockInfoItem));
    }

    /**
     * 删除库存明细
     */
    @RequiresPermissions("system:StockInfoItem:remove")
    @Log(title = "库存明细", businessType = BusinessType.DELETE)
    @RequestMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id") Long id) {
        return toAjax(stockInfoItemService.deleteStockInfoItemById(id));
    }
}
