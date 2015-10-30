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

    public static final String PATTERN_BANK = "^[1-9]\\d*$"; // 银行卡号
    public static final String PATTERN_IDCARD = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|[Xx])$"; // 身份证匹配
    public static final String PATTERN_MONEY = "^\\d{1,7}(\\.\\d{0,2})?$";// 金额匹配、格式为"#####.##"

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
     * @param time  时间戳
     * @param formatStr  例如：yyyy-MM-dd hh:mm:ss.SSS   yyyy年MM月dd日 hh时mm分ss秒SSS毫秒
     * @return
     */
    public static String formatDate(long time, String formatStr) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(formatStr);
        return sdf1.format(time);
    }

    /**
     * 格式化金额 保留2位小数且三位三位的隔开
     * @param amount
     * @return
     */
    public static String formatAmount(BigDecimal amount) {
//        NumberFormat nf = new DecimalFormat("#,###.##");
        String format = amount.toString();
//        try{
//            format = nf.format(amount);
//        }catch (Exception e) {
//
//        }
        return format;
    }

    /**
     * 格式化金额 保留2位小数且三位三位的隔开
     * @param amount
     * @return
     */
    public static String formatAmountStr(String amount) {
//        NumberFormat nf = new DecimalFormat("#,###.##");
        String format = amount;
//        try{
//            format = nf.format(new BigDecimal(amount));
//        }catch (Exception e) {
//
//        }
        return format;
    }
}
