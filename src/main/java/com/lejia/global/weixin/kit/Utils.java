package com.lejia.global.weixin.kit;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class Utils {
    static final int BUFFER_SIZE = 4096;

    public Utils() {
    }

    public static String toXml(Map<String, String> params) {
        Document document = DocumentHelper.createDocument();
        Element xml = document.addElement("xml");
        Iterator iterator = params.keySet().iterator();

        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            String value = (String)params.get(key);
            xml.addElement(key).addCDATA(value);
        }

        return xml.asXML();
    }

    public static Map<String, String> simpleXmlToMap(String xmlStr) {
        XmlHelper xmlHelper = XmlHelper.of(xmlStr);
        return xmlHelper.toMap();
    }

    public static Map<String, String> parserXmlElement(String xml) {
        Map<String, String> _map = new HashMap();
        SAXReader reader = new SAXReader();
        StringReader sr = new StringReader(xml);
        InputSource is = new InputSource(sr);
        Document document = null;

        try {
            document = reader.read(is);
        } catch (DocumentException var10) {
            var10.printStackTrace();
        }

        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        Iterator var8 = elementList.iterator();

        while(var8.hasNext()) {
            Element element = (Element)var8.next();
            _map.put(element.getName(), element.getText());
        }

        return _map;
    }

    public static Map<String, Object> xmlToMap(String xmlstr) {
        Document doc = null;

        try {
            doc = DocumentHelper.parseText(xmlstr);
        } catch (DocumentException var4) {
            var4.printStackTrace();
        }

        Element rootElement = doc.getRootElement();
        Map<String, Object> mapXml = new HashMap();
        elementToMap(mapXml, rootElement);
        return mapXml;
    }

    public static void elementToMap(Map<String, Object> map, Element rootElement) {
        List<Element> elements = rootElement.elements();
        new HashMap();
        Iterator var4 = elements.iterator();

        while(true) {
            while(var4.hasNext()) {
                Element element = (Element)var4.next();
                List<Element> es = element.elements();
                if (es.size() > 0) {
                    ArrayList<Map> list = new ArrayList();
                    Iterator var8 = es.iterator();

                    while(var8.hasNext()) {
                        Element e = (Element)var8.next();
                        elementChildToList(list, e);
                    }

                    map.put(element.getName(), list);
                } else {
                    map.put(element.getName(), element.getText());
                }
            }

            return;
        }
    }

    public static void elementChildToList(ArrayList<Map> arrayList, Element rootElement) {
        List<Element> elements = rootElement.elements();
        if (elements.size() > 0) {
            ArrayList<Map> list = new ArrayList();
            Map<String, Object> sameTempMap = new HashMap();
            Iterator var5 = elements.iterator();

            while(var5.hasNext()) {
                Element element = (Element)var5.next();
                elementChildToList(list, element);
                sameTempMap.put(element.getName(), element.getText());
            }

            arrayList.add(sameTempMap);
        }

    }

    public static String formatSignMap(Map<String, String> map) {
        Iterator<String> it = map.keySet().iterator();
        String[] array = new String[map.size()];

        for(int var3 = 0; it.hasNext(); array[var3++] = (String)it.next()) {
            ;
        }

        Arrays.sort(array, String.CASE_INSENSITIVE_ORDER);
        StringBuffer sb = new StringBuffer(200);

        for(int i = 0; i < array.length; ++i) {
            if (!StringUtils.isBlank((String)map.get(array[i]))) {
                sb.append(array[i] + "=" + (String)map.get(array[i]));
                if (i != array.length - 1) {
                    sb.append("&");
                }
            }
        }

        System.out.println("------------------" + sb.toString());
        return sb.toString();
    }

    public static String sign(String sign_temp, String key) {
        sign_temp = sign_temp + "&key=" + key;
        String sign = CryptTool.MD5(sign_temp);
        return sign;
    }

    public static String xml2JSON(String xml) {
        return (new XMLSerializer()).read(xml).toString();
    }

    public static String json2XML(String json) {
        JSONObject jobj = JSONObject.fromObject(json);
        String xml = (new XMLSerializer()).write(jobj);
        return xml;
    }

    public static String InputStreamTOString(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        boolean var3 = true;

        int count;
        while((count = in.read(data, 0, 4096)) != -1) {
            outStream.write(data, 0, count);
        }

        return new String(outStream.toByteArray(), "UTF-8");
    }
}

