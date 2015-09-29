package com.miqian.mq.net;

import android.content.Context;
import android.util.Log;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.BankBranchResult;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.CityInfoResult;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.MessageInfoResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.OrderLianResult;
import com.miqian.mq.entity.PayOrderResult;
import com.miqian.mq.entity.ProducedOrderResult;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.RegularEarnResult;
import com.miqian.mq.entity.RegularPlanResult;
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
    public static void setIDCardCheck(Context context, final ICallback<Meta> callback, String idNo, final String realName) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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
     * 活期、定期赚、定期计划
     * 认购订单生成页面
     * @param amt  金额
     * @param prodId 0:充值产品  1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
     * @param callback
     */
    public static void getProduceOrder(Context context, final ICallback<ProducedOrderResult> callback, String amt, String prodId) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));
        mList.add(new Param("prodId", prodId));
        new MyAsyncTask(context, Urls.order_produced, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                ProducedOrderResult producedOrderResult = JsonUtil.parseObject(result, ProducedOrderResult.class);
                if (producedOrderResult.getCode().equals("000000")) {
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
     *
     * @param callback
     */
    public static void rollIn(Context context, final ICallback<PayOrderResult> callback, String amt, String bankNo) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("custId", RSAUtils.encryptURLEncode(UserUtil.getUserId(context))));
        mList.add(new Param("amt", amt));
//        mList.add(new Param("bankCode", bankCode));
        mList.add(new Param("bankNo", RSAUtils.encryptURLEncode(bankNo)));

        new MyAsyncTask(context, Urls.roll_in, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
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
     * 充值结果查询
     *
     * @param callback
     */
    public static void rollInResult(Context context, final ICallback<OrderLianResult> callback, String orderNo) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("orderNo", orderNo));

        new MyAsyncTask(context, Urls.rollin_result, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                Log.e("", result);
                OrderLianResult orderLianResult = JsonUtil.parseObject(result, OrderLianResult.class);
                callback.onSucceed(orderLianResult);
//                if (payOrderResult.getCode().equals("000000")) {
//                } else {
//                    callback.onFail(payOrderResult.getMessage());
//                }
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
     */
    public static void checkCaptcha(Context context, final ICallback<Meta> callback, String phone, int operationType,String captcha) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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
     * @param context
     * @param callback
     * @param phone
     * @param operationType 13001——注册  ；13002——找回密码 ；13003——重新绑定手机号第一次获取验证码 ；13004——重新绑定手机号第二次获取验证码
     *                      13005——银行卡信息补全        13006——修改银行卡         13007——非首次提现  13008——找回交易密码
     */
    public static void getCaptcha(Context context, final ICallback<Meta> callback, String phone, int operationType) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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
    public static void setPayPassword(Context context, final ICallback<Meta> callback, String payPassword, String confirmPayPassword) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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
    public static void changePayPassword(Context context, final ICallback<Meta> callback, String tradeType, String idCard, String mobilePhone, String captcha, String payPassword, String confirmPayPassword
    ) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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
    public static void changePassword(Context context, final ICallback<LoginResult> callback,  String oldPassword, String newPassword, String confirmPassword) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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


  /**
   * 获取首页信息
   *
   * @param callback
   */
  public static void getHomePageInfo(Context context, final ICallback<HomePageInfo> callback) {
    if (mList == null) {
      mList = new ArrayList<Param>();
    }
    mList.clear();
    //27.154.228.194:30001/commonService/getHome
    //http://10.0.1.193:9000 + "/jsonRes"
    new MyAsyncTask(context, "http://27.154.228.194:30001/commonService/getHome", mList, new ICallback<String>() {

      @Override
      public void onSucceed(String result) {
        Log.e("", result);
        HomePageInfo info = JsonUtil.parseObject(result, HomePageInfo.class);
        if (info.getCode() == 0) {
          callback.onSucceed(info);
        } else {
          callback.onFail(info.getMessage());
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
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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
    public static void autoIdentifyBankCard(Context context, final ICallback<BankCardResult> callback,String bankNo) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
        mList.add(new Param("bankNo", RSAUtils.encryptURLEncode(bankNo)));

        new MyAsyncTask(context, Urls.autoIdentifyBankCard, mList, new ICallback<String>() {

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

    //绑定银行卡
    public static void bindBank(Context context, final ICallback<Meta> callback,String bankNo,String tradeType, String bankCode,String bankName,String branchName,String prov,String city) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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
     * @param context
     * @param callback
     */
    public static void getMainRegular(Context context, final ICallback<GetRegularResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("operationType", "0"));
        params.add(new Param("pageNo", "1"));
        params.add(new Param("pageSize", "50"));
        new MyAsyncTask(context, Urls.getRegMain,  params, new ICallback<String>() {

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
     * @param context
     * @param operationType 0为获取定期赚和定期计划，1为获取定期赚，2为获取定期计划。
     * @param subjectId     标的编号，如传入则返回改标的相关信息
     * @param callback
     */
    public static void getRegularDetails(Context context, String operationType, String subjectId, final ICallback<RegularPlanResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("operationType", operationType));
        params.add(new Param("subjectId", subjectId));
        new MyAsyncTask(context, Urls.getRegMain, params, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                RegularPlanResult meta = JsonUtil.parseObject(result, RegularPlanResult.class);
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
     * 获取定期赚详情
     * @param context
     * @param operationType 0为获取定期赚和定期计划，1为获取定期赚，2为获取定期计划。
     * @param subjectId     标的编号，如传入则返回改标的相关信息
     * @param callback
     */
    public static void getRegularEarnDetails(Context context, String operationType, String subjectId, final ICallback<RegularEarnResult> callback) {
        ArrayList params = new ArrayList<>();
        params.add(new Param("operationType", operationType));
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
     * @param context
     * @param callback
     */
    public static void getRegularPlanDetails(Context context, final ICallback<GetRegularResult> callback) {
        new MyAsyncTask(context, Urls.getRegMain,  null, new ICallback<String>() {

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
    public static void getAllCity(Context context, final ICallback<CityInfoResult> callback) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();

        new MyAsyncTask(context, Urls.getAllCity, mList, new ICallback<String>() {

            @Override
            public void onSucceed(String result) {
                CityInfoResult cityInfoResult = JsonUtil.parseObject(result, CityInfoResult.class);
                if (cityInfoResult.getCode().equals("000000")) {
                    callback.onSucceed(cityInfoResult);
                } else {
                    callback.onFail(cityInfoResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        }).executeOnExecutor();
    }

    //获取支行接口
    public static void getSubBranch(Context context, final ICallback<BankBranchResult> callback,String provinceName,String city,String bankCode) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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
     *
     * @param callback
     */
    public static void loginOut(Context context, final ICallback<Meta> callback) {
        if (mList == null) {
            mList = new ArrayList<Param>();
        }
        mList.clear();
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

}
