package com.miqian.mq.utils;

/**
 * Created by Joy on 2015/9/11.
 */
public class TypeUtil {
    //验证码
//    13001——注册
//    13002——找回登录密码
//    13003— 秒钱宝赎回
//    13004——旧活期赎回
//    13005——修改登录密码
//    15001——开户
//    15002——快捷充值
//    15003——绑卡 已开户未绑卡
//    15004——修改交易密码
    public static final int CAPTCHA_REGISTER = 13001;//注册
    public static final int CAPTCHA_FINDPASSWORD = 13002;//找回登录密码
    public static final int CAPTCHA_REDEEM_MIAOQIANBAO = 13003;//秒钱宝赎回
    public static final int CAPTCHA_REDEEM_CURRENT = 13004;//旧活期赎回
    public static final int CAPTHCA_MODIFYLOGINPW = 13005;//修改登录密码
    public static final int CAPTCHA_OPENACCOUNT = 15001;//开户
    public static final int CAPTCHA_QUICK_RECHARGE = 15002;//快捷充值
    public static final int CAPTCHA_BIND_CAR = 15003;//绑卡 已开户未绑卡
    public static final int CAPTCHA_TRADE_PW = 15004; //修改交易密码


    //是否设置交易密码
    public static final int TRADEPASS_NOSET = 20001;
    public static final int TRADEPASS_SET = 20002;

    //区分交易密码和登录密码
    public static final int PASSWORD_LOGIN = 20003;
    public static final int PASSWORD_TRADE = 20004;
    //发送验证码的类型
    public static final int SENDCAPTCHA_FORGETPSW =30001 ;


    //设置交易密码
    public static final int TRADEPASSWORD_FIRST_SETTING =40001 ;
    //设置交易密密码成功
    public static final int TRADEPASSWORD_SETTING_SUCCESS =40002 ;


}
