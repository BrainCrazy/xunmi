package com.ruoyi.project.system.type.controller;

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
import com.ruoyi.project.system.type.domain.HdGoodsType;
import com.ruoyi.project.system.type.service.IHdGoodsTypeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 用户商品类型Controller
 * 
 * @author ruoyi
 * @date 2019-10-21
 */
@Controller
@RequestMapping("/system/type")
public class HdGoodsTypeController extends BaseController
{
    private String prefix = "system/type";

    @Autowired
    private IHdGoodsTypeService hdGoodsTypeService;

    @RequiresPermissions("system:type:view")
    @GetMapping()
    public String type()
    {
        return prefix + "/type";
    }

    /**
     * 查询用户商品类型列表
     */
    @RequiresPermissions("system:type:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HdGoodsType hdGoodsType)
    {
        startPage();
        if(ShiroUtils.getSysUser().getDeptId()==110){
            hdGoodsType.setUserId(ShiroUtils.getUserId());
        }
        List<HdGoodsType> list = hdGoodsTypeService.selectHdGoodsTypeList(hdGoodsType);
        return getDataTable(list);
    }

    /**
     * 导出用户商品类型列表
     */
    @RequiresPermissions("system:type:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdGoodsType hdGoodsType)
    {
        List<HdGoodsType> list = hdGoodsTypeService.selectHdGoodsTypeList(hdGoodsType);
        ExcelUtil<HdGoodsType> util = new ExcelUtil<HdGoodsType>(HdGoodsType.class);
        return util.exportExcel(list, "type");
    }

    /**
     * 新增用户商品类型
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存用户商品类型
     */
    @RequiresPermissions("system:type:add")
    @Log(title = "用户商品类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdGoodsType hdGoodsType)
    {
        hdGoodsType.setUserId(ShiroUtils.getUserId());
        return toAjax(hdGoodsTypeService.insertHdGoodsType(hdGoodsType));
    }

    /**
     * 修改用户商品类型
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HdGoodsType hdGoodsType = hdGoodsTypeService.selectHdGoodsTypeById(id);
        mmap.put("hdGoodsType", hdGoodsType);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户商品类型
     */
    @RequiresPermissions("system:type:edit")
    @Log(title = "用户商品类型", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdGoodsType hdGoodsType)
    {
        return toAjax(hdGoodsTypeService.updateHdGoodsType(hdGoodsType));
    }

    /**
     * 删除用户商品类型
     */
    @RequiresPermissions("system:type:remove")
    @Log(title = "用户商品类型", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hdGoodsTypeService.deleteHdGoodsTypeByIds(ids));
    }
}
