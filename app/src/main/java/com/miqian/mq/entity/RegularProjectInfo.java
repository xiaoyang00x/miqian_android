package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * @author wangduo
 * @description: 定期项目信息
 * @email: cswangduo@163.com
 * @date: 16/5/27
 */
public class RegularProjectInfo extends RegularBase {

    private String limit; // 期限

    private String presentationYearInterest; // 赠送年利率

    private String subjectStatus; // 标的状态

    private BigDecimal subjectTotalPrice; // 标的总额

    private String prodId; // 产品类型

    private String startTimestamp; // 开始时间戳

    private String businessType; // 标的业务类型

    private BigDecimal residueAmt; // 剩余额度

    private String yearInterest; // 年化收益率

    private String personTime; // 认购人次

    private String subjectMaxBuy; // 最大可以认购金额

    private String fromInvestmentAmount; // 最低认购金额/起投金额

    private String subjectName; // 标的名称

    private String continueInvestmentLimit; // 续投金额

    private String subjectId; // 标的ID

    private String endTimestamp; // 结束时间戳

    private String presentationYesNo; // 是否赠送收益

    private String purchasePrice; // 已投总额

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPresentationYearInterest() {
        return presentationYearInterest;
    }

    public void setPresentationYearInterest(String presentationYearInterest) {
        this.presentationYearInterest = presentationYearInterest;
    }

    public String getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(String subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public BigDecimal getSubjectTotalPrice() {
        return subjectTotalPrice;
    }

    public void setSubjectTotalPrice(BigDecimal subjectTotalPrice) {
        this.subjectTotalPrice = subjectTotalPrice;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public BigDecimal getResidueAmt() {
        return residueAmt;
    }

    public void setResidueAmt(BigDecimal residueAmt) {
        this.residueAmt = residueAmt;
    }

    public String getYearInterest() {
        return yearInterest;
    }

    public void setYearInterest(String yearInterest) {
        this.yearInterest = yearInterest;
    }

    public String getPersonTime() {
        return personTime;
    }

    public void setPersonTime(String personTime) {
        this.personTime = personTime;
    }

    public String getSubjectMaxBuy() {
        return subjectMaxBuy;
    }

    public void setSubjectMaxBuy(String subjectMaxBuy) {
        this.subjectMaxBuy = subjectMaxBuy;
    }

    public String getFromInvestmentAmount() {
        return fromInvestmentAmount;
    }

    public void setFromInvestmentAmount(String fromInvestmentAmount) {
        this.fromInvestmentAmount = fromInvestmentAmount;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getContinueInvestmentLimit() {
        return continueInvestmentLimit;
    }

    public void setContinueInvestmentLimit(String continueInvestmentLimit) {
        this.continueInvestmentLimit = continueInvestmentLimit;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getPresentationYesNo() {
        return presentationYesNo;
    }

    public void setPresentationYesNo(String presentationYesNo) {
        this.presentationYesNo = presentationYesNo;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
