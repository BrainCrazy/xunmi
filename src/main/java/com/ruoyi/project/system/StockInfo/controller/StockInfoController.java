package com.ruoyi.project.system.StockInfo.controller;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.StockInfo.domain.StockInfo;
import com.ruoyi.project.system.StockInfo.service.IStockInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 库存管理Controller
 *
 * @author wqf
 * @date 2020-02-18
 */
@Controller
@RequestMapping("/system/StockInfo")
public class StockInfoController extends BaseController {
    private String prefix = "system/StockInfo";

    @Autowired
    private IStockInfoService stockInfoService;

    @RequiresPermissions("system:StockInfo:view")
    @GetMapping()
    public String StockInfo() {
        return prefix + "/StockInfo";
    }

    /**
     * 查询库存管理列表
     */
    @RequiresPermissions("system:StockInfo:list")
    @PostMapping("/list")
    @DataScope(deptAlias = "d", userAlias = "u")
    @ResponseBody
    public TableDataInfo list(StockInfo stockInfo) {
        startPage();
        List<StockInfo> list = stockInfoService.selectStockInfoList(stockInfo);
        return getDataTable(list);
    }

    /**
     * 导出库存管理列表
     */
    @RequiresPermissions("system:StockInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StockInfo stockInfo) {
        List<StockInfo> list = stockInfoService.selectStockInfoList(stockInfo);
        ExcelUtil<StockInfo> util = new ExcelUtil<StockInfo>(StockInfo.class);
        return util.exportExcel(list, "StockInfo");
    }

    /**
     * 新增库存管理
     */
    @GetMapping("/addStock/{id}")
    public String add(@PathVariable("id") Long id, ModelMap mmap) {
        StockInfo stockInfo = stockInfoService.selectStockInfoById(id);
        mmap.put("stockInfo", stockInfo);
        return prefix + "/addStock";
    }

    /**
     * 新增库存管理
     */
    @RequestMapping("/saveAddStock")
    @ResponseBody
    public AjaxResult saveAddStock(long id, long pendigStorageCount, String packageNo) {
        return toAjax(stockInfoService.saveAddStock(id, pendigStorageCount, packageNo));
    }

    /**
     * 新增库存管理
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存库存管理
     */
    @RequiresPermissions("system:StockInfo:add")
    @Log(title = "库存管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MultipartFile goodsImage, StockInfo stockInfo, String packageNo) {
        try {
            return toAjax(stockInfoService.insertStockInfo(stockInfo, packageNo, goodsImage));
        } catch (Exception e) {
            return AjaxResult.error("保存失败，请联系管理员");
        }

    }

    /**
     * 修改库存管理
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StockInfo stockInfo = stockInfoService.selectStockInfoById(id);
        mmap.put("stockInfo", stockInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存库存管理
     */
    @RequiresPermissions("system:StockInfo:edit")
    @Log(title = "库存管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StockInfo stockInfo) {
        return toAjax(stockInfoService.updateStockInfo(stockInfo));
    }

    /**
     * 删除库存管理
     */
    @Log(title = "库存管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(stockInfoService.deleteStockInfoByIds(ids));
    }
}
