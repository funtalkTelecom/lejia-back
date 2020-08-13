package com.lejia.service;

import com.lejia.dto.Result;
import com.lejia.global.*;
import com.lejia.mapper.*;
import com.lejia.pojo.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WxinService extends BaseService {
	
	@Autowired SessionUtil sessionUtil;
	@Autowired private ApiSessionUtil apiSessionUtil;


	public Result isH5GetAccessToken(String code,String appId) {
		String appsecret=SystemParam.get(appId);
		String grant_type = "authorization_code";//授权（必填）
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
		String params = "appid=" + appId + "&secret=" + appsecret + "&code=" + code + "&grant_type=" + grant_type;//请求参数
		String Openid = null;
		String accessToken = null;
		Map<String, Object> map = new HashMap<>();
		try {
			String data = HttpUtil.get(requestUrl, params);
			if(StringUtils.isEmpty(data))return new Result(Result.ERROR,"请求错误");
			JSONObject json = JSONObject.fromObject(data);
			Openid=String.valueOf(json.get("openid"));//用户的唯一标识（openid）
			accessToken=String.valueOf(json.get("access_token"));//用户的唯一标识（access_token）
			if(StringUtils.isEmpty(Openid))return new Result(Result.ERROR,"无法获取信息");
			Result s = this.getUserinfo(accessToken,Openid);
			map.put("Openid",Openid);
			map.put("accessToken",accessToken);
			map.put("rawData",s.getData());
			return new Result(Result.OK,map);
		} catch (Exception e) {
			log.error("获取用户Openid失败",e);
		}
		return new Result(Result.ERROR,"无法获取accessToken");
	}

	public Result getUserinfo(String accessToken,String openId) {
		Map<String, Object> map = new HashMap<>();
		String lang = "zh_CN";//授权（必填）
		String requestUrl = "https://api.weixin.qq.com/sns/userinfo";
		String params = "access_token=" + accessToken + "&openid=" + openId + "&lang=" + lang;//请求参数
		String data = HttpUtil.get(requestUrl, params);
		if(StringUtils.isEmpty(data))return new Result(Result.ERROR,"请求错误");
		JSONObject json = JSONObject.fromObject(data);
		String nickname=String.valueOf(json.get("nickname"));
		String sex=String.valueOf(json.get("sex"));
		String province=String.valueOf(json.get("province"));
		String city=String.valueOf(json.get("city"));
		String country=String.valueOf(json.get("country"));
		String headimgurl=String.valueOf(json.get("headimgurl"));
		String unionid=String.valueOf(json.get("unionid"));
		map.put("nickname",nickname);
		map.put("sex",sex);
		map.put("province",province);
		map.put("city",city);
		map.put("country",country);
		map.put("headimgurl",headimgurl);
		map.put("unionid",unionid);

		return new Result(Result.ERROR,map);
	}

}
