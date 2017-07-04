package com.miqian.mq.utils;

/**
 * Created by Joy on 2015/9/11.
 */
public class Constants {

    public static final int BASE_ID = 0;
    public static final int RQF_PAY = BASE_ID + 1;

    public final static int MAXCOUNT = 5; // 图案锁 最大验证次数
    public final static String VERIFYFAILED = "verifyFailed"; // 图案锁验证失败

    /* activity之间传递数据key值 */
    public static final String SUBJECTID = "subjectId"; // 标的id号
    public static final String PRODID = "prodId"; // 标的类型:定期项目(转让) 定期计划(转让)
    public static final String PROMID = "promId"; // 优惠券使用:促销id
    /* activity之间传递数据key值 */

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

    /**
     * 秒钱产品类型：3 秒钱宝 1 定期项目 2 定期计划
     */
    public static final int PRODUCT_TYPE_REGULAR_PROJECT = 1;
    public static final int PRODUCT_TYPE_REGULART_PLAN = 2;
    public static final int PRODUCT_TYPE_MQB = 3;

    /**
     * 产品状态： 1 创建,2 待审核,3 待上线, 4 待开标,5 开标,6 满标,7 已结束未满标,8 满标下线,9 未认购下线
     */
    public static final String PRODUCT_STATUS_CJ = "1";
    public static final String PRODUCT_STATUS_DSH = "2";
    public static final String PRODUCT_STATUS_DSX = "3";
    public static final String PRODUCT_STATUS_DKB = "4";
    public static final String PRODUCT_STATUS_KB = "5";
    public static final String PRODUCT_STATUS_MB = "6";
    public static final String PRODUCT_STATUS_JSWMB = "7";
    public static final String PRODUCT_STATUS_MBXX = "8";
    public static final String PRODUCT_STATUS_WRGXX = "9";

    /**
     * 聚合产品状态，将明细的产品状态聚合为三种状态：0待开标、1已开标、2已满额
     * 待开标：1 创建,2 待审核,3 待上线, 4 待开标,
     * 已开标：5 开标
     * 已满额：7 已结束未满标,8 满标下线,9 未认购下线
     */
    public static final int STATUS_DKB = 0;
    public static final int STATUS_YKB = 1;
    public static final int STATUS_YMB = 2;

    /**
     * 根据服务器的明细产品状态判断返回聚合产品状态
     * @param productStatus
     * @return
     */
    public static int getCurrentStatus(String productStatus) {
        if(PRODUCT_STATUS_CJ.equals(productStatus) ||
                PRODUCT_STATUS_DSH.equals(productStatus) ||
                PRODUCT_STATUS_DSX.equals(productStatus) ||
                PRODUCT_STATUS_DKB.equals(productStatus) ) {
            return STATUS_DKB;
        }else if(PRODUCT_STATUS_KB.equals(productStatus)) {
            return STATUS_YKB;
        }else {
            return STATUS_YMB;
        }
    }

}
