package com.miqian.mq.entity;

/**
 * Created by Administrator on 2016/6/18.
 */
public class TransFer {

    private String discountRate;
    private String predictRate;
    private String subjectId;
    private String investId;
    private String principle;
    private String prodId;

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getPredictRate() {
        return predictRate;
    }

    public void setPredictRate(String predictRate) {
        this.predictRate = predictRate;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
}
