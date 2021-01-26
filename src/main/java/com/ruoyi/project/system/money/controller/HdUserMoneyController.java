package com.ruoyi.project.system.money.controller;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.money.domain.HdUserMoney;
import com.ruoyi.project.system.money.service.IHdUserMoneyService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户余额变动Controller
 *
 * @author ruoyi
 * @date 2019-10-21
 */
@Controller
@RequestMapping("/system/money")
public class HdUserMoneyController extends BaseController {
    private String prefix = "system/money";

    @Autowired
    private IHdUserMoneyService hdUserMoneyService;
    @Autowired
    private IUserService userService;

    @GetMapping("/chongzhi")
    public String zhifu(ModelMap mmap) {

        return prefix + "/chongzhi";
    }

    @RequiresPermissions("system:money:view")
    @GetMapping()
    public String money(ModelMap mmap) {
        User user = userService.selectUserById(ShiroUtils.getUserId());
        mmap.put("usermoney", user.getMoney() / 100);
        mmap.put("username", user.getUserName());
        return prefix + "/money";
    }

    @RequestMapping("/balance")
    @ResponseBody
    public AjaxResult getBalance() {
        //查询用户
        User user = userService.selectUserById(ShiroUtils.getUserId());
        ShiroUtils.setSysUser(user);
        return AjaxResult.success(user);
    }

    /**
     * 查询用户余额变动列表
     */
    @RequiresPermissions("system:money:list")
    @PostMapping("/list")
    @DataScope(deptAlias = "d", userAlias = "u")
    @ResponseBody
    public TableDataInfo list(HdUserMoney hdUserMoney) {
        startPage();
        List<HdUserMoney> list = hdUserMoneyService.selectHdUserMoneyList(hdUserMoney);
        return getDataTable(list);
    }

    /**
     * 导出用户余额变动列表
     */
    @RequiresPermissions("system:money:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdUserMoney hdUserMoney) {
        List<HdUserMoney> list = hdUserMoneyService.selectHdUserMoneyList(hdUserMoney);
        ExcelUtil<HdUserMoney> util = new ExcelUtil<HdUserMoney>(HdUserMoney.class);
        return util.exportExcel(list, "money");
    }

    /**
     * 新增用户余额变动
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存用户余额变动
     */
    @RequiresPermissions("system:money:add")
    @Log(title = "用户余额变动", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdUserMoney hdUserMoney) {
        return toAjax(hdUserMoneyService.insertHdUserMoney(hdUserMoney));
    }

    /**
     * 修改用户余额变动
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        HdUserMoney hdUserMoney = hdUserMoneyService.selectHdUserMoneyById(id);
        mmap.put("hdUserMoney", hdUserMoney);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户余额变动
     */
    @RequiresPermissions("system:money:edit")
    @Log(title = "用户余额变动", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdUserMoney hdUserMoney) {
        return toAjax(hdUserMoneyService.updateHdUserMoney(hdUserMoney));
    }

    /**
     * 删除用户余额变动
     */
    @RequiresPermissions("system:money:remove")
    @Log(title = "用户余额变动", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hdUserMoneyService.deleteHdUserMoneyByIds(ids));
    }
}
