package com.lejia.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SmsBase {
    @JsonIgnore
    private String smsTempUrl;
    @JsonIgnore
    private String smsUrl;
    @JsonIgnore
    private String wxTempUrl;

    public SmsBase(String smsTempUrl, String smsUrl, String wxTempUrl) {
        this.smsTempUrl = smsTempUrl;
        this.smsUrl = smsUrl;
        this.wxTempUrl = wxTempUrl;
    }

    public String getSmsTempUrl() {
        return this.smsTempUrl;
    }

    public void setSmsTempUrl(String smsTempUrl) {
        this.smsTempUrl = smsTempUrl;
    }

    public String getSmsUrl() {
        return this.smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getWxTempUrl() {
        return this.wxTempUrl;
    }

    public void setWxTempUrl(String wxTempUrl) {
        this.wxTempUrl = wxTempUrl;
    }
}