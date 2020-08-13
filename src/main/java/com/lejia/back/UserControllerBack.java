package com.lejia.back;

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
public class UserControllerBack {

	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private UserService userService;
	@Autowired private PermissionService permissionService;
	@Autowired private CorporationService corporationService;

	public static String RANK_AUTH_CODE = "rankAuthCode";

	@GetMapping("/")
	@Powers({PowerConsts.NOLOGINPOWER})
	public ModelAndView redirectIndex(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/login-index");
		return mv;
	}

    @GetMapping("/index")
    @Powers({PowerConsts.NOLOGINPOWER})
    public ModelAndView index(HttpServletRequest request) {
        return new ModelAndView("inner-index");
    }

    @GetMapping("/login-index")
    @Powers({PowerConsts.NOLOGINPOWER})
    public ModelAndView loginIndex(HttpServletRequest request) {
        return new ModelAndView("login");
    }

    @RequestMapping("/login")
    @Powers({PowerConsts.NOLOGINPOWER})
    public ModelAndView login(User user, HttpServletRequest request, HttpServletResponse response) throws IOException,
			                 NoSuchAlgorithmException{
		int limitResult=ReqLimitUtils.residualReqNum("login",new ReqLimitUtils.ReqLimit("yes","login",1L,5,0L));//每秒请求超过100次后限制访问30分钟
		if(limitResult<=0){
			return new ModelAndView("/login").addObject("errormsg",
					"您登陆过于频繁，请稍后在登。");
		}
		int remMe = NumberUtils.toInt(request.getParameter("rem-me"));
		String loginName=request.getParameter("loginName");
		String pwd=request.getParameter("pwd");
		if(!StringUtils.defaultString(loginName,"").matches("^[a-zA-Z0-9_]{5,20}$")){
			log.warn("帐号不符合规范");
			return new ModelAndView("/login").addObject("errormsg","帐号不符合规范");
		}
		if(StringUtils.defaultString(pwd,"").length() != 24){
			log.warn("密码不符合规范");
			return new ModelAndView("/login").addObject("errormsg","密码不符合规范");
		}
		String validCode = request.getParameter("validCode");
		Object rand = request.getSession().getAttribute(RANK_AUTH_CODE);
		request.getSession().removeAttribute(RANK_AUTH_CODE);
		if (/*StringUtils.isNotEmpty(isValid) && */!String.valueOf(rand).equals(validCode)) {
			return new ModelAndView("/login").addObject("errormsg","验证码错误");
		}
		Map<String, Object> map = userService.login(user.getLoginName(), user.getPassword());
		if (map.get("user") == null) {
			return new ModelAndView("/login").addObject("errormsg",(String) map.get("error"));
		}else{
			User u = (User) map.get("user");
			request.getSession().setAttribute("user", u);
			String client_info = String.format("[登录请求] ip:%s ua:%s ",request.getRemoteAddr(),
					request.getHeader("User-Agent"));
			log.info("用户名："+u.getLoginName()+"|"+client_info);
			CookieHandle ch=new CookieHandle();
			ch.delAdminCookie(request, response);
			if(remMe == 1) {
				ch.addCookie(response, "loginName", user.getLoginName());
			}
			String redirectURL = request.getParameter("redirectURL");
			if(StringUtils.isNotBlank(redirectURL)){
				response.sendRedirect(redirectURL);
				return null;
			}
			return new ModelAndView("redirect:/index");
		}
    	
    }

	/**
	 * 生成验证码
	 * @return
	 */
	@RequestMapping("/auth-code-image")
	@Powers( { PowerConsts.NOLOGINPOWER })
	public void authCodeImage(HttpServletRequest request, HttpServletResponse response){
		new AuthCodeUtil().downLoadAuthCode(request, response);
	}
    
    @GetMapping("/login-out")
    @Powers({PowerConsts.NOLOGINPOWER})
    public ModelAndView loginOut(HttpServletRequest request) {
    	request.getSession().invalidate();
        return new ModelAndView("login");
    }
   
    @RequestMapping("/user/query-user")
    @Powers({PowerConsts.SYSTEMMOUULE_USERLIST_LIST})
    public ModelAndView queryUser(User user, HttpServletRequest request) {
        request.setAttribute("roles",((PageInfo)permissionService.listRole(null).getData()).getList());
        request.setAttribute("corps", ((PageInfo)corporationService.pageCorporation(new Corporation()).getData()).getList());
/*        List angets = ((PageInfo)agentService.pageAgent(new Agent()).getData()).getList();
        request.setAttribute("agents", angets);*/
        return new ModelAndView("user/query-user");
    }
    
    @RequestMapping("/list-user")
    @Powers({PowerConsts.SYSTEMMOUULE_USERLIST_LIST})
    public Result listUser(User user) {
		return userService.pageUser(user);
    }

	@RequestMapping("edit-user-index")
	@Powers({PowerConsts.SYSTEMMOUULE_USERLIST_ADD})
	public Result editUserIndex(User user){
		return  new Result(Result.OK, userService.getUser(user.getId()));
	}

	/**
	 * 添加用户
	 */
	@RequestMapping("/add-user")
	@Powers({PowerConsts.SYSTEMMOUULE_USERLIST_ADD})
	public Result addUser(User user){
		user.setStatus("1");//使用中
		return userService.saveUser(user);
	}

	/**
	 * 冻结、解冻用户
	 * @return
	 */
	@RequestMapping("/freeze-user")
	@Powers({PowerConsts.SYSTEMMOUULE_USERLIST_ADD})
	public Result freezeUser(User user){
		return userService.freezeUser(user);
	}

	/**
	 * 重置密码
	 * @return
	 */
	@RequestMapping("/reset-pwd")
	@Powers({PowerConsts.SYSTEMMOUULE_USERLIST_ADD})
	public Result resetPwd(User user){
		return userService.resetPwd(user);
	}

	/**
	 * 重置密码
	 * @return
	 */
	@RequestMapping("/update-pwd-index")
	@Powers({PowerConsts.SYSTEMMOUULE_UPDATE_PWD})
	public ModelAndView updatePwdIndex(User user){
		return new ModelAndView("user/update-pwd");
	}

	/**
	 * 重置密码
	 * @return
	 */
	@RequestMapping("/update-pwd")
	@Powers({PowerConsts.SYSTEMMOUULE_UPDATE_PWD})
	public Result updatePwd(HttpServletRequest request){
	    String pwd = ObjectUtils.toString(request.getParameter("pwd"));
	    String confrimPwd = request.getParameter("confrimPwd");
	    if(!pwd.equals(confrimPwd)) return new Result(Result.ERROR, "新密码与确认密码不一致");
        String originPwd = request.getParameter("originPwd");
	    return userService.updatePwd(originPwd, pwd);
	}

}
