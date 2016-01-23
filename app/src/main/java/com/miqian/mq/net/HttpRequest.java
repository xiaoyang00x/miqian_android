package com.miqian.mq.net;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.AutoIdentyCardResult;
import com.miqian.mq.entity.BankBranchResult;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.CapitalRecordResult;
import com.miqian.mq.entity.CityInfoResult;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.entity.CurrentRecordResult;
import com.miqian.mq.entity.GetHomeActivityResult;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.entity.HomePageInfoResult;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.MessageInfoResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.OrderLianResult;
import com.miqian.mq.entity.ProducedOrderResult;
import com.miqian.mq.entity.ProjectInfoResult;
import com.miqian.mq.entity.PushDataResult;
import com.miqian.mq.entity.RedPaperData;
import com.miqian.mq.entity.RedeemData;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.RegularEarnResult;
import com.miqian.mq.entity.RegularPlanResult;
import com.miqian.mq.entity.RepaymentResult;
import com.miqian.mq.entity.RollOutResult;
import com.miqian.mq.entity.SubscribeOrderResult;
import com.miqian.mq.entity.TransferDetailResult;
import com.miqian.mq.entity.UpdateInfo;
import com.miqian.mq.entity.UpdateResult;
import com.miqian.mq.entity.UserCurrentResult;
import com.miqian.mq.entity.UserMessageResult;
import com.miqian.mq.entity.UserRegularDetailResult;
import com.miqian.mq.entity.UserRegularResult;
import com.miqian.mq.entity.WithDrawResult;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;

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
     * 活期首页
     */
    public static void getCurrentHome(Context context, final ICallback<CurrentInfoResult> callback) {
        List<Param> mList = new ArrayList<>();
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
     * 充值
     */
    public static void rollIn(Context context, final ICallback<String> callback, String amt, String bankNo, String realName, String idCard) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));
        mList.add(new Param("bankNo", RSAUtils.encryptURLEncode(bankNo)));
        mList.add(new Param("realName", RSAUtils.encryptURLEncode(realName)));
        mList.add(new Param("idCard", RSAUtils.encryptURLEncode(idCard)));

        new MyAsyncTask(context, Urls.roll_in, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if ("999991".equals(meta.getCode()) || "000000".equals(meta.getCode()) || "996633".equals(meta.getCode()) ) {
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
     * 充值结果查询
     */
    public static void rollInResult(Context context, final ICallback<OrderLianResult> callback, String orderNo) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("orderNo", orderNo));

        new MyAsyncTask(context, Urls.rollin_result, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                OrderLianResult orderLianResult = JsonUtil.parseObject(result, OrderLianResult.class);
                callback.onSucceed(orderLianResult);
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
    public static void getUserInfo(Context context, final ICallback<LoginResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

        new MyAsyncTask(context, Urls.user_info, mList, new ICallback<String>() {

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

    /**
     * 注册
     */
    public static void register(Context context, final ICallback<RegisterResult> callback,
                                String mobilePhone, String captcha, String password, String invitationCode) {
        List<Param> mList = new ArrayList<>();
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
        List<Param> mList = new ArrayList<>();
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
     * @param operationType 13001——注册  ；13002——找回密码 ；13003——重新绑定手机号第一次获取验证码 ；13004——重新绑定手机号第二次获取验证码
     *                      13005——银行卡信息补全        13006——修改银行卡         13007——非首次提现  13008——找回交易密码
     */
    public static void getCaptcha(Context context, final ICallback<Meta> callback, String phone, int operationType) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("mobilePhone", RSAUtils.encryptURLEncode(phone)));
        mList.add(new Param("operationType", "" + operationType));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));

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
    public static void getMessageList(Context context, String id, final ICallback<UserMessageResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("id", id));
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        new MyAsyncTask(context, Urls.getMessageList, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                UserMessageResult userMessageResult = JsonUtil.parseObject(result, UserMessageResult.class);
                if (userMessageResult.getCode().equals("000000")) {
                    callback.onSucceed(userMessageResult);
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
    public static void getPushList(Context context, String id, String page, final ICallback<PushDataResult> callback) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("id", id));
        mList.add(new Param("page", page));
        new MyAsyncTask(context, Urls.getPushList, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                PushDataResult pushDataResult = JsonUtil.parseObject(result, PushDataResult.class);
                if (pushDataResult.getCode().equals("000000")) {
                    callback.onSucceed(pushDataResult);
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
        mList.add(new Param("startId", startId ));
        mList.add(new Param("endId", endId ));
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
    public static void changePassword(Context context, final ICallback<LoginResult> callback,
                                      String oldPassword, String newPassword, String confirmPassword) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
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
        new MyAsyncTask(context, Urls.homeInfo, null, new ICallback<String>() {

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
     * 获取定期首页数据
     */
    public static void getMainRegular(Context context, final ICallback<GetRegularResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("operationType", "0"));
        params.add(new Param("pageNo", "1"));
        params.add(new Param("pageSize", "50"));
        new MyAsyncTask(context, Urls.getRegMain, params, new ICallback<String>() {

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
     * @param prodId    3：为获取定期赚 5：为获取定期计划
     * @param subjectId 标的编号，如传入则返回改标的相关信息
     */
    public static void getRegularDetails(Context context, String prodId, String subjectId, final ICallback<RegularPlanResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("prodId", prodId));
        params.add(new Param("subjectId", subjectId));
        new MyAsyncTask(context, Urls.getRegMain, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegularPlanResult regularPlanResult = JsonUtil.parseObject(result, RegularPlanResult.class);
                if (regularPlanResult.getCode().equals("000000")) {
                    callback.onSucceed(regularPlanResult);
                } else {
                    callback.onFail(regularPlanResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    /**
     * 获取定期赚详情
     *
     * @param prodId    3：为获取定期赚 5：为获取定期计划
     * @param subjectId 标的编号，如传入则返回改标的相关信息
     */
    public static void getRegularEarnDetails(Context context, String prodId, String subjectId,
                                             final ICallback<RegularEarnResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("prodId", prodId));
        params.add(new Param("subjectId", subjectId));
        new MyAsyncTask(context, Urls.getRegMain, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegularEarnResult meta = JsonUtil.parseObject(result, RegularEarnResult.class);
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
     * 获取定期计划详情
     */
    public static void getRegularPlanDetails(Context context,
                                             final ICallback<GetRegularResult> callback) {
        new MyAsyncTask(context, Urls.getRegMain, null, new ICallback<String>() {

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
     * 项目匹配
     *
     * @param pageNo     页码
     * @param pageSize   每页条数
     * @param peerCustId 默认不填,(活期赚不填写) 定期计划的匹配项目填:1372
     */
    public static void projectMatch(Context context, final ICallback<ProjectInfoResult> callback, String pageNo, String pageSize, String peerCustId) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("pageNo", pageNo));
        mList.add(new Param("pageSize", pageSize));
        mList.add(new Param("peerCustId", peerCustId));

        new MyAsyncTask(context, Urls.project_match, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                ProjectInfoResult projectInfoResult = JsonUtil.parseObject(result, ProjectInfoResult.class);
                if (projectInfoResult.getCode().equals("000000")) {
                    callback.onSucceed(projectInfoResult);
                } else {
                    callback.onFail(projectInfoResult.getMessage());
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
     * @param prodId    0:充值产品  1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
     * @param subjectId 0:活期
     */
    public static void subjectIdOrder(Context context, final ICallback<SubscribeOrderResult> callback, String amt, String prodId, String payPassword, String subjectId, String promList, String prodList) {
        List<Param> mList = new ArrayList<>();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));
        mList.add(new Param("prodId", prodId));
        mList.add(new Param("payPassword", RSAUtils.encryptURLEncode(payPassword)));
        mList.add(new Param("subjectId", subjectId));
        mList.add(new Param("promList", promList));
        mList.add(new Param("prodList", prodList));
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
     *
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
     * @param activityId 活动 ID
     * @param activityPlanId   计划 ID
     */
    public static void getActivityFeedback(Context context, String activityId, String activityPlanId, final ICallback<Meta> callback) {
        ArrayList params = new ArrayList<>();
        String custId = Pref.getString(Pref.USERID, context, null);
        if (!TextUtils.isEmpty(custId)) {
            params.add(new Param("custId",  RSAUtils.encryptURLEncode(custId)));
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

}
