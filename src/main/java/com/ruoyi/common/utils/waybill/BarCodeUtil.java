package com.ruoyi.common.utils.waybill;

import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yibo.su
 * @version V1.0
 * @date 2020/1/19 4:21 下午
 */
@Slf4j
public class BarCodeUtil {

    public static byte[] getBarCode(String msg, String barCodeStr) {
        try {
            ByteArrayOutputStream ous = new ByteArrayOutputStream();
            if (StringUtils.isEmpty(msg)) {
                log.error("条形码内容不能为空");
                return null;
            }
            //选择条形码类型(好多类型可供选择)
            Code128Bean bean = new Code128Bean();
            //设置长宽
            final int dpi = 512;
            bean.setBarHeight(6);
            bean.doQuietZone(false);
            if (barCodeStr != null) {
//                bean.setFontSize(18);
                bean.setFontName(barCodeStr);
                bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
            }else{
                bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
            }

            String format = "image/png";
            // 输出流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format,
                    dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            //生成条码
            bean.generateBarcode(canvas, msg);
            canvas.finish();
            return ous.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws IOException {
        byte[] bytes = BarCodeUtil.getBarCode("MY200672698873V","test");
        FileOutputStream fos = new FileOutputStream("/Users/bairong/Desktop/test2.jpg");
        fos.write(bytes);
        fos.flush();
        fos.close();
    }

}
