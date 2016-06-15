package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/21.
 */
public class Reg {
    private String regTotalIncome;//待收收益
    private String regTotalAmt;//待收本金
    private String perTotalAmt;//定期赚投资本金
    private String perTotalIncome;//定期赚已收收益
    private String transTotalAmt;//转让累计金额

    public String getRegTotalIncome() {
        return regTotalIncome;
    }

    public void setRegTotalIncome(String regTotalIncome) {
        this.regTotalIncome = regTotalIncome;
    }

    public String getRegTotalAmt() {
        return regTotalAmt;
    }

    public void setRegTotalAmt(String regTotalAmt) {
        this.regTotalAmt = regTotalAmt;
    }

    public String getPerTotalAmt() {
        return perTotalAmt;
    }

    public void setPerTotalAmt(String perTotalAmt) {
        this.perTotalAmt = perTotalAmt;
    }

    public String getPerTotalIncome() {
        return perTotalIncome;
    }

    public void setPerTotalIncome(String perTotalIncome) {
        this.perTotalIncome = perTotalIncome;
    }

    public String getTransTotalAmt() {
        return transTotalAmt;
    }

    public void setTransTotalAmt(String transTotalAmt) {
        this.transTotalAmt = transTotalAmt;
    }
}
