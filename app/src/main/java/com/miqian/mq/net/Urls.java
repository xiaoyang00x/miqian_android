package com.miqian.mq.net;

import com.miqian.mq.utils.Config;

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
//    public static final String SERVER_TEST = "https://testapi.shicaidai.com:8443/";
//    public static final String SERVER_TEST = "http://172.18.0.156:8081/";
//    public static final String SERVER_TEST = "http://192.168.1.225:8081/miaoqian-app/";
    public static final String SERVER_TEST = "https://devapi.miaoqian.com/";
    public static final String SERVER_TEST_WEB = "https://testmobile.miaoqian.com/";

    //线上
    public static final String SERVER_ONLINE = "https://api.shicaidai.com/";
    public static final String SERVER_ONLINE_WEB = "https://mobile.miaoqian.com/";

    /**
     * 打包时，修改Config.DEBUG
     * Config.DEBUG = true 为测试环境
     * Config.DEBUG = flase 为线上环境
     */
    private static String getServer() {
        if (Config.DEBUG) {
            return SERVER_TEST;
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
    public final static String idcard_check = getServer() + "commonService/idCardAuth";
    //充值预处理接口
    public final static String roll_in_preprocess = getServer() + "trans/rechargePreprocess";
    //充值转入接口
    public final static String roll_in = getServer() + "trans/recharge";//getServer() + "assetService/v2/recharge";
    //充值转入结果查询接口
    public final static String rollin_result = getServer() + "trans/rechargeTransfer";//public final static String rollin_result = base_server + "assetService/queryOrderLianLian";
    //充值失败错误原因上传接口
    public final static String rollin_error = getServer() + "assetService/rechargeFailureLianLian";
    //活期首页
    public final static String current_home = getServer() + "currentService/getCurrentMain";
    //认购预处理
    public final static String order_produced = getServer() + "assetService/subscribePreprocess";
    //认购
    public final static String subscribe_order = getServer() + "trans/subscribe";//"assetService/subscribe";
    //认购快捷支付
    public final static String quick_subscribe_order = getServer() + "assetService/quickPayment";
    //获取用户信息
    public final static String user_info = getServer() + "user/getUserInfo";// /userService/getUserInfo

    //获取APP配置：广告、tab
    public final static String app_config = getServer() + "commonService/getAdverts";

    //获取验证码
    public final static String getCaptcha = getServer() + "captcha/getCaptcha";  /// commonService/getCaptcha
    //登录
    public final static String login = getServer() + "user/login";// userService/login
    //注册
    public final static String register = getServer() + "user/register";  // userService/register
    //个人消息列表
    public final static String getMessageList = getServer() + "pushService/getMessageList";
    //系统公告列表
    public final static String getPushList = getServer() + "pushService/getPushList";
    //系统消息详情
    public final static String getPushDetail = getServer() + "pushService/v2/getPushDetail";
    //个人消息详情
    public final static String getMessageDetail = getServer() + "pushService/v2/getMessageDetail";
    //设置消息已读
    public final static String setMessageReaded = getServer() + "pushService/setMessageReaded";
    //删除消息
    public final static String deleteMessage = getServer() + "pushService/deleteMessage";
    //设置交易密码
    public final static String changePayPassword = getServer() + "userService/changePayPassword";
    //修改交易密码
    public final static String setPayPassword = getServer() + "userService/setPayPassword";
    //修改登录密码
    public final static String changePassword = getServer() + "user/changePassword";//user/changePassword
    //找回登录密码
    public final static String getPassword = getServer() + "user/getPassword";///userService/getPassword
    //检验验证码
    public final static String checkCaptcha = getServer() + "captcha/checkCaptcha";//commonService/checkCaptcha
    //获取用户的银行卡
    public final static String getUserBankCard = getServer() + "userService/getUserBankCard";
    //自动识别银行卡
    public final static String autoIdentifyBankCard = getServer() + "assetService/autoIdentifyBankCard";
    //绑定银行卡
    public final static String bindBank = getServer() + "bank/custBindBankBranch";//  commonService/bindBank
    //获取城市接口
    public final static String getAllCity = getServer() + "areaData/getCityByProv";// commonService/getAllCity
    //获取支行
    public final static String getSubBranch = getServer() + "bank/getBranch";//commonService/getSubBranch
    //登出
    public final static String loginOut = getServer() + "user/logout";//userService/logout
    //提现
    public final static String withdrawCash = getServer() + "assetService/withdrawCash";
    //我的活期
    public final static String user_current = getServer() + "userService/getMyCurrent";
    //我的定期
    public final static String user_regular = getServer() + "mine/getMyRegBuyList";  ///userRegService/v2/getMyRegBuyList
    //我的定期详情
    public final static String user_regular_detail = getServer() + "userRegService/v2/getRegDetail";
    //项目匹配
    public final static String project_match = getServer() + "webView/getProjMatchList/";
    //我的定期转让情况
    public final static String regular_transfer_detail = getServer() + "commonService/getRegTrans";
    //还款详情
    public final static String repayment_detail = getServer() + "commonService/getRepaymentList";

    //定期首页
    public final static String REGULA_PROJECT = getServer() + "subjectService/regularList";
    //定期转让首页
    public final static String REGULA_PROJECT_TRANSFER = getServer() + "transferService/regularList";

    //红包定期列表
    public final static String getFitSubject = getServer() + "subjectService/getFitSubject";

    //home
    public final static String homeInfo = getServer() + "commonService/v3/getHome";
    //records of capital
    public final static String recordsCapital = getServer() + "mine/getFundFlow";// userService/getAssetRecord

    //我的促销接口
    public final static String getCustPromotion = getServer() + "userService/getCustPromotion";
    //赎回
    public final static String redeem = getServer() + "assetService/redeem";
    //我的活期资金记录
    public final static String getMyCurrentRecord = getServer() + "userService/getMyCurrentRecord";
    //修改绑定手机
    public final static String changePhone = getServer() + "userService/changePhone";
    //提现预处理
    public final static String withdrawPreprocess = getServer() + "assetService/withdrawPreprocess";
    //版本更新
    public final static String forceUpdate = getServer() + "commonService/v2/forceUpdate";
    //查询标的相关记录
    public final static String findInvestInfo = getServer() + "userRegService/v2/findInvestInfo";
    //查询标的相关记录
    public final static String getRegTransDetail = getServer() + "userRegService/v2/getRegTransDetail";
    //我的转让详情
    public final static String getRegTransFerredDetail = getServer() + "userRegService/v2/getRegTransFerredDetail";
    //发起转让
    public final static String launchTransfer = getServer() + "assetService/transfer";

    /**
     * 网页的URL
     */
    //注册页：《秒钱用户注册协议》
    public final static String web_register_law = "https://res.shicaidai.com/miaoqian/webView/service-items.html";
    //《网络借贷风险及禁止行为提示》
    public final static String web_register_law_net = "https://res.shicaidai.com/miaoqian/webView/dengluxieyi.html";
    //充值页面：《资金管理协议》
    public final static String web_recharge_law = "https://res.shicaidai.com/miaoqian/webView/zjglxy.html";
    //活期认购页：《活期赚服务协议》
    public final static String web_current_law = "https://res.shicaidai.com/miaoqian/webView/hqzfwxy.html";
    //定期赚认购页面：《定期赚服务协议》
    public final static String web_regular_law = "https://res.shicaidai.com/miaoqian/webView/dqzfwxy.html";
    //定期计划认购页面：《定期计划服务协议》
    public final static String web_regplan_law = "https://res.shicaidai.com/miaoqian/webView/regplan-buy.html";
    //发起转让页面：秒钱债权转让协议
    public final static String web_transfer_law = "https://res.shicaidai.com/miaoqian/webView/regplan-buy.html";
    //发起转让页面：可转金额说明
    public final static String web_transferamt_expaint = "https://res.shicaidai.com/miaoqian/webView/regplan-buy.html";
    //定期赚详情
    public final static String web_regular_earn_detail = getServer() + "webView/getRegDetail/";
    //定期计划详情
    public final static String web_regular_plan_detail = getServer() + "webView/regPlanDetail/";

    //提现说明
    public final static String web_rollout = "https://res.shicaidai.com/miaoqian/webView/presentDescription.html";
    //红包、拾财券使用规则(认购页面)
    public final static String web_promote = "https://res.shicaidai.com/miaoqian/webView/preferServiceRegulations.html";


    //连连支付支持绑定的银行界面
    public final static String web_support_bank = getServer() + "webView/querySupportBankLianLian";

    //活期详情
    public final static String web_current = getServer() + "webView/currentDetail";
    //帮助中心
    public final static String web_help = "https://res.shicaidai.com/miaoqian/webView/helpCenter.html";

    /**
     * 小坛的接口start
     */
    //提现初始化
    public final static String withdrawinit_jx = getServer() + "trans/withdrawInit";
    //提现预处理
    public final static String withdrawPreprocess_jx = getServer() + "trans/withdrawPreprocess";

    /**
     * 小坛的接口end
     */
    //首页运营活动
    public final static String get_home_activity = getServer() + "activityService/getHomeActivity";
    public final static String get_activity_feedback = getServer() + "activityService/getActivityFeedback";

    //每日计息
    public final static String web_current_earning = "https://res.shicaidai.com/miaoqian/current/meirijixi.html";
    //本息保障
    public final static String web_current_safe = "https://res.shicaidai.com/miaoqian/current/benxibaozhang.html";
    //活存活取
    public final static String web_current_back = "https://res.shicaidai.com/miaoqian/current/huocunhuoqu.html";


    //管理团队
    public final static String web_aboutus_team = "https://res.shicaidai.com/miaoqian/about/guanlituandui.html";
    //合作伙伴
    public final static String web_aboutus_cooperation = "https://res.shicaidai.com/miaoqian/about/hezuohuoban.html";
    //关于秒钱
    public final static String web_aboutus_introduce = "https://res.shicaidai.com/miaoqian/about/miaoqianjianjie.html";
    //战略团队
    public final static String web_aboutus_zhanlue = "https://res.shicaidai.com/miaoqian/about/zhanluehuoban.html";

    //我的邀请
    public final static String web_my_invite = getServer() + "activityService/inviteMain";

    //线下转账充值
    public final static String web_into_offline = getWebServer() + "Client/offlineRecharge.html";

    //江西银行跳转接口
    public final static String jx_rollin_url = getServer() + "trans/rechargeForJump";
    public final static String jx_password_url = getServer() + "user/setPayPasswordForJump";
    public final static String jx_auto_claims_url = getServer() + "signProtocol/autoClaimsTransferForJump";
    public final static String jx_auto_subscribe_url = getServer() + "signProtocol/autoSubscribeForJump";
    public final static String jx_auto_withdraw_url = getServer() + "trans/withdrawForJum";//提现

    //江西银行开通存管
    public final static String jx_open = getServer() + "user/openAccount";
    //江西银行存管状态
    public final static String jx_open_preprocess = getServer() + "user/openAccountPreprocess";

    public final static String jx_custbankcardinfo = getServer() + "bank/custBankCardInfo";
}
