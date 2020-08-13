package com.lejia.global.weixin;

import com.lejia.global.weixin.kit.HttpKit;
import com.lejia.global.weixin.kit.Utils;
import java.util.Map;

public class PayApi {
    private static String sendPayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public PayApi() {
    }

    public static String sendPay(Map<String, String> params) {
        return HttpKit.post(sendPayUrl, (Map)null, Utils.toXml(params));
    }
}
