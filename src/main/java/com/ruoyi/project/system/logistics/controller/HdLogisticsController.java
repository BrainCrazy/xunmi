package com.ruoyi.project.system.logistics.controller;

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
import com.ruoyi.project.system.logistics.domain.HdLogistics;
import com.ruoyi.project.system.logistics.service.IHdLogisticsService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 物流公司Controller
 * 
 * @author ruoyi
 * @date 2019-10-22
 */
@Controller
@RequestMapping("/system/logistics")
public class HdLogisticsController extends BaseController
{
    private String prefix = "system/logistics";

    @Autowired
    private IHdLogisticsService hdLogisticsService;

    @RequiresPermissions("system:logistics:view")
    @GetMapping()
    public String logistics()
    {
        return prefix + "/logistics";
    }

    /**
     * 查询物流公司列表
     */
    @RequiresPermissions("system:logistics:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HdLogistics hdLogistics)
    {
        startPage();
        List<HdLogistics> list = hdLogisticsService.selectHdLogisticsList(hdLogistics);
        return getDataTable(list);
    }

    /**
     * 导出物流公司列表
     */
    @RequiresPermissions("system:logistics:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdLogistics hdLogistics)
    {
        List<HdLogistics> list = hdLogisticsService.selectHdLogisticsList(hdLogistics);
        ExcelUtil<HdLogistics> util = new ExcelUtil<HdLogistics>(HdLogistics.class);
        return util.exportExcel(list, "logistics");
    }

    /**
     * 新增物流公司
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存物流公司
     */
    @RequiresPermissions("system:logistics:add")
    @Log(title = "物流公司", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdLogistics hdLogistics)
    {
        return toAjax(hdLogisticsService.insertHdLogistics(hdLogistics));
    }

    /**
     * 修改物流公司
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HdLogistics hdLogistics = hdLogisticsService.selectHdLogisticsById(id);
        mmap.put("hdLogistics", hdLogistics);
        return prefix + "/edit";
    }

    /**
     * 修改保存物流公司
     */
    @RequiresPermissions("system:logistics:edit")
    @Log(title = "物流公司", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdLogistics hdLogistics)
    {
        return toAjax(hdLogisticsService.updateHdLogistics(hdLogistics));
    }

    /**
     * 删除物流公司
     */
    @RequiresPermissions("system:logistics:remove")
    @Log(title = "物流公司", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hdLogisticsService.deleteHdLogisticsByIds(ids));
    }
}
