package com.miqian.mq.utils;

/**
 * Created by Joy on 2015/9/11.
 */
public class TypeUtil {
    //验证码
//    13001——注册
//    13002——找回密码
//    13003——重新绑定手机号第一次获取验证码
//    13004——重新绑定手机号第二次获取验证码
//    13005——银行卡信息补全
//    13006——修改银行卡
//    13007——非首次提现
//    13008——找回交易密码
    public static final int CAPTCHA_REGISTER = 13001;
    public static final int CAPTCHA_FINDPASSWORD = 13002;
    public static final int CAPTCHA_BINDTEL_FIRST = 13003;
    public static final int CAPTCHA_BINTTEL_SECOND = 13004;
    public static final int CAPTCHA_BANDINFO = 13005;
    public static final int CAPTCHA_MODIFYBNADCARD = 13006;
    public static final int CAPTCHA_WITHDRAW = 13007;
    public static final int CAPTCHA_TRADEPASSWORD = 13007;

    //是否设置交易密码
    public static final int TRADEPASS_NOSET = 20001;
    public static final int TRADEPASS_SET = 20002;

    //区分交易密码和登录密码
    public static final int PASSWORD_LOGIN = 20003;
    public static final int PASSWORD_TRADE = 20004;
    //发送验证码的类型
    public static final int SENDCAPTCHA_FORGETPSW =30001 ;
}
