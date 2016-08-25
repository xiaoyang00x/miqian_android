package com.miqian.mq;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.miqian.mq.utils.ChannelUtil;
import com.miqian.mq.utils.Config;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import cn.udesk.UdeskSDKManager;

/**
 * Created by Joy on 2015/8/31.
 */
public class MyApplication extends MultiDexApplication {
    private static MyApplication myApplication;
    private static boolean isCurrent;
    private static boolean isOnMainAcitivity;
    private static boolean isBackStage;
    private static boolean showTips;
    private static HashMap<String, Boolean> pushList = new HashMap<>();
    public static long homePressTime;

    public static HashMap<String, Boolean> getPushList() {
        return pushList;
    }

    public static void setPushList(HashMap<String, Boolean> pushList) {
        MyApplication.pushList = pushList;
    }

    public static boolean isShowTips() {
        return showTips;
    }

    public static void setShowTips(boolean showTips) {
        MyApplication.showTips = showTips;
    }

    public static boolean isOnMainAcitivity() {
        return isOnMainAcitivity;
    }

    public static void setIsOnMainAcitivity(boolean isOnMainAcitivity) {
        MyApplication.isOnMainAcitivity = isOnMainAcitivity;
    }

    //判断app的状态,退出app则为false,点home键还是为true
    public static boolean isCurrent() {
        return isCurrent;
    }

    public static void setIsCurrent(boolean isCurrent) {
        MyApplication.isCurrent = isCurrent;
    }

    //isCurrent为true时，判断是否在前台
    public static boolean isBackStage() {
        return isBackStage;
    }

    public static void setIsBackStage(boolean isBackStage) {
        if (!isBackStage() && isBackStage) {
            homePressTime = System.currentTimeMillis();
        }
        MyApplication.isBackStage = isBackStage;
    }

    //将消息加入队列中
    public void addJpushList(String id, boolean isShow) {
        pushList.put(id, isShow);
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

        //设置友盟渠道号
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, "54cc3aedfd98c5b1d000039a", ChannelUtil.getChannel(this)));
        //友盟  禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);

        //Udesk 初始化
        UdeskSDKManager.getInstance().initApiKey(this, Config.UDESK_DOMAIN, Config.UDESK_SECRETKEY);

    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache()).diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO).build();
        ImageLoader.getInstance().init(config);
    }

    public static MyApplication getInstance() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }

        return myApplication;
    }
}
