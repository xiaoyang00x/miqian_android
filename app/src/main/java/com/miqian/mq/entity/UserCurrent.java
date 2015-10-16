package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/30.
 */
public class UserCurrent {

    private String curAmt;//活期待收收益
    private String curYesterDayAmt;//活期昨日新增收益
    private String curAsset;//活期待收本金

    private String currentBuyUpLimit;//活期赚认购上限

    private String currentBuyDownLimit;//活期赚认购下限

    private String currentSwitch;//活期赚认购开关

    public String getCurrentBuyUpLimit() {
        return currentBuyUpLimit;
    }

    public void setCurrentBuyUpLimit(String currentBuyUpLimit) {
        this.currentBuyUpLimit = currentBuyUpLimit;
    }

    public String getCurrentBuyDownLimit() {
        return currentBuyDownLimit;
    }

    public void setCurrentBuyDownLimit(String currentBuyDownLimit) {
        this.currentBuyDownLimit = currentBuyDownLimit;
    }

    public String getCurrentSwitch() {
        return currentSwitch;
    }

    public void setCurrentSwitch(String currentSwitch) {
        this.currentSwitch = currentSwitch;
    }

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
