package com.miqian.mq.net;

import android.util.Log;

import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.utils.JsonUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/4.
 */
public class HttpRequest {
    private static List<Param> mList;

    /**
     * 测试
     *
     * @param callback
     * @param phone
     * @param password
     */
    public static void testHttp(final ICallback<JpushInfo> callback, String phone, String password) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", phone));
        mList.add(new Param("captcha", "13"));
        mList.add(new Param("password", password));

        HttpUtils.httpPostRequest(Urls.test, mList, new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFail("网络请求错误，code：111");
            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    String result;
                    try {
                        result = response.body().string();
                        Log.e("", result);
                    } catch (IOException e) {
                        callback.onFail("网络请求错误，code：" + response.code());
                    }
//                    JpushInfo jpushInfo = JsonUtil.parseObject(response.body().toString(), JpushInfo.class);
//                    if (response.getCode() == 1000) {
//                        callback.onSucceed(jpushInfo);
//                    } else {
//                        callback.onFail(response.getUserinfo());
//                    }
                } else {
                    callback.onFail("网络请求错误，code：" + response.code());
                }
            }
        });
    }
}
