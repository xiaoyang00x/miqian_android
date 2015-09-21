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

	public static final String USERID = "UseId";
	// 银行选择的序列
	public static final String BANK_INDEX = "BankIndex";

//	public static final String TOINDEX = "ToIndex";

	// 首次开启
	public static final String FIRST_LOAD = "FirstLoad";

	public static final String VISITOR = "Visitor_0000";

	public static final String NICKNAME = "Nickname";

	public static final String USERICON = "Usericon";

	public static final String TELEPHONE = "Telephone";

	public static final String SERVER_ERROR_CODE = "ServerErrorCode";

	//用户信息
	public static final String REALNAME_STATUS = "RealnameStatus";

	public static final String USER_ = "ServerErrorCode";

	// 是否退出应用
	public static final String EXIT = "Exit";
//	// app是否在后台
//	public static final String BACKSTAGE = "BackStage";

	public static String IsPush="IsPush";

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
