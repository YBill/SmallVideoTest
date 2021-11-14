package com.bill.smallvideotest;

import android.app.Application;
import android.content.Context;

import com.bill.smallvideotest.cache.MyFileNameGenerator;
import com.bill.videocache.HttpProxyCacheServer;

/**
 * author ywb
 * date 2021/11/10
 * desc
 */
public class MyApplication extends Application {

    private static Context mContext;

    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .fileNameGenerator(new MyFileNameGenerator())
                .build();
    }
}
