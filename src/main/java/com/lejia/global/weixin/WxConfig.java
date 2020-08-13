package com.lejia.global.weixin;

public class WxConfig {
    public static String appId = null;
    public static String appSecret = null;
    public static String web_site = null;

    public WxConfig() {
    }

    public static void init(String appId, String appSecret, String web_site) {
        appId = appId;
        appSecret = appSecret;
        web_site = web_site;
    }
}