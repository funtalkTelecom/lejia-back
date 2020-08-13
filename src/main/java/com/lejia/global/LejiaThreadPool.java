package com.lejia.global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class LejiaThreadPool {

    private final static Logger log = LoggerFactory.getLogger(LejiaThreadPool.class);
    private static ThreadPoolExecutor pool = null;
    private static ThreadPoolExecutor rechargePool = null;

    /**
     * 初始化数据
     */
    public static final void init(){
        pool = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(600), new ThreadPoolExecutor.AbortPolicy());
    }

    public final static void addTask(Runnable command){
        if(pool == null) init();
        pool.execute(command);
    }

    public final static void addRecharge(Runnable command){
        if(rechargePool == null) rechargePool = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(600), new ThreadPoolExecutor.AbortPolicy());
        rechargePool.execute(command);
    }

    public final static void close(){
        if(pool != null) pool.shutdown();
    }

}

