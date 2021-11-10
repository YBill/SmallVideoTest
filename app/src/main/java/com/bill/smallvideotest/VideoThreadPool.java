package com.bill.smallvideotest;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * author ywb
 * date 2021/11/5
 * desc
 */
public class VideoThreadPool {

    //    private ExecutorService sCachedService;
    private ExecutorService sFixService;

    private static class SingletonHolder {
        private static VideoThreadPool instance = new VideoThreadPool();
    }

    public static VideoThreadPool getInstance() {
        return SingletonHolder.instance;
    }

    private VideoThreadPool() {
//        sCachedService = Executors.newCachedThreadPool();
        sFixService = Executors.newFixedThreadPool(3);
    }

    public void executeRunnable(Runnable runnable) {
        try {
            if (sFixService != null && !sFixService.isShutdown()) {
                sFixService.execute(runnable);
            }
        } catch (RejectedExecutionException e) {
            // 打印当前线程池中活跃的线程数量
            int threadCount = ((ThreadPoolExecutor) sFixService).getActiveCount();
            Log.d("Bill", "[executeRunnable]: current alive thread count = " + threadCount);
            e.printStackTrace();
        }

    }

    public void shutdownExecutor() {
        if (sFixService != null && !sFixService.isShutdown()) {
            sFixService.shutdown();
            sFixService = null;
        }
    }

}
