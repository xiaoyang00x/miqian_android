package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/30.
 */
public class UserCurrent {

    private String curAmt;//活期待收收益
    private String curYesterDayAmt;//活期昨日新增收益
    private String curAsset;//活期待收本金
    private String currentSwitch;//活期赚认购开关
    private String currentYearRate;//活期待收本金
    private BigDecimal currentBuyUpLimit;//活期赚认购上限
    private BigDecimal currentBuyDownLimit;//活期赚认购下限
    private BigDecimal balance;//可用余额

    public String getCurrentSwitch() {
        return currentSwitch;
    }

    public void setCurrentSwitch(String currentSwitch) {
        this.currentSwitch = currentSwitch;
    }

    public String getCurAmt() {
        return curAmt;
    }

    public void setCurAmt(String curAmt) {
        this.curAmt = curAmt;
    }

    public String getCurYesterDayAmt() {
        return curYesterDayAmt;
    }

    public void setCurYesterDayAmt(String curYesterDayAmt) {
        this.curYesterDayAmt = curYesterDayAmt;
    }

    public String getCurAsset() {
        return curAsset;
    }

    public void setCurAsset(String curAsset) {
        this.curAsset = curAsset;
    }

    public BigDecimal getCurrentBuyUpLimit() {
        return currentBuyUpLimit;
    }

    public void setCurrentBuyUpLimit(BigDecimal currentBuyUpLimit) {
        this.currentBuyUpLimit = currentBuyUpLimit;
    }

    public BigDecimal getCurrentBuyDownLimit() {
        return currentBuyDownLimit;
    }

    public void setCurrentBuyDownLimit(BigDecimal currentBuyDownLimit) {
        this.currentBuyDownLimit = currentBuyDownLimit;
    }

    public String getCurrentYearRate() {
        return currentYearRate;
    }

    public void setCurrentYearRate(String currentYearRate) {
        this.currentYearRate = currentYearRate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
