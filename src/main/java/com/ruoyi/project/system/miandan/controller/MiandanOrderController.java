package com.ruoyi.project.system.miandan.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.waybill.SfPrintOrderParam;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.item.mapper.HdItemMapper;
import com.ruoyi.project.system.miandan.domain.MiandanOrder;
import com.ruoyi.project.system.miandan.service.IMiandanOrderService;
import com.ruoyi.project.system.ship.service.IHdOrderShipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 纯贴面单Controller
 *
 * @author wqf
 * @date 2020-02-14
 */
@Controller
@RequestMapping("/system/miandan")
@Slf4j
public class MiandanOrderController extends BaseController {
    private String prefix = "system/miandan";

    @Autowired
    private IMiandanOrderService miandanOrderService;

    @Autowired
    private IHdOrderShipService orderShipService;

    @Autowired
    private HdItemMapper hdItemMapper;

    @RequestMapping("/orderDetail")
    @ResponseBody
    public TableDataInfo orderDetail(Long id) {
        List<HdItem> list = miandanOrderService.getorderDetail(id);
        return getDataTable(list);
    }

    @RequestMapping("/queryCount")
    @ResponseBody
    public AjaxResult queryCount() {
        MiandanOrder miandanOrder = new MiandanOrder();
        miandanOrder.setUserId(ShiroUtils.getUserId());
        miandanOrder.setStatus(10);
        List<MiandanOrder> list = miandanOrderService.selectMiandanOrderList(miandanOrder);
        String res = list.size() + "-";
        miandanOrder.setStatus(30);
        list = miandanOrderService.selectMiandanOrderList(miandanOrder);
        res += list.size() + "-";
        miandanOrder.setStatus(null);
        miandanOrder.setExceptionFlag(20);
        list = miandanOrderService.selectMiandanOrderList(miandanOrder);
        res += list.size();
        return AjaxResult.success(res);
    }

    @RequestMapping("/download")
    @RequiresPermissions("system:miandan:confirmStock")
    public ResponseEntity<byte[]> downloadMiandan(long id) throws Exception {
        //查询订单信息
        MiandanOrder miandanOrder = miandanOrderService.selectMiandanOrderById(id);
        List<SfPrintOrderParam.Item> items = new ArrayList<>();
        String itemJson = miandanOrder.getItems();
        JSONArray jsonArray = JSONArray.parseArray(itemJson);
        JSONObject json;
        SfPrintOrderParam.Item item;
        //解析json串
        for (int i = 0; i < jsonArray.size(); i++) {
            json = jsonArray.getJSONObject(i);
            item = new SfPrintOrderParam.Item();
            Long itemId = json.getLong("item_id");
            //查询item表
            HdItem hdItem = hdItemMapper.selectHdItemById(itemId);
            item.setCategoryName(hdItem.getCategoryName());
            item.setItemId(itemId);
            item.setCategoryId(hdItem.getCategoryId());
            item.setCount(json.getInteger("variation_quantity_purchased"));
            items.add(item);
        }
        byte[] file = orderShipService.createLogisticsPDF(Long.parseLong(miandanOrder.getAuthNumber()), miandanOrder.getShopeeOrderNo());
        if (file == null || file.length == 0) {
            file = orderShipService.createLogisticsImg(Long.parseLong(miandanOrder.getAuthNumber()), miandanOrder.getShopeeOrderNo(), miandanOrder.getCountry(), items);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + miandanOrder.getShopeeOrderNo() + ".pdf");
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(file, headers, statusCode);
        return entity;
    }

    @RequiresPermissions("system:miandan:view")
    @GetMapping()
    public String miandan() {
        return prefix + "/miandan";
    }

    /**
     * 查询纯贴面单列表
     */
    @RequiresPermissions("system:miandan:list")
    @PostMapping("/list")
    @ResponseBody
    @DataScope(deptAlias = "d", userAlias = "u")
    public TableDataInfo list(MiandanOrder miandanOrder) {
        startPage();
        List<MiandanOrder> list = miandanOrderService.selectMiandanOrderList(miandanOrder);
        return getDataTable(list);
    }

    /**
     * 导出纯贴面单列表
     */
    @RequiresPermissions("system:miandan:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MiandanOrder miandanOrder) {
        List<MiandanOrder> list = miandanOrderService.selectMiandanOrderList(miandanOrder);
        ExcelUtil<MiandanOrder> util = new ExcelUtil<MiandanOrder>(MiandanOrder.class);
        return util.exportExcel(list, "miandan");
    }

    /**
     * 新增纯贴面单
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存纯贴面单
     */
    @RequiresPermissions("system:miandan:add")
    @Log(title = "纯贴面单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MiandanOrder miandanOrder) {
        return toAjax(miandanOrderService.insertMiandanOrder(miandanOrder));
    }

    /**
     * 修改纯贴面单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        MiandanOrder miandanOrder = miandanOrderService.selectMiandanOrderById(id);
        mmap.put("miandanOrder", miandanOrder);
        return prefix + "/edit";
    }

    /**
     * 修改保存纯贴面单
     */
    @RequiresPermissions("system:miandan:edit")
    @Log(title = "纯贴面单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MiandanOrder miandanOrder) {
        return toAjax(miandanOrderService.updateMiandanOrder(miandanOrder, miandanOrder.getStatus()));
    }

    /**
     * 添加运单号
     */
    @RequiresPermissions("system:miandan:edit")
    @Log(title = "纯贴面单", businessType = BusinessType.UPDATE)
    @PostMapping("/addPackageNo")
    @ResponseBody
    public AjaxResult addPackageNo(MiandanOrder miandanOrder) {
        if (StringUtils.isEmpty(miandanOrder.getPackageNo())) {
            throw new BusinessException("运单号不能为空");
        }
        return toAjax(miandanOrderService.updateMiandanOrder(miandanOrder, 20));
    }

    /**
     * 确认入库
     */
    @RequiresPermissions("system:miandan:confirmStock")
    @Log(title = "纯贴面单确认入库", businessType = BusinessType.UPDATE)
    @PostMapping("/confirmStock/{id}")
    @ResponseBody
    public AjaxResult confirmStock(@PathVariable("id") Long id) {
        return toAjax(miandanOrderService.updateStatusById(id, 20, 30));
    }

    /**
     * 确认已发货
     */
    @RequiresPermissions("system:miandan:outStock")
    @Log(title = "纯贴面单确认已发货", businessType = BusinessType.UPDATE)
    @PostMapping("/outStock/{id}")
    @ResponseBody
    public AjaxResult confirmOutStock(@PathVariable("id") Long id) {
        try {
            miandanOrderService.confirmOutStock(id);
            return AjaxResult.success();
        } catch (Exception e) {
            log.error("", e);
            return AjaxResult.error("操作失败,请联系管路员");
        }

    }

    /**
     * 删除纯贴面单
     */

    @Log(title = "纯贴面单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(miandanOrderService.deleteMiandanOrderByIds(ids));
    }
}
