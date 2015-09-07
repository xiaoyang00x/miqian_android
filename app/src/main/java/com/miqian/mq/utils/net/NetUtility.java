package com.miqian.mq.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关--工具类
 */
public class NetUtility {

    public static final String TAG = "NetUtility";
    public static final String NO_CONN = "FAIL";

    private static final int REQUEST_TIMEOUT = 30 * 1000;// 设置请求超时30秒
    private static final int SO_TIMEOUT = 30 * 1000; // 设置等待数据超时时间30秒钟
    private static Context mContext;

    public static boolean isNetworkAvailable(Context context) {
        try {
            mContext = context;
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    //public static String sendHttpRequestByGet(String url) {
    //    //return HttpUtils.sendHttpRequestByGet(url);
    //    return "";
    //}
    //
    //public static String sendHttpRequestByGet(Activity mActivity, String url, boolean isGoGome) {
    //    //return HttpUtils.sendHttpRequestByGet(mActivity, url, isGoGome);
    //    return "";
    //}
    //
    //public static String sendHttpRequestByPost(final Activity mActivity, final String url, final String json,
    //        boolean isGoGome) {
    //    //return HttpUtils.sendHttpRequestByPost(mActivity, url, json, HttpUtils.NORMAL, isGoGome);
    //    return "";
    //}
    //
    //public static String sendHttpRequestByPost(final String url, final String json ) {
    //    //return HttpUtils.sendHttpRequestByPost(url, json, false);
    //    return "";
    //}


}
