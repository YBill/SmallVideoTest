package com.bill.smallvideotest.cache;

import android.content.Context;

import com.bill.smallvideotest.BuildConfig;
import com.bill.smallvideotest.MyApplication;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.Logger;

import java.io.File;

public class ProxyCacheManager {

    public static class SingletonHolder {
        private static final ProxyCacheManager instance = new ProxyCacheManager();
    }

    public static ProxyCacheManager getInstance() {
        return SingletonHolder.instance;
    }

    private final HttpProxyCacheServer proxyCacheServer;

    private ProxyCacheManager() {
        Context context = MyApplication.getContext();
        proxyCacheServer = newProxy(context);
    }

    public HttpProxyCacheServer getProxy() {
        return proxyCacheServer;
    }

    private HttpProxyCacheServer newProxy(Context context) {
        Logger.setDebug(BuildConfig.DEBUG);
        return new HttpProxyCacheServer.Builder(context)
                .maxCacheSize(512 * 1024 * 1024)       // 512MB for cache
                .fileNameGenerator(new MyFileNameGenerator())
                .build();
    }


    /**
     * 删除所有缓存文件
     *
     * @return 返回缓存是否删除成功
     */
    public boolean clearAllCache(Context context) {
        return deleteFiles(proxyCacheServer.getCacheRoot());
    }

    /**
     * 删除url对应默认缓存文件
     *
     * @return 返回缓存是否删除成功
     */
    public boolean clearDefaultCache(Context context, String url) {
        File pathTmp = proxyCacheServer.getTempCacheFile(url);
        File path = proxyCacheServer.getCacheFile(url);
        return deleteFile(pathTmp.getAbsolutePath()) &&
                deleteFile(path.getAbsolutePath());

    }

    /**
     * delete directory
     */
    public boolean deleteFiles(File root) {
        File[] files = root.listFiles();
        if (files == null)
            return true;
        for (File f : files) {
            if (!f.isDirectory() && f.exists()) { // 判断是否存在
                if (!f.delete()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * delete file
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return true;
        if (!file.isFile()) {
            String[] filePaths = file.list();

            if (filePaths != null) {
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
            }
        }
        return file.delete();
    }
}