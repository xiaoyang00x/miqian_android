package com.miqian.mq;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Joy on 2015/8/31.
 */
public class MyApplication extends Application {
    private  static MyApplication  myApplication;
    private  static boolean   isCurrent;

    public static boolean isCurrent() {
        return isCurrent;
    }

    public static void setIsCurrent(boolean isCurrent) {
        MyApplication.isCurrent = isCurrent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

  public static    MyApplication   getInstance(){

      if (myApplication==null){
          myApplication =new MyApplication();
      }

   return  myApplication; }
}
