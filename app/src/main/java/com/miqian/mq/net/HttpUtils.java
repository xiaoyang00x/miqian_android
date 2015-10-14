package com.miqian.mq.net;

import android.content.Context;
import android.util.Log;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.UserUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/9/4.
 */
public class HttpUtils {

  public static final String APP_KEY = "&key=jwoxoWHeauio";
  private static final MediaType CONTENT_TYPE =
      MediaType.parse("application/x-www-form-urlencoded");

  public static String httpPostRequest(Context context, String url, List<Param> list) {
    if (MobileOS.getNetworkType(context) == -1) {
      return MyAsyncTask.NETWORK_ERROR;
    }
    final OkHttpClient client = new OkHttpClient();
    FormEncodingBuilder builder = new FormEncodingBuilder();
    if(list == null) {
      list = new ArrayList<>();
    }
    list.add(new Param("timer", "" + System.currentTimeMillis()));
    sortParam(list);
    if (list != null && list.size() > 0) {
      for (Param param : list) {
        builder.add(param.key, param.value);
      }
    }
    RequestBody requestBody = null;
    try {
      requestBody = builder.build();
    } catch (Exception e) {
      requestBody = RequestBody.create(CONTENT_TYPE, "");
    }
    Headers.Builder headerBuilder = getRequestHeader(context, getSign(list));
    final Request request = new Request.Builder().url(url).post(requestBody).headers(headerBuilder.build()).build();
    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        String httpString = response.body().string();
        Log.e("url:", url);
        Log.e("json:", httpString);
        return httpString;
        //                return response.body().string();
      }
      return null;
    } catch (IOException e) {
      return MyAsyncTask.SERVER_ERROR + e.getMessage();
    }
  }

  public static String getSign(List<Param> list) {
    StringBuffer signBuffer = new StringBuffer();
    if (list != null && list.size() > 0) {
      for (Param param : list) {
        signBuffer.append("&" + param.key);
        signBuffer.append("=" + param.value);
      }
      signBuffer.append(APP_KEY);
      signBuffer.deleteCharAt(0);
      return MobileOS.getMd5(signBuffer.toString()).toLowerCase();
    }
    return "";
  }

  /**
   * 对List<Param>按key进行升序排序
   */
  public static void sortParam(List<Param> list) {
    if (list == null) {
      return;
    }
    Collections.sort(list, new Comparator<Param>() {
      @Override public int compare(Param lhs, Param rhs) {
        return lhs.key.compareToIgnoreCase(rhs.key);
      }
    });
  }

  /**
   * 获取请求头
   */
  public static Headers.Builder getRequestHeader(Context context, String sign) {
    Headers.Builder headerBuilder = new Headers.Builder();
    headerBuilder.add("deviceId", MobileOS.getIMEI(context));
    headerBuilder.add("cType", "android");
    headerBuilder.add("appName", "miqian");
    headerBuilder.add("appVersion", MobileOS.getClientVersion(context));
    headerBuilder.add("channelCode", "0000");
    headerBuilder.add("sign", sign);
    headerBuilder.add("token", UserUtil.getToken(context));
    headerBuilder.add("osVersion", MobileOS.getOsVersion());
    return headerBuilder;
  }
}
