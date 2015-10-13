package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/12.
 */
public class AutoIdentyCard implements Serializable{

    private  String bankUrlBig;
    private  String bankName;
    private  String bankUrlSmall;
    private  String cardType;      //2：储蓄卡，3：信用卡
    private  String supportStatus; //1：支持，否则不支持
    private  String bankCode;  // 核心银行编码

    public String getBankUrlBig() {
        return bankUrlBig;
    }

    public void setBankUrlBig(String bankUrlBig) {
        this.bankUrlBig = bankUrlBig;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankUrlSmall() {
        return bankUrlSmall;
    }

    public void setBankUrlSmall(String bankUrlSmall) {
        this.bankUrlSmall = bankUrlSmall;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(String supportStatus) {
        this.supportStatus = supportStatus;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
