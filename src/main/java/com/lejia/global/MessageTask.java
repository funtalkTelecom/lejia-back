package com.lejia.global;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class MessageTask implements Runnable {
    private String phone;
    private String message;
    private String path;

    MessageTask(String phone, String message, String path) {
        this.phone = phone;
        this.message = message;
        this.path = path;
    }

    public void run() {
        InputStream inStream = null;

        try {
            URL url = new URL(this.path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            OutputStream out = conn.getOutputStream();
            out.write(("&method=sendSms&destNum=" + this.phone + "&message=" + this.message).getBytes());
            out.flush();
            out.close();
            inStream = conn.getInputStream();
            readInputStream(inStream);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                    inStream = null;
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

    }

    private static final byte[] readInputStream(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = null;

        try {
            byte[] buffer = new byte[1024];
            boolean var4 = false;

            int len;
            while((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }

            data = outStream.toByteArray();
            outStream.close();
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                    inStream = null;
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return data;
    }
}
