package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 15/9/24.
 */
public class RegularPlanResult extends Meta {
    public RegularPlan getData() {
        return data;
    }

    public void setData(RegularPlan data) {
        this.data = data;
    }

    private RegularPlan data;
}
