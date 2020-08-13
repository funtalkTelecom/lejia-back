package com.lejia.controller;

import com.lejia.config.annotation.Powers;
import com.lejia.dto.Result;
import com.lejia.global.PowerConsts;
import com.lejia.service.HomePageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiHomeController extends BaseReturn{

    @Autowired
    HomePageInfoService homePageInfoService;

    @GetMapping("/getHomePageInfo")
    @Powers({PowerConsts.NOLOGINPOWER})
    public Result getHomePageInfo(){

       return  homePageInfoService.getHomePageInfo();

    }
}
