package com.danikula.videocache;

import android.util.Log;

/**
 * author ywb
 * date 2021/11/15
 * desc
 */
public final class Logger {

    private static final String TAG = "VideoCache";

    private static boolean IS_DEBUG = false;

    public static void setDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    public static void debug(String msg) {
        if (IS_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void info(String msg) {
        if (IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void warn(String msg) {
        if (IS_DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void error(String msg) {
        if (IS_DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void error(String msg, Throwable throwable) {
        if (IS_DEBUG) {
//            Log.e(TAG, msg, throwable);
            Log.e(TAG, msg);
        }
    }
}