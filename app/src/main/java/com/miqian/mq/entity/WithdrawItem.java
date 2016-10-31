package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WithdrawItem {

    private  String feeAmt;
    private  String withdrawLimitLowestAmt;
    private  String withdrawLimitDayCount;
    private  String withdrawLimitMonthFreeCount;
    private  String withdrawLimitPrompt;
    private  String monthCount;
    private  String dayCount;
    private  String enable;

    public String getWithdrawLimitLowestAmt() {
        return withdrawLimitLowestAmt;
    }

    public void setWithdrawLimitLowestAmt(String withdrawLimitLowestAmt) {
        this.withdrawLimitLowestAmt = withdrawLimitLowestAmt;
    }

    public String getWithdrawLimitDayCount() {
        return withdrawLimitDayCount;
    }

    public void setWithdrawLimitDayCount(String withdrawLimitDayCount) {
        this.withdrawLimitDayCount = withdrawLimitDayCount;
    }

    public String getWithdrawLimitMonthFreeCount() {
        return withdrawLimitMonthFreeCount;
    }

    public void setWithdrawLimitMonthFreeCount(String withdrawLimitMonthFreeCount) {
        this.withdrawLimitMonthFreeCount = withdrawLimitMonthFreeCount;
    }

    public String getWithdrawLimitPrompt() {
        return withdrawLimitPrompt;
    }

    public void setWithdrawLimitPrompt(String withdrawLimitPrompt) {
        this.withdrawLimitPrompt = withdrawLimitPrompt;
    }

    public String getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(String monthCount) {
        this.monthCount = monthCount;
    }

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(String feeAmt) {
        this.feeAmt = feeAmt;
    }

}
