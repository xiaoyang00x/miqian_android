package com.miqian.mq.utils;

/**
 * 全局配置参数
 * 
 */
public class AppConfig {
    /**
     * DEBUG模式 【注：生产debug为false；调试debug为true】
     * tips： 如果发现 BuildConfig.DEBUG 的值在某些adt下出现bug，可以手动设置这里的值。
     */
    public static final boolean DEBUG = false;
//    public static final boolean DEBUG = BuildConfig.DEBUG;
    /**
     * 用户升级版本号 【APK升级的版本号】
     */
    public static final String USERUPDATEVERSONCODE = "44.0.1";
}
