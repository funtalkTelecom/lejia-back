package com.lejia.pojo;

import java.util.List;

public class WxTemp {
    private String touser;
    private String appId;
    private String templateId;
    private String url;
    private String serviceType;
    private String serviceId;
    private String project;
    private List<WxTempData> wxTempData;

    public WxTemp() {
    }

    public WxTemp(String touser, String appId, String templateId, String url, String serviceType, String serviceId, String project, List<WxTempData> wxTempData) {
        this.touser = touser;
        this.appId = appId;
        this.templateId = templateId;
        this.url = url;
        this.serviceType = serviceType;
        this.serviceId = serviceId;
        this.project = project;
        this.wxTempData = wxTempData;
    }

    public String getTouser() {
        return this.touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<WxTempData> getWxTempData() {
        return this.wxTempData;
    }

    public void setWxTempData(List<WxTempData> wxTempData) {
        this.wxTempData = wxTempData;
    }
}

