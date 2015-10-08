package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/8.
 */
public class RollOut  implements Serializable{
    private String orderNo;
    private String moneyOrder;
    private String addTime;
    private String cardNum;
    private String bankName;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMoneyOrder() {
        return moneyOrder;
    }

    public void setMoneyOrder(String moneyOrder) {
        this.moneyOrder = moneyOrder;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }



}
