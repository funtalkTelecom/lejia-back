package com.lejia.global;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {

    public final static void scale(String srcpath, String result, int width, int height, boolean kar) throws Exception {
        Thumbnails.of(srcpath)
//		        .scale(0.25f)           //按比例
                .size(width, height)    //指定大小
//		        .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("images/watermark.png")), 0.5f) //水印watermark(位置，水印图，透明度)
                .outputQuality(1f)    //输出质量
                .keepAspectRatio(kar) //默认是按照比例缩放的
//		        .rotate(90)             //(角度),正数：顺时针 负数：逆时针
//		        .outputFormat("gif")    //转换格式
//		        .sourceRegion(x, y, width_r, height_r) //指定坐标裁剪(x, y, w, h)
                .toFile(result);
    }

    public final static void scaleByRegion(String srcpath, String result, int width, int height, int x, int y, int width_r, int height_r) throws Exception {
        Thumbnails.of(srcpath)
                .size(width, height)    //指定大小
                .outputQuality(1f)    //输出质量
                .sourceRegion(x, y, width_r, height_r) //指定坐标裁剪(x, y, w, h)
                .toFile(result);
    }

    public final static boolean contains(String suffix) {
        String[] imagesSuffixs={"gif","jpg","jpeg","png","bmp"};
        for(String v:imagesSuffixs){
            if(suffix.equals(v)){
                return true;
            }
        }
        return false;
    }

    public static int getHeight(File file) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
            return bi.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static int getWidth(File file) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
            return bi.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getRatio(File file) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
            double width = bi.getWidth();
            return width/bi.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 本地图片转换成base64字符串
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param imgFile	图片本地路径
     * @return
     */
    public static String imageToBase64ByLocal(String imgFile) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in!=null)in.close();
            }catch (IOException e){
            }
        }
        return new String(Base64.encodeBase64(data));
//        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        // 返回Base64编码过的字节数组字符串
//        return encoder.encode(data);
    }

    public static String imageToBase64WithHeadByLocal(String imgFile) {
        String head="";
        String suffix_v = imgFile.substring(imgFile.lastIndexOf(".") + 1).toLowerCase();
        if(StringUtils.equals("gif",suffix_v))head="data:image/gif;base64,";
        if(StringUtils.equals("png",suffix_v))head="data:image/png;base64,";
        if(StringUtils.equals("jpeg",suffix_v))head="data:image/jpeg;base64,";
        if(StringUtils.equals("jpg",suffix_v))head="data:image/jpeg;base64,";
        if(StringUtils.equals("x-icon",suffix_v))head="data:image/x-icon;base64,";
        return head+imageToBase64ByLocal(imgFile);
    }

    public static void main(String[] args) {


        System.out.println(ImageUtils.imageToBase64WithHeadByLocal("D:\\face-1.jpg"));

//        JSONObject jsonObject=new JSONObject();
        Map jsonObject=new HashMap<>();
        jsonObject.put("appkey","LY_XCX");
        jsonObject.put("reqType","REALNAME1");
        jsonObject.put("image1",ImageUtils.imageToBase64WithHeadByLocal("D:\\face-1.jpg"));
        jsonObject.put("image2",ImageUtils.imageToBase64WithHeadByLocal("D:\\back-1.jpg"));
        jsonObject.put("image3",ImageUtils.imageToBase64WithHeadByLocal("D:\\nohat-1.jpg"));
        String xxxx=JSONObject.fromObject(jsonObject).toString();
//        xxxx=xxxx.replaceAll("\\\\r\\\\n","");
         System.out.println(xxxx);
    }
}
