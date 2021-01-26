package com.ruoyi.project.system.userOrder.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.google.common.collect.Maps;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.pay.AlipayConfig;
import com.ruoyi.common.utils.pay.WeixinTools;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.qrcode.QRCodeUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.money.domain.HdUserMoney;
import com.ruoyi.project.system.money.mapper.HdUserMoneyMapper;
import com.ruoyi.project.system.money.service.IHdUserMoneyService;
import com.ruoyi.project.system.user.service.IUserService;
import com.ruoyi.project.system.userOrder.domain.HdUserOrder;
import com.ruoyi.project.system.userOrder.service.IHdUserOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 支付订单Controller
 *
 * @author ruoyi
 * @date 2019-10-21
 */
@Controller
@RequestMapping("/system/userOrder")
public class HdUserOrderController extends BaseController {
    protected final Logger logger = LoggerFactory.getLogger(HdUserOrderController.class);

    private String prefix = "system/userOrder";

    @Autowired
    private IHdUserOrderService hdUserOrderService;
    @Autowired
    private IHdUserMoneyService hdUserMoneyService;

    @Autowired
    private HdUserMoneyMapper userMoneyMapper;

    @Autowired
    private IUserService userService;

    @RequiresPermissions("system:userOrder:view")
    @GetMapping()
    public String userOrder() {
        return prefix + "/userOrder";
    }

    /**
     * 查询支付订单列表
     */
    @RequiresPermissions("system:userOrder:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HdUserOrder hdUserOrder) {
        startPage();
        List<HdUserOrder> list = hdUserOrderService.selectHdUserOrderList(hdUserOrder);
        return getDataTable(list);
    }

    /**
     * 导出支付订单列表
     */
    @RequiresPermissions("system:userOrder:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HdUserOrder hdUserOrder) {
        List<HdUserOrder> list = hdUserOrderService.selectHdUserOrderList(hdUserOrder);
        ExcelUtil<HdUserOrder> util = new ExcelUtil<HdUserOrder>(HdUserOrder.class);
        return util.exportExcel(list, "userOrder");
    }

    /**
     * 新增支付订单
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存支付订单
     */
    @RequiresPermissions("system:userOrder:add")
    @Log(title = "支付订单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HdUserOrder hdUserOrder) {
        return toAjax(hdUserOrderService.insertHdUserOrder(hdUserOrder));
    }

    /**
     * 修改支付订单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        HdUserOrder hdUserOrder = hdUserOrderService.selectHdUserOrderById(id);
        mmap.put("hdUserOrder", hdUserOrder);
        return prefix + "/edit";
    }

    /**
     * 修改保存支付订单
     */
    @RequiresPermissions("system:userOrder:edit")
    @Log(title = "支付订单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HdUserOrder hdUserOrder) {
        return toAjax(hdUserOrderService.updateHdUserOrder(hdUserOrder));
    }

    /**
     * 删除支付订单
     */
    @RequiresPermissions("system:userOrder:remove")
    @Log(title = "支付订单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hdUserOrderService.deleteHdUserOrderByIds(ids));
    }


    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public String pcOrder(HttpServletRequest request, HttpServletResponse response, Long amount, int payType, ModelMap mmap) {
        PrintWriter out = null;
        try {
            BigDecimal b1 = new BigDecimal(Double.valueOf(amount)).multiply(new BigDecimal(100));
            HdUserMoney hdUserMoney = new HdUserMoney();
            hdUserMoney.setCreateTime(new Date());
            hdUserMoney.setUserId(getUserId());
            hdUserMoney.setAmount(b1.longValue());
            hdUserMoney.setChangeType(20);
            hdUserMoney.setUserName(getSysUser().getUserName());
            hdUserMoney.setStatus(10);
            hdUserMoney.setBalance(getSysUser().getMoney());
            userMoneyMapper.insertHdUserMoney(hdUserMoney);
            if (0 == payType) {
                //微信支付-
                String ip = StringUtils.getRemoteAddr(request).split(",")[0];
                String amoutnStr = b1.toString();
                String code_url = WeixinTools.unifiedorderNative(request, response, String.valueOf(hdUserMoney.getId()),
                        String.valueOf(hdUserMoney.getId()), amoutnStr, ip, String.valueOf(hdUserMoney.getId()));
                System.out.println("微信下单二维码code地址：" + code_url);
                mmap.put("code_url", code_url);
                mmap.put("amount", amount);
                return prefix + "/weixinpay";
            } else if (1 == payType) {
                // 支付宝
                // 获得初始化的AlipayClient
                AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
                //设置请求参数
                AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
                alipayRequest.setReturnUrl(AlipayConfig.return_url);
                alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
                alipayRequest.setBizContent("{\"out_trade_no\":\"" + hdUserMoney.getId() + "\","
                        + "\"total_amount\":\"" + amount + "\","
                        + "\"subject\":\"" + hdUserMoney.getId() + "\","
                        + "\"body\":\"" + hdUserMoney.getId() + "\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
                //请求
                String result = alipayClient.pageExecute(alipayRequest).getBody();
                /** 设置回传格式*/
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html;charset=UTF-8");
                out = response.getWriter();
                out.write(result);//直接将完整的表单html输出到页面
                return null;
            } else {
                mmap.put("msg", "请选择支付方式！");
                mmap.put("code", 500);
            }
        } catch (Exception e) {
            mmap.put("msg", "添加失败");
            mmap.put("code", 500);
            return "system/order/pay";
        }
        return "system/money/chongzhi";
    }

    @RequestMapping(value = "notify-url")
    @ResponseBody
    public String notify_url(HttpServletRequest request) {
        System.out.println(request.getParameter("trade_status").toString() + "========================");
        System.out.println(request.getParameter("out_trade_no").toString() + "-------------------------");

        try {
            String trade_status = request.getParameter("trade_status");
            if (trade_status == null || "".equals(trade_status)) {
                return null;
            }
            String out_trade_no = request.getParameter("out_trade_no");
            HdUserMoney userMoney = userMoneyMapper.selectHdUserMoneyById(Long.parseLong(out_trade_no));
            if ("20".equals(userMoney.getStatus())) {
                logger.info("支付宝交易关闭调用接口！");
                logger.info("支付宝交易，该订单已完成修改交易状态！");
                return "success";
            }
            logger.info("code:" + request.getParameter("trade_status") + "||| out_trade_no:" + request.getParameter("out_trade_no"));
            if ("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)) {
                logger.info("支付宝交易成功");
                userMoney.setStatus(20);
                hdUserMoneyService.updateHdUserMoney(userMoney);
                logger.info("支付宝回调成功！");
                return "success";
            }
            return "error";
        } catch (NullPointerException ex) {
            logger.info("交易被关闭了");
            logger.info("支付宝回调失败！out_trade_no:" + request.getParameter("out_trade_no"));
            logger.error(ex.getMessage());
            return "error";
        }
    }

    @ResponseBody
    @RequestMapping({"noticeUrlwx"})
    public void noticeUrlwx(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String returnStr = "";
        String noticeXml = "";
        this.logger.info("----------微信回调获取数据开始！----------------");
        request.setCharacterEncoding("utf-8");
        InputStream inputStream = null;
        inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String s;
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();
        this.logger.info("------------微信回调获取数据结束！--------------");
        noticeXml = sb.toString();
        this.logger.info("微信返回的：" + noticeXml);
        Map<String, Object> map = this.parseXml(noticeXml);
        this.logger.info("map:" + map.toString());
        String out_trade_no = map.get("out_trade_no").toString().trim();
        if (out_trade_no.length() > 17) {
            out_trade_no = out_trade_no.substring(0, 17);
        }


        HdUserMoney userMoney = userMoneyMapper.selectHdUserMoneyById(Long.parseLong(out_trade_no));
        String transaction_id = map.get("transaction_id").toString().trim();
        String fee = map.get("total_fee").toString().trim();
        this.logger.info("noticeUrlwx - 微信支付回调:out_trade_no:" + out_trade_no + ", transaction_id:" + transaction_id + ", fee:" + fee);
        this.logger.info("------------微信回调业务处理开始！--------------");
        if ("20".equals(userMoney.getStatus())) {
            returnStr = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            this.logger.info("微信回调，状态已经修改");
            this.logger.info("微信回调，修改状态结束-1");
        } else if ("SUCCESS".equals(map.get("result_code").toString().trim())) {
            returnStr = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            userMoney.setStatus(20);
            hdUserMoneyService.updateHdUserMoney(userMoney);
            this.logger.info("微信回调，修改状态成功");
            this.logger.info("微信回调，修改状态结束-2");
        } else {
            returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[FAIL]]></return_msg></xml>";
            this.logger.info("微信回调，修改状态失败");
            this.logger.info("微信回调，修改状态结束-3");
        }

        this.logger.info("------------微信回调业务处理结束！--------------");
        this.logger.info("------------微信回调响应开始！--------------");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(returnStr.getBytes());
        out.flush();
        out.close();
        this.logger.info("------------微信回调响应结束！--------------");
        this.logger.info("微信回调结束");
    }

    public Map<String, Object> parseXml(String sourceXml) {
        try {
            Map<String, Object> map = Maps.newHashMap();
            Document document = DocumentHelper.parseText(sourceXml);
            Element root = document.getRootElement();
            Iterator it = root.elementIterator();

            while (it.hasNext()) {
                Element element = (Element) it.next();
                String key = element.getName();
                String value = element.getText();
                map.put(key, value);
            }

            return map;
        } catch (Exception var9) {
            var9.printStackTrace();
            this.logger.error(var9.getMessage());
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "getQRCode")
    public String getQRCode(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String code_url = request.getParameter("code_url");
            BufferedImage buffImg = QRCodeUtil.encode(code_url, 300, 300);
            // 设置图像缓存为no-cache。
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/png");
            ServletOutputStream sos = response.getOutputStream();
            ImageIO.write(buffImg, "png", sos);
            sos.close();
            sos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }
}
