package com.lejia.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lejia.dto.Result;
import com.lejia.global.Constants;
import com.lejia.global.SessionUtil;
import com.lejia.global.Utils;

import com.lejia.pojo.Corporation;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CorporationService {
	

	@Autowired private ConsumerService consumerService;

	public Result pageCorporation(Corporation corporation) {
		return new Result(Result.OK, "更新成功");

	}

	public Corporation findCorporationById(Integer id) {
		Corporation c = new Corporation();

		return c;
	}

	//////////////////////////////////////////
	public Object queryCorpBusiData() {
		return new Result(Result.OK, "更新成功");
	}
	/**
	 * 收支明细
	 */
	public Object financeList(String startDate,String endDate,Corporation corporation){
		return new Result(Result.OK, "更新成功");
	}



	public Result corpEdit(Corporation corp) {



		return new Result(Result.OK, "更新成功");
	}

	public Result corpCreate(Corporation corp) {



		return new Result(Result.OK, "添加成功");
	}






}
