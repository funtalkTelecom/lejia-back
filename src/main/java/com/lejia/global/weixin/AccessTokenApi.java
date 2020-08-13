package com.lejia.global.weixin;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lejia.global.weixin.kit.HttpKit;
import com.lejia.global.weixin.kit.ParaMap;
import java.io.IOException;
import java.util.Map;

public class AccessTokenApi {
    private static String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    private static AccessToken accessToken;

    public AccessTokenApi() {
    }

    public static AccessToken getAccessToken() {
        if (accessToken != null && accessToken.isAvailable()) {
            return accessToken;
        } else {
            refreshAccessToken();
            return accessToken;
        }
    }

    public static void refreshAccessToken() {
        accessToken = requestAccessToken();
    }

    private static synchronized AccessToken requestAccessToken() {
        AccessToken result = null;

        for(int i = 0; i < 3; ++i) {
            Map<String, String> queryParas = ParaMap.create("appid", WxConfig.appId).put("secret", WxConfig.appSecret).getData();
            String json = HttpKit.get(url, queryParas);
            result = new AccessToken(json);
            if (result.isAvailable()) {
                break;
            }
        }

        return result;
    }

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        ApiConfig ac = new ApiConfig();
        ac.setAppId("wx9583a7ea9d3fd293");
        ac.setAppSecret("c446fe8f5886e95d9c96ef74a61a45e1");
        AccessToken at = getAccessToken();
        if (at.isAvailable()) {
            System.out.println("access_token : " + at.getAccessToken());
        } else {
            System.out.println(at.getErrorCode() + " : " + at.getErrorMsg());
        }

    }
}