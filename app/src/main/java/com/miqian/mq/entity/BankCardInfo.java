package com.miqian.mq.entity;

/**
 * Created by Administrator on 2017/6/27.
 */

public class BankCardInfo {

    private  String custName;
    private  String jxId;
    private  String bankNo;

    public String getJxId() {
        return jxId;
    }

    public void setJxId(String jxId) {
        this.jxId = jxId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
}
