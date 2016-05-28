package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: 定期转让项目信息
 * @email: cswangduo@163.com
 * @date: 16/5/26
 */
public class RegularTransferInfo {

    private String limit; // 期限

    private String predictRate; // 预计年化收益率

    private String subjectStatus; // 标的状态 00：待开标 01：已开标 02：已满标（已售罄）03：已到期 04：已撤销 05：已流标 06：审批中 07：还款中 08：正常还完

    private String prodId; // 产品类型

    private String limitDay4Transfer; // 再次转让限制天数

    private String originalRate; // 原始年化收益率

    private String personTime; // 认购人次

    private String fromInvestmentAmount; // 最低认购金额/起投金额

    private String subjectMaxBuy; // 最大可以认购金额

    private String subjectId; // 标的ID

    private String subjectName; // 标的名称

    private String startTimestamp; // 开始时间戳

    private String residueTimestamp; // 剩余时间

    private String endTimestamp; // 结束时间戳

    private String subjectTotalPrice; // 标的总额

    private String purchasePrice; // 已投总额

    private String residueAmt; // 剩余额度

    private String discountRate; // 折让信息

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPredictRate() {
        return predictRate;
    }

    public void setPredictRate(String predictRate) {
        this.predictRate = predictRate;
    }

    public String getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(String subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getLimitDay4Transfer() {
        return limitDay4Transfer;
    }

    public void setLimitDay4Transfer(String limitDay4Transfer) {
        this.limitDay4Transfer = limitDay4Transfer;
    }

    public String getOriginalRate() {
        return originalRate;
    }

    public void setOriginalRate(String originalRate) {
        this.originalRate = originalRate;
    }

    public String getPersonTime() {
        return personTime;
    }

    public void setPersonTime(String personTime) {
        this.personTime = personTime;
    }

    public String getFromInvestmentAmount() {
        return fromInvestmentAmount;
    }

    public void setFromInvestmentAmount(String fromInvestmentAmount) {
        this.fromInvestmentAmount = fromInvestmentAmount;
    }

    public String getSubjectMaxBuy() {
        return subjectMaxBuy;
    }

    public void setSubjectMaxBuy(String subjectMaxBuy) {
        this.subjectMaxBuy = subjectMaxBuy;
    }

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

    public String getResidueTimestamp() {
        return residueTimestamp;
    }

    public void setResidueTimestamp(String residueTimestamp) {
        this.residueTimestamp = residueTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
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

    public String getResidueAmt() {
        return residueAmt;
    }

    public void setResidueAmt(String residueAmt) {
        this.residueAmt = residueAmt;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }
}
