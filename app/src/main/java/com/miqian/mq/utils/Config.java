package com.miqian.mq.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Description:配置信息
 * 
 * @author Jackie
 * @created 2013-11-26 下午5:01:34
 */

public class Config {

	/**
	 * 是否调试.
	 */
	public final static boolean DEBUG = false;

	// 关于手机屏幕的一些属性
	public static int WIDTH = 480; // 屏幕宽度
	public static int HEIGHT = 800; // 屏幕高度
	public static float DENSITY = 1.5f; // 屏幕密度

	public static final String UDESK_DOMAIN = "miaoqian.udesk.cn";// 在udesk平台申请的演示用域名。
	public static final String UDESK_SECRETKEY = "981627cd4151ede26a2f2c3258aacba1";// udesk平台分配的演示用secret

	private String sdk_Token = "xxxxxxxxx";//用户身份的唯一识别必填必须唯一

	public static void init(Activity activity) {
		if (WIDTH == 480 || HEIGHT == 800) {
			DisplayMetrics displayMetrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			WIDTH = displayMetrics.widthPixels;
			HEIGHT = displayMetrics.heightPixels;
			DENSITY = displayMetrics.density;
		}
	}
}
