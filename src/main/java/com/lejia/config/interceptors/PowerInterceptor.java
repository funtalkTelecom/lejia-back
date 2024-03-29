package com.lejia.config.interceptors;

import com.lejia.config.Utils;
import com.lejia.config.annotation.Powers;
import com.lejia.config.aop.syslog.SyslogDao;
import com.lejia.dto.Result;
import com.lejia.global.PowerConsts;
import com.lejia.global.ReqLimitUtils;
import com.lejia.global.SessionUtil;
import com.lejia.global.SystemParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

@Component
public class PowerInterceptor implements HandlerInterceptor {
	@Autowired private SyslogDao syslogDao;

	public final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final String[] no_redirect =new String[]{"flogout"};//不用重定向地址，部分地址重定向会产生死循环
    public static final String COOKIE_NAME = "TT_TOKEN";
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception {
    	request.setAttribute("_t_start_time",System.currentTimeMillis());
		String path=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		boolean _bool=Utils.isAjax(request);
		int limitResult=ReqLimitUtils.residualReqNum("interceptor",new ReqLimitUtils.ReqLimit("yes","plat",1L,100,30*60L));//每秒请求超过100次后限制访问30分钟
		if(limitResult<=0){
		    Utils.returnResult(new Result(Result.ERROR,"抱歉，您的请求过于频繁，请稍候再试!"));
			return false;
		}
		if(!this.isOpenStressTest(handler)) {
			if(_bool)Utils.returnResult(new Result(Result.ERROR,"未开启压力测试"));
			else {
				response.sendRedirect(path+"error-page?errormsg=未开启压力测试");
			}
			return false;
		}
    	boolean _need_login=hasNologin(handler);
		if(_need_login) return true;//有非登陆注解      直接通过
		String redirect = request.getRequestURL() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
		redirect = URLEncoder.encode(redirect, "utf-8");
		String actionName=request.getRequestURI();
		String login_page=path+"login-index"+(ArrayUtils.contains(no_redirect,actionName)?"":"?redirectURL="+redirect);
		String power_info_page=path+"/no-power";
		Object user =request.getSession().getAttribute("user");
		if(user == null) {
			if(_bool)Utils.returnResult(new Result(Result.TIME_OUT, "登录超时或未登录，请登录！"));
			else response.sendRedirect(login_page);
			return false;
		}
		boolean hasPower = hasPower(request,handler);
		if(!hasPower){
			if(_bool)Utils.returnResult(new Result(Result.NOPOWER, "抱歉，权限不足！"));
			else response.sendRedirect(power_info_page);
			return false;
		}
        return true;
    }

	private boolean isOpenStressTest(Object handler) {
		if(handler == null)return true;
		if(!(handler instanceof HandlerMethod))return true;
		HandlerMethod m=(HandlerMethod) handler;
		String name = m.getBean().getClass().getName();
		if(!"com.lejia.controller.StressTestController".equals(name)) return true;
		if("true".equals(SystemParam.get("is_stress_test"))) return true;
		return false;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
    	Long _t_start_time=(Long) request.getAttribute("_t_start_time");
		log.info(String.format("请求[%s]耗时[%s]ms",request.getRequestURI(),(System.currentTimeMillis()-_t_start_time)));
		String param=JSONObject.fromObject(request.getParameterMap()).toString();
		this.syslogDao.saveSysLog("用户请求",request.getRequestURI(),param,(System.currentTimeMillis()-_t_start_time));
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) throws Exception {
    }
    
    /**
     * 是否需要登录
     * @param handler
     * @return	true 不需要，false 需要
     */
    public static final boolean hasNologin(Object handler){
    	if(handler == null)return false;
		if(!(handler instanceof HandlerMethod))return false;
		HandlerMethod m=(HandlerMethod) handler;
		Powers powers =m.getMethodAnnotation(Powers.class);
		if(powers == null) return false;
		PowerConsts[] powerConsts=powers.value();
		for (PowerConsts pc :powerConsts) {
    		if(pc.getId()==PowerConsts.NOLOGINPOWER.getId())return true;
		}
		return false;
	}
	
//	当前用户是否有权限操作此方法
	public static final boolean hasPower(HttpServletRequest request,Object handler){
		Map<Integer, Object> userPower = SessionUtil.getPower(request);
		return hasPower(request,handler,userPower);
	}
	//	当前用户是否有权限操作此方法
	public static final boolean hasPower(HttpServletRequest request,Object handler,Map<Integer, Object> userPower){
//		if(SessionUtil.isSuperAdmin())return true;//是超级管理员
		if(handler == null)return false;
		if(!(handler instanceof HandlerMethod))return false;
		HandlerMethod m=(HandlerMethod) handler;
		Powers powers =m.getMethodAnnotation(Powers.class);
		if(powers == null)return true;
		PowerConsts[] powerConsts=powers.value();
		Set<Integer> user_power_set=userPower.keySet();
		for (PowerConsts pc :powerConsts) {
			if(user_power_set.contains(pc.getId()))return true;
			if(pc.getId()==PowerConsts.NOLOGINPOWER.getId())return true;
			if(pc.getId()==PowerConsts.NOPOWER.getId())return true;//powerArray.length == 1
		}
		return false;
	}

}