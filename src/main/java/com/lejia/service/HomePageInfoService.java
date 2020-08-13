package com.lejia.service;

import com.lejia.dto.Result;

import com.lejia.mapper.IndexCategoryMapper;
import com.lejia.mapper.IndexDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HomePageInfoService {


    @Autowired IndexCategoryMapper indexCategoryMapper;
    @Autowired IndexDataMapper indexDataMapper;

    public Result getHomePageInfo(){

        Map<String,Object> homeInfo=new HashMap<>();

        homeInfo.put("category",indexCategoryMapper.selectAll());
        homeInfo.put("data",indexDataMapper.selectByCategoryId(1));

        return  new Result(200,homeInfo);

    }
}
