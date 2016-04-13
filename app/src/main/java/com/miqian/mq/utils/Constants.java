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
    public static final String ERROR_LIAN_DEFAULT = "{" +
            "\"1003\": \"连连支付判断交易存在风险，请联系客服确认解决\"," +
            "\"1014\": \"本卡本日交易额度超限，请参考限额说明。降低额度后重试\"," +
            "\"1015\": \"本卡本月交易额度超限，请参考限额说明。换卡请联系客服\"," +
            "\"1016\": \"超过该卡交易金额，请确认卡上余额或降低额度后重试\"," +
            "\"1019\": \"单笔充值额度超限，请参考限额说明。\"," +
            "\"1100\": \"卡号无效或不支持，麻烦确认卡号，如再次错误说明不支持，请换卡\"," +
            "\"1105\": \"该卡交易存在风险，请联系客服确认\"," +
            "\"1107\": \"该卡不支持在线支付，请开通在线支付或换张卡后重试\"," +
            "\"1200\": \"暂不支持该行卡，请选择其他银行卡重试\"," +
            "\"6001\": \"卡余额不足，请确认卡上余额后重试\"," +
            "\"8901\": \"无相关记录，请重试，如再次出现请联系客服\"," +
            "\"8911\": \"无相关记录，请重试，如再次出现请联系客服\"," +
            "\"9998\": \"验证信息失效，请重试。\"," +
            "\"llErrorCodeVersion\": \"1.1\"" +
            "}";

}
