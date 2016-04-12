package com.miqian.mq.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    // 极光推送开关
    public static final String J_PUSH = "Jpush";

    /**
     * Preferences的存储文件名称
     */
    final static String PREFS_NAME = "miqian_preferences";

    public static final String TOKEN = "Token";

    // 消息是否读取
    public static final String MESSAGE_READ = "MessageRead";

    public static final String USERID = "UseId";

    public static final String DEVICE_ID = "DeviceId";

    // 首次开启
    public static final String FIRST_LOAD = "FirstLoad";

    public static final String VISITOR = "Visitor_0000";

    public static final String NICKNAME = "Nickname";

    public static final String ERROR_LIAN = "ErrorLian";
    public static final String ERROR_LIAN_VERSION = "ErrorLianVersion";

    public static final String TELEPHONE = "Telephone";

    //真实姓名
    public static final String REAL_NAME = "REAL_NAME";

    public static final String SERVER_ERROR_CODE = "ServerErrorCode";

    public static final String CURRENT_RATE = "CurrentRate";

    //用户信息
    public static final String PAY_STATUS = "PayStatus";
    //	渠道id
    public static final String CHANNEL_ID = "ChannelId";

    // 是否退出应用
    public static final String EXIT = "Exit";
//	// app是否在后台
//	public static final String BACKSTAGE = "BackStage";

    public static String IsPush = "IsPush";

    public static String CITY = "city";
    public static String PUSH = "push";

    // 图案锁 加密后存储
    public final static String GESTUREPSW = "gesturePsw";
    // 图案锁 剩余解锁验证次数
    public final static String UNLOCKCOUNT = "unlockCount";
    // 手势状态 是否打开
    public final static String GESTURESTATE = "gestureState";
    // 推送开关
    public final static String PUSH_STATE = "pushstate";

    private static SharedPreferences getSettings(final Context contex) {
        SharedPreferences mSharedPreferences = contex.getSharedPreferences(PREFS_NAME, 0);
        return mSharedPreferences;
    }

    public static String getString(final String key, final Context context, final String defaultValue) {
        return getSettings(context).getString(key, defaultValue);
    }

    public static boolean getBoolean(final String key, final Context context, final boolean defaultValue) {
        return getSettings(context).getBoolean(key, defaultValue);
    }

    public static int getInt(final String key, final Context context, final int defaultValue) {
        return getSettings(context).getInt(key, defaultValue);
    }

    public static long getLong(final String key, final Context context, final long defaultValue) {
        return getSettings(context).getLong(key, defaultValue);
    }

    public static boolean saveString(final String key, final String value, final Context context) {
        final SharedPreferences.Editor settingsEditor = getSettings(context).edit();
        settingsEditor.putString(key, value);
        return settingsEditor.commit();
    }

    public static boolean saveBoolean(final String key, final boolean value, final Context context) {
        final SharedPreferences.Editor settingsEditor = getSettings(context).edit();
        settingsEditor.putBoolean(key, value);
        return settingsEditor.commit();
    }

    public static boolean saveInt(final String key, final int value, final Context context) {
        final SharedPreferences.Editor settingsEditor = getSettings(context).edit();
        settingsEditor.putInt(key, value);
        return settingsEditor.commit();
    }

    public static boolean saveLong(final String key, final long value, final Context context) {
        final SharedPreferences.Editor settingsEditor = getSettings(context).edit();
        settingsEditor.putLong(key, value);
        return settingsEditor.commit();
    }
}
