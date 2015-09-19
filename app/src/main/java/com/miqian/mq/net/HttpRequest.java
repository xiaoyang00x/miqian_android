package com.miqian.mq.net;

import android.content.Context;
import android.text.style.MetricAffectingSpan;
import android.util.Log;

import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.entity.MessageInfoResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.PayOrderResult;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.TestClass;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.UserUtil;

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

        new MyAsyncTask(context, Urls.test, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                TestClass test = JsonUtil.parseObject(result, TestClass.class);
                Log.e("", "L: " + RSAUtils.decryptByPrivate(test.getTestEncrypt()));
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
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

        new MyAsyncTask(context, Urls.idcard_check, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
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
        }).executeOnExecutor();
    }

    /**
     * 活期首页
     *
     * @param callback
     */
    public static void getCurrentHome(Context context, final ICallback<CurrentInfoResult> callback) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        new MyAsyncTask(context, Urls.current_home, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                CurrentInfoResult currentInfoResult = JsonUtil.parseObject(result, CurrentInfoResult.class);
                if (currentInfoResult.getCode().equals("000000")) {
                    callback.onSucceed(currentInfoResult);
                } else {
                    callback.onFail(currentInfoResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
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

        new MyAsyncTask(context, Urls.roll_in, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Log.e("", result);
                PayOrderResult payOrderResult = JsonUtil.parseObject(result, PayOrderResult.class);
                if (payOrderResult.getCode().equals("000000")) {
                    callback.onSucceed(payOrderResult);
                } else {
                    callback.onFail(payOrderResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 获取用户信息
     *
     * @param callback
     */
    public static void getUserInfo(Context context, final ICallback<LoginResult> callback) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.user_info, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Log.e("", result);
                LoginResult loginResult = JsonUtil.parseObject(result, LoginResult.class);
                if (loginResult.getCode().equals("000000")) {
                    callback.onSucceed(loginResult);
                } else {
                    callback.onFail(loginResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 注册
     *
     * @param callback
     */
    public static void register(Context context, final ICallback<RegisterResult> callback, String mobilePhone, String captcha, String password, String invitationCode) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("captcha", captcha));
        mList.add(new Param("invitationCode", invitationCode));
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("password", RSAUtils.encryptURLEncode(password)));

        new MyAsyncTask(context, Urls.register, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
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
        }).executeOnExecutor();
    }

    //登录
    public static void login(Context context, final ICallback<LoginResult> callback, String mobilePhone, String password) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("password", RSAUtils.encryptURLEncode(password)));

        new MyAsyncTask(context, Urls.login, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                LoginResult loginResult = JsonUtil.parseObject(result, LoginResult.class);
                if (loginResult.getCode().equals("000000")) {
                    callback.onSucceed(loginResult);
                } else {
                    callback.onFail(loginResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //检验验证码

    /**
     * @param context
     * @param callback
     * @param phone
     * @param operationType 13001——注册  ；13002——找回密码 ；13003——重新绑定手机号第一次获取验证码 ；13004——重新绑定手机号第二次获取验证码
     *                      13005——银行卡信息补全        13006——修改银行卡         13007——非首次提现
     * @param custId        用户Id   非必填  注册不用填
     */
    public static void checkCaptcha(Context context, final ICallback<Meta> callback, String phone, int operationType, String custId,String captcha) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("operationType", "" + operationType));
        mList.add(new Param("custId", custId));
        mList.add(new Param("captcha", captcha));

        new MyAsyncTask(context, Urls.checkCaptcha, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (meta.getCode().equals("000000")) {
                    callback.onSucceed(meta);
                } else {
                    callback.onFail(meta.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //获取验证码

    /**
     * @param context
     * @param callback
     * @param phone
     * @param operationType 13001——注册  ；13002——找回密码 ；13003——重新绑定手机号第一次获取验证码 ；13004——重新绑定手机号第二次获取验证码
     *                      13005——银行卡信息补全        13006——修改银行卡         13007——非首次提现
     * @param custId        用户Id   非必填  注册不用填
     */
    public static void getCaptcha(Context context, final ICallback<Meta> callback, String phone, int operationType, String custId) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("operationType", "" + operationType));
        mList.add(new Param("custId", custId));

        new MyAsyncTask(context, Urls.getCaptcha, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (meta.getCode().equals("000000")) {
                    callback.onSucceed(meta);
                } else {
                    callback.onFail(meta.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //获得推送信息详情
    public static void getPushDetail(Context context, final ICallback<MessageInfoResult> callback, int pushSource, String id, String custId) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("pushSource", "" + pushSource));
        mList.add(new Param("id", id));
        if (pushSource == 1) {
            mList.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));

        } else {
            mList.add(new Param("custId", "" + custId));
        }
        new MyAsyncTask(context, Urls.getPushDetail, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                MessageInfoResult messageInfoResult = JsonUtil.parseObject(result, MessageInfoResult.class);
                if (messageInfoResult.getCode().equals("000000")) {
                    callback.onSucceed(messageInfoResult);
                } else {
                    callback.onFail(messageInfoResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }


    //设置交易密码
    public static void setPayPassword(Context context, final ICallback<Meta> callback, String custId, String payPassword, String confirmPayPassword) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        mList.add(new Param("payPassword", RSAUtils.encryptURLEncode(payPassword)));
        mList.add(new Param("confirmPayPassword", RSAUtils.encryptURLEncode(confirmPayPassword)));

        new MyAsyncTask(context, Urls.setPayPassword, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (meta.getCode().equals("000000")) {
                    callback.onSucceed(meta);
                } else {
                    callback.onFail(meta.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //修改交易密码
    public static void changePayPassword(Context context, final ICallback<Meta> callback, String custId, String tradeType, String idCard, String mobilePhone, String captcha, String payPassword, String confirmPayPassword
    ) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        mList.add(new Param("tradeType", tradeType));
        mList.add(new Param("idCard", RSAUtils.encryptURLEncode(idCard)));
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("captcha", captcha));
        mList.add(new Param("payPassword", RSAUtils.encryptURLEncode(payPassword)));
        mList.add(new Param("confirmPayPassword", RSAUtils.encryptURLEncode(confirmPayPassword)));

        new MyAsyncTask(context, Urls.changePayPassword, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (meta.getCode().equals("000000")) {
                    callback.onSucceed(meta);
                } else {
                    callback.onFail(meta.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //修改登录密码
    public static void changePassword(Context context, final ICallback<LoginResult> callback, String custId, String oldPassword, String newPassword, String confirmPassword) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        mList.add(new Param("oldPassword", RSAUtils.encryptURLEncode(oldPassword)));
        mList.add(new Param("newPassword", RSAUtils.encryptURLEncode(newPassword)));
        mList.add(new Param("confirmPassword", RSAUtils.encryptURLEncode(confirmPassword)));

        new MyAsyncTask(context, Urls.changePassword, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                LoginResult loginResult = JsonUtil.parseObject(result, LoginResult.class);
                if (loginResult.getCode().equals("000000")) {
                    callback.onSucceed(loginResult);
                } else {
                    callback.onFail(loginResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //找回登录密码
    public static void getPassword(Context context, final ICallback<Meta> callback, String mobilePhone, String newPassword, String confirmPassword, String captcha) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("newPassword", RSAUtils.encryptURLEncode(newPassword)));
        mList.add(new Param("confirmPassword", RSAUtils.encryptURLEncode(confirmPassword)));
        mList.add(new Param("captcha", captcha));

        new MyAsyncTask(context, Urls.getPassword, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (meta.getCode().equals("000000")) {
                    callback.onSucceed(meta);
                } else {
                    callback.onFail(meta.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

}
