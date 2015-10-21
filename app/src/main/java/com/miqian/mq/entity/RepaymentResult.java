package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/15.
 */
public class RepaymentResult extends Meta {

    private RepaymentPlan data;

    public RepaymentPlan getData() {
        return data;
    }

    public void setData(RepaymentPlan data) {
        this.data = data;
    }
}
