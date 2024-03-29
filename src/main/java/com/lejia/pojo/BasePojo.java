package com.lejia.pojo;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasePojo {
	@Transient
	protected int limit = 0;
	@Transient
	protected int start = 0;
	@Transient
	protected int pageNum = 0;
	@Transient
	protected int sort = 0;
	@Transient
	protected List<Object> list = new ArrayList<Object>();
	@Transient
	protected Map<String, Object> map = new HashMap<String, Object>();

	@Transient
	protected String temp;
	
//	@Transient
//	@Value("${app.idworker.workerid}")
//	private static int workerId;
//	@Transient
//	public static IdWorker idWorker = new IdWorker(workerId, 0);
//	@Transient
//	private long generalId = idWorker.nextId();

//	public static long nextId() {
//		return idWorker.nextId();
//	}

	public int startToPageNum() {
		if(this.limit==0) return 0;
		return (this.start/this.limit)+1;
	}
	public int getPageNum() {
		return this.pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

//	public long getGeneralId() {
//		return generalId;
//	}
//	public void setGeneralId(long generalId) {
//		this.generalId = generalId;
//	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public List<Object> getList() {
		return list;
	}
	public void setList(List<Object> list) {
		this.list = list;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}
