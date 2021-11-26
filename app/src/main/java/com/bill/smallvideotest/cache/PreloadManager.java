package com.bill.smallvideotest.cache;

import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 视频预加载工具，使用AndroidVideoCache实现
 * 地址：https://github.com/danikula/AndroidVideoCache
 */
public class PreloadManager {

    /**
     * 单线程池，按照添加顺序依次执行{@link PreloadTask}
     */
//    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    /**
     * 设置最多3个线程同时下载
     */
    private final ExecutorService mExecutorService = Executors.newFixedThreadPool(3);

    /**
     * 保存正在预加载的{@link PreloadTask}
     */
    private final LinkedHashMap<String, PreloadTask> mPreloadTasks = new LinkedHashMap<>();

    /**
     * 标识是否需要预加载
     */
    private boolean mIsStartPreload = true;

    private final HttpProxyCacheServer mHttpProxyCacheServer;

    /**
     * 预加载的大小，每个视频预加载1M
     */
    private static final int PRELOAD_LENGTH = 1024 * 1024;

    public static class SingletonHolder {
        private static PreloadManager instance = new PreloadManager();
    }

    public static PreloadManager getInstance() {
        return SingletonHolder.instance;
    }

    private PreloadManager() {
        mHttpProxyCacheServer = VideoCacheManager.getInstance().getProxy();
    }

    /**
     * 开始预加载
     *
     * @param rawUrl   原始视频地址
     * @param position 视频位置
     * @param isLoad   是否立即加载
     */
    public void addPreloadTask(String rawUrl, int position, boolean isLoad) {
        if (isPreloaded(rawUrl)) {
            Log.i("Bill", "已经预加载完成 : " + position);
            return;
        }
        Log.i("Bill", "添加到待加载队列: " + position);
        PreloadTask task = new PreloadTask();
        task.mRawUrl = rawUrl;
        task.mPosition = position;
        task.mPreloadLength = PRELOAD_LENGTH;
        task.mCacheServer = mHttpProxyCacheServer;
        mPreloadTasks.put(rawUrl, task);

        if (isLoad) {
            task.executeOn(mExecutorService);
        }
    }

    /**
     * 判断该播放地址是否已经预加载
     */
    private boolean isPreloaded(String rawUrl) {
        //先判断是否有缓存文件，如果已经存在缓存文件，并且其大小等于1KB，则表示已经预加载完成了
        File cacheFile = mHttpProxyCacheServer.getCacheFile(rawUrl);
        if (cacheFile.exists()) {
            if (cacheFile.length() >= 1024) {
                return true;
            } else {
                //这种情况一般是缓存出错，把缓存删掉，重新缓存
                cacheFile.delete();
                return false;
            }
        }
        //再判断是否有临时缓存文件，如果已经存在临时缓存文件，并且临时缓存文件超过了预加载大小，则表示已经预加载完成了
        File tempCacheFile = mHttpProxyCacheServer.getTempCacheFile(rawUrl);
        if (tempCacheFile.exists()) {
            if (tempCacheFile.length() >= PRELOAD_LENGTH) {
                return true;
            } else {
                tempCacheFile.delete();
                return false;
            }
        }

        return false;
    }

    /**
     * 恢复加载
     *
     * @param position        当前位置
     * @param isReverseScroll 是否反向滑动
     */
    public void resumePreload(int position, boolean isReverseScroll) {
        if (mIsStartPreload)
            return;
        mIsStartPreload = true;

        for (Map.Entry<String, PreloadTask> next : mPreloadTasks.entrySet()) {
            PreloadTask task = next.getValue();
            if (isReverseScroll) {
                if (task.mPosition < position) {
                    if (!isPreloaded(task.mRawUrl)) {
                        Log.i("Bill", "恢复预加载，反向滑动 position：" + task.mPosition);
                        task.executeOn(mExecutorService);
                    }
                }
            } else {
                if (task.mPosition > position) {
                    if (!isPreloaded(task.mRawUrl)) {
                        Log.i("Bill", "恢复预加载，正向滑动 position：" + task.mPosition);
                        task.executeOn(mExecutorService);
                    }
                }
            }
        }
    }

    /**
     * 取消加载
     */
    public void pausePreload() {
        if (!mIsStartPreload)
            return;
        mIsStartPreload = false;

        for (Map.Entry<String, PreloadTask> next : mPreloadTasks.entrySet()) {
            PreloadTask task = next.getValue();
            if (!isPreloaded(task.mRawUrl)) {
                Log.i("Bill", "取消预加载：" + task.mPosition);
                task.cancel();
            }
        }
    }

    /**
     * 删除范围外的任务和磁盘数据
     */
    public void removePreloadTaskAndDiskOutOfRange(int startSafePos, int endSafePos) {
        Iterator<Map.Entry<String, PreloadTask>> iterator = mPreloadTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PreloadTask> next = iterator.next();
            PreloadTask task = next.getValue();
            if (task.mPosition >= startSafePos && task.mPosition <= endSafePos)
                continue;
            Log.i("Bill", "删除本地文件 position = " + task.mPosition);
            task.cancel();
            iterator.remove();
            VideoCacheManager.getInstance().clearCache(task.mRawUrl);
        }
    }

    /**
     * 通过原始地址取消预加载
     *
     * @param rawUrl 原始地址
     */
    public void removePreloadTask(String rawUrl) {
        PreloadTask task = mPreloadTasks.get(rawUrl);
        if (task != null) {
            task.cancel();
            mPreloadTasks.remove(rawUrl);
        }
    }

    /**
     * 取消所有的预加载
     */
    public void removeAllPreloadTask() {
        Iterator<Map.Entry<String, PreloadTask>> iterator = mPreloadTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PreloadTask> next = iterator.next();
            PreloadTask task = next.getValue();
            task.cancel();
            iterator.remove();
        }
    }

    /**
     * 获取播放地址
     */
    public String getPlayUrl(String rawUrl) {
        PreloadTask task = mPreloadTasks.get(rawUrl);
        if (task != null) {
            task.cancel();
        }
        if (isPreloaded(rawUrl)) {
            return mHttpProxyCacheServer.getProxyUrl(rawUrl);
        } else {
            return rawUrl;
        }
    }
}
