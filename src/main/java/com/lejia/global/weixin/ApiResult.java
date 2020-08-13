package com.lejia.global.weixin;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiResult {
    private Map<String, Object> attrs;
    private String json;

    public ApiResult(String jsonStr) {
        this.json = jsonStr;

        try {
            Map<String, Object> temp = (Map)(new ObjectMapper()).readValue(jsonStr, Map.class);
            this.attrs = temp;
            this.refreshAccessTokenIfInvalid();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public ApiResult(int code, String msg) {
        Map<String, Object> temp = new HashMap();
        temp.put("errcode", code);
        temp.put("errmsg", msg);
        this.attrs = temp;
    }

    private void refreshAccessTokenIfInvalid() {
        if (this.isAccessTokenInvalid()) {
            AccessTokenApi.refreshAccessToken();
        }

    }

    public String getJson() {
        return this.json;
    }

    public String toString() {
        return this.getJson();
    }

    public boolean isSucceed() {
        Integer errorCode = this.getErrorCode();
        return errorCode == null || errorCode == 0;
    }

    public Integer getErrorCode() {
        return (Integer)this.attrs.get("errcode");
    }

    public String getErrorMsg() {
        Integer errorCode = this.getErrorCode();
        if (errorCode != null) {
            String result = ReturnCode.get(errorCode);
            if (result != null) {
                return result;
            }
        }

        return (String)this.attrs.get("errmsg");
    }

    public <T> T get(String name) { return (T)this.attrs.get(name); }

    public String getStr(String name) {
        return (String)this.attrs.get(name);
    }

    public Integer getInt(String name) {
        return (Integer)this.attrs.get(name);
    }

    public Long getLong(String name) {
        return (Long)this.attrs.get(name);
    }

    public BigInteger getBigInteger(String name) {
        return (BigInteger)this.attrs.get(name);
    }

    public Double getDouble(String name) {
        return (Double)this.attrs.get(name);
    }

    public BigDecimal getBigDecimal(String name) {
        return (BigDecimal)this.attrs.get(name);
    }

    public Boolean getBoolean(String name) {
        return (Boolean)this.attrs.get(name);
    }

    public List getList(String name) {
        return (List)this.attrs.get(name);
    }

    public Map getMap(String name) {
        return (Map)this.attrs.get(name);
    }

    public boolean isAccessTokenInvalid() {
        Integer ec = this.getErrorCode();
        return ec != null && (ec == 40001 || ec == 42001 || ec == 42002 || ec == 40014);
    }
}