package com.miqian.mq;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Joy on 2015/8/31.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }
}
