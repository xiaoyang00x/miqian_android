package com.miqian.mq.utils;

/**
 * Created by Joy on 2015/9/11.
 */
public class Constants {

    public static final int BASE_ID = 0;
    public static final int RQF_PAY = BASE_ID + 1;

    public final static int MAXCOUNT = 5; // 图案锁 最大验证次数
    public final static String VERIFYFAILED = "verifyFailed"; // 图案锁验证失败

    public static final String RET_CODE_SUCCESS = "0000";// 0000 交易成功
    public static final String RET_CODE_PROCESS = "2008";// 2008 支付处理中
    public static final String RET_CODE_LIMIT = "1005";// 700314 额度受限
    public static final String RESULT_PAY_SUCCESS = "SUCCESS";
    public static final String RESULT_PAY_PROCESSING = "PROCESSING";
    public static final String ERROR_LIAN_DEFAULT = "{\"1005\":\"单笔金额超限\",\"1108\":\"您输入的证件号姓名或手机号有误\",\"1114\":\"您更换过预留手机号请联系客服解决\",\"1200\":\"银行暂不支持\",\"2008\":\"银行正在处理请稍后\",\"9990_0\":\"银行服务暂时异常请稍后重试\",\"9990_1\":\"您在银行预留的信息不全\",\"9991\":\"支付失败\",\"llErrorCodeVersion\":\"1.1\"}";

}
