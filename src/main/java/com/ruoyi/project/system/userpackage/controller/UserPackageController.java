package com.ruoyi.project.system.userpackage.controller;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import com.ruoyi.project.system.StockInfoItem.service.IStockInfoItemService;
import com.ruoyi.project.system.unpackage.domain.UnpackingOrder;
import com.ruoyi.project.system.unpackage.service.IUnpackingOrderService;
import com.ruoyi.project.system.userpackage.domain.UserPackage;
import com.ruoyi.project.system.userpackage.service.IUserPackageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 用户快递Controller
 *
 * @author wqf
 * @date 2020-02-15
 */
@Controller
@RequestMapping("/system/userpackage")
@Slf4j
public class UserPackageController extends BaseController {
    private String prefix = "system/userpackage";

    @Autowired
    private IUserPackageService userPackageService;

    @Autowired
    private IUnpackingOrderService unpackingOrderService;

    @Autowired
    private IStockInfoItemService stockInfoItemService;


    @GetMapping()
    public String userpackage() {
        return prefix + "/userpackage";
    }

    /**
     * 查询用户快递列表
     */
    @RequestMapping("/orderDetail")
    @ResponseBody
    public TableDataInfo list(String shopeeOrderNo) {
        List<UserPackage> list = userPackageService.selectByShopeeOrderNo(shopeeOrderNo);
        return getDataTable(list);
    }

    /**
     * 查询用户快递列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo getUserPackageInfo(String packageNo) {
        List<UserPackage> list = userPackageService.getUserPackageInfo(packageNo, false);
        return getDataTable(list);
    }

    @ResponseBody
    @RequestMapping("/saveStockImage")
    public AjaxResult saveStockImage(MultipartFile stockImage, long id, int orderType) throws IOException {
        return toAjax(userPackageService.saveStockImage(stockImage, id, orderType));
    }

    /**
     * 用户查看仓库图片
     *
     * @param id   此id为虾皮订单号
     * @param mmap
     * @return
     */
    @RequestMapping("/showImage/{id}")
    public String showImage(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("shopeeOrderNo", id);
        return prefix + "/showImage";
    }

    @RequestMapping("/toStockImage/{id}")
    public String toStockImage(@PathVariable("id") String id, ModelMap mmap) {
        String[] arr = id.split("-");
        mmap.put("id", arr[0]);
        mmap.put("photograph", arr[1]);
        mmap.put("orderType", arr[2]);
        return prefix + "/uploadImage";
    }

    /**
     * 查询用户快递
     */
    @PostMapping("/packageList")
    @ResponseBody
    public TableDataInfo getPackageList(long id, int orderType) {
        return getDataTable(userPackageService.getUserPackageInfoByType(id, orderType));
    }

    /**
     * 新增保存用户快递
     */
    @Log(title = "用户快递", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(UserPackage userPackage, MultipartFile goodsImageDate) {
        try {
            return toAjax(userPackageService.insertUserPackage(userPackage, goodsImageDate));
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException("保存失败");
        }

    }

    /**
     * 修改用户快递
     *
     * @param id   此id为虾皮订单号
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        List<UserPackage> list = userPackageService.selectByShopeeOrderNo(id);
        List<UnpackingOrder> orders = unpackingOrderService.selectByShopeeOrderNos(new String[]{id});
        if (CollectionUtils.isEmpty(orders)) {
            throw new BusinessException("未查询到订单！");
        }
        List<StockInfoItem> items = stockInfoItemService.selectStockInfoItemByShopeeOrderNo(id);
        if (!CollectionUtils.isEmpty(items)) {
            UserPackage userPackage;
            for (StockInfoItem stockInfoItem : items) {
                userPackage = new UserPackage();
                userPackage.setCategoryName(stockInfoItem.getCategoryName());
                userPackage.setGoodsCount(new Long(stockInfoItem.getGoodsCount()).intValue());
                userPackage.setGoodsName(stockInfoItem.getGoodsName());
                userPackage.setGoodsImage(stockInfoItem.getGoodsImageUrl());
                userPackage.setVariationName("库存商品");
                userPackage.setPackageNo("库存商品");
                list.add(userPackage);
            }
        }
        mmap.put("remark", orders.get(0).getRemark());
        mmap.put("userPackages", list);
        mmap.put("shopeeOrderNo", id);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户快递
     */
    @Log(title = "用户快递", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Long[] id, String[] packageNo, String shopeeOrderNo, String remark) {
        return toAjax(userPackageService.updatePackageNos(id, packageNo, shopeeOrderNo, remark));
    }

    /**
     * 删除用户快递
     */
    @RequiresPermissions("system:userpackage:remove")
    @Log(title = "用户快递", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(userPackageService.deleteUserPackageByIds(ids));
    }
}
