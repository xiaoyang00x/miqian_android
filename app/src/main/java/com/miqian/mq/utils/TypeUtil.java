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
    public static final int CAPTCHA_TRADEPASSWORD = 13008;
    //发送验证码的类型
    public static final int SENDCAPTCHA_FORGETPSW =30001 ;

    //修改绑定手机
    public static final int MODIFY_PHONE =30002 ;
    //认购跳到开通汇付界面
    public static final int TYPE_OPENHF_INVESTMENT =40003 ;
    //注册跳到开通汇付界面
    public static final int TYPE_OPENHF_REGISTER =40004 ;
    //充值跳到开通汇付界面
    public static final int TYPE_OPENHF_ROOLIN =40005 ;
    //提现跳到开通汇付界面
    public static final int TYPE_OPENHF_ROLLOUT=40006 ;


}
