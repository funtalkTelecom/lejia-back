package com.lejia.controller;

import com.lejia.config.annotation.Powers;
import com.lejia.dto.Result;
import com.lejia.global.PowerConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/stress-test")
public class StressTestController extends BaseReturn{
    public final Logger log = LoggerFactory.getLogger(this.getClass());



    @RequestMapping("/query-lejia")
    @Powers({PowerConsts.NOLOGINPOWER})
    public Result qeruyHk(HttpServletRequest request){
        long a = System.currentTimeMillis();

        log.info("----------------查询号码耗时【"+(System.currentTimeMillis()-a)+"】ms");
        return new Result(Result.OK, "");
    }
}
