package com.lejia.security;

import com.lejia.global.SystemParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    protected static final Logger log = LoggerFactory.getLogger(HTMLFilter.class);
    protected static final String FILTER_TAG_CUSTOM ="custom";
    protected static final String FILTER_TAG_LANG ="lang";
    protected static final String FILTER_TAG_ESAPJ ="esapi";
    protected static final String FILTER_TAG_JSOUP ="jsoup";
    protected static final String NO_FILTER_TAG ="#no_filter#";
    private String requestURI;
    private String filterTag;
    private JSONObject securityNoFilterJson;
    /**没被包装过的HttpServletRequest（特殊场景，需要自己过滤）*/
    HttpServletRequest orgRequest;
    private final static JsoupFilter jsoupFilter = new JsoupFilter();
    private final static HTMLFilter htmlFilter = new HTMLFilter();

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
        this.requestURI=request.getRequestURI();
        filterTag=SystemParam.get("security_filter_tag");
        //security_no_filterJson格式：{"路径":["参数1","参数2"],"路径":"#no_filter#"} 如:{"/user-login":["name","pwd"],"/index":["name"],"/index-all":#no_filter#}
        String securityNoFilter=SystemParam.get("security_no_filter");
        if(StringUtils.isNotEmpty(securityNoFilter)){
            this.securityNoFilterJson=JSONObject.fromObject(securityNoFilter);
        }
    }

    /**
     *  处理应急或者确实不需要过滤的参数
     * 忽略明确的参数名  不进行处理的请求name
     * @return true 忽略 false 不忽略，需要过滤
     */
    private boolean checkIgnoreParam(String servletPath,String name){
        if(securityNoFilterJson==null || !securityNoFilterJson.containsKey(servletPath)){return false;}
        Object object=securityNoFilterJson.get(servletPath);
        if(object instanceof String){
            if(StringUtils.equals(String.valueOf(object),NO_FILTER_TAG)){
                return true;
            }
        }
        if(object instanceof JSONArray){
            JSONArray jsonArray=(JSONArray)object;
            if(jsonArray.contains(name)){return true;}
        }
        return false;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        //非json类型，直接返回
        if(!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(super.getHeader(HttpHeaders.CONTENT_TYPE))){
            return super.getInputStream();
        }

        if(checkIgnoreParam(requestURI,null)){
            return super.getInputStream();
        }

        //为空，直接返回
        String json = IOUtils.toString(super.getInputStream(), "utf-8");
        if (StringUtils.isBlank(json)) {
            return super.getInputStream();
        }

        //xss过滤
        json = xssEncode(json);
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes("utf-8"));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    @Override
    public String getParameter(String name) {
        if(checkIgnoreParam(requestURI,name)){
            return super.getParameter(name);
        }
        String value = super.getParameter(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        if(checkIgnoreParam(requestURI,name)){
            return parameters;
        }

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    @Override
    public Map<String,String[]> getParameterMap() {
        Map<String,String[]> map = new LinkedHashMap<>();
        Map<String,String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            if(!checkIgnoreParam(requestURI,key)){
                for (int i = 0; i < values.length; i++) {
                    values[i] = xssEncode(values[i]);
                }
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    private String xssEncode(String input) {
        String res=input;
        if(StringUtils.equals(filterTag,FILTER_TAG_CUSTOM)){
            res=htmlFilter.filter(input);
        }
        if(StringUtils.equals(filterTag,FILTER_TAG_JSOUP)){
            res=jsoupFilter.filter(input);
        }
        /*
        if(StringUtils.equals(filterTag,FILTER_TAG_LANG)){
            res=StringEscapeUtils.escapeHtml(input);
        }
        if(StringUtils.equals(filterTag,FILTER_TAG_ESAPJ)){
            res=ESAPI.encoder().encodeForHTML(input);
        }*/
        log.debug("[xss过滤标签和属性] [原字符串为] : {} [过滤后的字符串为] : {}",input,res);
        return res;
    }

    /**
     * 获取最原始的request
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) request).getOrgRequest();
        }

        return request;
    }

    public void xssTest() {
        List<String> htmlList=new ArrayList<>(100);
        htmlList.add("<p><a href=\"www.test.xhtml\">test</a><a title=\"哈哈哈\" href=\"/aaaa.bbv.com\" href1=\"www.baidu.com\" href2=\"www.baidu.com\" οnclick=\"click()\"></a><script>ss</script><img script=\"xxx\" " +
                "οnclick=function  src=\"https://www.xxx.png\" title=\"\" width=\"100%\" alt=\"\"/>" +
                "<br/></p><p>电饭锅进口量的说法</p><p>————————</p><p><span style=\"text-decoration: line-through;\">大幅度发</span></p>" +
                "<p><em>sd</em></p><p><em><span style=\"text-decoration: underline;\">dsf</span></em></p><p><em>" +
                "<span style=\"border: 1px solid rgb(0, 0, 0);\">撒地方</span></em></p><p><span style=\"color: rgb(255, 0, 0);\">似懂非懂</span><br/></p>" +
                "<p><span style=\"color: rgb(255, 0, 0);\"><strong>撒地方</strong></span></p><p><span style=\"color: rgb(221, 217, 195);\"><br/></span></p>" +
                "<p style=\"text-align: center;\"><span style=\"color: rgb(0, 0, 0); font-size: 20px;\">撒旦法</span></p><p><br/></p>");
        htmlList.add("<input type=\"text\" name=\"address1\" value=\"a\"/><script>alert(document.cookie)</script><!- \">");
        htmlList.add("<script>alert('hello，gaga!');</script>");
        htmlList.add(">\"'><img src=\"javascript.:alert('XSS')\">");
        htmlList.add(">\"'><script>alert('XSS')</script>");
        htmlList.add("<table background='javascript.:alert(([code])'></table>");
        htmlList.add("<object type=text/html data='javascript.:alert(([code]);'></object>");
        htmlList.add("\"+alert('XSS')+\"");
        htmlList.add("'><script>alert(document.cookie)</script>");
        htmlList.add("='><script>alert(document.cookie)</script>");
        htmlList.add("<script>alert(document.cookie)</script>");
        htmlList.add("<script>alert(vulnerable)</script>");
        htmlList.add("<s&#99;ript>alert('XSS')</script>");
        htmlList.add("<img src=\"javas&#99;ript:alert('XSS')\">");
        htmlList.add("%0a%0a<script>alert(\\\"Vulnerable\\\")</script>.jsp");
        //jsoup 无效
        htmlList.add("%3c/a%3e%3cscript%3ealert(%22xss%22)%3c/script%3e");
        //jsoup 无效
        htmlList.add("%3c/title%3e%3cscript%3ealert(%22xss%22)%3c/script%3e");
        //jsoup 无效
        htmlList.add("%3cscript%3ealert(%22xss%22)%3c/script%3e/index.html");
        htmlList.add("<script>alert('Vulnerable')</script>");
        htmlList.add("a.jsp/<script>alert('Vulnerable')</script>");
        htmlList.add("\"><script>alert('Vulnerable')</script>");
        htmlList.add("<IMG SRC=\"javascript.:alert('XSS');\">");
        htmlList.add("<IMG src=\"/javascript.:alert\"('XSS')>");
        htmlList.add("<IMG src=\"/JaVaScRiPt.:alert\"('XSS')>");
        htmlList.add("<IMG src=\"/JaVaScRiPt.:alert\"(&quot;XSS&quot;)>");
        htmlList.add("<IMG SRC=\"jav&#x09;ascript.:alert('XSS');\">");
        htmlList.add("<IMG SRCbackground-image=\"jav&#x0A;ascript.:alert('XSS');\">");
        htmlList.add("<IMG SRC=\"jav&#x0D;ascript.:alert('XSS');\">");
        htmlList.add("\"<IMG src=\"/java\"\\0script.:alert(\\\"XSS\\\")>\";'>out");
        htmlList.add("<IMG SRC=\" javascript.:alert('XSS');\">");
        htmlList.add("<SCRIPT>a=/XSS/alert(a.source)</SCRIPT>");
        htmlList.add("<BODY BACKGROUND=\"javascript.:alert('XSS')\">");
        htmlList.add("<BODY ONLOAD=alert('XSS')>");
        htmlList.add("<IMG DYNSRC=\"javascript.:alert('XSS')\">");
        htmlList.add("<IMG LOWSRC=\"javascript.:alert('XSS')\">");
        htmlList.add("<BGSOUND SRC=\"javascript.:alert('XSS');\">");
        htmlList.add("<br size=\"&{alert('XSS')}\">");
        htmlList.add("<LAYER SRC=\"http://xss.ha.ckers.org/a.js\"></layer>");
        htmlList.add("<LINK REL=\"stylesheet\"HREF=\"javascript.:alert('XSS');\">");
        htmlList.add("<IMG SRC='vbscript.:msgbox(\"XSS\")'>");
        htmlList.add("<META. HTTP-EQUIV=\"refresh\"CONTENT=\"0;url=javascript.:alert('XSS');\">");
        htmlList.add("<IFRAME. src=\"/javascript.:alert\"('XSS')></IFRAME>");
        htmlList.add("<FRAMESET><FRAME. src=\"/javascript.:alert\"('XSS')></FRAME></FRAMESET>");
        htmlList.add("<TABLE BACKGROUND=\"javascript.:alert('XSS')\">");
        //jsoup 无效 因为把style设置为白名单
        htmlList.add("<DIV STYLE=\"background-image: url(javascript.:alert('XSS'))\">");
        //jsoup 无效
        htmlList.add("<DIV STYLE=\"behaviour: url('http://www.how-to-hack.org/exploit.html&#39;);\">");
        //jsoup 无效
        htmlList.add("<DIV STYLE=\"width: expression(alert('XSS'));\">");
        htmlList.add("<STYLE>@im\\port'\\ja\\vasc\\ript:alert(\"XSS\")';</STYLE>");
        htmlList.add("<IMG STYLE='xss:expre\\ssion(alert(\"XSS\"))'>");
        htmlList.add("<STYLE. TYPE=\"text/javascript\">alert('XSS');</STYLE>");
        //jsoup 无效
        htmlList.add("<STYLE. TYPE=\"text/css\">.XSS{background-image:url(\"javascript.:alert('XSS')\");}</STYLE><A CLASS=XSS></A>");
        //jsoup 无效
        htmlList.add("<STYLE. type=\"text/css\">BODY{background:url(\"javascript.:alert('XSS')\")}</STYLE>");
        htmlList.add("<BASE HREF=\"javascript.:alert('XSS');//\">");
        //jsoup 无效
        htmlList.add("getURL(\"javascript.:alert('XSS')\")");
        //jsoup 无效 最后由eval拼凑成执行脚本
        htmlList.add("a=\"get\";b=\"URL\";c=\"javascript.:\";d=\"alert('XSS');\";eval(a+b+c+d);");
        htmlList.add("<XML SRC=\"javascript.:alert('XSS');\">");
        htmlList.add("\"> <BODY NLOAD=\"a();\"><SCRIPT>function a(){alert('XSS');}</SCRIPT><\"");
        htmlList.add("<SCRIPT. SRC=\"http://xss.ha.ckers.org/xss.jpg\"></SCRIPT>");
        htmlList.add("<IMG SRC=\"javascript.:alert('XSS')\"");
        htmlList.add("<SCRIPT. a=\">\"SRC=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>");
        htmlList.add("<SCRIPT.=\">\"SRC=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>");
        htmlList.add("<SCRIPT. a=\">\"''SRC=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>");
        htmlList.add("<SCRIPT.\"a='>'\"SRC=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>");
        htmlList.add("<SCRIPT>document.write(\"<SCRI\");</SCRIPT>PTSRC=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>");
        htmlList.add("<A HREF=http://www.gohttp://www.google.com/ogle.com/>link</A>");
        for (String str:htmlList){
            xssEncode(str);
        }
    }
}
