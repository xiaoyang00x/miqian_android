package com.miqian.mq.entity;

/**
 * Created by Administrator on 2017/6/29.
 */

public class WithDrawPrepress {

    private String feeAmt;//计费金额
    private boolean bindBankStatus;//银行卡绑定状态 true为绑定，false为未绑定
    private String bankUnionNumber;//开户行联行号
    private boolean enable;//是否是可提现的时间  true为可提现，false为不可提现

    public String getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(String feeAmt) {
        this.feeAmt = feeAmt;
    }

    public boolean isBindBankStatus() {
        return bindBankStatus;
    }

    public void setBindBankStatus(boolean bindBankStatus) {
        this.bindBankStatus = bindBankStatus;
    }

    public String getBankUnionNumber() {
        return bankUnionNumber;
    }

    public void setBankUnionNumber(String bankUnionNumber) {
        this.bankUnionNumber = bankUnionNumber;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
