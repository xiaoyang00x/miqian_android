package com.miqian.mq;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Joy on 2015/8/31.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication;
    private static boolean isCurrent;

    public static boolean isCurrent() {
        return isCurrent;
    }

    public static void setIsCurrent(boolean isCurrent) {
        MyApplication.isCurrent = isCurrent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        initImageLoader(getApplicationContext());
    }
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO).build();
        ImageLoader.getInstance().init(config);
    }
    public static MyApplication getInstance() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }

        return myApplication;
    }
}
