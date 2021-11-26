package com.bill.baseplayer.util;

import android.util.Log;

/**
 * author ywb
 * date 2021/11/26
 * desc
 */
public class MLog {

    private static final String DefaultTag = "VideoPlayer";

    public static void v(String msg) {
        v(DefaultTag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void d(String msg) {
        d(DefaultTag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void i(String msg) {
        i(DefaultTag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void w(String msg) {
        w(DefaultTag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void e(String msg) {
        e(DefaultTag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

}
