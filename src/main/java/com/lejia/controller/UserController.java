package com.lejia.controller;

import com.github.pagehelper.PageInfo;
import com.lejia.config.annotation.Powers;
import com.lejia.dto.Result;
import com.lejia.global.AuthCodeUtil;
import com.lejia.global.CookieHandle;
import com.lejia.global.PowerConsts;
import com.lejia.global.ReqLimitUtils;
import com.lejia.pojo.Corporation;
import com.lejia.pojo.User;
import com.lejia.service.CorporationService;
import com.lejia.service.PermissionService;
import com.lejia.service.UserService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class UserController {

	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private UserService userService;
	@Autowired private PermissionService permissionService;
	@Autowired private CorporationService corporationService;

	public static String RANK_AUTH_CODE = "rankAuthCode";

	@RequestMapping("/admin/login")
	@Powers({PowerConsts.NOLOGINPOWER})
	public Result adminLogin(User user, HttpServletRequest request, HttpServletResponse response) throws IOException,
			NoSuchAlgorithmException{

		int limitResult=ReqLimitUtils.residualReqNum("login",
				     new ReqLimitUtils.ReqLimit("yes","login",1L,6,0L));

		if(limitResult<=0){
			return new Result(Result.WARN,"登录频繁,已限制登录,请稍后再试");
		}
		String userName=request.getParameter("loginName");
		String passWord=request.getParameter("pwd");

		Map<String, Object> map = userService.login(user.getLoginName(), user.getPassword());

		if (map.get("user") == null) {
			return new Result(Result.WARN,"没有查询到用户");
		}else{

			return new  Result(Result.OK,"登录成功");
		}

	}



}
