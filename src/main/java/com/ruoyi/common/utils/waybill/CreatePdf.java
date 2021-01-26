package com.ruoyi.common.utils.waybill;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ruoyi.common.exception.BusinessException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author yibo.su
 * @version V1.0
 * @date 2020/3/26 10:46 下午
 */
public class CreatePdf {

    public static byte[] createPdf(SfPrintOrderParam param) throws Exception {
        String country = param.getCountry();
        if (country == null) {
            return createPdf(param, null, null);
        }
        switch (country) {
            case "TW":
                return createPdf(param, "MHei-Medium", "UniCNS-UCS2-H");
            case "VN":
            case "TH":
                return createPdf(param, "font/tahoma.ttf", BaseFont.IDENTITY_H);
            default:
                return createPdf(param, null, null);
        }
    }

    public static byte[] createPdf(SfPrintOrderParam param, String customizationFontName, String encoding) throws Exception {
        float ly = 270.0F;
        float lx = 300.0F;
//        int offset = param.getItems().size() - 5;
//        if (offset > 0) {
//            ly += offset * 10.5F;
//        }
        Document document = new Document(new RectangleReadOnly(lx, ly), 2, 2, 5, 5);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, bos);
        document.open();
        // 字体
        BaseFont baseFont = BaseFont.createFont("Helvetica", "UTF-8", BaseFont.NOT_EMBEDDED);
        if (customizationFontName == null) {
            customizationFontName = "Helvetica";
        }
        if (encoding == null) {
            encoding = "UTF-8";
        }
        BaseFont customizationFont = BaseFont.createFont(customizationFontName, encoding, BaseFont.NOT_EMBEDDED);
//        BaseFont.createFont()
        // 外框
        PdfPTable tableName = new PdfPTable(1);
        tableName.setWidthPercentage(99);
        PdfPCell pdfCell = new PdfPCell();
        pdfCell.setBorderWidth(2F);

        // 添加订单号
        PdfPTable tableName1 = new PdfPTable(1);
        tableName1.setWidthPercentage(102);
        PdfPCell cellOrder = microSoftFont("Order ID: " + param.getOrderId(), new Font(baseFont, 12, Font.NORMAL), 19, false, true);
        cellOrder.disableBorderSide(1);
        tableName1.addCell(cellOrder);
        pdfCell.addElement(tableName1);

        // 添加条形码
        PdfPTable tableName2 = new PdfPTable(1);
        tableName2.setWidthPercentage(102);
        byte[] barCode = BarCodeUtil.getBarCode(param.getTrackingNo(), null);
        if (barCode == null) {
            throw new BusinessException("生成条形码异常");
        }
        PdfPCell imageCell = new PdfPCell();
        imageCell.setImage(Image.getInstance(barCode));
        imageCell.disableBorderSide(2);
        imageCell.setPaddingTop(5);
//        imageCell.setPaddingBottom(5);
        imageCell.setPaddingLeft(30);
        imageCell.setPaddingRight(30);
        tableName2.addCell(imageCell);
        pdfCell.addElement(tableName2);

        PdfPTable tableName3 = new PdfPTable(1);
        tableName3.setWidthPercentage(102);
        PdfPCell cellTrackingNo = microSoftFont("Tracking NO.: " + param.getTrackingNo(), new Font(baseFont, 8, Font.NORMAL), 15, true, false);
        cellTrackingNo.setPaddingTop(3);
        cellTrackingNo.disableBorderSide(1);
        tableName3.addCell(cellTrackingNo);
        pdfCell.addElement(tableName3);

        PdfPTable tableName4 = new PdfPTable(4);
        tableName4.setWidthPercentage(102);
        tableName4.setTotalWidth(new float[]{0.25f, 0.25f, 0.25f, 0.25f});
        PdfPCell cell41 = microSoftFont(param.getFirstMileName(), new Font(baseFont, 15, Font.BOLD), 41, true, true);
        PdfPCell cell42 = microSoftFont(param.getLastMileName(), new Font(baseFont, 15, Font.BOLD), 41, true, true);
        PdfPCell cell43 = microSoftFont(param.getGoodsToDeclare(), new Font(baseFont, 33, Font.BOLD), 41, true, true);
        PdfPCell cell44 = microSoftFont(param.getLaneCode(), new Font(baseFont, 15, Font.NORMAL), 41, true, true);
        tableName4.addCell(cell41);
        tableName4.addCell(cell42);
        tableName4.addCell(cell43);
        tableName4.addCell(cell44);
        pdfCell.addElement(tableName4);

        PdfPTable tableName5 = new PdfPTable(2);
        tableName5.setWidthPercentage(102);
        tableName5.setTotalWidth(new float[]{0.5f, 0.5f});
        String buyerUsername = param.getBuyerUsername();
        if (buyerUsername.length() > 12) {
            buyerUsername = buyerUsername.substring(0, 12) + "...";
        }
        PdfPCell cell51 = new PdfPCell();
        PdfPTable tableName51 = new PdfPTable(1);
        tableName51.setWidthPercentage(101);
        PdfPCell cell511 = microSoftFont("ship To: " + buyerUsername, new Font(customizationFont, 10, Font.BOLD), 14, false, false);
        cell511.disableBorderSide(15);
        tableName51.addCell(cell511);
        PdfPCell cell512 = microSoftFont(param.getFullAddress(), new Font(customizationFont, 8, Font.NORMAL), 34, false, false);
        cell512.disableBorderSide(15);
        tableName51.addCell(cell512);
        PdfPCell cell513 = microSoftFont("Tel: " + param.getPhone(), new Font(baseFont, 10, Font.BOLD), 12, false, false);
        cell513.disableBorderSide(15);
        tableName51.addCell(cell513);
        cell51.addElement(tableName51);
        tableName5.addCell(cell51);
        PdfPCell cell52 = microSoftFont(param.getWarehouseAddress(), new Font(baseFont, 10, Font.NORMAL), 60, false, true);
        tableName5.addCell(cell52);
        pdfCell.addElement(tableName5);
        float totalHeight = tableName5.getTotalHeight();
        if (totalHeight > 40F) {
            document.setPageSize(new RectangleReadOnly(lx, ly + totalHeight - 40F));
        }
        PdfPTable tableName6 = new PdfPTable(2);
        tableName6.setWidthPercentage(101);
        tableName6.setTotalWidth(new float[]{0.7f, 0.3f});
        PdfPCell cell61 = new PdfPCell();
        cell61.disableBorderSide(15);
        List<SfPrintOrderParam.Item> items = param.getItems();
        int size = items.size();
        if (items.size() > 5) {
            size = 5;
        }
        for (int i = -1; i < size; i++) {
            PdfPTable tableName61 = new PdfPTable(1);
            tableName61.setWidthPercentage(101);
            PdfPCell cell60;
            if (i == -1) {
                cell60 = microSoftFont("Items:", new Font(baseFont, 9, Font.BOLD), 10, false, true);
            } else {
                SfPrintOrderParam.Item item = items.get(i);
                cell60 = microSoftFont(item.getCategoryName() + " " + item.getCount(), new Font(baseFont, 9, Font.NORMAL), 10, false, true);
            }
            cell60.disableBorderSide(15);
            tableName61.addCell(cell60);
            cell61.addElement(tableName61);
        }
        tableName6.addCell(cell61);
        PdfPCell cell62 = microSoftFont(param.getServiceCode(), new Font(baseFont, 33, Font.BOLD), 40, false, false);
        cell62.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell62.disableBorderSide(15);
        tableName6.addCell(cell62);
        pdfCell.addElement(tableName6);

        tableName.addCell(pdfCell);
        document.add(tableName);
        document.close();
        writer.close();
        return bos.toByteArray();
    }

    /**
     * @param str         字符串
     * @param font        字体
     * @param high        表格高度
     * @param alignCenter 是否水平居中
     * @param alignMiddle 是否垂直居中
     *                    //     * @param haveColor 是否有背景色(灰色)
     * @return
     */
    private static PdfPCell microSoftFont(String str, Font font, int high, boolean alignCenter, boolean alignMiddle) {
        PdfPCell pdfPCell = new PdfPCell(new Phrase(str, font));
        pdfPCell.setMinimumHeight(high);
        // 设置可以居中
        pdfPCell.setUseAscender(true);
        if (alignCenter) {
            // 设置水平居中
            pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        }
        if (alignMiddle) {
            // 设置垂直居中
            pdfPCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        }
//        if (haveColor){
//            //颜色代码 RGB
//            pdfPCell.setBackgroundColor(new BaseColor(217,217,217));
//        }
        return pdfPCell;
    }


    public static void main(String[] args) throws Exception {
        // 简体
//        byte[] pdf = createPdf(param, "STSong-Light", "UniGB-UCS2-H");
        // 繁体
        byte[] pdf = createPdf(param, "MHei-Medium", "UniCNS-UCS2-H");
        //
//        byte[] pdf = createPdf(param, "./tahoma.ttf", BaseFont.IDENTITY_H);

        FileOutputStream fos = new FileOutputStream("/Users/bairong/Desktop/huodai/" + System.currentTimeMillis() + ".pdf");
        fos.write(pdf);
        fos.flush();
        fos.close();
    }

    static SfPrintOrderParam param = new SfPrintOrderParam();

    static {
        param.setOrderId("2002112XHTVDM6");
        param.setTrackingNo("MY204815178174W");
        param.setFirstMileName("MYF3");
        param.setLastMileName("MYL4");
        param.setGoodsToDeclare(true);
        param.setLaneCode("S-MY06");
        param.setBuyerUsername("Arifrahmanhakim");
        param.setFullAddress("Jl. Arifrahmanhakim kp. Bulak.Rt. 02. Rw. 13. No. 05. Kel.Kemirimuka. Kec. Beji. Depok 1. (Dekat masjid almunawaroh) mama Meri, KOTA DEPOK,BEJI, JAWA BARAT, ID, 16423");
//        param.setFullAddress("ST1-1-5 Kelompok Seri Tanjung, Jalan 7/1d Seksyen 7,, Bangi, 43650, Selangor");
//        param.setFullAddress("ภิชญาพร ปานขาว, 66934536249 Y.P.Residence ห้อง 302 41/1 หมู่ 19 ต.บางปะกง อำเภอบางปะกง จังหวัดฉะเชิงเทรา 24130");
//        param.setFullAddress("全家頭屋雙龍店 苗栗縣頭屋鄉尖豐路１３５－２號 店號F008678");
//        param.setFullAddress("Hoan nghênh　Được tiếp đãi ân cần  Lâu quá không gặp");

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
        items.add(item3);
        items.add(item3);
    }
}
