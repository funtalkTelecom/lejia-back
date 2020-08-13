package com.lejia.global;

import com.lejia.pojo.WxTemp;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Messager {
    private static final Logger log = LoggerFactory.getLogger(Messager.class);
    private static ThreadPoolExecutor pool = null;
    private static final String phoneMatcher = "^1\\d{10,10}$";

    public Messager() {
    }

    public static final synchronized void init() {
        pool = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(600), new AbortPolicy());
    }

    public static final void send(String phoneNum, String message, String url) {
        try {
            if (StringUtils.isBlank(url)) {
                log.error("短信接口地址为空");
                return;
            }

            if (StringUtils.isBlank(phoneNum)) {
                log.error("要短信通知的手机号码为空");
                return;
            }

            String[] phoneNums = phoneNum.split(",");
            String[] var4 = phoneNums;
            int var5 = phoneNums.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String phone = var4[var6];
                phone = phone.replaceAll(" ", "");
                if (!phone.matches("^1\\d{10,10}$")) {
                    log.error("手机号码【" + phoneNum + "】格式不对");
                    return;
                }

                if (StringUtils.isBlank(message)) {
                    log.error("短信内容为空");
                    return;
                }

                log.info("向手机号【" + phone + "】发送信息【" + message + "】");
                message = URLEncoder.encode(message, "UTF-8");
                getPool().execute(new MessageTask(phone, message, url));
            }
        } catch (Exception var8) {
            log.error("发送短信异常，短信内容【" + message + "】", var8);
        }

    }

    private static ThreadPoolExecutor getPool() {
        if (pool == null) {
            init();
        }

        return pool;
    }

    public static final void sendSmsTemp(String phoneNum, Map message, Integer templeatId, String url) {
        try {
            if (StringUtils.isBlank(url)) {
                log.error("短信模板接口地址为空");
                return;
            }

            if (StringUtils.isBlank(phoneNum)) {
                log.error("要短信通知的手机号码为空");
                return;
            }

            if (templeatId == null || templeatId == 0) {
                log.error("短信模板id错误");
                return;
            }

            String[] phoneNums = phoneNum.split(",");
            if (message == null) {
                log.info("无模板参数");
            } else {
                log.info("短信参数:" + message.toString());
            }

            log.info("短信模板id:" + templeatId);
            String[] var5 = phoneNums;
            int var6 = phoneNums.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String phone = var5[var7];
                phone = phone.replaceAll(" ", "");
                if (!phone.matches("^1\\d{10,10}$")) {
                    log.error("手机号码【" + phoneNum + "】格式不对");
                    return;
                }

                log.info("向手机号【" + phone + "】发送信息");
                getPool().execute(new SmsTempTask(phone, message, templeatId, url));
            }
        } catch (Exception var9) {
            log.error("发送短信异常，短信内容【" + message + "】", var9);
        }

    }

    public static final void sendWxTemp(WxTemp wxTemp, String url) {
        try {
            if (StringUtils.isBlank(url)) {
                log.error("微信模板接口地址为空");
                return;
            }

            if (StringUtils.isBlank(wxTemp.getTouser())) {
                log.error("openid为空");
                return;
            }

            if (StringUtils.isBlank(wxTemp.getTemplateId())) {
                log.error("模板id为空");
                return;
            }

            if (StringUtils.isBlank(wxTemp.getAppId())) {
                log.error("appid为空");
                return;
            }

            getPool().execute(new WxTempTask(wxTemp, url));
        } catch (Exception var3) {
            log.error("发送微信模板异常", var3);
        }

    }

    public static final void close() {
        if (pool != null) {
            pool.shutdown();
        }

    }
}
