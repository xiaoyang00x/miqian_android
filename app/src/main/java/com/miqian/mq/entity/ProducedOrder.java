package com.miqian.mq.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jackie on 2015/9/24.
 */
public class ProducedOrder {

    private List<Promote> promList;
    private BigDecimal balance;     //余额
    private String predictIncome;      //预计收益

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


    public String getPredictIncome() {
        return predictIncome;
    }

    public void setPredictIncome(String predictIncome) {
        this.predictIncome = predictIncome;
    }

}
