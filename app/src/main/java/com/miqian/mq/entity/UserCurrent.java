package com.miqian.mq.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/30.
 */
public class UserCurrent implements Serializable{

    private BigDecimal curAmt;//活期待收收益
    private String curYesterDayAmt;//活期昨日新增收益
    private BigDecimal curAsset;//活期待收本金
    private String currentSwitch;//活期赚认购开关
    private String currentYearRate;//活期待收本金
    private BigDecimal currentBuyUpLimit;//活期赚认购上限
    private BigDecimal currentBuyDownLimit;//活期赚认购下限
    private BigDecimal balance;//可用余额
    private BigDecimal lmtMonthAmt;//赎回限制每月金额
    private BigDecimal curMonthAmt;//当月已赎回总额
    private BigDecimal curDayResidue;//当日剩余可赎回额度
    private String warmPrompt;//温馨提示
    private BigDecimal lmtDayAmt;//赎回限制每日金额
    private String redeemSwitch; //2017手Q 赎回开关

    public BigDecimal getLmtDayAmt() {
        return lmtDayAmt;
    }

    public void setLmtDayAmt(BigDecimal lmtDayAmt) {
        this.lmtDayAmt = lmtDayAmt;
    }

    public BigDecimal getLmtMonthAmt() {
        return lmtMonthAmt;
    }

    public void setLmtMonthAmt(BigDecimal lmtMonthAmt) {
        this.lmtMonthAmt = lmtMonthAmt;
    }

    public BigDecimal getCurMonthAmt() {
        return curMonthAmt;
    }

    public void setCurMonthAmt(BigDecimal curMonthAmt) {
        this.curMonthAmt = curMonthAmt;
    }

    public BigDecimal getCurDayResidue() {
        return curDayResidue;
    }

    public void setCurDayResidue(BigDecimal curDayResidue) {
        this.curDayResidue = curDayResidue;
    }

    public String getWarmPrompt() {
        return warmPrompt;
    }

    public void setWarmPrompt(String warmPrompt) {
        this.warmPrompt = warmPrompt;
    }

    public String getCurrentSwitch() {
        return currentSwitch;
    }

    public void setCurrentSwitch(String currentSwitch) {
        this.currentSwitch = currentSwitch;
    }

    public BigDecimal getCurAmt() {
        return curAmt;
    }

    public void setCurAmt(BigDecimal curAmt) {
        this.curAmt = curAmt;
    }

    public String getCurYesterDayAmt() {
        return curYesterDayAmt;
    }

    public void setCurYesterDayAmt(String curYesterDayAmt) {
        this.curYesterDayAmt = curYesterDayAmt;
    }

    public BigDecimal getCurAsset() {
        return curAsset;
    }

    public void setCurAsset(BigDecimal curAsset) {
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

    public String getRedeemSwitch() {
        return redeemSwitch;
    }

    public void setRedeemSwitch(String redeemSwitch) {
        this.redeemSwitch = redeemSwitch;
    }
}
