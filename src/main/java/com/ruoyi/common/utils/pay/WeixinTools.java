package com.ruoyi.common.utils.pay;

import com.ruoyi.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付帮组类
 * @author Administrator
 *
 */
public class WeixinTools {


    /**
     *
     * @param body	body 商品简介
     * @param out_trade_no	商户系统内部的订单号
     * @param total_fee	订单总金额，单位为分
     * @param spbill_create_ip		用户端ip
     * @param goodsId
     * @param notify_url
     * @return
     */
    public static String h5WeixinPay(HttpServletRequest request, HttpServletResponse response,
                                     String body, String out_trade_no, int total_fee, String spbill_create_ip, String goodsId, String notify_url){
        String nonce_str = StringUtils.getRandom(10); // 制定随机字符串

        String notify_url_temp = "http://49.4.15.20:8084/huodai/system/userOrder/noticeUrlwx";
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", WeChatConfig.APP_ID);
        packageParams.put("mch_id", WeChatConfig.MCH_ID);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no); // 订单号
        packageParams.put("total_fee", total_fee+"");  //这里写的金额为1 分到时修改
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url_temp);
        packageParams.put("trade_type", WeChatConfig.TRADE_TYPE_MWEB);
        packageParams.put("product_id", goodsId); // 商户根据自己业务传递的参数 当trade_type=NATIVE时必填
        packageParams.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://www.ok5588.cn\",\"wap_name\": \"充值\"}} ");
        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(WeChatConfig.APP_ID, WeChatConfig.APP_SECRET, WeChatConfig.KEY);
        // 统一下单签名
        String sign = reqHandler.createSign(packageParams);


        String xml="<xml>"+
                "<appid>"+WeChatConfig.APP_ID+"</appid>"+
                "<mch_id>"+WeChatConfig.MCH_ID+"</mch_id>"+
                "<nonce_str>"+nonce_str+"</nonce_str>"+
                "<sign>"+sign+"</sign>"+
                "<body><![CDATA["+body+"]]></body>"+
                "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
                "<total_fee>"+total_fee+"</total_fee>"+
                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
                "<notify_url>"+notify_url_temp+"</notify_url>"+
                "<trade_type>"+WeChatConfig.TRADE_TYPE_MWEB+"</trade_type>"+
                "<product_id>"+goodsId+"</product_id>"+
                "<scene_info>{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://www.ok5588.cn\",\"wap_name\": \"充值\"}} </scene_info>"+
                "</xml>";
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String code_url = new GetWxOrderno().getPayH5Url(createOrderURL, xml);
//        System.out.println("h5 pay url------------>"+code_url);
        return code_url;
    }

    /**
     * 微信下单（扫码支付）
     * @param body 商品简介
     * @param out_trade_no 商户系统内部的订单号
     * @param total_fee 订单总金额，单位为分
     * @param spbill_create_ip APP和网页支付提交用户端ip
     * @return
     */
    public static String unifiedorderNative(HttpServletRequest request, HttpServletResponse response,
                                            String body, String out_trade_no, String total_fee, String spbill_create_ip, String goodsId) throws Exception{
        String nonce_str = StringUtils.getRandom(10); // 制定随机字符串
        //这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
        String notify_url = "http://www.yixuan1932.com/system/userOrder/noticeUrlwx";
        
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", WeChatConfig.APP_ID);
        packageParams.put("mch_id", WeChatConfig.MCH_ID);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no); // 订单号
        packageParams.put("total_fee", total_fee+"");  //这里写的金额为1 分到时修改
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", WeChatConfig.TRADE_TYPE_NATIVE);
        packageParams.put("product_id", goodsId); // 商户根据自己业务传递的参数 当trade_type=NATIVE时必填
        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(WeChatConfig.APP_ID, WeChatConfig.APP_SECRET, WeChatConfig.KEY);
        // 统一下单签名
        String sign = reqHandler.createSign(packageParams);

        String xml="<xml>"+
                "<appid>"+WeChatConfig.APP_ID+"</appid>"+
                "<mch_id>"+WeChatConfig.MCH_ID+"</mch_id>"+
                "<nonce_str>"+nonce_str+"</nonce_str>"+
                "<sign>"+sign+"</sign>"+
//                "<body><![CDATA["+body+"]]></body>"+
                "<body>"+body+"</body>"+
                "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
                "<total_fee>"+total_fee+"</total_fee>"+
                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
                "<notify_url>"+notify_url+"</notify_url>"+
                "<trade_type>"+WeChatConfig.TRADE_TYPE_NATIVE+"</trade_type>"+
                "<product_id>"+goodsId+"</product_id>"+
                "</xml>";
//        String allParameters =  reqHandler.genPackage(packageParams);
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//        String code_url = new GetWxOrderno().getPayUrl(createOrderURL, xml);
        String code_url = new GetWxOrderno().getPayUrl(createOrderURL, xml);
        return code_url;
    }

    /**
     * 微信下单（公众号支付）
     * @param body 商品简介
     * @param out_trade_no 商户系统内部的订单号
     * @param total_fee 订单总金额，单位为分
     * @param spbill_create_ip APP提交用户端ip
     * @return
     */
    public static SortedMap<String, String> unifiedorderJsapi(HttpServletRequest request, HttpServletResponse response,
                                                              String body, String out_trade_no, int total_fee, String spbill_create_ip, String goodsId) throws Exception{
        String nonce_str = StringUtils.getRandom(10); // 制定随机字符串
        //这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
        String notify_url = "http://www.yixuan1932.com/system/userOrder/noticeUrlwx";
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", WeChatConfig.APP_ID);
        packageParams.put("mch_id", WeChatConfig.MCH_ID);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no); // 订单号
        packageParams.put("total_fee", total_fee+"");  //这里写的金额为1 分到时修改
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        //packageParams.put("openid",openid);
        packageParams.put("trade_type", WeChatConfig.TRADE_TYPE_APP);
        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(WeChatConfig.APP_ID, WeChatConfig.APP_SECRET, WeChatConfig.KEY);
        // 统一下单签名
        String sign = reqHandler.createSign(packageParams);

//        String xml="<xml>"+
//                "<appid>"+WeChatConfig.APP_ID+"</appid>"+
//                "<mch_id>"+WeChatConfig.MCH_ID+"</mch_id>"+
//                "<nonce_str>"+nonce_str+"</nonce_str>"+
//                "<sign>"+sign+"</sign>"+
//                "<body><![CDATA["+body+"]]></body>"+
//                "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
//                "<total_fee>"+total_fee+"</total_fee>"+
//                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
//                "<notify_url>"+notify_url+"</notify_url>"+
//               "<openid>"+openid+"</openid>"+
//                "<trade_type>"+WeChatConfig.TRADE_TYPE_JSAPI+"</trade_type>"+
//                "<product_id>"+goodsId+"</product_id>"+
//                "</xml>";
        String xml="<xml>"+
                "<appid>"+WeChatConfig.APP_ID+"</appid>"+
                "<body>"+body+"</body>"+
                "<mch_id>"+WeChatConfig.MCH_ID+"</mch_id>"+
                "<nonce_str>"+nonce_str+"</nonce_str>"+
                "<notify_url>"+notify_url+"</notify_url>"+
                "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
                "<total_fee>"+total_fee+"</total_fee>"+
                "<trade_type>"+WeChatConfig.TRADE_TYPE_APP+"</trade_type>"+
                "<sign>"+sign+"</sign>"+
                "</xml>";

        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String prepay_id = new GetWxOrderno().getPayPrepayId(createOrderURL, xml);

        /** 生成支付签名 */
        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        Long timestamp = Calendar.getInstance().getTime().getTime();

        finalpackage.put("appid", WeChatConfig.APP_ID);
        finalpackage.put("timestamp", timestamp.toString().substring(0,10));
        finalpackage.put("noncestr", nonce_str);
        finalpackage.put("prepayid", prepay_id);
        finalpackage.put("partnerid", WeChatConfig.MCH_ID);
        finalpackage.put("package","Sign=WXPay");
        String finalsign = reqHandler.createSign(finalpackage);
        finalpackage.put("sign", finalsign);

        return finalpackage;
    }




}
