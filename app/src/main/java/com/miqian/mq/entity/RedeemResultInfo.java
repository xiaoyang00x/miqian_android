package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2017/7/12.
 */

public class RedeemResultInfo {

    private BigDecimal amt;//赎回本金
    private BigDecimal interest;//利息
    private String orderNo;//业务编号
    private String arriveDesc;//到账时间描述
    private String tips;//温馨提示

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getArriveDesc() {
        return arriveDesc;
    }

    public void setArriveDesc(String arriveDesc) {
        this.arriveDesc = arriveDesc;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
