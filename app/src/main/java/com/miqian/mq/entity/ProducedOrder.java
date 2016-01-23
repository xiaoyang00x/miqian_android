package com.miqian.mq.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jackie on 2015/9/24.
 */
public class ProducedOrder {

    private List<Promote> promList;
    private BigDecimal balance;     //余额
    private String best;        //优惠金额
    private String currentYearRate;     //年化收益
    private BigDecimal balanceCurrent;      //活期余额
    private String bindCardStatus;      //银行卡绑定状态
    private String bankCardNo;      //银行卡号
    private String bankUrlSmall;      //绑定银行图标url
    private String bankName;      //银行名称
    private String supportStatus;      //是否支持连连
    private String predictIncome;      //预计收益
    private String singleAmtLimit; //单笔限额
    private String dayAmtLimit; // 每日限额
    private String monthAmtLimit ; // 每月限额

    public List<Promote> getPromList() {
        return promList;
    }

    public void setPromList(List<Promote> promList) {
        this.promList = promList;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }

    public String getCurrentYearRate() {
        return currentYearRate;
    }

    public void setCurrentYearRate(String currentYearRate) {
        this.currentYearRate = currentYearRate;
    }

    public BigDecimal getBalanceCurrent() {
        return balanceCurrent;
    }

    public void setBalanceCurrent(BigDecimal balanceCurrent) {
        this.balanceCurrent = balanceCurrent;
    }

    public String getBindCardStatus() {
        return bindCardStatus;
    }

    public void setBindCardStatus(String bindCardStatus) {
        this.bindCardStatus = bindCardStatus;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankUrlSmall() {
        return bankUrlSmall;
    }

    public void setBankUrlSmall(String bankUrlSmall) {
        this.bankUrlSmall = bankUrlSmall;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(String supportStatus) {
        this.supportStatus = supportStatus;
    }

    public String getPredictIncome() {
        return predictIncome;
    }

    public void setPredictIncome(String predictIncome) {
        this.predictIncome = predictIncome;
    }

    public String getSingleAmtLimit() {
        return singleAmtLimit;
    }

    public void setSingleAmtLimit(String singleAmtLimit) {
        this.singleAmtLimit = singleAmtLimit;
    }

    public String getDayAmtLimit() {
        return dayAmtLimit;
    }

    public void setDayAmtLimit(String dayAmtLimit) {
        this.dayAmtLimit = dayAmtLimit;
    }

    public String getMonthAmtLimit() {
        return monthAmtLimit;
    }

    public void setMonthAmtLimit(String monthAmtLimit) {
        this.monthAmtLimit = monthAmtLimit;
    }
}
