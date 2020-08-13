package com.lejia.config.aop.syslog;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 */
public class SysLogBean implements Serializable {
	private static final long serialVersionUID = 1L;
    /**项目*/
    private String project;
	/**用户id*/
	private Integer userid;
    /**用户名*/
	private String username;
    /**用户操作*/
	private String operation;
    /**请求方法*/
	private String method;
    /**请求参数*/
	private String params;
    /**执行时长(毫秒)*/
	private Long time;
    /**IP地址*/
	private String ip;
    /**创建时间*/
	private Date createDate;
	/**用户浏览器*/
	private String userAgent;

	public SysLogBean(String project, Integer userid, String username, String operation, String method, String params, Long time, String userAgent, String ip, Date createDate) {
		this.project = project;
		this.userid = userid;
		this.username = username;
		this.operation = operation;
		this.method = method;
		this.params = params;
		this.time = time;
		this.userAgent=userAgent;
		this.ip = ip;
		this.createDate = createDate;
	}
    /**
     * 设置：项目名称
     */
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    /**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：用户操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 * 获取：用户操作
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * 设置：请求方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：请求方法
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 设置：请求参数
	 */
	public void setParams(String params) {
		this.params = params;
	}
	/**
	 * 获取：请求参数
	 */
	public String getParams() {
		return params;
	}
	/**
	 * 设置：IP地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * 获取：IP地址
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
