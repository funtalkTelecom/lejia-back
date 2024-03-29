package com.lejia.config;

import com.lejia.dto.Result;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class Utils {

	public static final Logger log = LoggerFactory.getLogger(Utils.class);


	public static String returnResult(Result res){
		return returnJson(res.getCode(), res.getData());
	}

		private static String returnJson(int code, Object data){
			Result res = new Result(code, data);
			JSONObject jobj = JSONObject.fromObject(res);

			return render(jobj.toString(), "text/json;charset=UTF-8");
	}
	
	public static String renderHtml(HttpServletResponse response,String text) {
		return render(text, "text/html;charset=UTF-8");
	}
	public static String renderJson(HttpServletResponse response,String text) {
		return render(text, "text/json;charset=UTF-8");
	}
	
	public static String render(String text, String contentType) {
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		PrintWriter w = null;
		try{
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType(contentType);
			w = response.getWriter();
			w.write(text);
			w.flush();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if(w != null){
				w.close();
				w = null;
			}
		}
		return null;
	}
	/**
	 * 是否以AJAX的方式提交的
	 * @return
	 */
	public final static boolean isAjax(HttpServletRequest request){
		String requestType =request.getHeader("X-Requested-With");
		String app = request.getParameter("__app");
		if ((requestType != null && requestType.equals("XMLHttpRequest")) ||(app != null && "xcx".equals(app))) {

			return true;
		} else {
			return false;
		}
	}
}
