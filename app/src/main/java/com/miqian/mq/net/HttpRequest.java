package com.miqian.mq.net;

import android.content.Context;
import android.util.Log;

import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.PayOrderResult;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.TestClass;
import com.miqian.mq.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2015/9/4.
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
        mList.add(new Param("invitationCode", ""));
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
    /**
     * 身份认证
     *
     * @param callback
     */
    public static void setIDCardCheck(Context context, final ICallback<Meta> callback, String custId, String idNo, final String realName) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        mList.add(new Param("idNo", RSAUtils.encryptURLEncode(idNo)));
        mList.add(new Param("realName", RSAUtils.encryptURLEncode(realName)));

        HttpUtils.httpPostRequest(context, Urls.idcard_check, mList, new ICallbackString() {

            @Override
            public void onSuccess(String result) {
//                Log.e("", result);
//                TestClass test = JsonUtil.parseObject(result, TestClass.class);
//                Log.e("", "L: " + RSAUtils.decryptByPrivate(test.getTestEncrypt()));
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

    /**
     * 充值
     *
     * @param callback
     */
    public static void rollIn(Context context, final ICallback<PayOrderResult> callback, String custId, String amt, String bankCode, String bankNo) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        mList.add(new Param("amt", amt));
        mList.add(new Param("bankCode", bankCode));
        mList.add(new Param("bankNo", RSAUtils.encryptURLEncode(bankNo)));

        HttpUtils.httpPostRequest(context, Urls.roll_in, mList, new ICallbackString() {

            @Override
            public void onSuccess(String result) {
                Log.e("", result);
                PayOrderResult PayOrderResult = JsonUtil.parseObject(result, PayOrderResult.class);
                if (PayOrderResult.getCode().equals("000000")) {
                    callback.onSucceed(PayOrderResult);
                }
//                Log.e("", "L: " + RSAUtils.decryptByPrivate(test.getTestEncrypt()));
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
//      mList.add(new Param("invitationCode", invitationCode));
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("password", RSAUtils.encryptURLEncode(password)));
        HttpUtils.httpPostRequest(context, Urls.register, mList, new ICallbackString() {

            @Override
            public void onSuccess(String result) {
                RegisterResult registerResult = JsonUtil.parseObject(result, RegisterResult.class);
                if (registerResult.getCode().equals("000000")) {
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
    //登录
    public static void login(Context context, final ICallback<LoginResult> callback, String mobilePhone, String password) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("password", RSAUtils.encryptURLEncode(password)));
        HttpUtils.httpPostRequest(context, Urls.login, mList, new ICallbackString() {

            @Override
            public void onSuccess(String result) {
                LoginResult loginResult = JsonUtil.parseObject(result, LoginResult.class);
                if (loginResult.getCode().equals("000000") {
                    callback.onSucceed(loginResult);
                } else {
                    callback.onFail(loginResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    //获取验证码

    /**
     *
     * @param context
     * @param callback
     * @param phone
     * @param operationType 13001——注册  ；13002——找回密码 ；13003——重新绑定手机号第一次获取验证码 ；13004——重新绑定手机号第二次获取验证码
                            13005——银行卡信息补全        13006——修改银行卡         13007——非首次提现
     * @param custId     用户Id   非必填  注册不用填
     */
    public static void getCaptcha(Context context, final ICallback<Meta> callback, String phone,int operationType,String custId) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("operationType", ""+operationType));
        mList.add(new Param("custId", custId));

        HttpUtils.httpPostRequest(context, Urls.getCaptcha, mList, new ICallbackString() {

            @Override
            public void onSuccess(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (meta.getCode() == 1000) {
                    callback.onSucceed(meta);
                } else {
                    callback.onFail(meta.getMessage());
                }
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }


}
