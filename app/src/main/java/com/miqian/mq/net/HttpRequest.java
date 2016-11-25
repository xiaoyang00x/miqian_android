package com.miqian.mq.net;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.MyApplication;
import com.miqian.mq.activity.WebHFActivity;
import com.miqian.mq.activity.user.HfUpdateActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.ConfigResult;
import com.miqian.mq.entity.CurrentDetailResult;
import com.miqian.mq.entity.CurrentMathProjectResult;
import com.miqian.mq.entity.CurrentProjectResult;
import com.miqian.mq.entity.CurrentRecordResult;
import com.miqian.mq.entity.GetHomeActivityResult;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.entity.HomePageInfoResult;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.MessageInfoResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.OperationResult;
import com.miqian.mq.entity.ProducedOrderResult;
import com.miqian.mq.entity.PushDataResult;
import com.miqian.mq.entity.RedPaperData;
import com.miqian.mq.entity.RedeemData;
import com.miqian.mq.entity.RegTransDetailResult;
import com.miqian.mq.entity.RegTransFerredDetailResult;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.RegularDetailResult;
import com.miqian.mq.entity.RegularProjectListResult;
import com.miqian.mq.entity.RegularTransferListResult;
import com.miqian.mq.entity.RepaymentResult;
import com.miqian.mq.entity.SubscribeOrderResult;
import com.miqian.mq.entity.SubscriptionRecordsResult;
import com.miqian.mq.entity.TransferDetailResult;
import com.miqian.mq.entity.UpdateResult;
import com.miqian.mq.entity.UserCurrentResult;
import com.miqian.mq.entity.UserMessageResult;
import com.miqian.mq.entity.UserRegularDetailResult;
import com.miqian.mq.entity.UserRegularResult;
import com.miqian.mq.entity.WithDrawResult;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2015/9/4.
 */
public class HttpRequest {

    private static final String CODE_SUCCESS = "000000";

    /**
     * APP的配置：广告，tab图标
     */
    public static void getConfig(Context context, final ICallback<ConfigResult> callback) {
        List<Param> mList = new ArrayList<>();
        new MyAsyncTask(context, Urls.app_config, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                ConfigResult meta = JsonUtil.parseObject(result, ConfigResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 秒钱宝、定期赚、定期计划
     * 认购订单生成页面
     *
     * @param amt 金额
     */
    public static void getProduceOrder(Context context, final ICallback<ProducedOrderResult> callback, String amt, String subjectId) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("hfCustId", RSAUtils.encryptURLEncode(UserUtil.getHfCustId(context))));
        mList.add(new Param("amt", amt));
        mList.add(new Param("subjectId", subjectId));
//        mList.add(new Param("prodId", prodId));
        new MyAsyncTask(context, Urls.order_produced, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                ProducedOrderResult meta = JsonUtil.parseObject(result, ProducedOrderResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode()) || TextUtils.equals("996633", meta.getCode())) {
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

    /**
     * 获取用户信息
     */
    public static void getUserInfo(final Context context, final ICallback<LoginResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.user_info, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                LoginResult meta = JsonUtil.parseObject(result, LoginResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
                    UserUtil.saveHfInfo(context, meta.getData());
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

    /**
     * 获取汇付账户信息
     */
    public static void getHfUserInfo(final Context context, final ICallback<LoginResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.hf_user_info, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                LoginResult meta = JsonUtil.parseObject(result, LoginResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
                    UserUtil.saveHfInfo(context, meta.getData());
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

    /**
     * 注册
     */
    public static void register(Context context, final ICallback<RegisterResult> callback,
                                String mobilePhone, String captcha, String password, String invitationCode) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("captcha", captcha));
        mList.add(new Param("invitationCode", invitationCode));
        mList.add(new Param("mobile", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("password", RSAUtils.encryptURLEncode(password)));

        new MyAsyncTask(context, Urls.register, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegisterResult meta = JsonUtil.parseObject(result, RegisterResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    //登录
    public static void login(final Context context, final ICallback<LoginResult> callback, String mobilePhone, String password) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("mobile", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("password", RSAUtils.encryptURLEncode(password)));

        new MyAsyncTask(context, Urls.login, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                LoginResult meta = JsonUtil.parseObject(result, LoginResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
                    UserUtil.saveUserInfo(MyApplication.getInstance(), meta.getData());
                    UserUtil.saveHfInfo(context, meta.getData());
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

    //检验验证码

    /**
     * @param operationType 13001——注册  ；13002——找回密码 ；13003——重新绑定手机号第一次获取验证码 ；13004——重新绑定手机号第二次获取验证码
     *                      13005——银行卡信息补全        13006——修改银行卡         13007——非首次提现
     */
    public static void checkCaptcha(Context context, final ICallback<Meta> callback, String phone, int operationType, String captcha) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("operationType", "" + operationType));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("captcha", captcha));

        new MyAsyncTask(context, Urls.checkCaptcha, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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
     * @param operationType 13001——注册  ；13002——找回密码 ；13003——重新绑定手机号第一次获取验证码 ；13004——重新绑定手机号第二次获取验证码
     *                      13005——银行卡信息补全        13006——修改银行卡         13007——非首次提现  13008——找回交易密码
     */
    public static void getCaptcha(Context context, final ICallback<Meta> callback, String phone, int operationType, String money) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("operationType", "" + operationType));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        if (operationType == TypeUtil.CAPTCHA_REDEEM) {
            mList.add(new Param("extra", money));
        }
        new MyAsyncTask(context, Urls.getCaptcha, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    //获得消息详情
    public static void getPushDetail(Context context, final ICallback<MessageInfoResult> callback, boolean isMessage, String id) {
        List<Param> mList = new ArrayList<>();
        String url = "";
        mList.add(new Param("id", id));
        if (isMessage) {
            mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
            url = Urls.getMessageDetail;
        } else {
            mList.add(new Param("custId", ""));
            url = Urls.getPushDetail;
        }
        new MyAsyncTask(context, url, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                MessageInfoResult meta = JsonUtil.parseObject(result, MessageInfoResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    //获得个人消息
    public static void getMessageList(final Context context, String id, final ICallback<UserMessageResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("id", id));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        new MyAsyncTask(context, Urls.getMessageList, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserMessageResult meta = JsonUtil.parseObject(result, UserMessageResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
                    callback.onSucceed(meta);
                    //保存数据到本地，缓存用
                    Pref.saveString(Pref.DATA_MESSAGE, result, context);
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

    //获得公告列表
    public static void getPushList(final Context context, String id, String page, final ICallback<PushDataResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("id", id));
        mList.add(new Param("page", page));
        new MyAsyncTask(context, Urls.getPushList, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                PushDataResult meta = JsonUtil.parseObject(result, PushDataResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
                    callback.onSucceed(meta);
                    //保存数据到本地，缓存用
                    Pref.saveString(Pref.DATA_PUSH, result, context);
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

    //设置消息已读
    public static void setMessageReaded(Context context, final ICallback<Meta> callback, String startId, String endId, int isAll) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("startId", startId));
        mList.add(new Param("endId", endId));
        mList.add(new Param("isAll", isAll + ""));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        new MyAsyncTask(context, Urls.setMessageReaded, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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
    public static void changePassword(Context context, final ICallback<Meta> callback,
                                      String oldPassword, String newPassword, String confirmPassword, String captcha) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("oldPassword", RSAUtils.encryptURLEncode(oldPassword)));
        mList.add(new Param("newPassword", RSAUtils.encryptURLEncode(newPassword)));
        mList.add(new Param("confirmPassword", RSAUtils.encryptURLEncode(confirmPassword)));
        mList.add(new Param("captcha", captcha));

        new MyAsyncTask(context, Urls.changePassword, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    //找回登录密码
    public static void getPassword(Context context, final ICallback<Meta> callback,
                                   String mobilePhone, String newPassword, String confirmPassword, String captcha) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("mobile", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("newPassword", RSAUtils.encryptURLEncode(newPassword)));
        mList.add(new Param("confirmPassword", RSAUtils.encryptURLEncode(confirmPassword)));
        mList.add(new Param("captcha", captcha));

        new MyAsyncTask(context, Urls.getPassword, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 获取首页信息
     */
    public static void getHomePageInfo(Context context, final ICallback<HomePageInfoResult> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        new MyAsyncTask(context, Urls.homeInfo, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                HomePageInfoResult meta = JSON.parseObject(result, HomePageInfoResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 获取秒钱宝项目列表数据
     *
     * @param context
     * @param callback
     * @param pageNo   获取第*页数据
     * @param pageSize 每页数据条数
     */
    public static void getCurrentProjectList(Context context, final ICallback<CurrentProjectResult> callback, int pageNo, int pageSize) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("pageNo", String.valueOf(pageNo)));
        params.add(new Param("pageSize", String.valueOf(pageSize)));
        new MyAsyncTask(context, Urls.current_home, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                CurrentProjectResult meta = JsonUtil.parseObject(result, CurrentProjectResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 获取秒钱宝详情
     *
     * @param subjectId 标的编号，如传入则返回改标的相关信息
     */
    public static void getCurrentDetail(Context context, String subjectId, String custId, final ICallback<CurrentDetailResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("productCode", subjectId));
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        new MyAsyncTask(context, Urls.current_detail, params, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                CurrentDetailResult meta = JsonUtil.parseObject(result, CurrentDetailResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 获取定期首页列表数据
     *
     * @param context
     * @param callback
     */
    public static void getRegularProjectList(Context context, final ICallback<RegularProjectListResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("pageNo", "1"));
        params.add(new Param("pageSize", "50"));
        new MyAsyncTask(context, Urls.REGULA_PROJECT, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegularProjectListResult meta = JsonUtil.parseObject(result, RegularProjectListResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 获取定期项目转让列表数据
     *
     * @param context
     * @param callback
     * @param nextPage 获取第*页数据
     */
    public static void getRegularTransferList(Context context, final ICallback<RegularTransferListResult> callback, int nextPage) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("pageNo", String.valueOf(nextPage)));
        params.add(new Param("pageSize", "20"));
        new MyAsyncTask(context, Urls.REGULA_PROJECT_TRANSFER, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegularTransferListResult meta = JsonUtil.parseObject(result, RegularTransferListResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 获取红包定期列表
     */
    public static void getFitSubject(Context context, String promId, final ICallback<GetRegularResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("couponsId", promId));
        new MyAsyncTask(context, Urls.getFitSubject, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                GetRegularResult meta = JsonUtil.parseObject(result, GetRegularResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 定期详情（定期项目、定期计划）
     *
     * @param context
     * @param subjectId 标的编号，如传入则返回改标的相关信息
     * @param prodId    产品编号
     * @param custId    用户id
     * @param callback
     */
    public static void getRegularDetail(Context context, String subjectId, int prodId, String custId, final ICallback<RegularDetailResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("subjectId", subjectId));
        params.add(new Param("prodId", String.valueOf(prodId)));
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        new MyAsyncTask(context, Urls.REGULA_PROJECT_DETAIL, params, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                RegularDetailResult meta = JsonUtil.parseObject(result, RegularDetailResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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


    /**
     * 登出
     */
    public static void loginOut(Context context, final ICallback<Meta> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.loginOut, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 我的秒钱宝
     */
    public static void getUserCurrent(Context context, final ICallback<UserCurrentResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.user_current, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserCurrentResult meta = JsonUtil.parseObject(result, UserCurrentResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 我的秒钱宝--项目匹配
     */
    public static void getCurrentProjectMath(Context context, final ICallback<CurrentMathProjectResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.user_current_project_math, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                CurrentMathProjectResult meta = JsonUtil.parseObject(result, CurrentMathProjectResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 我的定期
     *
     * @param pageNo   页码(默认1)
     * @param pageSize 每页条数（默认20）
     * @param clearYn  默认为N  N：计息中  Y：已结息
     */
    public static void getUserRegular(Context context, final ICallback<UserRegularResult> callback, String pageNo, String pageSize, String clearYn) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("pageNo", pageNo));
        mList.add(new Param("pageSize", pageSize));
        mList.add(new Param("clearYn", clearYn));

        new MyAsyncTask(context, Urls.user_regular, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserRegularResult meta = JsonUtil.parseObject(result, UserRegularResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 我的定期详情
     *
     * @param investId 投资产品id
     * @param clearYn  默认为N  N：计息中  Y：已结息
     */
    public static void getUserRegularDetail(Context context, final ICallback<UserRegularDetailResult> callback, String investId, String clearYn) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("investId", investId));
        mList.add(new Param("clearYn", clearYn));

        new MyAsyncTask(context, Urls.user_regular_detail, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserRegularDetailResult meta = JsonUtil.parseObject(result, UserRegularDetailResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 我的定期转让情况
     *
     * @param investId 投资id
     * @param clearYn  是否结息
     */
    public static void getTransferDeatil(Context context, final ICallback<TransferDetailResult> callback, String investId, String clearYn) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("investId", investId));
        mList.add(new Param("clearYn", clearYn));

        new MyAsyncTask(context, Urls.regular_transfer_detail, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                TransferDetailResult meta = JsonUtil.parseObject(result, TransferDetailResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 还款详情
     *
     * @param investId 投资id
     */
    public static void getRepayment(Context context, final ICallback<RepaymentResult> callback, String investId) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("investId", investId));

        new MyAsyncTask(context, Urls.repayment_detail, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RepaymentResult meta = JsonUtil.parseObject(result, RepaymentResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 秒钱宝、定期赚、定期计划
     * 认购接口
     *
     * @param amt       金额
     * @param subjectId
     */
    public static void subscribeOrder(Context context, final ICallback<SubscribeOrderResult> callback, String amt, String subjectId, String promotionId) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("hfCustId", RSAUtils.encryptURLEncode(UserUtil.getHfCustId(context))));
        mList.add(new Param("amt", amt));
//        mList.add(new Param("payPassword", RSAUtils.encryptURLEncode(payPassword)));
        mList.add(new Param("subjectId", subjectId));
        mList.add(new Param("promotionId", promotionId));
        new MyAsyncTask(context, Urls.subscribe_order, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                SubscribeOrderResult meta = JsonUtil.parseObject(result, SubscribeOrderResult.class);
                callback.onSucceed(meta);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 我的定期赚转让详情
     */
    public static void getRegTransFerredDetail(Context context, String investId, final ICallback<RegTransFerredDetailResult> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        params.add(new Param("investId", investId));
        new MyAsyncTask(context, Urls.getRegTransFerredDetail, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegTransFerredDetailResult meta = JsonUtil.parseObject(result, RegTransFerredDetailResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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


    /**
     * 发起转让详情
     */
    public static void getRegTransDetail(Context context, String investId, String clearYn, final ICallback<RegTransDetailResult> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        params.add(new Param("investId", investId));
        params.add(new Param("clearYn", clearYn));
        new MyAsyncTask(context, Urls.getRegTransDetail, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegTransDetailResult meta = JsonUtil.parseObject(result, RegTransDetailResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 我的促销接口，包括红包，拾财券等
     * 促销券使用状态status: 0可使用,1使用完,2已过期,3赠送
     */
    public static void getCustPromotion(Context context, final ICallback<RedPaperData> callback, String status, String pageNo, String pageSize) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("status", status));
        mList.add(new Param("pageNo", pageNo));
        mList.add(new Param("pageSize", pageSize));
        new MyAsyncTask(context, Urls.getCustPromotion, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RedPaperData meta = JsonUtil.parseObject(result, RedPaperData.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    //赎回
    public static void redeem(Context context, final ICallback<RedeemData> callback, String amt, String mobilePhone, String captcha) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("hfCustId", RSAUtils.encryptURLEncode(UserUtil.getHfCustId(context))));
        mList.add(new Param("mobile", RSAUtils.encryptURLEncode(mobilePhone)));
        mList.add(new Param("amt", amt));
        mList.add(new Param("captcha", captcha));
        new MyAsyncTask(context, Urls.redeem, mList, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                RedeemData meta = JsonUtil.parseObject(result, RedeemData.class);
                callback.onSucceed(meta);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * API-资金记录/秒钱宝交易记录
     *
     * @param context
     * @param callback
     * @param pageNo
     * @param pageSize
     * @param billType    资金记录类型 00 全部 01 充值 02 提现 03 认购 04 赎回 05 转让 06 到期还款
     * @param productCode 资金产品类型 0 全部 3 秒钱宝
     */
    public static void getFundFlow(Context context, final ICallback<CurrentRecordResult> callback,
                                   String pageNo, String pageSize, String billType, String productCode) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("pageNo", pageNo));
        mList.add(new Param("pageSize", pageSize));
        mList.add(new Param("billType", billType));
        mList.add(new Param("productCode", productCode));
        new MyAsyncTask(context, Urls.getFundFlow, mList, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                CurrentRecordResult meta = JsonUtil.parseObject(result, CurrentRecordResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    //修改绑定手机
    public static void changePhone(Context context, final ICallback<Meta> callback,
                                   String oldMobilePhone, String oldCaptcha, String newMobilePhone, String newCaptcha) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("oldCaptcha", oldCaptcha));
        mList.add(new Param("oldMobilePhone", RSAUtils.encryptURLEncode(oldMobilePhone)));
        mList.add(new Param("newMobilePhone", RSAUtils.encryptURLEncode(newMobilePhone)));
        mList.add(new Param("newCaptcha", newCaptcha));

        new MyAsyncTask(context, Urls.changePhone, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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


    //赎回预处理
    public static void withdrawPreprocess(Context context, final ICallback<WithDrawResult> callback, String amt) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));

        new MyAsyncTask(context, Urls.withdrawPreprocess, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                WithDrawResult meta = JsonUtil.parseObject(result, WithDrawResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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


    /**
     * 获取首页运营活动列表
     */
    public static void getHomeActivity(Context context, final ICallback<GetHomeActivityResult> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        new MyAsyncTask(context, Urls.get_home_activity, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                GetHomeActivityResult meta = JsonUtil.parseObject(result, GetHomeActivityResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 获取首页运营活动已读反馈
     *
     * @param activityId     活动 ID
     * @param activityPlanId 计划 ID
     */
    public static void getActivityFeedback(Context context, String activityId, String activityPlanId, final ICallback<Meta> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        params.add(new Param("activityId", activityId));
        params.add(new Param("activityPlanId", activityPlanId));
        new MyAsyncTask(context, Urls.get_activity_feedback, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
            }

            @Override
            public void onFail(String error) {
            }
        }).executeOnExecutor();
    }

    /**
     * 标的相关记录
     */
    public static void findInvestInfo(Context context, String investId, final ICallback<OperationResult> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        params.add(new Param("investId", investId));
        new MyAsyncTask(context, Urls.findInvestInfo, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                OperationResult meta = JsonUtil.parseObject(result, OperationResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 发起转让
     */
    public static void launchTransfer(Context context, String amt, String payPassword, String transferList, final ICallback<Meta> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        params.add(new Param("amt", amt));
        params.add(new Param("payPassword", RSAUtils.encryptURLEncode(payPassword)));
        params.add(new Param("transferList", transferList));
        new MyAsyncTask(context, Urls.launchTransfer, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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

    /**
     * 版本是否强制更新(正常更新为友盟更新)
     */
    public static void forceUpdate(final Context context, final ICallback<UpdateResult> callback) {
        new MyAsyncTask(context, Urls.forceUpdate, null, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UpdateResult meta = JsonUtil.parseObject(result, UpdateResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
                    callback.onSucceed(meta);
//                    UpdateInfo updateInfo = meta.getData();
//                    if ("2".equals(updateInfo.getUpgradeSign())) {
//                        Pref.saveString(Pref.FORCE_UPDATE, MobileOS.getClientVersion(context), context);
//                    }
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

    /**
     * 汇付充值接口
     */
    public static void rollinHf(final Activity activity, String hfCustId, String amt) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(activity))));
        params.add(new Param("hfCustId", RSAUtils.encryptURLEncode(hfCustId)));
        params.add(new Param("amt", amt));
        WebHFActivity.startActivity(activity, Urls.hf_rollin_url, params, HfUpdateActivity.REQUEST_CODE_ROLLIN);
    }

    /**
     * 汇付提现接口
     */
    public static void rolloutHf(final Activity activity, String amt) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(activity))));
        params.add(new Param("hfCustId", RSAUtils.encryptURLEncode(UserUtil.getHfCustId(activity))));
        params.add(new Param("amt", amt));
        WebHFActivity.startActivity(activity, Urls.hf_rollout_url, params, HfUpdateActivity.REQUEST_CODE_ROLLOUT);
    }

    /**
     * 汇付老用户激活
     */
    public static void activateHf(final Activity activity, String hfCustId) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(activity))));
        params.add(new Param("hfCustId", RSAUtils.encryptURLEncode(hfCustId)));
        WebHFActivity.startActivity(activity, Urls.hf_activate, params, HfUpdateActivity.REQUEST_CODE_ACTIVATE);
    }

    /**
     * 汇付用户开户接口
     */
    public static void registerHf(final Activity activity, String userName, String idCard) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(activity))));
        params.add(new Param("userName", RSAUtils.encryptURLEncode(userName)));
        params.add(new Param("idCard", RSAUtils.encryptURLEncode(idCard)));
        WebHFActivity.startActivity(activity, Urls.hf_register, params, HfUpdateActivity.REQUEST_CODE_REGISTER);
    }

    /**
     * 汇付用户开通自动投标
     */
    public static void autoHf(final Activity activity) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(activity))));
        WebHFActivity.startActivity(activity, Urls.hf_auto, params, HfUpdateActivity.REQUEST_CODE_AUTO);
    }

    /**
     * 我的秒钱宝认购记录
     *
     * @param context
     * @param callback
     * @param projectCode 项目编号
     */
    public static void getUserCurrentProduct(Context context, final ICallback<SubscriptionRecordsResult> callback, String projectCode) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("projectCode", projectCode));
        new MyAsyncTask(context, Urls.getUserCurrentProduct, mList, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                SubscriptionRecordsResult meta = JsonUtil.parseObject(result, SubscriptionRecordsResult.class);
                if (TextUtils.equals(CODE_SUCCESS, meta.getCode())) {
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
