package com.miqian.mq.entity;

/**
 * Created by Administrator on 2017/6/27.
 */

public class BankCardInfo {

    private  String custName;
    private  String electronicNo;
    private  String bankNo;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getElectronicNo() {
        return electronicNo;
    }

    public void setElectronicNo(String electronicNo) {
        this.electronicNo = electronicNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
}
