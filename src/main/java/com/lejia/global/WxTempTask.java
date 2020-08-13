package com.lejia.global;

import com.lejia.pojo.WxTemp;
import net.sf.json.JSONObject;

class WxTempTask implements Runnable {
    private WxTemp wxTemp;
    private String url;

    WxTempTask(WxTemp wxTemp, String url) {
        this.wxTemp = wxTemp;
        this.url = url;
    }

    public void run() {
        try {
            HttpUtils.doHttpPost(this.url, JSONObject.fromObject(this.wxTemp).toString(), "application/json", "UTF-8");
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
