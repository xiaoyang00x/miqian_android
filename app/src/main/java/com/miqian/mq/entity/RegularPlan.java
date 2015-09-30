package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by guolei_wang on 15/9/17.
 * 定期计划
 */
public class RegularPlan implements Serializable {

    private String subjectId;//标的ID
    private String subjectName;//标的名称
    private long startTimestamp; //开始时间
    private long endTimestamp; //结束时间
    private String subjectStatus; //标的状态
    private String yearInterest; //年化收益
    private String limit; //期限
    private String presentationYesNo; // "N",//是否赠送收益
    private String presentationYearInterest; //赠送年利率
    private String fromInvestmentAmount; //起投金额
    private String payMode; //还款方式
    private String subjectTotalPrice; //标的总额
    private String purchasePrice; //已认购金额
    private float purchasePercent; //认购进度
    private String bxbzf; //本息保障方
    private String ddbzf; //兜底保障方
    private String promotionDesc; //"满1万元送100元红包"//促销描述

    public String getSubjectId() {
        return subjectId;
    }

    public String getBxbzf() {
        return bxbzf;
    }

    public void setBxbzf(String bxbzf) {
        this.bxbzf = bxbzf;
    }

    public String getDdbzf() {
        return ddbzf;
    }

    public void setDdbzf(String ddbzf) {
        this.ddbzf = ddbzf;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(String subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public String getYearInterest() {
        return yearInterest;
    }

    public void setYearInterest(String yearInterest) {
        this.yearInterest = yearInterest;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPresentationYesNo() {
        return presentationYesNo;
    }

    public void setPresentationYesNo(String presentationYesNo) {
        this.presentationYesNo = presentationYesNo;
    }

    public String getPresentationYearInterest() {
        return presentationYearInterest;
    }

    public void setPresentationYearInterest(String presentationYearInterest) {
        this.presentationYearInterest = presentationYearInterest;
    }

    public String getFromInvestmentAmount() {
        return fromInvestmentAmount;
    }

    public void setFromInvestmentAmount(String fromInvestmentAmount) {
        this.fromInvestmentAmount = fromInvestmentAmount;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getSubjectTotalPrice() {
        return subjectTotalPrice;
    }

    public void setSubjectTotalPrice(String subjectTotalPrice) {
        this.subjectTotalPrice = subjectTotalPrice;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public float getPurchasePercent() {
        return purchasePercent;
    }

    public void setPurchasePercent(float purchasePercent) {
        this.purchasePercent = purchasePercent;
    }

    public String getPromotionDesc() {
        return promotionDesc;
    }

    public void setPromotionDesc(String promotionDesc) {
        this.promotionDesc = promotionDesc;
    }
}
