package com.ruoyi.project.system.aboutme.controller;

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
import com.ruoyi.project.system.aboutme.domain.HdAboutme;
import com.ruoyi.project.system.aboutme.service.IHdAboutmeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 关于我们Controller
 * 
 * @author ruoyi
 * @date 2019-11-26
 */
@Controller
@RequestMapping("/system/aboutme")
public class HdAboutmeController extends BaseController
{
    private String prefix = "system/aboutme";

    @Autowired
    private IHdAboutmeService hdAboutmeService;

    @RequiresPermissions("system:aboutme:view")
    @GetMapping()
    public String aboutme()
    {
        return prefix + "/aboutme";
    }

    /**
     * 查询关于我们列表
     */
    @RequiresPermissions("system:aboutme:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HdAboutme hdAboutme)
    {
        startPage();
        List<HdAboutme> list = hdAboutmeService.selectHdAboutmeList(hdAboutme);
        return getDataTable(list);
    }
    @PostMapping("/info")
    @ResponseBody
    public AjaxResult info()
    {
        AjaxResult ajaxResult=new AjaxResult();
        HdAboutme list = hdAboutmeService.selectHdAboutmeById((long) 1);
        ajaxResult.put("about",list);
        ajaxResult.put("code",0);
        return ajaxResult;
    }
    /**
     * 导出关于我们列表
     */
    @RequiresPermissions("system:aboutme:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdAboutme hdAboutme)
    {
        List<HdAboutme> list = hdAboutmeService.selectHdAboutmeList(hdAboutme);
        ExcelUtil<HdAboutme> util = new ExcelUtil<HdAboutme>(HdAboutme.class);
        return util.exportExcel(list, "aboutme");
    }

    /**
     * 新增关于我们
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存关于我们
     */
    @RequiresPermissions("system:aboutme:add")
    @Log(title = "关于我们", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdAboutme hdAboutme)
    {
        return toAjax(hdAboutmeService.insertHdAboutme(hdAboutme));
    }

    /**
     * 修改关于我们
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HdAboutme hdAboutme = hdAboutmeService.selectHdAboutmeById(id);
        mmap.put("hdAboutme", hdAboutme);
        return prefix + "/edit";
    }

    /**
     * 修改保存关于我们
     */
    @RequiresPermissions("system:aboutme:edit")
    @Log(title = "关于我们", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdAboutme hdAboutme)
    {
        return toAjax(hdAboutmeService.updateHdAboutme(hdAboutme));
    }

    /**
     * 删除关于我们
     */
    @RequiresPermissions("system:aboutme:remove")
    @Log(title = "关于我们", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hdAboutmeService.deleteHdAboutmeByIds(ids));
    }
}
