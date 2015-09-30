package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/29.
 */
public class SubscribeOrder {

    private String orderNo;
    private String addTime;
    private long leftCnt;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public long getLeftCnt() {
        return leftCnt;
    }

    public void setLeftCnt(long leftCnt) {
        this.leftCnt = leftCnt;
    }
}
