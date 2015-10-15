package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WithdrawItem {

    private  String feeAmt;
    private  String amt;
    private  String feeId;
    private  String name;
    private  String feeCd;

    public String getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(String feeAmt) {
        this.feeAmt = feeAmt;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeeCd() {
        return feeCd;
    }

    public void setFeeCd(String feeCd) {
        this.feeCd = feeCd;
    }
}
