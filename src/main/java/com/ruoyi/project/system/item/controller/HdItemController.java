package com.ruoyi.project.system.item.controller;

import java.util.List;

import com.ruoyi.common.utils.security.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.item.service.IHdItemService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 商品明细Controller
 *
 * @author ruoyi
 * @date 2020-02-15
 */
@Controller
@RequestMapping("/system/item")
public class HdItemController extends BaseController {
    private String prefix = "system/item";

    @Autowired
    private IHdItemService hdItemService;

    @RequiresPermissions("system:item:view")
    @GetMapping()
    public String item() {
        return prefix + "/item";
    }

    /**
     * 查询商品明细列表
     */
    @RequiresPermissions("system:item:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HdItem hdItem) {
        startPage();
        List<HdItem> list = hdItemService.selectHdItemList(hdItem);
        return getDataTable(list);
    }

    /**
     * 导出商品明细列表
     */
    @RequiresPermissions("system:item:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdItem hdItem) {
        List<HdItem> list = hdItemService.selectHdItemList(hdItem);
        ExcelUtil<HdItem> util = new ExcelUtil<HdItem>(HdItem.class);
        return util.exportExcel(list, "item");
    }

    /**
     * 新增商品明细
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存商品明细
     */
    @RequiresPermissions("system:item:add")
    @Log(title = "商品明细", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdItem hdItem) {
        return toAjax(hdItemService.insertHdItem(hdItem));
    }

    /**
     * 修改商品明细
     */
    @GetMapping("/edit/{itemId}")
    public String edit(@PathVariable("itemId") Long itemId, ModelMap mmap) {
        HdItem hdItem = hdItemService.selectHdItemById(itemId);
        mmap.put("hdItem", hdItem);
        return prefix + "/edit";
    }

    /**
     * 修改保存商品明细
     */
    @RequiresPermissions("system:item:edit")
    @Log(title = "商品明细", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdItem hdItem) {
        return toAjax(hdItemService.updateHdItem(hdItem));
    }

    /**
     * 删除商品明细
     */
    @RequiresPermissions("system:item:remove")
    @Log(title = "商品明细", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hdItemService.deleteHdItemByIds(ids));
    }

    @Log(title = "更新商品", businessType = BusinessType.OTHER)
    @PostMapping("/updateItem")
    @ResponseBody
    public AjaxResult updateItemByItemId(Long itemId) {
        if (itemId == null) {
            return AjaxResult.error("更新商品信息-未获取到商品id");
        }
        boolean b = hdItemService.updateItemByItemId(itemId);
        if (b) {
            return AjaxResult.success("更新商品信息-成功");
        } else {
            return AjaxResult.error("更新商品信息-失败");
        }
    }

}
