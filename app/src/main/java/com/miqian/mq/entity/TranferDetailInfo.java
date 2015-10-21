package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/21.
 */
public class TranferDetailInfo {

    private String discountRt;//折让率 百分比
    private String discountAmt;//折让金额
    private String applDt;//申请日期
    private String prin;//转让本金
    private String buyAmt;//已转让金额
    private String discount;//折让状态 （平价，加价：+1%，降价：-1%）
    private String transState;//转让状态

    public String getDiscountRt() {
        return discountRt;
    }

    public void setDiscountRt(String discountRt) {
        this.discountRt = discountRt;
    }

    public String getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getApplDt() {
        return applDt;
    }

    public void setApplDt(String applDt) {
        this.applDt = applDt;
    }

    public String getPrin() {
        return prin;
    }

    public void setPrin(String prin) {
        this.prin = prin;
    }

    public String getBuyAmt() {
        return buyAmt;
    }

    public void setBuyAmt(String buyAmt) {
        this.buyAmt = buyAmt;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTransState() {
        return transState;
    }

    public void setTransState(String transState) {
        this.transState = transState;
    }
}