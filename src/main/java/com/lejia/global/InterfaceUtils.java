package com.lejia.global;

import com.lejia.dto.InterfaceResult;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterfaceUtils {
    private static Logger log = LoggerFactory.getLogger(InterfaceUtils.class);

    private static String responseXml ="<?xml version='1.0'?><response><merid>%s</merid><appcode>%s</appcode><serial>%s</serial><sign>%s</sign><rescode>%s</rescode><resdesc>%s</resdesc><platresponse>%s</platresponse></response>";
    private static String requestXml ="<?xml version='1.0'?><request><merid>%s</merid><appcode>%s</appcode><serial>%s</serial><sign>%s</sign><platrequest>%s</platrequest></request>";

    //获取请求报文
    public static String getRequestXml(String merid, String appcode, String serial, String format, String key) {
        String xml = String.format(requestXml, merid, appcode, serial, "%s", format);
        Map<String, Object> map = (Map<String, Object>) parseXmlParam(xml).getMap();
        map.remove("data");
        map.remove("sign");
        String sign = getSign(map, key);
        return String.format(xml, sign);
    }

    //获取返回报文
    public static String getResponseXml(String merid, String appcode, String serial, String respcode, String respdesc, String format, String key) {
        String resultxml = String.format(responseXml, merid, appcode, serial, "%s", respcode, respdesc, format);
        Map<String, Object> map = (Map<String, Object>) parseXmlParam(resultxml).getMap();
        map.remove("data");
        map.remove("sign");
        String sign = getSign(map, key);
        return String.format(resultxml, sign);
    }

    //将入参报文解析成map
    public static InterfaceResult parseXmlParam(String xml) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Pattern p = Pattern.compile("<platrequest>[\\s\\S]*</platrequest>|<platrequest/>|<platresponse>[\\s\\S]*</platresponse>|<platresponse/>");
            Element root = Utils.prase_xml(xml);
            List<Element> list = root.elements();
            for (Element element : list) {
                String name = element.getName();
                if("platresponse".equals(name) || "platrequest".equals(name)) {
                    map.put("data", element);
                    String a = "";
                    Matcher m = p.matcher(xml);
                    if(m.find())  a = m.group();
                    map.put(name, a.replaceAll("<platresponse>","").replaceAll("</platresponse>","")
                            .replaceAll("<platrequest>","").replaceAll("</platrequest>","")
                            .replaceAll("<platrequest/>","").replaceAll("<platresponse/>","")
                            .replaceAll("\\s*",""));
                }else {
                    map.put(element.getName(), element.getTextTrim());
                }
            }
        } catch (Exception e) {
            log.error("接口接收参数格式错误2", e);
            return new InterfaceResult("C0002", "接口接收参数格式错误");
        }
        return new InterfaceResult("00000", map);
    }

    public static String getSign(Map<String, Object> mapParam, String key) {
        mapParam.remove("sign");
        mapParam.remove("data");
        String[] platRequestKeys = CryptTool.properties(mapParam);
        StringBuffer sign= new StringBuffer();
        for (String string : platRequestKeys) {
            sign.append(mapParam.get(string));
        }
        sign.append(key);
//        System.out.println("获取到sign_key:["+StringUtils.join(platRequestKeys,"|")+"]");
//        System.out.println("获取到明文sign:["+sign.toString()+"]");
        return CryptTool.MD5(sign.toString());
    }
}
