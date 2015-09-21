package com.miqian.mq.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Jackie 主要是系统的常用函数
 * **/
public class MobileOS {

	/**
	 * md5加密
	 * 
	 * @param s
	 * @return
	 */
	public static String getMd5(String s) {
		byte[] bytes = s.getBytes();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			return toHexString(algorithm.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取当前版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		String version = "1.0";
		PackageManager packageManager = context.getApplicationContext().getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;

	}

	/**
	 * 获取客户端版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getClientVersion(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
		}
		return verName;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static int getVersionCode(Context context) {
		int version = -1;
		try {
			version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}

	private static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	/**
	 * 毫秒时间转换成小时分钟数
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String toTimeString(int milliseconds) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm:ss", Locale.UK);
		TimeZone timeZone = TimeZone.getTimeZone("GMT");
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat.format(milliseconds);
	}

	/***
	 * <pre>
	 * add this permission to your AndroidManifest.xml
	 * <strong>android.permission.READ_PHONE_STATE</strong>
	 * 获取 IMEI, CellNumber, Sim serial number .etc.
	 * </pre>
	 */
	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return !TextUtils.isEmpty(imei) ? imei : getLocalMacAddress(context);
	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String address = info.getMacAddress();
		return !TextUtils.isEmpty(address) ? address : getIMSI(context);
	}

	/** 获取手机串号IMSI */
	public static String getIMSI(Context context) { // gsm -- imsi
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telephonyManager.getSubscriberId();
		return !TextUtils.isEmpty(imsi) ? imsi : UserUtil.getPrefKey(context, "000000");
	}

	/** 获取Squid统计的id(用IMSI 或者 IMEI) */
	public static String getMatoId(Context context) {
		String matoId = getIMSI(context);
		if (TextUtils.isEmpty(matoId)) {
			matoId = getIMEI(context);
		}
		return matoId;
	}

	/**
	 * 乐视 根据当前时间戳生成乐视tkey
	 * 
	 * @return tkey
	 */
	public static long generateTKey(int time) {
		int e = 0;
		for (int s = 0; s < 8; s++) {
			e = 1 & time;
			time >>= 1;
			e <<= 31;
			time += e;
		}
		return time ^ 185025305;
	}

	/**
	 * 获取运营商名字
	 */
	public static String getOperatorName(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telephonyManager.getSimOperator();
		String operatorName = "";
		if (operator != null) {
			if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
				operatorName = "中国移动";
			} else if (operator.equals("46001") || operator.equals("46006")) {
				operatorName = "中国联通";
			} else if (operator.equals("46003") || operator.equals("46005")) {
				operatorName = "中国电信";
			} else if (operator.equals("46020")) {
				operatorName = "中国铁通";
			}
		}
		return operatorName;
	}

	/** 获取手机型号 */
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	/** 获取系统名称 */
	public static String getVersionName(Context context) {
		return android.os.Build.VERSION.INCREMENTAL;
	}

	/** 获取系统版本号 */
	public static String getOsVersion() {
		String osVersion = android.os.Build.VERSION.RELEASE;
		return !TextUtils.isEmpty(osVersion) ? osVersion : "";
	}

	/**
	 * 获取网络类型，简单的区分wifi，移动数据网，无网络络三种
	 * 
	 * @return
	 */
	public static int getNetworkType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null) {
			if (!info.isAvailable()) {
				return -1;
			} else {
				if (info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected()) {
					return ConnectivityManager.TYPE_WIFI;
				} else if (info.getType() == ConnectivityManager.TYPE_MOBILE && info.isConnected()) {
					return ConnectivityManager.TYPE_MOBILE;
				}
			}
		}
		return -1;
	}

	/** 判断网络状态 */
	public static NetWorkEnum getNetWorkState(Context context) {
		int type = getNetworkType(context);
		if (type == ConnectivityManager.TYPE_MOBILE) {
			return NetWorkEnum.Mobile;
		} else if (type == ConnectivityManager.TYPE_WIFI) {
			return NetWorkEnum.Wifi;
		} else {
			return NetWorkEnum.NoConnection;
		}
	}

	public static enum NetWorkEnum {
		NoConnection, Mobile, Wifi
	}

	/**
	 * 获取网络类型名称
	 * 
	 * @return
	 */
	public static String getNetworkString(Context context) {
		int networkType = getNetworkType(context);
		if (networkType == ConnectivityManager.TYPE_WIFI) {
			return "wifi";
		} else if (networkType == ConnectivityManager.TYPE_MOBILE) {
			return "3g";
		} else {
			return "none";
		}
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1,第二位为3到9，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public static boolean isIcsOrNewer() {
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	/** 判断sd卡是否存在 **/
	public static boolean isSDCardMounted() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	public static boolean isJellyOrNewer() {
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
	}

	/**
	 * 判断指定包名的进程是否运行
	 * 
	 * @param context
	 * @param packageName
	 *            指定包名
	 * @return 是否运行
	 */
	public static boolean isRunning(Context context, String packageName) {
		ActivityManager am = (ActivityManager) context.getSystemService("miqian");
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		for (RunningAppProcessInfo rapi : infos) {
			if (rapi.processName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取屏幕宽度
	 *
	 * @return
	 */
	public static int getScreenWidth(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
		return display.getWidth();

	}

	/**
	 * 获取屏幕高度
	 */
	public static int getScreenHeight(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
		return display.getHeight();
	}

}
