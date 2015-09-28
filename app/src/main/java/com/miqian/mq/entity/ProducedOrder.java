package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2015/9/24.
 */
public class ProducedOrder {

    private List<Promote> promList;
    private String balance;
    private String best;

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
}
