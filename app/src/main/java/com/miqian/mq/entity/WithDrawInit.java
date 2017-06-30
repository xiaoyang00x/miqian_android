package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/6/28.
 */

public class WithDrawInit {
    private String bankUnionNumber;  //开户行联行号
    private String bankUrlBig;  //银行大图标
    private String bankUrlSmall;  //银行小图标
    private String bankCode;  //银行编码
    private String bankName;  //银行名称
    private String bankNo;  //银行卡号
    private boolean bindBankStatus;  //银行卡绑定状态
    private int monthRemain;  //本月剩余提现次数
    private int dayRemain;  //今日剩余提现次数
    private BigDecimal amt;  //可提现金额

    public String getBankUnionNumber() {
        return bankUnionNumber;
    }

    public void setBankUnionNumber(String bankUnionNumber) {
        this.bankUnionNumber = bankUnionNumber;
    }

    public String getBankUrlBig() {
        return bankUrlBig;
    }

    public void setBankUrlBig(String bankUrlBig) {
        this.bankUrlBig = bankUrlBig;
    }

    public String getBankUrlSmall() {
        return bankUrlSmall;
    }

    public void setBankUrlSmall(String bankUrlSmall) {
        this.bankUrlSmall = bankUrlSmall;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public boolean isBindBankStatus() {
        return bindBankStatus;
    }

    public void setBindBankStatus(boolean bindBankStatus) {
        this.bindBankStatus = bindBankStatus;
    }

    public int getMonthRemain() {
        return monthRemain;
    }

    public void setMonthRemain(int monthRemain) {
        this.monthRemain = monthRemain;
    }

    public int getDayRemain() {
        return dayRemain;
    }

    public void setDayRemain(int dayRemain) {
        this.dayRemain = dayRemain;
    }

    public BigDecimal getAmt() {

        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
}
