package com.miqian.mq.net;

import android.content.Context;
import android.util.Log;

import com.miqian.mq.utils.MobileOS;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import com.squareup.okhttp.Callback;


/**
 * Created by Administrator on 2015/9/4.
 */
public class HttpUtils {

    public static final String SERVER_ERROR = "服务端网络不通，请重试";
    private static final String NETWORK_ERROR = "您当前网络不可用";

    public static void httpPostRequest(Context context, String url, List<Param> list, final ICallbackString callback) {
        if (MobileOS.getNetworkType(context) == -1) {
            callback.onFail(NETWORK_ERROR);
            return;
        }
        final OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (list != null && list.size() > 0) {
            for (Param param : list) {
                builder.add(param.key, param.value);
            }
        }
        RequestBody requestBody = builder.build();

        final Request request = new Request.Builder().url(url).post(requestBody).addHeader("phoneId", "11").addHeader("partnerId", "sss").addHeader("sign", "2332").build();

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
}
