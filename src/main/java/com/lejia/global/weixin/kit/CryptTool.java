package com.lejia.global.weixin.kit;

import java.security.MessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptTool {
    protected static Logger log = LoggerFactory.getLogger(HttpKit.class);

    public CryptTool() {
    }

    public static final String MD5(String s) {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes("UTF-8"));
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            result = buf.toString().toUpperCase();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(MD5("FX1703210201073247754746E4145"));
    }

    public static String getSign(String serial, String key) {
        try {
            return MD5(serial + key);
        } catch (Exception var3) {
            log.error("获取密文异常", var3);
            return "9999";
        }
    }
}

