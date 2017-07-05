package com.miqian.mq.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jackie on 2015/9/19.
 */
public class FormatUtil {

    public static final String PATTERN_IDCARD = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|[Xx])$"; // 身份证匹配
    public static final String PATTERN_MONEY = "^(([1-9]{1}\\d{0,6})|([0]{1}))(\\.\\d{0,2})?$";
    public static final String PATTERN_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![\\x21-\\x2f\\x3a-\\x40\\x5b-\\x60\\x7b-\\x7e]+$)(?![\\x21-\\x40\\x5b-\\x60\\x7b-\\x7e]+$)(?![\\x21-\\x2f\\x3a-\\x7e]+$)[\\x21-\\x7e]{6,16}$";

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

    /**
     * 按照指定格式格式化时间
     *
     * @param time      时间戳
     * @param formatStr 例如：yyyy-MM-dd HH:mm:ss.SSS   yyyy年MM月dd日 HH时mm分ss秒SSS毫秒
     * @return
     */
    public static String formatDate(long time, String formatStr) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(formatStr);
        return sdf1.format(time);
    }

    /**
     * 格式化金额 保留2位小数且三位三位的隔开
     *
     * @param amount
     * @return
     */
    public static String formatAmount(BigDecimal amount) {
        NumberFormat nf = new DecimalFormat("#,###.##");
        String format = amount == null ? "0" : amount.toString();
        try {
            format = nf.format(amount);
        } catch (Exception e) {

        }
        return format;
    }

    /**
     * 格式化金额 保留2位小数且三位三位的隔开
     *
     * @param amount
     * @return
     */
    public static String formatAmountStr(String amount) {
        NumberFormat nf = new DecimalFormat("#,###.##");
        String format = TextUtils.isEmpty(amount) ? "0" : amount;
        try {
            BigDecimal b = new BigDecimal(amount);
            format = nf.format(new BigDecimal(amount));
        } catch (Exception e) {

        }
        return format;
    }

    /**
     * 充值输入框 金额格式化
     *
     * @param amount
     * @return
     */
    public static String formatAmtForInto(String amount) {
        String temp1;
        String temp2;
        if (amount.contains(".")) {
            temp1 = amount.substring(0, amount.indexOf("."));
            temp2 = amount.substring(amount.indexOf("."));
        } else {
            temp1 = amount;
            temp2 = "";
        }
        NumberFormat nf = new DecimalFormat("#,###");
        String format = TextUtils.isEmpty(temp1) ? "0" : temp1;
        try {
            format = nf.format(new BigDecimal(temp1));
        } catch (Exception e) {

        }
        return format + temp2;
    }
}
