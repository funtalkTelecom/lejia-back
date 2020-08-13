package com.lejia.pojo;

public class OrderWithBLOBs extends Order {
    private String address;

    private String shipData;

    private String extra;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getShipData() {
        return shipData;
    }

    public void setShipData(String shipData) {
        this.shipData = shipData == null ? null : shipData.trim();
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra == null ? null : extra.trim();
    }
}