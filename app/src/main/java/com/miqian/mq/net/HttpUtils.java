package com.miqian.mq.net;

import android.content.Context;
import android.text.TextUtils;

import com.miqian.mq.utils.ChannelUtil;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/4.
 */
public class HttpUtils {

    private static final String APP_KEY = "&key=jwoxoWHeauio";
    private static final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");

    public static String httpPostRequest(Context context, String url, List<Param> list) {
        if (MobileOS.getNetworkType(context) == -1) {
            return MyAsyncTask.NETWORK_ERROR;
        }
        final OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new Param("timer", "" + System.currentTimeMillis()));
        sortParam(list);
        if (list != null && list.size() > 0) {
            for (Param param : list) {
                if (!TextUtils.isEmpty(param.value)) {
                    builder.add(param.key, param.value);
                } else {
                    builder.add(param.key, "");
                }
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
                String body = response.body().string();
                response.body().close();
                response = null;
                return body;
            }
            return null;
        } catch (IOException e) {
            return MyAsyncTask.SERVER_ERROR + e.getMessage();
        }
    }

    public static String getSign(List<Param> list) {
        StringBuilder signBuffer = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (Param param : list) {
                signBuffer.append("&").append(param.key);
                signBuffer.append("=").append(param.value);
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
            @Override
            public int compare(Param lhs, Param rhs) {
                return lhs.key.compareToIgnoreCase(rhs.key);
            }
        });
    }

    /**
     * 获取请求头
     */
    private static Headers.Builder getRequestHeader(Context context, String sign) {
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add("deviceId", MobileOS.getIMEI(context));
        headerBuilder.add("cType", "android");
        headerBuilder.add("deviceModel", MobileOS.getDeviceModel());
        headerBuilder.add("appName", "miqian");
        headerBuilder.add("appVersion", MobileOS.getClientVersion(context));
        headerBuilder.add("channelCode", ChannelUtil.getChannel(context));
        headerBuilder.add("sign", sign);
        headerBuilder.add("token", UserUtil.getToken(context));
        headerBuilder.add("osVersion", MobileOS.getOsVersion());
        headerBuilder.add("netWorkStandard", MobileOS.getNetworkString(context));
        headerBuilder.add("Connection", "close");
        return headerBuilder;
    }

    public static String getUrl(List<Param> list) {
        StringBuilder urlBuffer = new StringBuilder();
        if (list != null && list.size() > 0) {
            urlBuffer.append("?");
            for (Param param : list) {
                urlBuffer.append(param.getKey()).append("=").append(param.getValue());
                urlBuffer.append("&");
            }
        }
        urlBuffer.deleteCharAt(urlBuffer.length() - 1);
        return urlBuffer.toString();
    }
}
