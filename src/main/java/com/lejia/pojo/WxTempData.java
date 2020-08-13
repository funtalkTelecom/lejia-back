package com.lejia.pojo;

public class WxTempData {
    private String title;
    private String value;
    private String color;

    public WxTempData(String title, String value, String color) {
        this.title = title;
        this.value = value;
        this.color = color;
    }

    public WxTempData(String title, String value) {
        this.title = title;
        this.value = value;
        this.color = "#333";
    }

    public WxTempData() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

