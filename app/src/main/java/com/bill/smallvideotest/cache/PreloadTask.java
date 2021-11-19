package com.bill.smallvideotest.cache;

import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 原理：主动去请求AndroidVideoCache生成的代理地址，触发AndroidVideoCache缓存机制
 * 缓存到 PreloadManager.PRELOAD_LENGTH 的数据之后停止请求，完成预加载播放器去播放
 * AndroidVideoCache生成的代理地址的时候，AndroidVideoCache会直接返回缓存数据，从
 * 而提升播放速度
 */
public class PreloadTask implements Runnable {

    /**
     * 原始地址
     */
    public String mRawUrl;

    /**
     * 列表中的位置
     */
    public int mPosition;

    /**
     * VideoCache服务器
     */
    public HttpProxyCacheServer mCacheServer;

    /**
     * 是否被取消
     */
    private boolean mIsCanceled;

    /**
     * 是否正在预加载
     */
    private boolean mIsExecuted;

    private final static Map<String, Integer> blackMap = new HashMap<>();

    /**
     * 将预加载任务提交到线程池，准备执行
     */
    public void executeOn(ExecutorService executorService) {
        if (mIsExecuted) return;
        mIsExecuted = true;
        executorService.submit(this);
    }

    /**
     * 取消预加载任务
     */
    public void cancel() {
        if (mIsExecuted) {
            mIsCanceled = true;
        }
    }

    @Override
    public void run() {
        if (!mIsCanceled) {
            start();
        }
        mIsExecuted = false;
        mIsCanceled = false;
    }

    /**
     * 开始预加载
     */
    private void start() {
        if (isItABlacklist(mRawUrl)) return;
        Log.i("Bill", "预加载开始：" + mPosition);
        HttpURLConnection connection = null;
        try {
            String proxyUrl = mCacheServer.getProxyUrl(mRawUrl);
            URL url = new URL(proxyUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5_000);
            connection.setReadTimeout(5_000);
            InputStream in = new BufferedInputStream(connection.getInputStream());
            int length;
            int read = -1;
            byte[] bytes = new byte[8 * 1024];
            while ((length = in.read(bytes)) != -1) {
                read += length;
                if (mIsCanceled || read >= PreloadManager.PRELOAD_LENGTH) {
                    if (mIsCanceled) {
                        Log.i("Bill", "预加载取消：" + mPosition + " 读取数据：" + read + " Byte");
                    } else {
                        Log.i("Bill", "预加载成功：" + mPosition + " 读取数据：" + read + " Byte");
                        PreloadManager.getInstance().removePreloadTask(mRawUrl);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Log.i("Bill", "预加载异常：" + mPosition + " 异常信息：" + e.getMessage());
            addToBlacklist(mRawUrl);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            Log.i("Bill", "预加载结束: " + mPosition);
        }
    }

    private void addToBlacklist(String url) {
        // 加入黑名单
        Integer currentFailNum = blackMap.get(url);
        if (currentFailNum == null) {
            blackMap.put(url, 1);
        } else {
            blackMap.put(url, currentFailNum + 1);
        }
    }

    private boolean isItABlacklist(String url) {
        // 如果失败2次说明这个地址可能有问题，就不缓存了
        Integer failNum = blackMap.get(url);
        if (failNum != null && failNum > 1) {
            Log.i("Bill", "拒绝此次预加载：" + mPosition);
            return true;
        }
        return false;
    }

}

