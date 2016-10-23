package com.miqian.mq.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jackie on 2015/9/24.
 */
public class ProducedOrder {

    private List<Promote> promotionList;
    private BigDecimal usableAmt;     //余额
    private String bestToUseAmt;        //最高抵用金额（红包、秒钱卡
//    private String currentYearRate;     //年化收益
    private String predictInterest;      //预计收益

    public List<Promote> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<Promote> promotionList) {
        this.promotionList = promotionList;
    }

    public BigDecimal getUsableAmt() {
        return usableAmt;
    }

    public void setUsableAmt(BigDecimal usableAmt) {
        this.usableAmt = usableAmt;
    }

    public String getBestToUseAmt() {
        return bestToUseAmt;
    }

    public void setBestToUseAmt(String bestToUseAmt) {
        this.bestToUseAmt = bestToUseAmt;
    }

//    public String getCurrentYearRate() {
//        return currentYearRate;
//    }
//
//    public void setCurrentYearRate(String currentYearRate) {
//        this.currentYearRate = currentYearRate;
//    }

    public String getPredictInterest() {
        return predictInterest;
    }

    public void setPredictInterest(String predictInterest) {
        this.predictInterest = predictInterest;
    }

}
