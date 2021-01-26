
package com.ruoyi.common.utils.pay;

public class WeChatConfig {
    /**
     * 公众号AppId
     */
    public static final String APP_ID = "wxaf73595873488684";

    /**
     * 公众号AppSecret
     */
    public static final String APP_SECRET = "aa78e2467717643725b8501aed160117 ";
    /*hSWHgUEFhLB7ocJnCyyenFOeJutvtvKG*/
    /*50779da692a92bb1333a9efcdcd61593*/

    /**
     * 微信支付商户号
     */
    public static final String MCH_ID = "1564230591";

    /**
     * 终端设备号
     * PC网页或公众号传"WEB"
     */
    public static final String DEVICE_INFO = "WEB";

    /**
     *
     */
    public static final String ip = "127.0.0.1";
    /*192.168.0.119*/

    /**
     * 微信支付API秘钥
     */

    public static final String KEY = "WinderinfoWinderinfoWinderinfo12";
    /*be9a940fdb58e046417dcdfdb30d3a9*/

    /**
     * 微信支付api证书路径
     */
    public static final String CERT_PATH = "***/apiclient_cert.p12";

    /**
     * 微信统一下单url
     */
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 微信申请退款url
     */
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";


    /**
     * 微信登录目录url
     */
    public static final String OPENID_URL = "http%3A%2F%2Fwww.qzkzt.com%2Ff%2Fuser%2FgetOpenid";

    /**
     * 微信支付通知url
     */
//    public static final String NOTIFY_URL = "http://www.qzkzt.com/f/order/noticeUrlwx";
    public static final String NOTIFY_URL = "http://www.yixuan1932.com/system/userOrder/noticeUrlwx";
//    public static final String NOTIFY_URL = "http://www.hdksw.com/order/app/noticeUrlwx";

    /**
     * 微信交易类型:公众号支付
     */
    public static final String TRADE_TYPE_JSAPI = "JSAPI";

    /**
     * 微信交易类型:原生扫码支付
     */
    public static final String TRADE_TYPE_NATIVE = "NATIVE";

    /**
     * 微信甲乙类型:APP支付
     */
    public static final String TRADE_TYPE_APP = "APP";

    /**
     * 终端设备号
     * h5手机支付传"MWEB"
     */
    public static final String TRADE_TYPE_MWEB = "MWEB";

//    // 微信支付partner_key
//    public static String  partner_key = "liujiayiliyipingejiaoyu123456789";
}

