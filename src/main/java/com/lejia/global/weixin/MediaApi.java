package com.lejia.global.weixin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaApi {
    private static Logger logger = LoggerFactory.getLogger(MediaApi.class);
    public static String downLoadMediaApi = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    public MediaApi() {
    }

    public static String getMediaUrl(String media_id) {
        String token = AccessTokenApi.getAccessToken().getAccessToken();
        token = downLoadMediaApi.replace("ACCESS_TOKEN", token).replace("MEDIA_ID", media_id);
        return token;
    }

    public static String downLoadMedia(String media_id, String requestMethod, String realDir) {
        String filePath = null;
        String requestUrl = getMediaUrl(media_id);

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            realDir = realDir + File.separator + "DownLoad";
            File file = new File(realDir);
            if (!file.exists() && !file.isDirectory()) {
                logger.info("//不存在");
                file.mkdir();
            } else {
                logger.info("//目录存在");
            }

            String disposition = conn.getHeaderField("Content-Disposition");
            String fileName = null;
            if (disposition != null && !"".equals(disposition)) {
                fileName = disposition.split(";")[1].split("=")[1].replaceAll("\"", "");
            }

            filePath = realDir + File.separator + fileName;
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] buf = new byte[8096];
            boolean var13 = false;

            int size;
            while((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }

            fos.close();
            bis.close();
            conn.disconnect();
            logger.info("下载媒体文件成功，filePath=" + filePath);
        } catch (Exception var14) {
            logger.error("下载微信服务器多媒体文件失败!");
            filePath = null;
        }

        return filePath;
    }

    public static void uploadMedia(String serviceURL, String fileExt, String mediaFileUrl) {
        String boundary = "---------";

        try {
            URL uploadUrl = new URL(serviceURL);
            HttpURLConnection uploadConn = (HttpURLConnection)uploadUrl.openConnection();
            uploadConn.setDoOutput(true);
            uploadConn.setDoInput(true);
            uploadConn.setRequestMethod("POST");
            uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            OutputStream outputStream = uploadConn.getOutputStream();
            URL mediaUrl = new URL(mediaFileUrl);
            HttpURLConnection meidaConn = (HttpURLConnection)mediaUrl.openConnection();
            meidaConn.setDoOutput(true);
            meidaConn.setRequestMethod("GET");
            String contentType = meidaConn.getHeaderField("Content-Type");
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n", fileExt).getBytes());
            outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());
            BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
            byte[] buf = new byte[8096];
            boolean var12 = false;

            int size;
            while((size = bis.read(buf)) != -1) {
                outputStream.write(buf, 0, size);
            }

            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            outputStream.close();
            bis.close();
            meidaConn.disconnect();
            InputStream inputStream = uploadConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String str = null;

            while((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            uploadConn.disconnect();
            JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
            System.out.println("打印测试结果__________________________________________" + jsonObject);
        } catch (Exception var19) {
            String error = String.format("上传媒体文件失败：%s", var19);
            System.out.println(error);
        }

    }
}
