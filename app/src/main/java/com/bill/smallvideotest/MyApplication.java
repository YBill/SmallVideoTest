package com.bill.smallvideotest;

import android.app.Application;
import android.content.Context;

/**
 * author ywb
 * date 2021/11/10
 * desc
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}
