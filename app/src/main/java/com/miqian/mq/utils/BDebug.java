package com.miqian.mq.utils;

import android.util.Log;

/**
 * 日志--工具类
 */
public class BDebug {

    public static void log(String msg) {
        if (AppConfig.DEBUG) {
            System.out.println(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }
}
