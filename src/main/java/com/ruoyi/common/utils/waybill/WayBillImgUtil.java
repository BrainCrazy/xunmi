package com.ruoyi.common.utils.waybill;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * 生成电子面单图片工具
 *
 * @author yibo.su
 * @version V1.0
 * @date 2020/1/16 5:31 下午
 */

@Slf4j
public class WayBillImgUtil {


    //图片的宽度
    public static final int IMG_WIDTH = 840;
    //图片的宽度
    public static final int IMG_HEIGHT = 840;


    private static Image insertImage(byte[] imageBytes, int imgWidth, int imgHeight, boolean isCompress) throws Exception {
        BufferedImage src = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (isCompress) {
            return src.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        }
        return src;
    }


    /**
     * 生成母订单
     *
     * @param orderParam 生成订单所需的参数对象
     * @return boolean
     */
    public static byte[] generateParentOrder(SfPrintOrderParam orderParam) {
        if (null == orderParam) {
            return null;
        }
        int startHeight = 20;  //表格的起始高度
        int startWidth = 20;   //表格的起始宽度
        try {
            BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            //设置背景色为白色
            g.setColor(Color.WHITE);
            //设置颜色区域大小
            g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);

            /*
             * 绘制表格 填充内容
             * */
            //表格线条的颜色
            g.setColor(Color.BLACK);
            //边框加粗
            g.setStroke(new BasicStroke(3.0f));
            //消除文本出现锯齿现象
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //表格的四个边框
            //上边框
            g.drawLine(startWidth, startHeight, startWidth + 800, startHeight);


            //绘制表格内容 第一行
            g.setFont(new Font("黑体", Font.BOLD, 20));
            g.drawString("Order ID : " + orderParam.getOrderId(), startWidth + 10, startHeight += 40);
            g.drawLine(startWidth, startHeight += 30, startWidth + 800, startHeight);

            //绘制第二行
            //导入条码图片
            String barCodeStr = "Tracking NO.: " + orderParam.getTrackingNo();
            Image sfBarImg = insertImage(BarCodeUtil.getBarCode(orderParam.getTrackingNo(), null), 700, 120, true);
            g.drawImage(sfBarImg, startWidth + 50, startHeight += 10, null);
            g.setFont(new Font("黑体", Font.BOLD, 18));
            g.drawString(barCodeStr, startWidth + 230, startHeight += 140);
            g.drawLine(startWidth, startHeight += 10, startWidth + 800, startHeight);

            int line3Height = startHeight;
            // 设置字体
            g.setFont(new Font("黑体", Font.BOLD, 35));
            g.drawString(orderParam.getFirstMileName(), startWidth + 50, startHeight += 80);
            g.drawString(orderParam.getLastMileName(), startWidth + 250, startHeight);
            g.drawString(orderParam.getLaneCode(), startWidth + 630, startHeight);
            g.setFont(new Font("黑体", Font.BOLD, 100));
            g.drawString(orderParam.getGoodsToDeclare(), startWidth + 465, startHeight + 20);
            //绘制产品类型框
            startHeight = Math.max(startHeight, 370);
            g.drawLine(startWidth, startHeight += 10, startWidth + 800, startHeight);

            // 绘制竖线
            g.drawLine(startWidth + 200, line3Height, startWidth + 200, startHeight);
            g.drawLine(startWidth + 600, line3Height, startWidth + 600, startHeight);
            // 第四行
            // 左侧栏
            g.setFont(new Font("黑体", Font.BOLD, 24));
            g.drawString("Ship To: " + shortenStr(orderParam.getBuyerUsername(), 16), startWidth + 10, startHeight += 30);
            int h1 = drawStringWithFontStyleLineFeed(g, orderParam.getFullAddress(), startWidth + 10, startHeight += 30, 380, new Font("黑体", Font.PLAIN, 22));
            g.setFont(new Font("黑体", Font.PLAIN, 23));
            g.drawString("Tel: " + orderParam.getPhone(), startWidth + 10, h1);
            // 右侧栏
//            g.setFont(new Font("黑体", Font.PLAIN, 23));
//            g.drawString(orderParam.getShopeeAddress(), startWidth + 410, startHeight);
            // 根据单词换行
            int h2 = drawStringWithFontStyleLineFeed(g, orderParam.getWarehouseAddress(), startWidth + 420, startHeight, 400, new Font("黑体", Font.PLAIN, 20));
            startHeight = Math.max(h1, h2);
            // 第二条竖线
            g.drawLine(startWidth + 400, line3Height, startWidth + 400, startHeight += 10);
            // 封底
            g.drawLine(startWidth, startHeight, startWidth + 800, startHeight);

            // 第五行
            g.setFont(new Font("黑体", Font.BOLD, 25));
            g.drawString("Items:", startWidth + 10, startHeight += 30);
            List<SfPrintOrderParam.Item> items = orderParam.getItems();
            startHeight += 30;
            int initHeight = startHeight;
            if (items != null && !items.isEmpty()) {
                g.setFont(new Font("黑体", Font.PLAIN, 22));
                for (SfPrintOrderParam.Item item : items) {
                    g.drawString(item.getCategoryName() + "   " + item.getCount(), startWidth + 10, initHeight);
                    initHeight += 30;
                }
            }
            // 右侧大字母
            g.setFont(new Font("黑体", Font.BOLD, 100));
            g.drawString(orderParam.getServiceCode(), startWidth + 550, startHeight += 40);
            startHeight = Math.max(820, startHeight);
            //左边框
            g.drawLine(startWidth, 20, startWidth, startHeight);
            //下边框
            g.drawLine(startWidth, startHeight, startWidth + 800, startHeight);
            //右边框
            g.drawLine(startWidth + 800, 20, startWidth + 800, startHeight);
            g.dispose();
            //生成订单图片
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", bos);
            log.info("运单生成成功,orderId-->{}", orderParam.getOrderId());
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 自动换行书写
     *
     * @return
     */
    private static int drawStringWithFontStyleLineFeed(Graphics g, String strContent, int loc_X, int loc_Y, int maxWidth, Font font) {
        g.setFont(font);
        //获取单词数量
        String[] str = strContent.split(" ");
        //获取字符高度
        int strHeight = g.getFontMetrics().getHeight();
        StringBuilder sb = new StringBuilder();
        int countWidth = 0;
        for (int i = 0; i < str.length; i++) {
            // 获取单词宽度
            String s;
            if (i == str.length - 1) {
                s = str[i];
            } else {
                s = str[i] + " ";
            }
            int strWidth = g.getFontMetrics().charsWidth(s.toCharArray(), 0, s.length());
            countWidth += strWidth;
            if (countWidth >= maxWidth) {
                String printStr;
                if (countWidth == maxWidth) {
                    sb.append(sb);
                    printStr = sb.toString();
                    sb = new StringBuilder();
                    countWidth = 0;
                } else {
                    printStr = sb.toString();
                    sb = new StringBuilder(s);
                    countWidth = strWidth;
                }
                g.drawString(printStr, loc_X, loc_Y);
                loc_Y = loc_Y + strHeight;
            } else {
                sb.append(s);
            }
        }
        if (sb.length() > 0) {
            g.drawString(sb.toString(), loc_X, loc_Y);
        }
        return loc_Y + strHeight;
    }

    /**
     * 缩短字符串
     *
     * @param str 字符串
     * @param len 保留长度
     * @return ~
     */
    private static String shortenStr(String str, int len) {
        if (str.length() < len + 3) {
            return str;
        }
        String substring = str.substring(0, len);
        return substring + "...";
    }


    public static void main(String[] args) throws IOException {
        System.out.println("123");
        SfPrintOrderParam param = new SfPrintOrderParam();
        param.setOrderId("2002112XHTVDM6");
        param.setTrackingNo("MY204815178174W");
        param.setFirstMileName("MYF3");
        param.setLastMileName("MYL4");
        param.setGoodsToDeclare(true);
        param.setLaneCode("S-MY06");
        param.setBuyerUsername("JINNY");
//        param.setFullAddress("NO.26, JLN OS 1/2, ONE SELAYANG, 68100 BATU CAVES, SELANGOR., Batu Caves, 68100, Selangor");
        param.setFullAddress("ST1-1-5 Kelompok Seri Tanjung, Jalan 7/1d Seksyen 7,, Bangi, 43650, Selangor");
        param.setPhone("60163380335");
        param.setWarehouseAddress("China Guangdong Shenzhen\n" +
                "1/F, Building 8,Tangtou industrial District ,Shiyan street ,baoan district");
        param.setServiceCode("M04");

        List<SfPrintOrderParam.Item> items = param.getItems();
        SfPrintOrderParam.Item item = new SfPrintOrderParam.Item();
        item.setCategoryName("Body Oil");
        item.setCount(20);
        SfPrintOrderParam.Item item2 = new SfPrintOrderParam.Item();
        item2.setCategoryName("Body Oil");
        item2.setCount(20);
        SfPrintOrderParam.Item item3 = new SfPrintOrderParam.Item();
        item3.setCategoryName("Body Oil");
        item3.setCount(20);
        items.add(item);
        items.add(item2);
        items.add(item3);
        byte[] bytes = WayBillImgUtil.generateParentOrder(param);
        FileOutputStream fos = new FileOutputStream("/Users/bairong/Desktop/huodai/" + System.currentTimeMillis() + ".jpeg");
        fos.write(bytes);
        fos.flush();
        fos.close();
    }

}
