package com.lejia.config.aop.syslog;

import com.lejia.global.SessionUtil;
import com.lejia.global.SystemParam;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SyslogDao {
    @Autowired private RedisTemplate redisStringTemplate;
    public final Logger log = LoggerFactory.getLogger(SyslogDao.class);
    /**
     *  存储访问日志
     * @param operation 操作：比如登录、注册、下单等
     * @param className 位置点：比如类对应的方法、action等
     * @param params    参数：访问参数、接口参数，json对象保存
     * @param time      操作耗时（单位毫秒）
     */
    public void saveSysLog(String operation,String className,String params,long time) {
        try {
            SysLogBean sysLog = new SysLogBean(SystemParam.get("site_name"),SessionUtil.getUserId(),null,operation,className,params,time,null,SessionUtil.getUserIp(),new Date());
            sysLog.setUsername(sysLog.getUserid()!=-1?SessionUtil.getUser().getName():null);
            String user_agent=SessionUtil.getRequest()!=null?SessionUtil.getRequest().getHeader(HttpHeaders.USER_AGENT):null;
            sysLog.setUserAgent(user_agent);
            JSONObject jsonObject=JSONObject.fromObject(sysLog);
            //channel=sys_log_bean请勿变动
            redisStringTemplate.convertAndSend("sys_log_bean",jsonObject.toString());
        }catch (Exception e){
            log.error("存储访问日志遇到异常",e);
        }
    }
}
