package com.ruoyi.project.system.shop.controller;

import com.ruoyi.common.shopee.ShopeeConstants;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.shop.domain.HdShop;
import com.ruoyi.project.system.shop.service.IHdShopService;
import com.ruoyi.project.system.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.List;

/**
 * 授权店铺Controller
 *
 * @author ruoyi
 * @date 2020-02-13
 */
@Controller
@RequestMapping("/system/shop")
@Slf4j
public class HdShopController extends BaseController {

    /**
     * 授权地址
     */
    public static final String AUTH_URL = "https://partner.shopeemobile.com/api/v1/shop/auth_partner";

    private String prefix = "system/shop";

    @Autowired
    private IHdShopService hdShopService;

    @RequiresPermissions("system:shop:view")
    @GetMapping()
    public String shop() {
        return prefix + "/shop";
    }

    /**
     * 查询授权店铺列表
     */
    @RequiresPermissions("system:shop:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HdShop hdShop) {
        startPage();
        List<HdShop> list = hdShopService.selectHdShopList(hdShop);
        return getDataTable(list);
    }

    /**
     * 导出授权店铺列表
     */
    @RequiresPermissions("system:shop:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdShop hdShop) {
        List<HdShop> list = hdShopService.selectHdShopList(hdShop);
        ExcelUtil<HdShop> util = new ExcelUtil<HdShop>(HdShop.class);
        return util.exportExcel(list, "shop");
    }

    /**
     * 新增授权店铺
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存授权店铺
     */
    @RequiresPermissions("system:shop:add")
    @Log(title = "授权店铺", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdShop hdShop) {
        return toAjax(hdShopService.insertHdShop(hdShop));
    }

    /**
     * 修改授权店铺
     */
    @GetMapping("/edit/{shopId}")
    public String edit(@PathVariable("shopId") Long shopId, ModelMap mmap) {
        HdShop hdShop = hdShopService.selectHdShopById(shopId);
        mmap.put("hdShop", hdShop);
        return prefix + "/edit";
    }

    /**
     * 修改保存授权店铺
     */
    @RequiresPermissions("system:shop:edit")
    @Log(title = "授权店铺", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdShop hdShop) {
        return toAjax(hdShopService.updateHdShop(hdShop));
    }

    /**
     * 删除授权店铺
     */
    @RequiresPermissions("system:shop:remove")
    @Log(title = "授权店铺", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hdShopService.deleteHdShopByIds(ids));
    }


    /**
     * 生成授权连接
     *
     * @param host 域名地址
     * @return ~
     */
    @RequestMapping("create/authorization/url")
    @ResponseBody
    public AjaxResult createAuthorizationUrl(String host) {
        if (StringUtils.isBlank(host)) {
            return AjaxResult.error("获取参数失败");
        }
        User sysUser = ShiroUtils.getSysUser();
        if (sysUser == null) {
            return AjaxResult.error("用户未登陆");
        }

        String redirectUrl = host + "/confirm/shopId?userId=" + sysUser.getUserId();
        // 生成url
        String url;
        try {
            url = AUTH_URL + "?id=" + ShopeeConstants.PARTNER_ID + "&token=" + DigestUtils.sha256Hex(ShopeeConstants.KEY + redirectUrl)
                    + "&redirect=" + URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (Exception e) {
            log.error("生成跳转连接失败-->", e);
            return AjaxResult.error("生成跳转连接失败");
        }
        return AjaxResult.success("生成成功", url);
    }


    /**
     * 授权完成，获取shopId
     *
     * @param shop_id shopId
     * @return ~
     */
    @RequestMapping("confirm/shopId")
    public String getShopId(Long shop_id, Long userId, ModelMap mmap) {
        String targetUrl = shop();
        if (shop_id == null || userId == null) {
            // 跳转授权失败页
            mmap.put("msg", "授权失败,没有获取到授权信息");
            return targetUrl;
        }
//        HdShop hdShop = hdShopService.selectHdShopById(shop_id);
//        if (hdShop != null) {
//            mmap.put("msg", "已完成授权");
//            return targetUrl;
//        }
        try {
            hdShopService.createShopInfo(shop_id, userId);
        } catch (Exception e) {
            log.error("确认授权异常,shopId-->{},userId-->{}", shop_id, userId, e);
            mmap.put("msg", "授权异常,请稍后再试");
            return targetUrl;
        }
        mmap.put("msg", "授权成功");
        return targetUrl;
    }
}
