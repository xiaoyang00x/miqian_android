package com.miqian.mq.net;

/**
 * Created by Administrator on 2015/9/4.
 */
public class Urls {

    public static final String RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCU2DBwdq+RCEL2hmR3c4Cs9Uno" + "\r" + "iadyKamYWTPpWSivJfLifXQWTED09z9KjK3nCjnFqquU7j7CmQwuEx9c9SkvjE+P" + "\r"
            + "NpD/23buQ31yIB41bVb0dDDjTLLqVd6aCbC85jJQ34qq6jNoMBs/IFTFBvjO++nE" + "\r" + "dRnBiq4YHLVtD/qbhwIDAQAB";

    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM+59Lcccqp9r3yk" + "\r" + "CRP2pSB6o9EP+igdwxTXFnVIwTg7N9cbIcsCZmfnZGm2OF4G3fx8dVSl5LfQo7sr" + "\r"
            + "TZ2iaDayq0pcqMr/pzft1KxOqV2JF966uJf+dINO2T7gIimG1XGg4NNQQC2AUeQQ" + "\r" + "cBpfqwa6uUUMLzus/Q7JfQ/5Idt7AgMBAAECgYB1pW7Iai8zCf3ijJCroswAqhSv" + "\r"
            + "aeoW4ExFOE/62tq7uyY0CKTJ8jEHddCz4kToU+FThzV4XIA97sdcUPkjZwC3vBm0" + "\r" + "GQHAqxnpBJxQ85Z8YmJIhvpg2KU3TKtYn9p7M/jEQDxV7jw5lCbfnbiC34EXyJ7C" + "\r"
            + "PE31u3pSc3LiMl1JOQJBAPXH7NSSb7srinHwnaApEDms3AVbSn4Ucftd/dGFALgT" + "\r" + "9yDE7zYe/8T5yftQ5ivxxbVHJmS6dNGDLVnHRe3YdNUCQQDYXPsQRZOMhr1kWUUU" + "\r"
            + "bjfMjfc62ElLR+hREeYVNPGWFRq76qiHHdzHIND2T8ayUDyTGg0ydYrHzh9VJaRg" + "\r" + "lHcPAkEA7FtGtWXdbPGCQfBSpSLYJ57EN6KDEJw4wLKy1DpDZsJMdYbtQ+6rqJFO" + "\r"
            + "XBCASOJIbvvtQBDPJmwvwJaR+RifwQJAd+WGpyy3KA9ekmY5tqmhODSPhUdnzlTJ" + "\r" + "s2skwim1moKVbv8JZt6wgpR315a7uAJonue32ndycWiSyl9yqKZ9FwJAFDNB7i0w" + "\r"
            + "7vhqwHmFAXxnffRtEVZbJk2AsEcE0qJqwoxGbij+O1+OIksDGlxjc/LgnXLlXvvY" + "\r" + "ZU0zSqeXVAcdRQ==";

//    public static final String base_server = "https://api.github.com/repos/square/okhttp/contributors";
//    public static final String base_server = "https://api.shicaidai.com/";
    public static final String base_server = "http://172.20.8.143:8080/miqian-app/";


//  测试
    public final static String test = base_server + "userService/register";
    // 身份认证
    public final static String idcard_check = base_server + "commonService/idCardAuth";
    // 充值转入接口
    public final static String roll_in = base_server + "assetService/addRecharge";

//    public final static String login = base_server + "/userService/login";
    //获取验证码
    public final static String getCaptcha = base_server + "commonService/getCaptcha";
    //登录
    public final static String login = base_server + "userService/login";

    public final static String register  = base_server + "userService/register";
}
