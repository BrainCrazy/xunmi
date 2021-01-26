package com.ruoyi.common.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author yibo.su
 * @version V1.0
 * @date 2020/2/9 11:48 下午
 */
public class ShopeeUtil {
    public static final String HOST = "https://partner.shopeemobile.com";

    public static String sign(String url, String key, String json) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST);
        sb.append(url);
        sb.append("|");
        sb.append(json);
        System.out.println(sb.toString());
        return sha256Hmac(sb.toString(), key);
    }



    public static String sha256Hmac(String data, String key) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] array = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();

    }
}
