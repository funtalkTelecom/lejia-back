package com.lejia.controller;

import com.lejia.config.annotation.Powers;
import com.lejia.dto.Result;
import com.lejia.global.LockUtils;
import com.lejia.global.PowerConsts;
import com.lejia.service.ConsumerService;
import com.lejia.service.WxinService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WxinController {

	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired ConsumerService consumerService;
	@Autowired
	WxinService wxinService;
    @GetMapping("/get_open_id")
    @Powers({PowerConsts.NOLOGINPOWER})
	public Result getOpenid(@RequestParam(value="getcode",required=false)String getcode,@RequestParam(value="userId",required=false) String userId
			,@RequestParam(value="appId",required=false) String appId) {
    	if(StringUtils.isEmpty(appId)) return new Result(Result.ERROR,"授权参数异常");
		Result result = this.consumerService.getOpenId(getcode,appId);
		int userid =NumberUtils.toInt(userId);
		if(result.getCode()==Result.OK){
			String openid  =String.valueOf(result.getData());
			if(!LockUtils.tryLock("goi"+openid)) return new Result(Result.ERROR,"授权异常");
			try{
				result= consumerService.isOpenid(openid,userid,appId);
			}finally {
				LockUtils.unLock("goi"+openid);
			}
		}
		return result;
	}

    @GetMapping("/check_union_id")
    @Powers({PowerConsts.NOLOGINPOWER})
	public Result checkUnionId(@RequestParam(value="appId",required=false) String appId) {
		return consumerService.checkUnionId(appId);
	}
    @GetMapping("/get_union_id")
    @Powers({PowerConsts.NOLOGINPOWER})
	public Result checkUnionId(@RequestParam(value="encryptedData",required=false) String encryptedData,
							   @RequestParam(value="iv",required=false) String iv,
							   @RequestParam(value="signature",required=false) String signature,
							   @RequestParam(value="rawData",required=false) String rawData,
							   @RequestParam(value="appId",required=false) String appId
	) throws Exception {
		return consumerService.updateUnionId(encryptedData,iv,signature,rawData,appId);
	}


	@GetMapping("get_access_token")
	@Powers({PowerConsts.NOLOGINPOWER})
	public Result getAccessToken(@RequestParam(value="getcode",required=false)String getcode,@RequestParam(value="userId",required=false) String userId
			,@RequestParam(value="appId",required=false) String appId) {
		int userid =NumberUtils.toInt(userId);
		if(StringUtils.isEmpty(getcode) &&  StringUtils.isEmpty(appId) ) return new Result(Result.ERROR,"授权参数异常");
		Result result = this.wxinService.isH5GetAccessToken(getcode,appId);
		if(result.getCode()==Result.OK){
			Map map = (Map) result.getData();
			String openid  =String.valueOf(map.get("Openid"));
			String accessToken  =String.valueOf(map.get("accessToken"));
			Map rawMap = (Map) map.get("rawData");
			if(!LockUtils.tryLock("goi"+openid)) return new Result(Result.ERROR,"授权异常");
			try{
				result= consumerService.fromH5Consumer(openid,userid,rawMap,appId);
			}finally {
				LockUtils.unLock("goi"+openid);
			}
		}
		return result;
	}

	@GetMapping("/getQrcode")
	@Powers({PowerConsts.NOLOGINPOWER})
	public void getQrcode(@RequestParam(value="scene",required=false)String scene,
						  @RequestParam(value="page",required=false)String page,
						  @RequestParam(value="width",required=false)String width,
						  @RequestParam(value="auto_color",required=false)String auto_color,
						  @RequestParam(value="line_color",required=false)String line_color,
						  @RequestParam(value="is_hyaline",required=false)String is_hyaline,
						  HttpServletResponse response) {
		Result result =consumerService.getAccess_token();
		if(result.getCode()==Result.OK) consumerService.getQrcode(String.valueOf(result.getData()),scene,page,width,auto_color,line_color,is_hyaline,response);

	}
}
