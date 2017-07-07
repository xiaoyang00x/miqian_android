package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/29.
 */
public class SubscribeOrder {

    private String orderNo;//订单号
    private String orderTime;//订单时间
    private BigDecimal amt;//认购金额
    private BigDecimal realPayAmt;//余额支付
    private String promDesc;//红包使用

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public BigDecimal getRealPayAmt() {
        return realPayAmt;
    }

    public void setRealPayAmt(BigDecimal realPayAmt) {
        this.realPayAmt = realPayAmt;
    }

    public String getPromDesc() {
        return promDesc;
    }

    public void setPromDesc(String promDesc) {
        this.promDesc = promDesc;
    }
}
