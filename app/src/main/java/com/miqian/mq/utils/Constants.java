package com.miqian.mq.utils;

/**
 * Created by Joy on 2015/9/11.
 */
public class Constants {
    // 打印Logcat的开关 ，发包时关闭
    public static final  boolean  Debug=true;

    public static final int BASE_ID = 0;
    public static final int RQF_PAY = BASE_ID + 1;

    public static final String RET_CODE_SUCCESS = "0000";// 0000 交易成功
    public static final String RET_CODE_PROCESS = "2008";// 2008 支付处理中
    public static final String RET_CODE_LIMIT = "700314";// 700314 额度受限
    public static final String RESULT_PAY_SUCCESS = "SUCCESS";
    public static final String RESULT_PAY_PROCESSING = "PROCESSING";

}
