package com.miqian.mq.entity;

/**
 * Created by Jackie on 2017/7/12.
 */

public class RedeemResultInfo {

    private int amt;//赎回本金
    private int interest;//利息
    private int orderNo;//业务编号
    private int arriveDesc;//到账时间描述
    private boolean tips;//温馨提示

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getArriveDesc() {
        return arriveDesc;
    }

    public void setArriveDesc(int arriveDesc) {
        this.arriveDesc = arriveDesc;
    }

    public boolean isTips() {
        return tips;
    }

    public void setTips(boolean tips) {
        this.tips = tips;
    }
}
