package com.lejia.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lejia.global.weixin.AccessTokenApi;
import com.lejia.global.weixin.WxConfig;
import com.lejia.dto.Result;
import com.lejia.global.*;
import com.lejia.mapper.*;
import com.lejia.pojo.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.lang.System;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ConsumerService extends BaseService {
	
	@Autowired SessionUtil sessionUtil;
	@Autowired private UserMapper userMapper;
	@Autowired private ConsumerLogMapper consumerLogMapper;
	@Autowired private ConsumerMapper consumerMapper;
	@Autowired private ApiSessionUtil apiSessionUtil;

	public void test() {
		List<User> list=this.userMapper.select(null);
		for (User user : list) {
			System.out.println(user.getName());
		}
	}

	public Result getOpenId(String code,String appId) {
		String appsecret=SystemParam.get(appId);
		String grant_type = "authorization_code";//授权（必填）
		String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
		String params = "appid=" + appId + "&secret=" + appsecret + "&js_code=" + code + "&grant_type=" + grant_type;//请求参数
		String Openid = null;//发送请求
		try {
			long a = System.currentTimeMillis();
			String data = HttpUtil.get(requestUrl, params);
			if(StringUtils.isEmpty(data))return new Result(Result.ERROR,"请求错误");
			JSONObject json = JSONObject.fromObject(data);
			if(!json.containsKey("openid"))return new Result(Result.ERROR,"微信返回结果错误");
			Openid=String.valueOf(json.get("openid"));//用户的唯一标识（openid）
            String session_key=String.valueOf(json.get("session_key"));//用户的唯一标识（unionid）
            StringBuffer sb = new StringBuffer();
            sb.append(Openid).append(appId);
			this.apiSessionUtil.saveSessionKey(sb.toString(),session_key);
			return new Result(Result.OK,Openid);
		} catch (Exception e) {
			log.error("获取用户Openid失败",e);
		}
		return new Result(Result.ERROR,"无法获取Openid");
	}
	
	public Result isOpenid(String openid,int userId,String appId) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(openid==null) return new Result(Result.ERROR, "获取openid 失败");
		String token=TokenGenerator.generateValue();
		List cuLog = consumerLogMapper.findConsumerLogByOpenId(openid);
		Map<String,String> _map=new HashMap<>();
		_map.put("__sessid",token);
		if(cuLog.size()==0){
			//向userclient，userclientlog存数据
			Consumer userC = new Consumer();
			userC.setStatus(1);
			userC.setIsAgent(1);
			userC.setRegDate(new Date());
			userC.setIsPartner(0);  //是否合伙人 1是，2否
			userC.setPartnerCheck(0); //已确认的合伙人 1是0否；是方可提现
			userC.setUpConsumer(userId);
			userC.setPartnerType(0);    //普通合伙人
			consumerMapper.insert(userC);
			Integer userid = userC.getId();

			ConsumerLog log = new ConsumerLog();
			log.setUserId(userid);
			log.setOpenid(openid);
			log.setAppId(appId);  // 小程序ID
			log.setUnionId("");  //unionId
			log.setStatus(1);
			log.setLoginType(2);
			log.setSubTime(sdf.format(date));
			log.setAddDate(new Date());
			consumerLogMapper.insert(log);

			this.apiSessionUtil.saveOrUpdate(token,userC);
			_map.put("consumer_id",String.valueOf(userC.getId()));
			_map.put("isPartner",String.valueOf(userC.getIsPartner()));//是否合伙人 1是，2否
			_map.put("partnerCheck",String.valueOf(userC.getPartnerCheck())); //已确认的合伙人 1是0否；是方可提现
			_map.put("testUser",StringUtils.equals(userC.getCommpayName(),"测试")?"1":"0"); //临时借用

		}else{
			Map map = (Map) cuLog.get(0);
			Consumer Cparam = new Consumer();
			Integer id = NumberUtils.toInt(String.valueOf(map.get("userId"))) ;
			Cparam.setId(id);
			Consumer consumer = consumerMapper.selectOne(Cparam);
			this.apiSessionUtil.saveOrUpdate(token,consumer);
			_map.put("consumer_id",String.valueOf(Cparam.getId()));
			_map.put("isPartner",String.valueOf(consumer.getIsPartner()));//是否合伙人 1是，2否
			_map.put("partnerCheck",String.valueOf(consumer.getPartnerCheck()));//已确认的合伙人 1是0否；是方可提现
			_map.put("testUser",StringUtils.equals(consumer.getCommpayName(),"测试")?"1":"0"); //临时借用
		}
		return new Result(Result.OK, _map);
	}

	public Result insertConsumer(String nickName,long sex,String img,String province,String city){
		//昵称,性别 1男2女0未知,// 头像,//省份,//地市
		Consumer consumer= this.apiSessionUtil.getConsumer();
		Integer userid = consumer.getId();
		List list  = consumerLogMapper.findConsumerLogByUserId(userid);
		if(list.size()>0){
			consumerLogMapper.insertConsumerLog(userid,nickName,sex);
		}
		Consumer userC=new Consumer();
		userC.setId(userid);
		Consumer user = consumerMapper.selectOne(userC);
		if(user!=null){
			//更新userClient
			consumerMapper.insertConsumer(userid,nickName,img,province,city);
		}
		return new Result(Result.OK, "注册成功");
	}

	public  Consumer  getConsumerById(Consumer consumer)
	{
		consumer = consumerMapper.selectOne(consumer);
		return  consumer;
	}

	public  Consumer  getConsumer(int consumerId){
		return  consumerMapper.selectByPrimaryKey(consumerId);
	}

    public Result getAccess_token() {
		WxConfig.init(SystemParam.get("AppID"),SystemParam.get("AppSecret"),"");
		String ACCESS_TOKEN=AccessTokenApi.getAccessToken().getAccessToken();
		return new Result(Result.OK, ACCESS_TOKEN);
        /*String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
        String tokenParams = "grant_type=client_credential&appid=" + appid + "&secret=" + appsecret;
        String access_token = null;//发送请求
        try {
            String data = HttpUtil.get(tokenUrl, tokenParams);
            if (StringUtils.isEmpty(data)) return new Result(Result.ERROR, "请求错误");
            JSONObject json = JSONObject.fromObject(data);
            if (!json.containsKey("access_token")) return new Result(Result.ERROR, "微信返回结果错误");
            access_token = String.valueOf(json.get("access_token"));//用户的唯一标识（openid）
            return new Result(Result.OK, access_token);
        } catch (Exception e) {
            log.error("获取access_token失败", e);
        }
        return new Result(Result.ERROR, "无法获取access_token");*/
    }

    public Result getQrcode(String access_token, String scene, String page,String width,String auto_color,String line_color,String is_hyaline,HttpServletResponse response) {
		String requestUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + access_token;
        JSONObject json = new JSONObject();
        if(!StringUtils.isBlank(scene)){
            json.put("scene", scene);
        }
        if(!StringUtils.isBlank(page)){
            json.put("page", page);
        }
        if(!StringUtils.isBlank(width)){
            json.put("width", width);
        }
        if(!StringUtils.isBlank(line_color)){
            json.put("line_color", JSONObject.fromObject(line_color));
        }
        if(!StringUtils.isBlank(auto_color)){
            json.put("auto_color", auto_color);
        }
        if(!StringUtils.isBlank(is_hyaline)){
            json.put("is_hyaline", new Boolean(is_hyaline));
        }
        try {
            HttpUtil.getQrcode(requestUrl, json.toString(), null,response);
            return new Result(Result.OK,"success");
        }catch (Exception e){
            return new Result(Result.ERROR,"二维码生成失败");
        }
	}

	public ConsumerLog getConsumerLog(String appid,int consumer_id,int loginType){
		ConsumerLog consumerLog = new ConsumerLog();
		consumerLog.setUserId(consumer_id);
		consumerLog.setStatus(1);
		consumerLog.setLoginType(loginType);
		consumerLog.setAppId(appid);
		consumerLog = consumerLogMapper.selectOne(consumerLog);
		return consumerLog;
	}

	/**
	 * 获取用户的一个appid
	 * @param consumer_id
	 * @return
	 */
	public String getConsumerAppid(int consumer_id){
        Example example=new Example(ConsumerLog.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",consumer_id);
        criteria.andEqualTo("status",1);
        example.setOrderByClause("id");
		List<ConsumerLog> list= consumerLogMapper.selectByExample(example);
		if(list.isEmpty())return null;
		ConsumerLog consumerLog1 =list.get(0);
		return consumerLog1.getAppId();
	}
	/**
	 * 用昵称查询，当查询人多过多时禁止输出
	 * @param nickName
	 * @return
	 */
	public Result queryConsumerByNick(String nickName) {
		if(StringUtils.isEmpty(nickName))return new Result(Result.ERROR,"请输入昵称关键字查询");
		Example example=new Example(Consumer.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("isPartner",1);
		criteria.andLike("nickName","%"+nickName+"%");
		example.setOrderByClause("id desc");
		List<Consumer> list=this.consumerMapper.selectByExample(example);
		if(list.size()>5)return new Result(Result.ERROR,"查询用户太多，请输入更多昵称关键字");
		if(list.isEmpty())return new Result(Result.ERROR,"未找到相关昵称用户，请确认信息是否正确");
		//含敏感信息，只取需要的几个数据
		List<Consumer> list1=new ArrayList<>();
		for (Consumer consumer:list){
			Consumer consumer1=new Consumer();
			consumer1.setId(consumer.getId());
			consumer1.setNickName(consumer.getNickName());
			consumer1.setImg(consumer.getImg());
			list1.add(consumer1);
		}
		return new Result(Result.OK,list1);
	}
	/**
	 *
	 * @param start
	 * @param limit
	 * @return
	 */
	public PageInfo partnerPage(int start,int limit,String name,String phone,int qstatus,int partnerType) {
		Consumer pp=new Consumer();
		pp.setStart(start);
		pp.setLimit(limit);
		pp.setIsPartner(1);
		Example example=new Example(Consumer.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("isPartner",1);
		if(StringUtils.isNotEmpty(name))criteria.andLike("name","%"+name+"%");
		if(StringUtils.isNotEmpty(phone))criteria.andLike("phone","%"+phone+"%");
		if(qstatus!=-1)criteria.andEqualTo("partnerCheck",qstatus);
		if(partnerType!=-1)criteria.andEqualTo("partnerType",partnerType);
		example.setOrderByClause("id desc");
		PageHelper.startPage(pp.startToPageNum(),pp.getLimit());
		List<Consumer> _list = consumerMapper.selectByExample(example);
		PageInfo<Object> pm = new PageInfo(_list);
		List<Object> mapList=new ArrayList<>();
		for (Consumer consumer:_list) {
			Map map=new HashMap();
			map.put("id",consumer.getId());
			map.put("name",consumer.getName());
			if(consumer.getPartnerCheck()==null||consumer.getPartnerCheck().equals(0)){
				map.put("phone",consumer.getPhone());
				map.put("idcard",consumer.getIdcard());
			}else{
				map.put("phone",Utils.sensitive(consumer.getPhone(),3,4));
				map.put("idcard",Utils.sensitive(consumer.getIdcard(),4,4));
			}
			map.put("idcard_back",consumer.getIdcardBack());
			map.put("idcard_face",consumer.getIdcardFace());
			map.put("nick_name",consumer.getNickName());
			map.put("partner_check",consumer.getPartnerCheck());
			map.put("partnerType",consumer.getPartnerType());
			mapList.add(map);
		}
		pm.getList().clear();
		pm.getList().addAll(mapList);
		return pm;
	}

	public Result checkUnionId(String appId) {
		Consumer consumer= this.apiSessionUtil.getConsumer();
		Integer userid = consumer.getId();
		List list = consumerLogMapper.checkUnionIdByUserId(userid,appId);
		if(list.size()>0){
			Map map=(Map)list.get(0);
			String nuionId=String.valueOf(map.get("nuionId"));
			if(StringUtils.isNotEmpty(nuionId)){
				return new Result(Result.OK,"存在UnionId");
			}else{
				return new Result(Result.ERROR,"不存在UnionId");
			}
		}else {
			return new Result(Result.ERROR,"不存在UnionId");
		}
	}

	public Result updateUnionId(String encryptedData,String iv,String signature,String rawData, String appId) throws Exception {
		if(StringUtils.isBlank(encryptedData) || StringUtils.isBlank(rawData))return new Result(Result.ERROR,"参数有误");
        Consumer consumer= this.apiSessionUtil.getConsumer();
		String token=this.apiSessionUtil.getTokenStr();
        Integer userid = consumer.getId();
        ConsumerLog clog = new ConsumerLog();
        clog.setUserId(userid);
        clog.setAppId(appId);
        ConsumerLog log = consumerLogMapper.selectOne(clog);
		Integer consLogid =log.getId();
        String sessionKey="";
        if(log!=null){
            StringBuffer sb = new StringBuffer();
            String openId = log.getOpenid();
            sb.append(openId).append(appId);
            sessionKey = this.apiSessionUtil.getSessionKey(sb.toString());
        }
		String signature1 = SecuritySHA1Utils.shaEncode(rawData+sessionKey);
		if(!signature.equals(signature1))return new Result(Result.ERROR,"签名有误");
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        String newuserInfo = "";
        Aes aes = new Aes();
        byte[] resultByte = aes.decrypt(dataByte, keyByte, ivByte);
        if (null != resultByte && resultByte.length > 0) {
            newuserInfo = new String(resultByte, "UTF-8");
            Map jsonObject = MapJsonUtils.parseJSON2Map(newuserInfo);
            String unionId = ObjectUtils.toString(jsonObject.get("unionId"));
			if(StringUtils.isNotBlank(unionId)){
				List list = consumerLogMapper.findConsumerLogByUnionId(unionId);
				if(list.size()==0){
					consumerLogMapper.updateConsumerLogUnionId(consLogid,unionId);
				}else{
					Map m = (Map) list.get(0);
					Integer userId2 = NumberUtils.toInt(String.valueOf(m.get("userId"))) ;
					String nuionId2 = String.valueOf(m.get("nuionId"));
					consumerLogMapper.updateConsumerLogToUserId(consLogid,nuionId2,userId2);
					Consumer userC = consumerMapper.findConsumerById(userId2);
					this.apiSessionUtil.saveOrUpdate(token,userC);
				}
			}
        }
		return new Result(Result.OK,"成功");
	}

	public Result fromH5Consumer(String openid,int userId,Map rawMap,String appId) {
		String nickname =  String.valueOf(rawMap.get("nickname"));
		Integer sex = NumberUtils.toInt( String.valueOf(rawMap.get("sex")));
		String province =  String.valueOf(rawMap.get("province"));
		String city = String.valueOf(rawMap.get("city"));
		String headimgurl =  String.valueOf(rawMap.get("headimgurl"));
		String unionId =  String.valueOf(rawMap.get("unionid"));
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(openid==null) return new Result(Result.ERROR, "获取openid 失败");
		String token=TokenGenerator.generateValue();
		List cuLog = consumerLogMapper.findConsumerLogByOpenId(openid);
		Map<String,String> _map=new HashMap<>();
		_map.put("__sessid",token);
		if(cuLog.size()==0){
			Consumer userC = new Consumer();
			userC.setStatus(1);
			userC.setIsAgent(1);
			userC.setRegDate(new Date());
			userC.setIsPartner(0);  //是否合伙人 1是，2否
			userC.setPartnerCheck(0); //已确认的合伙人 1是0否；是方可提现
			userC.setUpConsumer(userId);
			userC.setPartnerType(0);    //普通合伙人
			userC.setNickName(nickname);
			userC.setProvince(province);
			userC.setCity(city);
			userC.setImg(headimgurl);
			consumerMapper.insert(userC);
			Integer userid = userC.getId();

			ConsumerLog log = new ConsumerLog();
			List list = consumerLogMapper.findConsumerLogByUnionId(unionId);
			if(list.size()==0){
				log.setUserId(userid);
			}else{
				Map m = (Map) list.get(0);
				Integer userId2 = NumberUtils.toInt(String.valueOf(m.get("userId"))) ;
				log.setUserId(userId2);
			}
			log.setOpenid(openid);
			log.setAppId(appId);  // 小程序ID
			log.setUnionId(unionId);  //unionId
			log.setStatus(1);
			log.setLoginType(5);
			log.setSubTime(sdf.format(date));
			log.setAddDate(new Date());
			log.setNickName(nickname);
			log.setSex(sex);
			consumerLogMapper.insert(log);

			this.apiSessionUtil.saveOrUpdate(token,userC);
			_map.put("consumer_id",String.valueOf(userC.getId()));
			_map.put("isPartner",String.valueOf(userC.getIsPartner()));//是否合伙人 1是，2否
			_map.put("partnerCheck",String.valueOf(userC.getPartnerCheck())); //已确认的合伙人 1是0否；是方可提现
			_map.put("testUser",StringUtils.equals(userC.getCommpayName(),"测试")?"1":"0"); //临时借用

		}else{
			Map map = (Map) cuLog.get(0);
			Consumer Cparam = new Consumer();
			Integer id = NumberUtils.toInt(String.valueOf(map.get("userId"))) ;
			Cparam.setId(id);
			Consumer consumer = consumerMapper.selectOne(Cparam);
			this.apiSessionUtil.saveOrUpdate(token,consumer);
			_map.put("consumer_id",String.valueOf(Cparam.getId()));
			_map.put("isPartner",String.valueOf(consumer.getIsPartner()));//是否合伙人 1是，2否
			_map.put("partnerCheck",String.valueOf(consumer.getPartnerCheck()));//已确认的合伙人 1是0否；是方可提现
			_map.put("testUser",StringUtils.equals(consumer.getCommpayName(),"测试")?"1":"0"); //临时借用
		}
		return new Result(Result.OK,_map);
	}
}
