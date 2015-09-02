package com.miqian.mq.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

	// 极光推送开关
	public static final String J_PUSH = "Jpush";

	/** Preferences的存储文件名称 */
	final static String PREFS_NAME = "miqian_preferences";

	public static final String TOKEN = "Token";

	// 消息是否读取
	public static final String MESSAGE_READ = "MessageRead";

	public static final String USERID = "useId";
	// 银行选择的序列
	public static final String BANK_INDEX = "BankIndex";

	public static final String TOINDEX = "toindex";

	public static final String LOCK = "lock";

	// 首次开启
	public static final String FIRST_LOAD = "FirstLoad";

	public static final String VISITOR = "0000";

	public static final String NICKNAME = "nickname";

	public static final String USERICON = "usericon";

	public static final String TELEPHONE = "telephone";

	public static final String SERVER_ERROR_CODE = "ServerErrorCode";
	// 是否退出应用
	public static final String EXIT = "exit";
	// 手势开关
	public static final String GESTURE_TOGGLE = "gesture_toggle";
	// 保存手势密码
	public static final String GESTURE = "gesture";
	// app是否在后台
	public static final String BACKSTAGE = "backStage";

	// 手势计数
	public static final String GESTURE_COUNT = "gestureCount";

	public static String SCREEN_ENTER="screen_enter";

	public static String IsPush="isPush";

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
