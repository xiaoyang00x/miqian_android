package com.miqian.mq.net;

/**
 * Created by Administrator on 2015/9/4.
 */
public class Urls {

//    public static final String base_server = "https://api.github.com/repos/square/okhttp/contributors";
//    public static final String base_server = "https://api.shicaidai.com/";
    public static final String base_server = "http://172.20.8.18:8081/miqian-app/";


//  测试
    public final static String test = base_server + "userService/register.do";
//    public final static String test = base_server + "commonService/getBankList.do";

//    public final static String login = base_server + "/userService/login.do";
    //获取验证码
    public final static String getCaptcha = base_server + "commonService/getCaptcha.do";
}
