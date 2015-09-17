package com.miqian.mq.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;


public class UserUtil {

	private static Context context;


	public static void saveToken(Context context, String token, String userId) {
		Pref.saveString(Pref.TOKEN, token, context);
		Pref.saveString(Pref.USERID, userId, context);
	};

	public static String getToken(Context context) {
		return Pref.getString(Pref.TOKEN, context, "");
	};

	public static String getPrefKey(Context context, String name) {
		return name + Pref.getString(Pref.USERID, context, Pref.VISITOR);
	};

	public static boolean hasLogin(Context context) {
		String token = getToken(context);
		if (TextUtils.isEmpty(token)) {
			return false;
		} else {
			return true;
		}
	}

}
