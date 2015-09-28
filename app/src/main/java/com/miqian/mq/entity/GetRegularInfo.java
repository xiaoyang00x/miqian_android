package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by Jackie on 2015/9/15.
 */
public class GetRegularInfo {

    private ArrayList<RegularPlan> planList;
    private ArrayList<RegularEarn> regList;

    public ArrayList<RegularPlan> getPlanList() {
        return planList;
    }

    public void setPlanList(ArrayList<RegularPlan> planList) {
        this.planList = planList;
    }

    public ArrayList<RegularEarn> getRegList() {
        return regList;
    }

    public void setRegList(ArrayList<RegularEarn> regList) {
        this.regList = regList;
    }
}
