package com.miqian.mq.entity;

/**
 * Created by sunyong on 9/28/15.
 */
public class DetailForRegularDeposit {
  String message, code;
  DetailItem data;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public DetailItem getData() {
    return data;
  }

  public void setData(DetailItem data) {
    this.data = data;
  }

  public static class DetailItem {
    //    "limit": "120",
    //    "bxbzf": "嘉实（厦门）融资租赁有限公司",
    //    "presentationYearInterest": "0",
    //    "subjectStatus": "07",
    //    "subjectTotalPrice": "1000",
    //    "startTimestamp": "1435923941000",
    //    "promotionDesc": "",
    //    "yearInterest": "10",
    //    "personTime": "1",
    //    "purchasePercent": "50",
    //    "fromInvestmentAmount": "100",
    //    "payMode": "月付息，到期还本",
    //    "subjectName": "【单笔认购每满1万元，追加0.1%年化收益，封顶2%】嘉实融资租赁提供全额回购保证140006号项目",
    //    "subjectBidAmt": "500",
    //    "subjectId": "000000000000000000000010914003",
    //    "subjectType": "00",
    //    "ddbzf": "本息盾（厦门）金融技术服务有限公司",
    //    "endTimestamp": "1436183141000",
    //    "presentationYesNo": ""

    String limit, bxbzf, presentationYearInterest, subjectStatus, subjectTotalPrice, startTimestamp,
        promotionDesc, yearInterest, personTime, purchasePercent, fromInvestmentAmount, payMode,
        subjectName, subjectBidAmt, subjectId, subjectType, ddbzf, endTimestamp, presentationYesNo;

    public String getSubjectStatus() {
      return subjectStatus;
    }

    public void setSubjectStatus(String subjectStatus) {
      this.subjectStatus = subjectStatus;
    }

    public String getLimit() {
      return limit;
    }

    public void setLimit(String limit) {
      this.limit = limit;
    }

    public String getBxbzf() {
      return bxbzf;
    }

    public void setBxbzf(String bxbzf) {
      this.bxbzf = bxbzf;
    }

    public String getPresentationYearInterest() {
      return presentationYearInterest;
    }

    public void setPresentationYearInterest(String presentationYearInterest) {
      this.presentationYearInterest = presentationYearInterest;
    }

    public String getSubjectTotalPrice() {
      return subjectTotalPrice;
    }

    public void setSubjectTotalPrice(String subjectTotalPrice) {
      this.subjectTotalPrice = subjectTotalPrice;
    }

    public String getStartTimestamp() {
      return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
      this.startTimestamp = startTimestamp;
    }

    public String getPromotionDesc() {
      return promotionDesc;
    }

    public void setPromotionDesc(String promotionDesc) {
      this.promotionDesc = promotionDesc;
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

    public String getPurchasePercent() {
      return purchasePercent;
    }

    public void setPurchasePercent(String purchasePercent) {
      this.purchasePercent = purchasePercent;
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

    public String getSubjectName() {
      return subjectName;
    }

    public void setSubjectName(String subjectName) {
      this.subjectName = subjectName;
    }

    public String getSubjectBidAmt() {
      return subjectBidAmt;
    }

    public void setSubjectBidAmt(String subjectBidAmt) {
      this.subjectBidAmt = subjectBidAmt;
    }

    public String getSubjectId() {
      return subjectId;
    }

    public void setSubjectId(String subjectId) {
      this.subjectId = subjectId;
    }

    public String getSubjectType() {
      return subjectType;
    }

    public void setSubjectType(String subjectType) {
      this.subjectType = subjectType;
    }

    public String getDdbzf() {
      return ddbzf;
    }

    public void setDdbzf(String ddbzf) {
      this.ddbzf = ddbzf;
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
  }
}
