package com.miqian.mq.net;

import android.content.Context;
import android.util.Log;

import com.miqian.mq.utils.MobileOS;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.squareup.okhttp.Callback;


/**
 * Created by Administrator on 2015/9/4.
 */
public class HttpUtils {

    public static final String APP_KEY = "&key=jwoxoWHeauio";
    public static final String SERVER_ERROR = "服务端网络不通，请重试";
    private static final String NETWORK_ERROR = "您当前网络不可用";

    public static void httpPostRequest(Context context, String url, List<Param> list, final ICallbackString callback) {
        if (MobileOS.getNetworkType(context) == -1) {
            callback.onFail(NETWORK_ERROR);
            return;
        }
        final OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        sortParam(list);
        if (list != null && list.size() > 0) {
            for (Param param : list) {
                builder.add(param.key, param.value);
            }
        }
        RequestBody requestBody = builder.build();
        Headers.Builder headerBuilder = getRequestHeader(getSign(list));
        final Request request = new Request.Builder().url(url).post(requestBody).headers(headerBuilder.build()).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFail("网络请求错误，code：111");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result;
                    try {
                        result = response.body().string();
                        callback.onSuccess(result);
                        Log.e("", result);
                    } catch (IOException e) {
                        callback.onFail("网络请求错误，code：" + response.code());
                    }
                } else {
                    callback.onFail("网络请求错误，code：" + response.code());
                }
            }
        });
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
     *
     * @param list
     * @return
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
     *
     * @return
     */
    public static Headers.Builder getRequestHeader(String sign) {
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add("deviceId", "11");
        headerBuilder.add("cType", "android");
        headerBuilder.add("appName", "miqian");
        headerBuilder.add("appVersion", "2332");
        headerBuilder.add("channelCode", "2332");
        headerBuilder.add("timer", "2332");
        headerBuilder.add("sign", sign);
        headerBuilder.add("token", "2332");
        headerBuilder.add("osVersion", "2332");
        return headerBuilder;
    }
}
