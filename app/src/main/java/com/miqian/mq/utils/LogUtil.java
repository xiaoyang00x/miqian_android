package com.miqian.mq.utils;

import android.util.Log;

/**
 *  LOG相关
 * @author guolei_wang
 *
 */
public class LogUtil {

	public static void d(String message) {
		if (Config.DEBUG)
			Log.d("---miaoqian---", "---" + System.currentTimeMillis() + "---" + message);
	}

	public static void d(String tag, String message) {
		if (Config.DEBUG)
			Log.d(tag, "---" + System.currentTimeMillis() + "---" + message);
	}

	public static void e(String tag, String message) {
		if (Config.DEBUG)
			Log.e(tag, "---" + System.currentTimeMillis() + "---" + message);
	}

	public static void v(String tag, String message) {
		if (Config.DEBUG)
			Log.v(tag, message);
	}
}
