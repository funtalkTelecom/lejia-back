package com.lejia.global;

import com.lejia.dto.Result;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
    private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public HttpUtils() {
    }

    public static Result doHttpPost(String url, String sendR, String type, String charset) throws Exception {
        log.info("请求url：" + url);
        log.info("请求参数：" + sendR);
        log.info("请求contentType：" + type);
        log.info("请求字符编码：" + charset);
        HttpClient httpClient = new HttpClient();
        HttpMethodBase getMethod = createMethod(url, sendR, type, charset);
        int statusCode = httpClient.executeMethod(getMethod);
        if (statusCode != 200) {
            log.error("Method failed: " + getMethod.getStatusLine() + "\tstatusCode: " + statusCode);
            return new Result(400, "返回码异常[" + statusCode + "]");
        } else {
            log.info("返回结果：" + getMethod.getResponseBodyAsString());
            return new Result(200, getMethod.getResponseBodyAsString());
        }
    }

    private static HttpMethodBase createMethod(String url, String param, String type, String charset) throws UnsupportedEncodingException {
        PostMethod method = null;
        method = new PostMethod(url);
        RequestEntity se = new StringRequestEntity(param, type, charset);
        method.setRequestEntity(se);
        method.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
        method.getParams().setParameter("http.socket.timeout", 30000);
        return method;
    }

    public static HttpResponse doGet(String host, String path, String method, Map<String, String> headers, Map<String, String> querys) throws Exception {
        org.apache.http.client.HttpClient httpClient = wrapClient(host);
        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        Iterator var7 = headers.entrySet().iterator();

        while(var7.hasNext()) {
            Entry<String, String> e = (Entry)var7.next();
            request.addHeader((String)e.getKey(), (String)e.getValue());
        }

        return httpClient.execute(request);
    }

    public static HttpResponse doPost(String host, String path, String method, Map<String, String> headers, Map<String, String> querys, Map<String, String> bodys) throws Exception {
        org.apache.http.client.HttpClient httpClient = wrapClient(host);
        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        Iterator var8 = headers.entrySet().iterator();

        while(var8.hasNext()) {
            Entry<String, String> e = (Entry)var8.next();
            request.addHeader((String)e.getKey(), (String)e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList();
            Iterator var12 = bodys.keySet().iterator();

            while(var12.hasNext()) {
                String key = (String)var12.next();
                nameValuePairList.add(new BasicNameValuePair(key, (String)bodys.get(key)));
            }

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

    public static HttpResponse doPost(String host, String path, String method, Map<String, String> headers, Map<String, String> querys, String body) throws Exception {
        org.apache.http.client.HttpClient httpClient = wrapClient(host);
        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        Iterator var8 = headers.entrySet().iterator();

        while(var8.hasNext()) {
            Entry<String, String> e = (Entry)var8.next();
            request.addHeader((String)e.getKey(), (String)e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    public static HttpResponse doPost(String host, String path, String method, Map<String, String> headers, Map<String, String> querys, byte[] body) throws Exception {
        org.apache.http.client.HttpClient httpClient = wrapClient(host);
        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        Iterator var8 = headers.entrySet().iterator();

        while(var8.hasNext()) {
            Entry<String, String> e = (Entry)var8.next();
            request.addHeader((String)e.getKey(), (String)e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    public static HttpResponse doPut(String host, String path, String method, Map<String, String> headers, Map<String, String> querys, String body) throws Exception {
        org.apache.http.client.HttpClient httpClient = wrapClient(host);
        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        Iterator var8 = headers.entrySet().iterator();

        while(var8.hasNext()) {
            Entry<String, String> e = (Entry)var8.next();
            request.addHeader((String)e.getKey(), (String)e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    public static HttpResponse doPut(String host, String path, String method, Map<String, String> headers, Map<String, String> querys, byte[] body) throws Exception {
        org.apache.http.client.HttpClient httpClient = wrapClient(host);
        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        Iterator var8 = headers.entrySet().iterator();

        while(var8.hasNext()) {
            Entry<String, String> e = (Entry)var8.next();
            request.addHeader((String)e.getKey(), (String)e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    public static HttpResponse doDelete(String host, String path, String method, Map<String, String> headers, Map<String, String> querys) throws Exception {
        org.apache.http.client.HttpClient httpClient = wrapClient(host);
        HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        Iterator var7 = headers.entrySet().iterator();

        while(var7.hasNext()) {
            Entry<String, String> e = (Entry)var7.next();
            request.addHeader((String)e.getKey(), (String)e.getValue());
        }

        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }

        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            Iterator var5 = querys.entrySet().iterator();

            while(var5.hasNext()) {
                Entry<String, String> query = (Entry)var5.next();
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }

                if (StringUtils.isBlank((CharSequence)query.getKey()) && !StringUtils.isBlank((CharSequence)query.getValue())) {
                    sbQuery.append((String)query.getValue());
                }

                if (!StringUtils.isBlank((CharSequence)query.getKey())) {
                    sbQuery.append((String)query.getKey());
                    if (!StringUtils.isBlank((CharSequence)query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode((String)query.getValue(), "utf-8"));
                    }
                }
            }

            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static org.apache.http.client.HttpClient wrapClient(String host) {
        org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }

    private static void sslClient(org.apache.http.client.HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                }
            };
            ctx.init((KeyManager[])null, new TrustManager[]{tm}, (SecureRandom)null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException var6) {
            throw new RuntimeException(var6);
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException(var7);
        }
    }
}

