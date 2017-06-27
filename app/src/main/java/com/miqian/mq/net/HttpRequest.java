package com.miqian.mq.net;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.miqian.mq.activity.WebBankActivity;
import com.miqian.mq.activity.rollin.IntoActivity;
import com.miqian.mq.activity.save.SaveBindAcitvity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.AutoIdentyCardResult;
import com.miqian.mq.entity.BankBranchResult;
import com.miqian.mq.entity.BankCardInfoResult;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.CapitalRecordResult;
import com.miqian.mq.entity.CaptchaResult;
import com.miqian.mq.entity.CityInfoResult;
import com.miqian.mq.entity.ConfigResult;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.entity.CurrentRecordResult;
import com.miqian.mq.entity.ErrorLianResult;
import com.miqian.mq.entity.GetHomeActivityResult;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.entity.HomePageInfoResult;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.MessageInfoResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.OperationResult;
import com.miqian.mq.entity.OrderRechargeResult;
import com.miqian.mq.entity.ProducedOrderResult;
import com.miqian.mq.entity.PushDataResult;
import com.miqian.mq.entity.RedPaperData;
import com.miqian.mq.entity.RedeemData;
import com.miqian.mq.entity.RegTransDetailResult;
import com.miqian.mq.entity.RegTransFerredDetailResult;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularDetailResult;
import com.miqian.mq.entity.RegularProjectListResult;
import com.miqian.mq.entity.RegularTransferListResult;
import com.miqian.mq.entity.RepaymentResult;
import com.miqian.mq.entity.RollOutResult;
import com.miqian.mq.entity.SubscribeOrderResult;
import com.miqian.mq.entity.TransferDetailResult;
import com.miqian.mq.entity.UpdateResult;
import com.miqian.mq.entity.UserCurrentResult;
import com.miqian.mq.entity.UserInfoResult;
import com.miqian.mq.entity.UserMessageResult;
import com.miqian.mq.entity.UserRegularDetailResult;
import com.miqian.mq.entity.UserRegularResult;
import com.miqian.mq.entity.WithDrawResult;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jackie on 2015/9/4.
 */
public class HttpRequest {

    /**
     * 身份认证
     */
    public static void setIDCardCheck(Context context, final ICallback<Meta> callback, String idNo, final String realName) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("idNo", RSAUtils.encryptURLEncode(idNo)));
        mList.add(new Param("realName", RSAUtils.encryptURLEncode(realName)));

        new MyAsyncTask(context, Urls.idcard_check, mList, new ICallback<String>() {

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

    /**
     * APP的配置：广告，tab图标
     */
    public static void getConfig(Context context, final ICallback<ConfigResult> callback) {
        List<Param> mList = new ArrayList<>();
        new MyAsyncTask(context, Urls.app_config, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                ConfigResult configResult = JsonUtil.parseObject(result, ConfigResult.class);
                if (configResult.getCode().equals("000000")) {
                    callback.onSucceed(configResult);
                } else {
                    callback.onFail(configResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 活期首页
     */
    public static void getCurrentHome(Context context, final ICallback<CurrentInfoResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
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
     * 活期、定期赚、定期计划
     * 认购订单生成页面
     *
     * @param amt    金额
     * @param prodId 0:充值产品  1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
     */
    public static void getProduceOrder(Context context, final ICallback<ProducedOrderResult> callback, String amt, String subjectId, String prodId) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));
        mList.add(new Param("subjectId", subjectId));
        mList.add(new Param("prodId", prodId));
        new MyAsyncTask(context, Urls.order_produced, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                ProducedOrderResult producedOrderResult = JsonUtil.parseObject(result, ProducedOrderResult.class);
                if ("000000".equals(producedOrderResult.getCode()) || "996633".equals(producedOrderResult.getCode())) {
                    callback.onSucceed(producedOrderResult);
                } else {
                    callback.onFail(producedOrderResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 充值预处理
     */
    public static void rollInPreprocess(Context context, final ICallback<String> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.roll_in_preprocess, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if ("999991".equals(meta.getCode()) || "000000".equals(meta.getCode()) || "996633".equals(meta.getCode())) {
                    callback.onSucceed(result);
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
     * 充值
     */
    public static void rollIn(Context context, final ICallback<OrderRechargeResult> callback, String amt, String bankNo, String captcha) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));
        mList.add(new Param("bankNo", RSAUtils.encryptURLEncode(bankNo)));
        mList.add(new Param("captcha", "captcha"));

        new MyAsyncTask(context, Urls.roll_in, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                OrderRechargeResult orderRechargeResult = JsonUtil.parseObject(result, OrderRechargeResult.class);
                if (orderRechargeResult.getCode().equals("000000")) {
                    callback.onSucceed(orderRechargeResult);
                } else {
                    callback.onFail(orderRechargeResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 充值结果查询
     */
    public static void rollInResult(Context context, final ICallback<Meta> callback, String timeType, String transferType) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("timeType", timeType));
        mList.add(new Param("transferType", transferType));

        new MyAsyncTask(context, Urls.rollin_result, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if (meta.getCode().equals("000000")) {
                    callback.onSucceed(meta);
                } else {
                    callback.onFail(meta.getMessage());
                }
//                OrderLianResult orderLianResult = JsonUtil.parseObject(result, OrderLianResult.class);
//                callback.onSucceed(orderLianResult);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 充值失败原因上传
     */
    public static void rollInError(final Context context, String orderNo, String error) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("orderNo", orderNo));
        mList.add(new Param("llJson", error));
        mList.add(new Param("llErrorCodeVersion", Pref.getString(Pref.ERROR_LIAN_VERSION, context, IntoActivity.showErrorString(context, "llErrorCodeVersion"))));

        new MyAsyncTask(context, Urls.rollin_error, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                ErrorLianResult errorLianResult = JsonUtil.parseObject(result, ErrorLianResult.class);
                String errorData = errorLianResult.getData();
                Map<String, String> userMap = null;
                if (!TextUtils.isEmpty(errorData)) {
                    userMap = JSON.parseObject(errorData, new TypeReference<Map<String, String>>() {
                    });
                    if (userMap.size() > 0) {
                        Pref.saveString(Pref.ERROR_LIAN, errorData, context);
                        Pref.saveString(Pref.ERROR_LIAN_VERSION, userMap.get("llErrorCodeVersion"), context);
                    }
                }
            }

            @Override
            public void onFail(String error) {
            }
        }).executeOnExecutor();
    }

    /**
     * 获取用户信息
     */
    public static void getUserInfo(Context context, final ICallback<UserInfoResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.user_info, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserInfoResult userInfoResult = JsonUtil.parseObject(result, UserInfoResult.class);
                if (userInfoResult.getCode().equals("000000")) {
                    callback.onSucceed(userInfoResult);
                } else {
                    callback.onFail(userInfoResult.getMessage());
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
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("mobile", RSAUtils.encryptURLEncode(mobilePhone)));
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

    public static void checkCaptcha(Context context, final ICallback<Meta> callback, String phone, int operationType, String captcha) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("mobile", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("operationType", "" + operationType));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
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

    public static void getCaptcha(Context context, final ICallback<CaptchaResult> callback, String phone, int operationType) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("mobile", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("operationType", "" + operationType));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        new MyAsyncTask(context, Urls.getCaptcha, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                CaptchaResult meta = JsonUtil.parseObject(result, CaptchaResult.class);
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

    //获得个人消息
    public static void getMessageList(final Context context, String id, final ICallback<UserMessageResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("id", id));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        new MyAsyncTask(context, Urls.getMessageList, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserMessageResult userMessageResult = JsonUtil.parseObject(result, UserMessageResult.class);
                if (userMessageResult.getCode().equals("000000")) {
                    callback.onSucceed(userMessageResult);
                    //保存数据到本地，缓存用
                    Pref.saveString(Pref.DATA_MESSAGE, result, context);
                } else {
                    callback.onFail(userMessageResult.getMessage());
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
                PushDataResult pushDataResult = JsonUtil.parseObject(result, PushDataResult.class);
                if (pushDataResult.getCode().equals("000000")) {
                    callback.onSucceed(pushDataResult);
                    //保存数据到本地，缓存用
                    Pref.saveString(Pref.DATA_PUSH, result, context);
                } else {
                    callback.onFail(pushDataResult.getMessage());
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

    //删除消息
    public static void deleteMessage(Context context, final ICallback<Meta> callback, int startId, String endId, int isAll) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("startId", startId + ""));
        mList.add(new Param("endId", endId + ""));
        mList.add(new Param("isAll", isAll + ""));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        new MyAsyncTask(context, Urls.deleteMessage, mList, new ICallback<String>() {

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

    //设置交易密码
    public static void setPayPassword(Context context, final ICallback<Meta> callback,
                                      String payPassword, String confirmPayPassword) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
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
    public static void changePayPassword(Context context, final ICallback<Meta> callback,
                                         String tradeType, String idCard, String mobilePhone, String captcha, String payPassword,
                                         String confirmPayPassword) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
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
    public static void changePassword(Context context, final ICallback<Meta> callback,
                                      String oldPassword, String newPassword, String confirmPassword) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("oldPassword", RSAUtils.encryptURLEncode(oldPassword)));
        mList.add(new Param("newPassword", RSAUtils.encryptURLEncode(newPassword)));
        mList.add(new Param("confirmPassword", RSAUtils.encryptURLEncode(confirmPassword)));

        new MyAsyncTask(context, Urls.changePassword, mList, new ICallback<String>() {

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

    //找回登录密码
    public static void getPassword(Context context, final ICallback<Meta> callback,
                                   String mobilePhone, String newPassword, String confirmPassword, String captcha) {
        List<Param> mList = new ArrayList<>();
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
                HomePageInfoResult homePageInfoResult = JSON.parseObject(result, HomePageInfoResult.class);

                if (homePageInfoResult.getCode().equals("000000")) {
                    callback.onSucceed(homePageInfoResult);
                } else {
                    callback.onFail(homePageInfoResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //获取用户的银行卡
    public static void getUserBankCard(Context context, final ICallback<BankCardResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.getUserBankCard, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                BankCardResult bankCardResult = JsonUtil.parseObject(result, BankCardResult.class);
                if (bankCardResult.getCode().equals("000000")) {
                    callback.onSucceed(bankCardResult);
                } else {
                    callback.onFail(bankCardResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //识别银行卡
    public static void autoIdentifyBankCard(Context context, final ICallback<AutoIdentyCardResult> callback,
                                            String bankNo) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("bankNo", RSAUtils.encryptURLEncode(bankNo)));

        new MyAsyncTask(context, Urls.autoIdentifyBankCard, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                AutoIdentyCardResult autoIdentyCardResult = JsonUtil.parseObject(result, AutoIdentyCardResult.class);
                if (autoIdentyCardResult.getCode().equals("000000")) {
                    callback.onSucceed(autoIdentyCardResult);
                } else {
                    callback.onFail(autoIdentyCardResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //绑定银行卡
    public static void bindBank(Context context, final ICallback<Meta> callback, String bankNo,
                                String tradeType, String bankCode, String bankName, String branchName, String prov,
                                String city) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("bankNo", RSAUtils.encryptURLEncode(bankNo)));
        mList.add(new Param("tradeType", tradeType));
        mList.add(new Param("bankCode", bankCode));
        mList.add(new Param("bankName", bankName));
        mList.add(new Param("branchName", branchName));
        mList.add(new Param("prov", prov));
        mList.add(new Param("city", city));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        new MyAsyncTask(context, Urls.bindBank, mList, new ICallback<String>() {

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

    /**
     * 获取定期首页列表数据
     */
    public static void getRegularProjectList(Context context, final ICallback<RegularProjectListResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("pageNo", "1"));
        params.add(new Param("pageSize", "50"));
        new MyAsyncTask(context, Urls.REGULA_PROJECT, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegularProjectListResult meta = JsonUtil.parseObject(result, RegularProjectListResult.class);
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

    /**
     * 获取红包定期列表
     */
    public static void getFitSubject(Context context, String promId, final ICallback<GetRegularResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("promId", promId));
        new MyAsyncTask(context, Urls.getFitSubject, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                GetRegularResult meta = JsonUtil.parseObject(result, GetRegularResult.class);
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

    /**
     * 获取定期详情
     *
     * @param subjectId 标的编号，如传入则返回改标的相关信息
     */
    public static void getRegularDetail(Context context, String subjectId, int prodId, final ICallback<RegularDetailResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("subjectId", subjectId));
        params.add(new Param("pageNo", "1"));
        params.add(new Param("pageSize", "50"));
        String url = prodId == RegularBase.REGULAR_03 || prodId == RegularBase.REGULAR_05 ?
                Urls.REGULA_PROJECT : Urls.REGULA_PROJECT_TRANSFER;
        new MyAsyncTask(context, url, params, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                RegularDetailResult meta = JsonUtil.parseObject(result, RegularDetailResult.class);
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

    //获取银行列表
    public static void getAllCity(final Context context, final ICallback<CityInfoResult> callback) {
        List<Param> mList = new ArrayList<>();

        new MyAsyncTask(context, Urls.getAllCity, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                //保存到本地数据库
                Pref.saveString(Pref.CITY, result, context);
                CityInfoResult cityInfoResult = JsonUtil.parseObject(result, CityInfoResult.class);
                if (cityInfoResult.getCode().equals("000000")) {
                    callback.onSucceed(cityInfoResult);
                } else {
                    callback.onFail(cityInfoResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                String city = Pref.getString(Pref.CITY, context, "");
                if (!TextUtils.isEmpty(city)) {
                    CityInfoResult cityInfoResult = JsonUtil.parseObject(city, CityInfoResult.class);
                    if (cityInfoResult.getCode().equals("000000")) {
                        callback.onSucceed(cityInfoResult);
                    } else {
                        callback.onFail(cityInfoResult.getMessage());
                    }
                } else {
                    callback.onFail(error);
                }

            }
        }).executeOnExecutor();
    }

    //获取支行接口
    public static void getSubBranch(Context context, final ICallback<BankBranchResult> callback,
                                    String provinceName, String city, String bankCode) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("provinceName", provinceName));
        mList.add(new Param("city", city));
        mList.add(new Param("bankCode", bankCode));
        new MyAsyncTask(context, Urls.getSubBranch, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                BankBranchResult bankBranchResult = JsonUtil.parseObject(result, BankBranchResult.class);
                if (bankBranchResult.getCode().equals("000000")) {
                    callback.onSucceed(bankBranchResult);
                } else {
                    callback.onFail(bankBranchResult.getMessage());
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

    /**
     * 提现
     */
    public static void withdrawCash(Context context, final ICallback<RollOutResult> callback, String amt,
                                    String bankCode, String bankNo, String payPassword) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("bankNo", RSAUtils.encryptURLEncode(bankNo)));
        mList.add(new Param("payPassword", RSAUtils.encryptURLEncode(payPassword)));
        mList.add(new Param("amt", amt));
        mList.add(new Param("bankCode", bankCode));

        new MyAsyncTask(context, Urls.withdrawCash, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RollOutResult rollOutResult = JsonUtil.parseObject(result, RollOutResult.class);
                callback.onSucceed(rollOutResult);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 我的活期
     */
    public static void getUserCurrent(Context context, final ICallback<UserCurrentResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.user_current, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserCurrentResult userCurrentResult = JsonUtil.parseObject(result, UserCurrentResult.class);
                if (userCurrentResult.getCode().equals("000000")) {
                    callback.onSucceed(userCurrentResult);
                } else {
                    callback.onFail(userCurrentResult.getMessage());
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
     * @param isForce  默认为0 1 强制刷新  0 不强制刷新
     */
    public static void getUserRegular(Context context, final ICallback<UserRegularResult> callback, String pageNo, String pageSize, String clearYn, String isForce) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("pageNo", pageNo));
        mList.add(new Param("pageSize", pageSize));
        mList.add(new Param("clearYn", clearYn));
        mList.add(new Param("isForce", isForce));

        new MyAsyncTask(context, Urls.user_regular, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserRegularResult userRegularResult = JsonUtil.parseObject(result, UserRegularResult.class);
                if (userRegularResult.getCode().equals("000000")) {
                    callback.onSucceed(userRegularResult);
                } else {
                    callback.onFail(userRegularResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 获取资金记录
     */
    public static void getCapitalRecords(Context context, final ICallback<CapitalRecordResult> callback, String pageNo, String pageSize, String operateType) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("pageNo", pageNo));
        mList.add(new Param("startDate", ""));
        mList.add(new Param("pageSize", pageSize));
        mList.add(new Param("endDate", ""));
        mList.add(new Param("operateType", operateType));

        new MyAsyncTask(context, Urls.recordsCapital, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                CapitalRecordResult capitalRecord = JsonUtil.parseObject(result, CapitalRecordResult.class);
                if (capitalRecord.getCode().equals("000000")) {
                    callback.onSucceed(capitalRecord);
                } else {
                    callback.onFail(capitalRecord.getMessage());
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
                UserRegularDetailResult userRegularDetailResult = JsonUtil.parseObject(result, UserRegularDetailResult.class);
                if (userRegularDetailResult.getCode().equals("000000")) {
                    callback.onSucceed(userRegularDetailResult);
                } else {
                    callback.onFail(userRegularDetailResult.getMessage());
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
                TransferDetailResult transferDetailResult = JsonUtil.parseObject(result, TransferDetailResult.class);
                if (transferDetailResult.getCode().equals("000000")) {
                    callback.onSucceed(transferDetailResult);
                } else {
                    callback.onFail(transferDetailResult.getMessage());
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
                RepaymentResult repaymentResult = JsonUtil.parseObject(result, RepaymentResult.class);
                if (repaymentResult.getCode().equals("000000")) {
                    callback.onSucceed(repaymentResult);
                } else {
                    callback.onFail(repaymentResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 活期、定期赚、定期计划
     * 认购接口
     *
     * @param amt       金额
     * @param subjectId 标的id
     * @param promList  红包列表
     */
    public static void subscribeOrder(Context context, final ICallback<SubscribeOrderResult> callback, String amt, String subjectId, String promList) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));
        mList.add(new Param("subjectId", subjectId));
        mList.add(new Param("promList", promList));
        new MyAsyncTask(context, Urls.subscribe_order, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                SubscribeOrderResult subscribeOrderResult = JsonUtil.parseObject(result, SubscribeOrderResult.class);
                callback.onSucceed(subscribeOrderResult);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 活期、定期赚、定期计划
     * 快捷认购接口
     *
     * @param amt       金额
     * @param prodId    0:充值产品  1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
     * @param subjectId 0:活期
     */
    public static void subscribeQuickOrder(Context context, final ICallback<SubscribeOrderResult> callback, String amt, String prodId, String orderNo, String subjectId, String promList, String prodList) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));
        mList.add(new Param("prodId", prodId));
        mList.add(new Param("orderNo", orderNo));
        mList.add(new Param("subjectId", subjectId));
        mList.add(new Param("promList", promList));
        mList.add(new Param("prodList", prodList));
        new MyAsyncTask(context, Urls.quick_subscribe_order, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                SubscribeOrderResult subscribeOrderResult = JsonUtil.parseObject(result, SubscribeOrderResult.class);
                callback.onSucceed(subscribeOrderResult);
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

    //我的促销接口，包括红包，拾财券等
    public static void getCustPromotion(Context context, final ICallback<RedPaperData> callback, String state, String pageNum, String pageSize) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("promTypCd", ""));
        mList.add(new Param("sta", state));
        mList.add(new Param("pageNum", pageNum));
        mList.add(new Param("pageSize", pageSize));
        new MyAsyncTask(context, Urls.getCustPromotion, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RedPaperData redPaperData = JsonUtil.parseObject(result, RedPaperData.class);
                if (redPaperData.getCode().equals("000000")) {
                    callback.onSucceed(redPaperData);
                } else {
                    callback.onFail(redPaperData.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //赎回
    public static void redeem(Context context, final ICallback<RedeemData> callback, String amt, String payPassword) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("payPassword", RSAUtils.encryptURLEncode(payPassword)));
        mList.add(new Param("amt", amt));
        new MyAsyncTask(context, Urls.redeem, mList, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                RedeemData redeemResult = JsonUtil.parseObject(result, RedeemData.class);
                callback.onSucceed(redeemResult);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //getMyCurrentRecord
    public static void getMyCurrentRecord(Context context, final ICallback<CurrentRecordResult> callback,
                                          String pageNo, String pageSize, String isForce) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("pageNo", pageNo));
        mList.add(new Param("pageSize", pageSize));
        mList.add(new Param("isForce", isForce));
        new MyAsyncTask(context, Urls.getMyCurrentRecord, mList, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                CurrentRecordResult currentRecordResult = JsonUtil.parseObject(result, CurrentRecordResult.class);
                if (currentRecordResult.getCode().equals("000000")) {
                    callback.onSucceed(currentRecordResult);
                } else {
                    callback.onFail(currentRecordResult.getMessage());
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


    //赎回预处理
    public static void withdrawPreprocess(Context context, final ICallback<WithDrawResult> callback, String amt) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));

        new MyAsyncTask(context, Urls.withdrawPreprocess, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                WithDrawResult drawResult = JsonUtil.parseObject(result, WithDrawResult.class);
                if (drawResult.getCode().equals("000000")) {
                    callback.onSucceed(drawResult);
                } else {
                    callback.onFail(drawResult.getMessage());
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

    /**
     * 版本是否强制更新(正常更新为友盟更新)
     */
    public static void forceUpdate(final Context context, final ICallback<UpdateResult> callback) {
        new MyAsyncTask(context, Urls.forceUpdate, null, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UpdateResult updateResult = JsonUtil.parseObject(result, UpdateResult.class);
                if ("000000".equals(updateResult.getCode())) {
                    callback.onSucceed(updateResult);
//                    UpdateInfo updateInfo = updateResult.getData();
//                    if ("2".equals(updateInfo.getUpgradeSign())) {
//                        Pref.saveString(Pref.FORCE_UPDATE, MobileOS.getClientVersion(context), context);
//                    }
                } else {
                    callback.onFail(updateResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }


//江西银行跳转接口

    /**
     * 江西银行充值接口
     */
    public static void rollinJx(final Activity activity, String amt, String bankNo, String captcha, String authCode) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("custId", "125145556"));
//        params.add(new Param("hfCustId", RSAUtils.encryptURLEncode(hfCustId)));
        params.add(new Param("amt", amt));
        params.add(new Param("bankNo", bankNo));
        params.add(new Param("captcha", captcha));
        params.add(new Param("authCode", authCode));
        params.add(new Param("cType", "android"));
        WebBankActivity.startActivity(activity, Urls.jx_rollin_url, params, 1);
//        WebBankActivity.startActivity(activity, Urls.jx_rollin_url, params, HfUpdateActivity.REQUEST_CODE_ROLLIN);
    }

    /**
     * 江西银行充值接口
     * authCode 授权码 (获取短信验证码返回)
     */
    public static void openJx(Context context, final ICallback<Meta> callback, String idCard, String userName, String mobile, String bankCardNo, String authCode, String captcha) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        params.add(new Param("idCard", idCard));
        params.add(new Param("userName", userName));
        params.add(new Param("mobile", mobile));
        params.add(new Param("bankCardNo", bankCardNo));
        params.add(new Param("authCode", authCode));
        params.add(new Param("captcha", captcha));

        new MyAsyncTask(context, Urls.jx_open, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if ("000000".equals(meta.getCode())) {
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
     * 标的相关记录
     */
    public static void jxCustCardInfo(Context context, final ICallback<BankCardInfoResult> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId", RSAUtils.encryptURLEncode(custId)));
        }
        new MyAsyncTask(context, Urls.jx_custbankcardinfo, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                BankCardInfoResult data = JsonUtil.parseObject(result, BankCardInfoResult.class);
                if (data.getCode().equals("000000")) {
                    callback.onSucceed(data);
                } else {
                    callback.onFail(data.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }
}
