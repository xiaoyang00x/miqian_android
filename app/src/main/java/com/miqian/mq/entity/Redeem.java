package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/9.
 */
public class Redeem implements Serializable{

    private  String amt;//提现金额
    private  String arriAmt;//已到金额
    private  String seqNo;//交易编号
    private  String addTime;//交易时间
    private  String interest;//利息

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getArriAmt() {
        return arriAmt;
    }

    public void setArriAmt(String arriAmt) {
        this.arriAmt = arriAmt;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
