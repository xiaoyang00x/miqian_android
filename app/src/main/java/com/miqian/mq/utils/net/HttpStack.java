package com.miqian.mq.utils.net;

import android.app.Activity;
import com.miqian.mq.utils.AppConfig;

public interface HttpStack {
    /**
     * 数据连接失败默认数据
     */
    public static final String NO_CONN = "FAIL";
    /**
     * POST请求默认request属性
     */
    public static final String HTTP_POST_BODY = "body";
    /**
     * cookie request请求头
     */
    public static final String HTTP_COOKIE = "Cookie";
    /**
     * cookie response请求头
     */
    public static final String SET_COOKIE = "Set-Cookie";
    /**
     * user-agent
     */
    public static final String HTTP_USER_AGENT = "User-Agent";
    /**
     * user-agent Message
     */
    public static final String HTTP_USER_AGENT_MESSAGE = String.format("android GomeShopApp %s;",
        AppConfig.USERUPDATEVERSONCODE);
    /**
     * POST 提交body体 content type
     */
    public static final String JSON_CHARSET = "application/json;charset=utf-8";
    /**
     * 请求超时 秒
     */
    public static final int TIMEOUT = 40;
    /**
     * UUID
     */
    public static String BOUNDARY = java.util.UUID.randomUUID ( ).toString ( ) ;
    /**
     * 全局配置
     */
    //public static GlobalConfig config = GlobalConfig.getInstance();


    /**
     * GET
     * 
     * @param url
     * @return
     */
    String get(String url);

    
    String get(Activity activity, String url, boolean isGoGome);

    /**
     * POST
     * 
     * @param url
     * @param json
     * @return
   String post(String url, String json);
     */
 
    
    String post(Activity activity, String url, String json, boolean isGoGome);
    String post(Activity activity, String url, String json, boolean isGoGome, boolean isJson);
}
