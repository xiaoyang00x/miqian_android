package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/15.
 */
public class CurrentInfo {

    private String buyItemCount;
    private String buyTotalSum;
    private BigDecimal currentBuyUpLimit;
    private BigDecimal currentBuyDownLimit;
    private String currentSwitch;
    private String currentYearRate;
    private BigDecimal balance;//可用余额;

    public String getBuyItemCount() {
        return buyItemCount;
    }

    public void setBuyItemCount(String buyItemCount) {
        this.buyItemCount = buyItemCount;
    }

    public String getBuyTotalSum() {
        return buyTotalSum;
    }

    public void setBuyTotalSum(String buyTotalSum) {
        this.buyTotalSum = buyTotalSum;
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

    public String getCurrentSwitch() {
        return currentSwitch;
    }

    public void setCurrentSwitch(String currentSwitch) {
        this.currentSwitch = currentSwitch;
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
