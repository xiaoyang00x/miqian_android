package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2015/10/21.
 */
public class RepaymentPlan {

    private List<RepaymentInfo> repaymentPlan;

    public List<RepaymentInfo> getRepaymentPlan() {
        return repaymentPlan;
    }

    public void setRepaymentPlan(List<RepaymentInfo> repaymentPlan) {
        this.repaymentPlan = repaymentPlan;
    }
}