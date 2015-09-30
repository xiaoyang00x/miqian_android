package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/30.
 */
public class UserCurrent {

    private String curAmt;//活期待收收益
    private String curYesterDayAmt;//活期昨日新增收益
    private String curAsset;//活期待收本金

    public String getCurAmt() {
        return curAmt;
    }

    public void setCurAmt(String curAmt) {
        this.curAmt = curAmt;
    }

    public String getCurYesterDayAmt() {
        return curYesterDayAmt;
    }

    public void setCurYesterDayAmt(String curYesterDayAmt) {
        this.curYesterDayAmt = curYesterDayAmt;
    }

    public String getCurAsset() {
        return curAsset;
    }

    public void setCurAsset(String curAsset) {
        this.curAsset = curAsset;
    }
}
