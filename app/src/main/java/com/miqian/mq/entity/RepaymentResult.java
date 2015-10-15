package com.miqian.mq.entity;

import java.util.List;

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

    public class RepaymentPlan {

        private List<RepaymentInfo> repaymentPlan;

        public List<RepaymentInfo> getRepaymentPlan() {
            return repaymentPlan;
        }

        public void setRepaymentPlan(List<RepaymentInfo> repaymentPlan) {
            this.repaymentPlan = repaymentPlan;
        }
    }
}
