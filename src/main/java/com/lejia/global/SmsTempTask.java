package com.lejia.global;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

class SmsTempTask implements Runnable {
    private String phone;
    private Map messageMap;
    private Integer templeatId;
    private String path;

    SmsTempTask(String phone, Map messageMap, Integer templeatId, String path) {
        this.phone = phone;
        this.messageMap = messageMap;
        this.templeatId = templeatId;
        this.path = path;
    }

    public void run() {
        InputStream inStream = null;

        try {
            String message = "";
            if (this.messageMap != null) {
                message = URLEncoder.encode(MapJsonUtils.parseMap2JSON(this.messageMap), "UTF-8");
            }

            URL url = new URL(this.path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            OutputStream out = conn.getOutputStream();
            out.write(("&destNum=" + this.phone + "&message=" + message + "&templeatId=" + this.templeatId).getBytes());
            out.flush();
            out.close();
            inStream = conn.getInputStream();
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                    inStream = null;
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

    }
}

