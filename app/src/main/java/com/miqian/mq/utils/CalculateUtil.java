package com.miqian.mq.utils;
import android.text.TextUtils;
import java.math.BigDecimal;

/**
 * Created by Tuliangtan on 2016/7/14.
 * 我的定期
 */
public class CalculateUtil {

    public static final int INTEREST_SHOWTYPE_ONE = 1;
    public static final int INTEREST_SHOWTYPE_TWO = 2;
    public static final int INTEREST_SHOWTYPE_THREE = 3;
    public static final int INTEREST_SHOWTYPE_FOUR = 4;
    public static final int INTEREST_SHOWTYPE_FIVE = 5;
    public static final int INTEREST_SHOWTYPE_SIX = 6;
    public static final int INTEREST_SHOWTYPE_SEVEN = 7;

    /**
     * @param projectState    项目状态
     * @param transedAmt      已转金额
     * @param subjectType     标的类型      "88": 88专属  "07":双倍收益卡  88节不可转让
     * @param realInterest    实际利率
     * @param presentInterest 赠送收益
     * @return int    显示的状态
     */
    public static int getShowInterest(String projectState, String subjectType
            , String realInterest, String presentInterest, String transedAmt) {
        int showType = 0;
        BigDecimal bdRealInterest = new BigDecimal(realInterest);
        BigDecimal bdPresentInterest = new BigDecimal(presentInterest);
        if ("0".equals(projectState) || "1".equals(projectState) || "2".equals(projectState)) {
            showType = isTransfer(subjectType, bdRealInterest, bdPresentInterest, transedAmt);
        } else {
            showType = isOirigin(subjectType, bdRealInterest, bdPresentInterest, transedAmt);
        }
        return showType;
    }
    public static int isTransfer(String subjectType, BigDecimal bdRealInterest, BigDecimal bdPresentInterest, String transedAmt) {
        int showType = 0;
        if (subjectType.equals("88")) {//88节双倍收益
            if (bdRealInterest.compareTo(bdPresentInterest) == 0) {
                showType = INTEREST_SHOWTYPE_TWO;
            }else {
                if (bdPresentInterest.compareTo(BigDecimal.ZERO)>0) {
                    showType = INTEREST_SHOWTYPE_THREE;
                } else {
                    showType = INTEREST_SHOWTYPE_ONE;
                }
            }
        } else if (subjectType.equals("07")) {//双倍收益卡
            if (!TextUtils.isEmpty(transedAmt)) {
                BigDecimal amtMoney = new BigDecimal(transedAmt);
                if (amtMoney.compareTo(BigDecimal.ZERO) > 0) {
                    showType = INTEREST_SHOWTYPE_ONE;
                } else {
                    if (bdRealInterest.compareTo(bdPresentInterest) == 0) {
                        showType = INTEREST_SHOWTYPE_TWO;
                    }else {
                        if (bdPresentInterest.compareTo(BigDecimal.ZERO)>0) {
                            showType = INTEREST_SHOWTYPE_THREE;
                        } else {
                            showType = INTEREST_SHOWTYPE_ONE;
                        }
                    }
                }
            }
        } else {
            if (bdPresentInterest.compareTo(BigDecimal.ZERO)>0) {
                showType = INTEREST_SHOWTYPE_THREE;
            } else {
                showType = INTEREST_SHOWTYPE_ONE;
            }
        }
        return showType;
    }

    public static int isOirigin(String subjectType, BigDecimal bdRealInterest, BigDecimal bdPresentInterest, String transedAmt) {
        int showType = 0;
        if (subjectType.equals("88")) {//88节双倍收益
            if (bdRealInterest.compareTo(bdPresentInterest) == 0) {
                showType = INTEREST_SHOWTYPE_FOUR;
            }
        } else if (subjectType.equals("07")) {//双倍收益卡
            if (!TextUtils.isEmpty(transedAmt)) {
                BigDecimal amtMoney = new BigDecimal(transedAmt);
                if (amtMoney.compareTo(BigDecimal.ZERO) > 0) {
                    showType = INTEREST_SHOWTYPE_FIVE;
                } else {
                    if (bdRealInterest.compareTo(bdPresentInterest) == 0) {
                        showType = INTEREST_SHOWTYPE_SIX;
                    }else {
                        if (bdPresentInterest.compareTo(BigDecimal.ZERO)>0) {
                            showType = INTEREST_SHOWTYPE_SEVEN;
                        } else {
                            showType = INTEREST_SHOWTYPE_FIVE;
                        }
                    }
                }
            }
        }else {
            if (bdPresentInterest.compareTo(BigDecimal.ZERO)>0) {
                showType = INTEREST_SHOWTYPE_SEVEN;
            } else {
                showType = INTEREST_SHOWTYPE_FIVE;
            }
        }
        return showType;
    }


}
