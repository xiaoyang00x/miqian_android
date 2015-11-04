package com.miqian.mq.net;

/**
 * Created by Administrator on 2015/9/4.
 */
public class Urls {

    /**
     * 测试key
     */
    public static final String RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCU2DBwdq+RCEL2hmR3c4Cs9Uno" + "\r"
            + "iadyKamYWTPpWSivJfLifXQWTED09z9KjK3nCjnFqquU7j7CmQwuEx9c9SkvjE+P" + "\r"
            + "NpD/23buQ31yIB41bVb0dDDjTLLqVd6aCbC85jJQ34qq6jNoMBs/IFTFBvjO++nE" + "\r"
            + "dRnBiq4YHLVtD/qbhwIDAQAB";

    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM+59Lcccqp9r3yk" + "\r"
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

//    /**
//     * 正式key
//     */
//    public static final String RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC84iihAQA8kxa4eWiQwZ6qLQpJ" + "\r"
//            + "wZC0yUpQB5qh3ZKeYbR2Nx8GK8EEmoQg4XMK+HFgrrzji8PTnv69h0MBqN3Jg1Ul" + "\r"
//            + "bLQLzF0xjicDjgA2YAWYRFQ3JBM5MjsM+kkLme++03dtpBlFLXVXBltTEoxeU41e" + "\r"
//            + "Pt94rFg+wNta0BNX9QIDAQAB";
//
//    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANfUTvSfntVeUkvb" + "\r"
//            + "U8MLLQnkOPa61ya/IVJj448GO765ul2HxWHIGUVa0ibGIAG/uh0bE5lHHTjSQu8r" + "\r"
//            + "2RmH++qK1fMZRbRpjjDws+iZ/oSN8un8A99yy6ToDN+9bCVePxDFtGeJDqze+C1W" + "\r"
//            + "wr7PZMeaxzjeP5oW+6F5iNyi0lcBAgMBAAECgYEAyqV/VNQisDgVkT/nz7RIn4b3" + "\r"
//            + "rUIVDT5cLR245et2YUfFolf+jibxA1uZAer41muMdhOcPUi2OUnCqwpCl2REtGL3" + "\r"
//            + "Kfumfngw0YIotN9lFstnB9vFv61WLgUyEvDlsmzId5lk3VSJ8wM++p9GCI6Kh4Mc" + "\r"
//            + "gF7EViONzLZHIh86i9kCQQD9DOoRU6ezNU8N+L7Fttm/9YhTq8+C6luFw1PIzuLT" + "\r"
//            + "n5mwStqexybXb4Rc2wdyhjSUCpGbi4jLTC6uYBj36BTTAkEA2lhUKaZ5HluSOFuH" + "\r"
//            + "yPjv1rGgHfUVKGkqLyxmKMglqsDcy5cJQr8+BJjArjfca5vo0C/e9BIcj+xBGCRg" + "\r"
//            + "sUtQWwJBAMZrJe5IJisDFeXisdPeOIeoQyk4gxIoKXONYVkHwBwn/R6a0ynFBtPO" + "\r"
//            + "gX89z20hhoC+eMX6d1zRc5kPiQR6WjkCQQCAEV8MLwDLtZpGRCmYz6qdmwH3kFEg" + "\r"
//            + "qs57bOde9EZjqUvYfnaIF/QgbhhWF53J4GDl+64iKp1mLYTgzDWKyEoRAkAHCfsc" + "\r"
//            + "ylevNflC7hVrPjA8nvJtuO5gA1W6u5la3fRIam7xCtHVKkJDZ8cKFttfCvEZJOT5" + "\r"
//            + "/SNc/6A5aNjEU9y6";

    /**
     * 测试
     */
    public static final String base_server = "https://testapi.shicaidai.com:8443/";

//    /**
//     * 线上
//     */
//    public static final String base_server = "https://api.shicaidai.com/";

//    public static final String base_server = "http://172.20.8.143:8080/miqian-app/";
//    public static final String base_server = "http://172.20.8.11:8082/miqian-app/";

    //测试
    public final static String test = base_server + "userService/register";
    //身份认证
    public final static String idcard_check = base_server + "commonService/idCardAuth";
    //充值转入接口
    public final static String roll_in = base_server + "assetService/addRecharge";
    //充值转入结果查询接口
    public final static String rollin_result = base_server + "assetService/queryOrderLianLian";
    //活期首页
    public final static String current_home = base_server + "currentService/getCurrentMain";
    //认购预处理
    public final static String order_produced = base_server + "assetService/subscribePreprocess";
    //认购
    public final static String subscribe_order = base_server + "assetService/subscribe";
    //获取用户信息
    public final static String user_info = base_server + "userService/getUserInfo";

    //获取验证码
    public final static String getCaptcha = base_server + "commonService/getCaptcha";
    //登录
    public final static String login = base_server + "userService/login";
    //注册
    public final static String register = base_server + "userService/register";
    //消息详情
    public final static String getPushDetail = base_server + "pushService/getPushDetail";
    //设置交易密码
    public final static String changePayPassword = base_server + "userService/changePayPassword";
    //修改交易密码
    public final static String setPayPassword = base_server + "userService/setPayPassword";
    //修改登录密码
    public final static String changePassword = base_server + "userService/changePassword";
    //找回登录密码
    public final static String getPassword = base_server + "userService/getPassword";
    //检验验证码
    public final static String checkCaptcha = base_server + "commonService/checkCaptcha";
    //获取用户的银行卡
    public final static String getUserBankCard = base_server + "userService/getUserBankCard";
    //自动识别银行卡
    public final static String autoIdentifyBankCard = base_server + "assetService/autoIdentifyBankCard";
    //绑定银行卡
    public final static String bindBank = base_server + "commonService/bindBank";
    //获取城市接口
    public final static String getAllCity = base_server + "commonService/getAllCity";
    //获取支行
    public final static String getSubBranch = base_server + "commonService/getSubBranch";
    //登出
    public final static String loginOut = base_server + "userService/logout";
    //提现
    public final static String withdrawCash = base_server + "assetService/withdrawCash";
    //我的活期
    public final static String user_current = base_server + "userService/getMyCurrent";
    //我的定期
    public final static String user_regular = base_server + "userService/getMyRegBuyList";
    //我的定期详情
    public final static String user_regular_detail = base_server + "commonService/getRegDetail";
    //项目匹配
    public final static String project_match = base_server + "subjectService/getMatchList";
    //我的定期转让情况
    public final static String regular_transfer_detail = base_server + "commonService/getRegTrans";
    //还款详情
    public final static String repayment_detail = base_server + "commonService/getRepaymentList";

    //定期首页
    public final static String getRegMain = base_server + "subjectService/getRegMain";

    //home
    public final static String homeInfo = base_server + "commonService/getHome";
    //records of capital
    public final static String recordsCapital = base_server + "userService/getAssetRecord";

    //我的促销接口
    public final static String getCustPromotion = base_server + "userService/getCustPromotion";
    //赎回
    public final static String redeem = base_server + "assetService/redeem";
    //我的活期资金记录
    public final static String getMyCurrentRecord = base_server + "userService/getMyCurrentRecord";
    //修改绑定手机
    public final static String changePhone = base_server + "userService/changePhone";
    //提现预处理
    public final static String withdrawPreprocess = base_server + "assetService/withdrawPreprocess";

    /**
     *网页的URL
     */
    //注册页：《秒钱用户注册协议》
    public final static String web_register_law = base_server + "webView/service-items";
//    //充值页面：《资金管理协议》
//    public final static String web_recharge_law = base_server + "webView/recharge";
//    //活期认购页：《活期赚服务协议》
//    public final static String web_current_law = base_server + "webView/current-buy";
//    //定期赚认购页面：《定期赚服务协议》
//    public final static String web_regular_law = base_server + "webView/regular-buy";
//    //定期计划认购页面：《定期计划服务协议》
//    public final static String web_regplan_law = base_server + "webView/regplan-buy";
    //定期赚详情
    public final static String web_regular_earn_detail = base_server + "webView/getRegDetail/";
    //定期计划详情
    public final static String web_regular_plan_detail = base_server + "webView/regPlanDetail";

    //提现说明
    public final static String web_rollout = base_server + "webView/presentDescription";
    //红包使用规则
    public final static String web_red_paper = base_server + "webView/hbServiceRegulations";
    //拾财券使用规则
    public final static String web_ticket = base_server + "webView/scServiceRegulations";
    //红包、拾财券使用规则(认购页面)
    public final static String web_promote = base_server + "webView/preferServiceRegulations";

    //活期详情
    public final static String web_current = base_server + "webView/currentDetail";
    //帮助中心
    public final static String web_help = base_server + "webView/helpCenter";
}
