package com.miqian.mq.net;

import android.content.Context;
import android.util.Log;

import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.TestClass;
import com.miqian.mq.utils.JsonUtil;

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
    public static void testHttp(Context context, final ICallback<Meta> callback, String phone, String password) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("captcha", "1423"));
        mList.add(new Param("password", RSAUtils.encryptURLEncode(password)));

        HttpUtils.httpPostRequest(context, Urls.test, mList, new ICallbackString() {

            @Override
            public void onSuccess(String result) {
                TestClass test = JsonUtil.parseObject(result, TestClass.class);
                Log.e("", "L: " + RSAUtils.decryptByPrivate(test.getTestEncrypt()));
//                Meta meta = JsonUtil.parseObject(result, Meta.class);
//                if (meta.getCode() == 1000) {
//                    callback.onSucceed(meta);
//                } else {
//                    callback.onFail(meta.getMessage());
//                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public static void register(Context context, final ICallback<RegisterResult> callback, String mobilePhone, String captcha, String password, String invitationCode) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("captcha", captcha));
        mList.add(new Param("invitationCode", invitationCode));
        mList.add(new Param("mobilePhone", mobilePhone));
        mList.add(new Param("password", password));
        HttpUtils.httpPostRequest(context, Urls.test, mList, new ICallbackString() {

            @Override
            public void onSuccess(String result) {
                RegisterResult registerResult = JsonUtil.parseObject(result, RegisterResult.class);
                if (registerResult.getCode() == 1000) {
                    callback.onSucceed(registerResult);
                } else {
                    callback.onFail(registerResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public static void login(Context context, final ICallback<RegisterResult> callback, String mobilePhone, String password) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", mobilePhone));
        mList.add(new Param("password", password));
        HttpUtils.httpPostRequest(context, Urls.login, mList, new ICallbackString() {

            @Override
            public void onSuccess(String result) {
                RegisterResult registerResult = JsonUtil.parseObject(result, RegisterResult.class);
                if (registerResult.getCode() == 1000) {
                    callback.onSucceed(registerResult);
                } else {
                    callback.onFail(registerResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

}
