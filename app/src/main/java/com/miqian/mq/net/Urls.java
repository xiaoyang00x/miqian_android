package com.miqian.mq.net;

import android.content.Context;
import android.text.TextUtils;

import com.miqian.mq.utils.Config;
import com.miqian.mq.utils.Pref;

/**
 * Created by Administrator on 2015/9/4.
 */
public class Urls {

    /**
     * 测试key
     */
    public static final String RSA_PUBLICE_TEST = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCU2DBwdq+RCEL2hmR3c4Cs9Uno" + "\r"
            + "iadyKamYWTPpWSivJfLifXQWTED09z9KjK3nCjnFqquU7j7CmQwuEx9c9SkvjE+P" + "\r"
            + "NpD/23buQ31yIB41bVb0dDDjTLLqVd6aCbC85jJQ34qq6jNoMBs/IFTFBvjO++nE" + "\r"
            + "dRnBiq4YHLVtD/qbhwIDAQAB";

    public static final String RSA_PRIVATE_TEST = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM+59Lcccqp9r3yk" + "\r"
            + "CRP2pSB6o9EP+igdwxTXFnVIwTg7N9cbIcsCZmfnZGm2OF4G3fx8dVSl5LfQo7sr" + "\r"
            + "TZ2iaDayq0pcqMr/pzft1KxOqV2JF966uJf+dINO2T7gIimG1XGg4NNQQC2AUeQQ" + "\r"
            + "cBpfqwa6uUUMLzus/Q7JfQ/5Idt7AgMBAAECgYB1pW7Iai8zCf3ijJCroswAqhSv" + "\r"
            + "aeoW4ExFOE/62tq7uyY0CKTJ8jEHddCz4kToU+FThzV4XIA97sdcUPkjZwC3vBm0" + "\r"
            + "GQHAqxnpBJxQ85Z8YmJIhvpg2KU3TKtYn9p7M/jEQDxV7jw5lCbfnbiC34EXyJ7C" + "\r"
            + "PE31u3pSc3LiMl1JOQJBAPXH7NSSb7srinHwnaApEDms3AVbSn4Ucftd/dGFALgT" + "\r"
            + "9yDE7zYe/8T5yftQ5ivxxbVHJmS6dNGDLVnHRe3YdNUCQQDYXPsQRZOMhr1kWUUU" + "\r"
            + "bjfMjfc62ElLR+hREeYVNPGWFRq76qiHHdzHIND2T8ayUDyTGg0ydYrHzh9VJaRg" + "\r"
            + "lHcPAkEA7FtGtWXdbPGCQfBSpSLYJ57EN6KDEJw4wLKy1DpDZsJMdYbtQ+6rqJFO" + "\r"
            + "XBCASOJIbvvtQBDPJmwvwJaR+RifwQJAd+WGpyy3KA9ekmY5tqmhODSPhUdnzlTJ" + "\r"
            + "s2skwim1moKVbv8JZt6wgpR315a7uAJonue32ndycWiSyl9yqKZ9FwJAFDNB7i0w" + "\r"
            + "7vhqwHmFAXxnffRtEVZbJk2AsEcE0qJqwoxGbij+O1+OIksDGlxjc/LgnXLlXvvY" + "\r"
            + "ZU0zSqeXVAcdRQ==";

    /**
     * 正式key
     */
    public static final String RSA_PUBLICE_ONLINE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC84iihAQA8kxa4eWiQwZ6qLQpJ" + "\r"
            + "wZC0yUpQB5qh3ZKeYbR2Nx8GK8EEmoQg4XMK+HFgrrzji8PTnv69h0MBqN3Jg1Ul" + "\r"
            + "bLQLzF0xjicDjgA2YAWYRFQ3JBM5MjsM+kkLme++03dtpBlFLXVXBltTEoxeU41e" + "\r"
            + "Pt94rFg+wNta0BNX9QIDAQAB";

    public static final String RSA_PRIVATE_ONLINE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANfUTvSfntVeUkvb" + "\r"
            + "U8MLLQnkOPa61ya/IVJj448GO765ul2HxWHIGUVa0ibGIAG/uh0bE5lHHTjSQu8r" + "\r"
            + "2RmH++qK1fMZRbRpjjDws+iZ/oSN8un8A99yy6ToDN+9bCVePxDFtGeJDqze+C1W" + "\r"
            + "wr7PZMeaxzjeP5oW+6F5iNyi0lcBAgMBAAECgYEAyqV/VNQisDgVkT/nz7RIn4b3" + "\r"
            + "rUIVDT5cLR245et2YUfFolf+jibxA1uZAer41muMdhOcPUi2OUnCqwpCl2REtGL3" + "\r"
            + "Kfumfngw0YIotN9lFstnB9vFv61WLgUyEvDlsmzId5lk3VSJ8wM++p9GCI6Kh4Mc" + "\r"
            + "gF7EViONzLZHIh86i9kCQQD9DOoRU6ezNU8N+L7Fttm/9YhTq8+C6luFw1PIzuLT" + "\r"
            + "n5mwStqexybXb4Rc2wdyhjSUCpGbi4jLTC6uYBj36BTTAkEA2lhUKaZ5HluSOFuH" + "\r"
            + "yPjv1rGgHfUVKGkqLyxmKMglqsDcy5cJQr8+BJjArjfca5vo0C/e9BIcj+xBGCRg" + "\r"
            + "sUtQWwJBAMZrJe5IJisDFeXisdPeOIeoQyk4gxIoKXONYVkHwBwn/R6a0ynFBtPO" + "\r"
            + "gX89z20hhoC+eMX6d1zRc5kPiQR6WjkCQQCAEV8MLwDLtZpGRCmYz6qdmwH3kFEg" + "\r"
            + "qs57bOde9EZjqUvYfnaIF/QgbhhWF53J4GDl+64iKp1mLYTgzDWKyEoRAkAHCfsc" + "\r"
            + "ylevNflC7hVrPjA8nvJtuO5gA1W6u5la3fRIam7xCtHVKkJDZ8cKFttfCvEZJOT5" + "\r"
            + "/SNc/6A5aNjEU9y6";

    //测试
//    public static final String SERVER_TEST = "https://testapi.miaoqian.com/";
//    public static final String SERVER_TEST = "http://172.18.0.156:8081/";
//    public static final String SERVER_TEST = "http://192.168.1.225:8081/miaoqian-app/";
//    public static final String SERVER_TEST_WEB = "https://testmobile.miaoqian.com/";

    public static final String SERVER_TEST = "https://devapi.miaoqian.com/";
    public static final String SERVER_TEST_WEB = "https://devmobile.miaoqian.com/";

    //线上
    public static final String SERVER_ONLINE = "https://api.shicaidai.com/";
    public static final String SERVER_ONLINE_WEB = "https://mobile.miaoqian.com/";

    /**
     * 打包时，修改Config.DEBUG
     * Config.DEBUG = true 为测试环境
     * Config.DEBUG = flase 为线上环境
     */
    public static String getServer(Context mContext) {
        if (Config.DEBUG) {
            String host = Pref.getString(Pref.SERVER_HOST, mContext, "");
            return TextUtils.isEmpty(host) ? SERVER_TEST : host;
        }
        return SERVER_ONLINE;
    }

    private static String getWebServer() {
        if (Config.DEBUG) {
            return SERVER_TEST_WEB;
        }
        return SERVER_ONLINE_WEB;
    }

    public static String getPubliceKey() {
        if (Config.DEBUG) {
            return RSA_PUBLICE_TEST;
        }
        return RSA_PUBLICE_ONLINE;
    }

    public static String getPrivateKey() {
        if (Config.DEBUG) {
            return RSA_PRIVATE_TEST;
        }
        return RSA_PRIVATE_ONLINE;
    }

    //身份认证
    public final static String idcard_check = "commonService/idCardAuth";
    //充值预处理接口
    public final static String roll_in_preprocess = "trans/rechargePreprocess";
    //充值转入接口
    public final static String roll_in =  "trans/recharge";// "assetService/v2/recharge";
    //充值转入结果查询接口
    public final static String rollin_result =  "trans/rechargeTransfer";//public final static String rollin_result = base_server + "assetService/queryOrderLianLian";
    //充值失败错误原因上传接口
    public final static String rollin_error =  "assetService/rechargeFailureLianLian";
    //活期首页
    public final static String current_home =  "newCurrent/queryNewCurrentForApp";
    //活期首页
    public final static String current_detail =  "newCurrent/queryNewCurrentDetail";
    //认购预处理
    public final static String order_produced =  "trans/subscribePreprocess";//assetService/subscribePreprocess
    //认购
    public final static String subscribe_order =  "trans/subscribe";//"assetService/subscribe";
    //获取用户信息
    public final static String user_info =  "user/getUserInfo";// /userService/getUserInfo

    //获取APP配置：广告、tab
    public final static String app_config =  "commonService/getAdverts";

    //获取验证码
    public final static String getCaptcha =  "captcha/getCaptcha";  /// commonService/getCaptcha
    //登录
    public final static String login =  "user/login";// userService/login
    //注册
    public final static String register =  "user/register";  // userService/register
    //个人消息列表
    public final static String getMessageList =  "pushService/getMessageList";
    //系统公告列表
    public final static String getPushList =  "pushService/getPushList";
    //系统消息详情
    public final static String getPushDetail =  "pushService/v2/getPushDetail";
    //个人消息详情
    public final static String getMessageDetail =  "pushService/v2/getMessageDetail";
    //设置消息已读
    public final static String setMessageReaded =  "pushService/setMessageReaded";
    //删除消息
    public final static String deleteMessage =  "pushService/deleteMessage";
    //设置交易密码
    public final static String changePayPassword =  "userService/changePayPassword";
    //修改交易密码
    public final static String setPayPassword =  "userService/setPayPassword";
    //修改登录密码
    public final static String changePassword =  "user/changePassword";//user/changePassword
    //找回登录密码
    public final static String getPassword =  "user/getPassword";///userService/getPassword
    //检验验证码
    public final static String checkCaptcha =  "captcha/checkCaptcha";//commonService/checkCaptcha
    //绑定银行卡
    public final static String bindBank =  "bank/custBindBankBranch";//  commonService/bindBank
    //获取城市接口
    public final static String getAllCity =  "areaData/getCityByProv";// commonService/getAllCity
    //获取支行
    public final static String getSubBranch =  "bank/getBranch";//commonService/getSubBranch
    //登出
    public final static String loginOut =  "user/logout";//userService/logout
    //我的活期
    public final static String user_current =  "userService/getMyCurrent";
    //我的秒钱宝
    public final static String user_mqb =  "mine/newCurrent";
    //我的定期
    public final static String user_regular =  "mine/getMyRegBuyList";  ///userRegService/v2/getMyRegBuyList
    //我的定期详情
    public final static String user_regular_detail =  "mine/getRegDetail";
    //项目匹配
    public final static String project_match =  "webView/getProjMatchList/";
    //我的定期转让情况
    public final static String regular_transfer_detail =  "commonService/getRegTrans";
    //还款详情
    public final static String repayment_detail =  "commonService/getRepaymentList";

    //定期首页
    public final static String REGULA_PROJECT =  "regular/queryRegularListForApp";
    //定期首页
    public final static String REGULA_DETAILS =  "regular/queryRegularDetail";
    //定期转让首页
    public final static String REGULA_PROJECT_TRANSFER =  "transferService/regularList";

    //红包定期列表
    public final static String getFitSubject =  "subjectService/getFitSubject";

    //home
    public final static String homeInfo =  "home/getAppHome";
    //records of capital
    public final static String recordsCapital =  "mine/getFundFlow";// userService/getAssetRecord

    //我的促销接口
    public final static String getCustPromotion =  "promotion/getCustPromotion";//"userService/getCustPromotion";
    //赎回
    public final static String redeem =  "assetService/redeem";
    //我的活期资金记录
    public final static String getMyCurrentRecord =  "userService/getMyCurrentRecord";
    public final static String forceUpdate =  "commonService/v2/forceUpdate";
    //查询标的相关记录
    public final static String findInvestInfo =  "mine/getRegRepayRecord";//userRegService/v2/findInvestInfo
    //查询标的相关记录
    public final static String getRegTransDetail =  "userRegService/v2/getRegTransDetail";

    /**
     * 网页的URL
     */
    //注册页：《秒钱会员注册协议》
    public final static String web_register_law = getWebServer() + "Client/protocol/registerProtocol.html";
    //网络交易资金账户服务第三方协议:
    public final static String web_otth_law = getWebServer() + "Client/protocol/netOTTHProtocol.html";
    //用户授权协议：
    public final static String web_authority_law = getWebServer() + "Client/protocol/userAuthorityProtocol.html";
    //秒钱宝服务协议:
    public final static String web_current_law_server = getWebServer() + "Client/protocol/protocol-mqb.html";
    //秒钱宝债权转让合同:
    public final static String web_current_law_claims = getWebServer() + "Client/protocol/protocol-mqbCT.html";
    //秒钱宝收益权转让合同:
    public final static String web_current_law_earnings = getWebServer() + "Client/protocol/protocol-mqbIT.html";
    //定期项目服务协议:
    public final static String web_regular_law_server = getWebServer() + "Client/protocol/protocol-regProject.html";
    //定期项目债权转让合同:
    public final static String web_regular_law_claims = getWebServer() + "Client/protocol/protocol-regProjectCT.html";
    //定期项目收益权转让合同:
    public final static String web_regular_law_earnings = getWebServer() + "Client/protocol/protocol-regProjectIT.html";
    //定期计划服务协议:
    public final static String web_plan_law_server = getWebServer() + "Client/protocol/protocol-regPlan.html";
    //定期计划债权转让合同:
    public final static String web_plan_law_claims = getWebServer() + "Client/protocol/protocol-regPlanCT.html";
    //定期计划收益权转让合同:
    public final static String web_plan_law_earnings = getWebServer() + "Client/protocol/protocol-regPlanIT.html";
    //定期赚详情
    public final static String web_regular_earn_detail = "regular/getProjectDetail";
    //定期计划详情
    public final static String web_regular_plan_detail = "regular/getProductProjectRelations";

    //红包、拾财券使用规则(认购页面)
    public final static String web_promote = getWebServer() + "Client/preferServiceRegulations.html";
    //银行限额说明
    public final static String web_support_bank = getWebServer() + "Client/limitDescription.html";
    //线下转账充值
    public final static String web_into_offline = getWebServer() + "Client/offlineRecharge.html";
    //帮助中心
    public final static String web_help = getWebServer() + "Client/help.html";
    //管理团队
    public final static String web_aboutus_team = getWebServer() + "Client/manageTeam.html";
    //合作伙伴
    public final static String web_aboutus_cooperation = getWebServer() + "Client/cooperationPartner.html";
    //关于秒钱
    public final static String web_aboutus_introduce = getWebServer() + "Client/aboutMiaoqian.html";
    //发展历程
    public final static String web_aboutus_development = getWebServer() + "Client/progress.html";

    /**
     * 小坛的接口start
     */
    //提现初始化
    public final static String withdrawinit_jx =  "trans/withdrawInit";
    //提现预处理
    public final static String withdrawPreprocess_jx =  "trans/withdrawPreprocess";

    /**
     * 小坛的接口end
     */
    //首页运营活动
    public final static String get_home_activity =  "home/getAppHomeActivity";
    public final static String get_activity_feedback =  "home/getActivityFeedback";

    //我的邀请
    public final static String web_my_invite =  "activityService/inviteMain";

    //江西银行开通存管
    public final static String jx_open =  "user/openAccount";
    //江西银行存管状态
    public final static String jx_open_preprocess =  "user/openAccountPreprocess";

    public final static String jx_custbankcardinfo =  "bank/custBankCardInfo";

    //江西银行跳转接口Web URL
    public final static String jx_password_url =  "user/setPayPasswordForJump";//设置交易密码
    public final static String jx_auto_claims_url =  "signProtocol/autoClaimsTransferForJump";//自动债权转让
    public final static String jx_auto_subscribe_url =  "signProtocol/autoSubscribeForJump";//自动投标
    public final static String jx_auto_withdraw_url =  "trans/withdrawForJump";//提现
}
