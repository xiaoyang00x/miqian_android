package com.miqian.mq.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by guolei_wang on 15/9/17.
 * 定期计划
 */
public class RegularPlan extends RegularBaseData {

    private BigDecimal fromInvestmentAmount; //起投金额
    private String payMode; //还款方式
    private BigDecimal subjectTotalPrice; //标的总额
    private BigDecimal purchasePrice; //已认购金额
    private float purchasePercent; //认购进度
    private String bxbzf; //本息保障方
    private String ddbzf; //兜底保障方
    private String promotionDesc; //"满1万元送100元红包"//促销描述
    private String promotionDescUrl;

    public String getPromotionDescUrl() {
        return promotionDescUrl;
    }

    public void setPromotionDescUrl(String promotionDescUrl) {
        this.promotionDescUrl = promotionDescUrl;
    }

    public BigDecimal getSubjectMaxBuy() {
        return subjectMaxBuy;
    }

    public void setSubjectMaxBuy(BigDecimal subjectMaxBuy) {
        this.subjectMaxBuy = subjectMaxBuy;
    }

    private BigDecimal subjectMaxBuy; //最大认购金额


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


    public BigDecimal getFromInvestmentAmount() {
        return fromInvestmentAmount;
    }

    public void setFromInvestmentAmount(BigDecimal fromInvestmentAmount) {
        this.fromInvestmentAmount = fromInvestmentAmount;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public BigDecimal getSubjectTotalPrice() {
        return subjectTotalPrice;
    }

    public void setSubjectTotalPrice(BigDecimal subjectTotalPrice) {
        this.subjectTotalPrice = subjectTotalPrice;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
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
