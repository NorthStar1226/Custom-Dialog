package com.zhangjq.luckydraw.util;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建日期：2022/4/21 15:45
 *
 * @author zhangjq
 * @version 1.0
 * 包名： com.idata.scan.base
 * 类说明：
 */

public class ThreadPoolExecutorUtil {

    private final  static  int CORE_POOL_SIZE=5;
    private final  static  int MAX_POOL_SIZE=5;
    private final  static  long KEEP_ALIVE_TIME=30*60*1000;

    private final static BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<>(5);

    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,workQueue, new ThreadPoolExecutor.CallerRunsPolicy());

    public  static  void exec(Runnable runnable){
        executor.execute(runnable);
    }

}