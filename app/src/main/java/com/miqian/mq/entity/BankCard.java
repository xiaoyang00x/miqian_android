package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/9/22.
 */
public class BankCard {
    private  String bankNo;
    private  String bankOpenName;
    private  String province;
    private  String bankUrlBig;
    private  String bankName;
    private  String bankUrlSmall;
    private  String city;
    private  String bankCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankOpenName() {
        return bankOpenName;
    }

    public void setBankOpenName(String bankOpenName) {
        this.bankOpenName = bankOpenName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
