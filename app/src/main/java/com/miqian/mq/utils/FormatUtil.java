package com.miqian.mq.utils;

import android.text.TextUtils;

/**
 * Created by Jackie on 2015/9/19.
 */
public class FormatUtil {

    public static final String PATTERN_BANK = "^[1-9]\\d*$"; // 银行卡号
    public static final String PATTERN_IDCARD = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|[Xx])$"; // 身份证匹配
    public static final String PATTERN_MONEY = "^\\d{1,6}(\\.\\d{0,2})?$";// 金额匹配、格式为"####.##"

    /**
     * 保留两位float小数
     *
     * @return
     */
    public static String getMoneyString(String money) {
        String moneyString = "";
        if (!TextUtils.isEmpty(money)) {
            try {
                float floatTemp = Float.parseFloat(money);
                moneyString = String.format("%.2f", floatTemp);
            } catch (Exception e) {
            }
        }
        return moneyString;
    }
}
