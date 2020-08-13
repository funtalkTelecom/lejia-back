package com.lejia.config.aop.syslog;

import net.sf.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired private SyslogDao syslogDao;

	@Pointcut("@annotation(com.lejia.config.aop.syslog.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		String params =null;
		String operation =null;
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		SysLog slog = method.getAnnotation(SysLog.class);
		if(slog != null)operation=slog.name();//注解上的描述

		String className = point.getTarget().getClass().getName() + "." + signature.getName() + "()";
		//请求的参数
		Object[] args = point.getArgs();
		try{
//			params =JSONArray.fromObject(args).toString();
			params =JSONObject.fromObject(args).toString();
		}catch (Exception e){
		}
		//保存日志
		syslogDao.saveSysLog(operation,className,params,time);

		return result;
	}


}
