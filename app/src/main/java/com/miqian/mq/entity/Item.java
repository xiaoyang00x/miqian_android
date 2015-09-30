package com.miqian.mq.entity;

/**
 * Created by sunyong on 9/16/15.
 */
public class Item {

  private String subjectId, subjectName, startTimestamp, endTimestamp, purchasePercent, personTime,
      subjectType, subjectStatus, subjectTotalPrice, subjectBidAmt, bxbzf, ddbzf, presentationYesNo,
      presentationYearInterest, fromInvestmentAmount, promotionDesc;

  private SchemeList[] schemeList;

  public String getSubjectId() {
    return subjectId;
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

  public String getStartTimestamp() {
    return startTimestamp;
  }

  public void setStartTimestamp(String startTimestamp) {
    this.startTimestamp = startTimestamp;
  }

  public String getEndTimestamp() {
    return endTimestamp;
  }

  public void setEndTimestamp(String endTimestamp) {
    this.endTimestamp = endTimestamp;
  }

  public String getPersonTime() {
    return personTime;
  }

  public void setPersonTime(String personTime) {
    this.personTime = personTime;
  }

  public String getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(String subjectType) {
    this.subjectType = subjectType;
  }

  public String getSubjectStatus() {
    return subjectStatus;
  }

  public void setSubjectStatus(String subjectStatus) {
    this.subjectStatus = subjectStatus;
  }

  public String getSubjectTotalPrice() {
    return subjectTotalPrice;
  }

  public void setSubjectTotalPrice(String subjectTotalPrice) {
    this.subjectTotalPrice = subjectTotalPrice;
  }

  public String getSubjectBidAmt() {
    return subjectBidAmt;
  }

  public void setSubjectBidAmt(String subjectBidAmt) {
    this.subjectBidAmt = subjectBidAmt;
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

  public String getPromotionDesc() {
    return promotionDesc;
  }

  public void setPromotionDesc(String promotionDesc) {
    this.promotionDesc = promotionDesc;
  }

  public SchemeList[] getSchemeList() {
    return schemeList;
  }

  public void setSchemeList(SchemeList[] schemeList) {
    this.schemeList = schemeList;
  }

  public static class SchemeList {
    private String limit, yearInterest, payMode;

    public String getLimit() {
      return limit;
    }

    public void setLimit(String limit) {
      this.limit = limit;
    }

    public String getYearInterest() {
      return yearInterest;
    }

    public void setYearInterest(String yearInterest) {
      this.yearInterest = yearInterest;
    }

    public String getPayMode() {
      return payMode;
    }

    public void setPayMode(String payMode) {
      this.payMode = payMode;
    }
  }

  public String getPurchasePercent() {
    return purchasePercent;
  }

  public void setPurchasePercent(String purchasePercent) {
    this.purchasePercent = purchasePercent;
  }
}