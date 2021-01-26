package com.ruoyi.common.utils.qrcode;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description:二维码帮助类<p>
 * @author 邓军
 * @version 2016-11-11
 * <p>Copyright:Copyright (c) 2016-11-11<p>
 * <p>Company:华源润通重庆分公司<p>
 */
public class QRCodeUtil {

	/**
	 * <p>Description:生成二维码<p>
	 * @author 邓军
	 * @version 2016-11-11
	 * @modifier 邓军
	 * @modifidate 2016-11-11
	 * <p>modifiContent:首次创建<p>
	 * @param content 二维码内容
	 * @param filePath 二维码图片存放地址
	 * @param width 二维码宽度
	 * @param height 二维码高度
	 */
	public static void encode(String content, String filePath, int width, int height){
		try {
		     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		     Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType,Object>();
		     // 指定纠错等级  
		     hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
		     //设置字符集编码类型  
		     hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		     // 指定编码格式 
		     BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		     File file1 = new File(filePath);
		     if(!file1.exists()){
		    	 file1.mkdirs();
		     }
		     MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
	}

	/**
	 * <p>Description:生成二维码<p>
	 * @author 邓军
	 * @version 2016-11-11
	 * @modifier 邓军
	 * @modifidate 2016-11-11
	 * <p>modifiContent:首次创建<p>
	 * @param content 二维码内容
	 * @param width 二维码宽度
	 * @param height 二维码高度
	 */
	public static BufferedImage encode(String content, int width, int height){
		try {
		     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		     Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType,Object>();
		     // 指定纠错等级  
		     hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
		     //设置字符集编码类型  
		     hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		     // 指定编码格式 
		     BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		     return MatrixToImageWriter.toBufferedImage(bitMatrix);
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		return null;
	}
	
	/**
	 * <p>Description:解码<p>
	 * @author 邓军
	 * @version May 21, 2013
	 * @modifier 邓军
	 * @modifidate May 21, 2013
	 * <p>modifiContent:首次创建<p>
	 * @param path 二维码地址
	 * @return
	 */
	public static String decode(String path) {
		String info="";
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			File file = new File(path);
			BufferedImage image = ImageIO.read(file);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = formatReader.decode(binaryBitmap, hints);
			info = result.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	/**
	 * <p>Description:二维码添加自定义logo<p>
	 * @author 邓军
	 * @version May 21, 2013
	 * @modifier 邓军
	 * @modifidate May 21, 2013
	 * <p>modifiContent:首次创建<p>
	 * @param file 二维码文件
	 * @param logoPath logo图片地址
	 */
	public static void addLogo(File file, String logoPath) {
		BufferedImage image = null;
		try {
			if (new File(logoPath).exists() && file.exists()) {
				BufferedImage logo = ImageIO.read(new File(logoPath));
				image = ImageIO.read(file);
				Graphics2D g = image.createGraphics();
				// logo宽高
				int width = image.getWidth() / 5;
				int height = image.getHeight() / 5;
				// logo起始位置，此目的是为logo居中显示
				int x = (image.getWidth() - width) / 2;
				int y = (image.getHeight() - height) / 2;
				g.drawImage(logo, x, y, width, height, null);
				g.dispose();
				ImageIO.write(image, "jpg", file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Description:二维码添加自定义logo<p>
	 * @author 邓军
	 * @version May 21, 2013
	 * @modifier 邓军
	 * @modifidate May 21, 2013
	 * <p>modifiContent:首次创建<p>
	 * @param filePath 二维码图片地址
	 * @param logoFile logo图片
	 */
	public static void addLogo(String filePath, File logoFile) {
		BufferedImage image = null;
		try {
			File file = new File(filePath);
			if (logoFile.exists() && file.exists()) {
				BufferedImage logo = ImageIO.read(logoFile);
				image = ImageIO.read(file);
				Graphics2D g = image.createGraphics();
				//logo宽高  
				int width = image.getWidth() / 5;
				int height = image.getHeight() / 5;
				//logo起始位置，此目的是为logo居中显示  
				int x = (image.getWidth() - width) / 2;
				int y = (image.getHeight() - height) / 2;
				g.drawImage(logo, x, y, width, height, null);
				g.dispose();
				ImageIO.write(image, "jpg", file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			//生成二维码图片
			QRCodeUtil.encode("weixin://wxpay/bizpayurl?pr=kZSMHSe", 200, 200);
//			QRCodeUtil.addLogo(new File("D:/qrcode.jpg"), "D:/logo2.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
