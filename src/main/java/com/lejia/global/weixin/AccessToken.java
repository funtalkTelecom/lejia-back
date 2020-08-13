package com.lejia.global.weixin;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class AccessToken {
    private String access_token;
    private Integer expires_in;
    private Integer errcode;
    private String errmsg;
    private Long expiredTime;
    private String json;

    public AccessToken(String jsonStr) {
        this.json = jsonStr;

        try {
            Map map = (Map)(new ObjectMapper()).readValue(jsonStr, Map.class);
            this.access_token = (String)map.get("access_token");
            this.expires_in = (Integer)map.get("expires_in");
            this.errcode = (Integer)map.get("errcode");
            this.errmsg = (String)map.get("errmsg");
            if (this.expires_in != null) {
                this.expiredTime = System.currentTimeMillis() + (long)((this.expires_in - 5) * 1000);
            }

        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public String getJson() {
        return this.json;
    }

    public boolean isAvailable() {
        if (this.expiredTime == null) {
            return false;
        } else if (this.errcode != null) {
            return false;
        } else if (this.expiredTime < System.currentTimeMillis()) {
            return false;
        } else {
            return this.access_token != null;
        }
    }

    public String getAccessToken() {
        return this.access_token;
    }

    public Integer getExpiresIn() {
        return this.expires_in;
    }

    public Integer getErrorCode() {
        return this.errcode;
    }

    public String getErrorMsg() {
        if (this.errcode != null) {
            String result = ReturnCode.get(this.errcode);
            if (result != null) {
                return result;
            }
        }

        return this.errmsg;
    }
}
