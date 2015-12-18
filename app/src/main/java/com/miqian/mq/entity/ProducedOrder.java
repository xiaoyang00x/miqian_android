package com.miqian.mq.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jackie on 2015/9/24.
 */
public class ProducedOrder {

    private List<Promote> promList;
    private String balance;     //余额
    private String best;        //优惠金额
    private String currentYearRate;     //年化利率
    private BigDecimal balanceCurrent;      //活期余额

    public List<Promote> getPromList() {
        return promList;
    }

    public void setPromList(List<Promote> promList) {
        this.promList = promList;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
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
}
