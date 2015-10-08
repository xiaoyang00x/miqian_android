package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/15.
 */
public class CurrentInfo {

    private String buyItemCount;
    private String buyTotalSum;
    private String currentBuyUpLimit;
    private String currentBuyDownLimit;
    private String currentSwitch;
//    webViewList

    public String getBuyItemCount() {
        return buyItemCount;
    }

    public void setBuyItemCount(String buyItemCount) {
        this.buyItemCount = buyItemCount;
    }

    public String getBuyTotalSum() {
        return buyTotalSum;
    }

    public void setBuyTotalSum(String buyTotalSum) {
        this.buyTotalSum = buyTotalSum;
    }

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
}
