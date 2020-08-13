package com.lejia.global.weixin;

import com.lejia.global.weixin.kit.HttpKit;
import com.lejia.global.weixin.kit.Utils;
import java.io.InputStream;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuApi {
    private static Logger log = LoggerFactory.getLogger(MenuApi.class);
    private static String getMenu = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
    private static String createMenu = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
    private static String delMenu = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";

    public MenuApi() {
    }

    public static ApiResult getMenu() {
        String jsonResult = HttpKit.get(getMenu + AccessTokenApi.getAccessToken().getAccessToken());
        log.info("查询菜单操作 ______________________");
        return new ApiResult(jsonResult);
    }

    public static ApiResult createMenu(String jsonStr) {
        String jsonResult = HttpKit.post(createMenu + AccessTokenApi.getAccessToken().getAccessToken(), jsonStr);
        log.info("创建菜单操作 ______________________");
        return new ApiResult(jsonResult);
    }

    public static ApiResult delMenu() {
        String jsonResult = HttpKit.get(delMenu + AccessTokenApi.getAccessToken().getAccessToken());
        log.info("删除菜单操作 ______________________");
        return new ApiResult(jsonResult);
    }

    public static String getMenuJson() throws Exception {
        String jsonMenuString = "";
        InputStream in = MenuApi.class.getClassLoader().getResourceAsStream("Menu.xml");
        jsonMenuString = Utils.InputStreamTOString(in);
        jsonMenuString = Utils.xml2JSON(jsonMenuString);
        jsonMenuString = "{\"button\":" + jsonMenuString.substring(1, jsonMenuString.length() - 1) + "}";
        return jsonMenuString;
    }

    public static String json2XML(String json) {
        JSONObject jobj = JSONObject.fromObject(json);
        String xml = (new XMLSerializer()).write(jobj);
        return xml;
    }

    public static void main(String[] args) {
        String STR_JSON = "{'button':[{'name':'我的E购','sub_button':[{'key':'31','name':'账号绑定','type':'click'},{'key':'32','name':'订单查询','type':'click'},{'key':'33','name':'E购商城','type':'click'},{'name':'活动信息','type':'view','url':'http://www.egt365.com/WeiXin365/activeMsg.htm'}]},{'name':'信息查询','sub_button':[{'key':'22','name':'平台公告','type':'click'},{'key':'repair','name':'送修地址','type':'click'},{'key':'phone','name':'客服热线','type':'click'}]},{'key':'SZPrice','name':'深圳报价','type':'click'}]}";
        String xml = json2XML(STR_JSON);
        System.out.println("xml = " + xml);
        String json = Utils.xml2JSON(xml);
        System.out.println("json=" + json);
    }
}
