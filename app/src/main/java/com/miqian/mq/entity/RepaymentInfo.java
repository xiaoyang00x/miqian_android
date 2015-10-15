package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/15.
 */
public class RepaymentInfo {

    private String repaymentDate;//还款日期
    private String repaymentPrincipal;//应还本金
    private String repaymentProfilt;//应还收益
    private String repaymentPresentation;//赠送收益
    private String state;//状态

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getRepaymentPrincipal() {
        return repaymentPrincipal;
    }

    public void setRepaymentPrincipal(String repaymentPrincipal) {
        this.repaymentPrincipal = repaymentPrincipal;
    }

    public String getRepaymentProfilt() {
        return repaymentProfilt;
    }

    public void setRepaymentProfilt(String repaymentProfilt) {
        this.repaymentProfilt = repaymentProfilt;
    }

    public String getRepaymentPresentation() {
        return repaymentPresentation;
    }

    public void setRepaymentPresentation(String repaymentPresentation) {
        this.repaymentPresentation = repaymentPresentation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
