package com.ruoyi.project.system.dengji.controller;

import java.util.List;
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
import com.ruoyi.project.system.dengji.domain.HdDengji;
import com.ruoyi.project.system.dengji.service.IHdDengjiService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 等级Controller
 * 
 * @author ruoyi
 * @date 2019-11-26
 */
@Controller
@RequestMapping("/system/dengji")
public class HdDengjiController extends BaseController
{
    private String prefix = "system/dengji";

    @Autowired
    private IHdDengjiService hdDengjiService;

    @RequiresPermissions("system:dengji:view")
    @GetMapping()
    public String dengji()
    {
        return prefix + "/dengji";
    }

    /**
     * 查询等级列表
     */
    @RequiresPermissions("system:dengji:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HdDengji hdDengji)
    {
        startPage();
        List<HdDengji> list = hdDengjiService.selectHdDengjiList(hdDengji);
        return getDataTable(list);
    }

    /**
     * 导出等级列表
     */
    @RequiresPermissions("system:dengji:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdDengji hdDengji)
    {
        List<HdDengji> list = hdDengjiService.selectHdDengjiList(hdDengji);
        ExcelUtil<HdDengji> util = new ExcelUtil<HdDengji>(HdDengji.class);
        return util.exportExcel(list, "dengji");
    }

    /**
     * 新增等级
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存等级
     */
    @RequiresPermissions("system:dengji:add")
    @Log(title = "等级", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdDengji hdDengji)
    {
        return toAjax(hdDengjiService.insertHdDengji(hdDengji));
    }

    /**
     * 修改等级
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HdDengji hdDengji = hdDengjiService.selectHdDengjiById(id);
        mmap.put("hdDengji", hdDengji);
        return prefix + "/edit";
    }

    /**
     * 修改保存等级
     */
    @RequiresPermissions("system:dengji:edit")
    @Log(title = "等级", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdDengji hdDengji)
    {
        return toAjax(hdDengjiService.updateHdDengji(hdDengji));
    }

    /**
     * 删除等级
     */
    @RequiresPermissions("system:dengji:remove")
    @Log(title = "等级", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hdDengjiService.deleteHdDengjiByIds(ids));
    }
}
