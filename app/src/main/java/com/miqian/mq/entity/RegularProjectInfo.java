package com.miqian.mq.entity;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目信息
 * @email: cswangduo@163.com
 * @date: 16/5/27
 */
public class RegularProjectInfo extends RegularBase {

    private String presentationYearInterest; // 赠送年利率

    private String businessType; // 标的业务类型

    private String yearInterest; // 年化收益率

    private BigDecimal continueInvestmentLimit; // 续投金额

    private BigDecimal unitAmount; // 每份投资金额

    private BigDecimal userSubjectRemainAmt; // 登录用户剩余可认购金额

    private String presentationYesNo; // 是否赠送收益

    private String limit; // 期限

    private String predictRate; // 预计年化收益率

    private int subjectStatus; // 标的状态 00：待开标 01：已开标 02：已满标（已售罄）03：已到期 04：已撤销 05：已流标 06：审批中 07：还款中 08：正常还完

    private String subjectType; // 标的类型 88:88-活动 00:标准标的 01:新手专属 02:众人拾财专属 03:老财主专享回馈 04:定向大额投资人
    public final static String TYPE_RATE = "88";

    private int prodId; // 产品类型  1活期，2活期转让，3定期，4定期转让，5定期计划，6计划转让

    private int limitDay4Transfer; // 再次转让限制天数

    private String transferFlag; // 转让天数信息

    private String originalRate; // 原始年化收益率

    private String personTime; // 认购人次

    private BigDecimal fromInvestmentAmount; // 最低认购金额/起投金额

    private BigDecimal subjectMaxBuy; // 最大可以认购金额

    private String subjectId; // 标的ID

    private String subjectName; // 标的名称

    private String projectCode; // 项目编号

    private long startTimestamp; // 开始时间戳

    private String residueTimestamp; // 剩余时间

    private long endTimestamp; // 结束时间戳

    private BigDecimal subjectTotalPrice; // 标的总额

    private BigDecimal purchasePrice; // 已投总额

    private BigDecimal residueAmt; // 剩余额度

    private BigDecimal discountRate; // 折让信息

    private BigDecimal actualAmt; // 全额认购实际支付金额

    private BigDecimal predictIncome; // 全额认购实际利息收益

    private String originalSubjectName; // 原始标的名称

    private String originalSubjectId; // 原始标的ID

    private String festival88; // 88理财节文案

    private String festival88_url; // 88理财节跳转url

    private ArrayList<RegularEarnDetail> subjectBar;

    private ArrayList<RegularProjectMatch> matchItem;

    private ArrayList<RegularProjectFeature> subjectFeature;

    public String getPresentationYearInterest() {
        return presentationYearInterest;
    }

    public void setPresentationYearInterest(String presentationYearInterest) {
        this.presentationYearInterest = presentationYearInterest;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getYearInterest() {
        return yearInterest;
    }

    public void setYearInterest(String yearInterest) {
        this.yearInterest = yearInterest;
    }

    public BigDecimal getContinueInvestmentLimit() {
        return continueInvestmentLimit;
    }

    public void setContinueInvestmentLimit(BigDecimal continueInvestmentLimit) {
        this.continueInvestmentLimit = continueInvestmentLimit;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public BigDecimal getUserSubjectRemainAmt() {
        return userSubjectRemainAmt;
    }

    public void setUserSubjectRemainAmt(BigDecimal userSubjectRemainAmt) {
        this.userSubjectRemainAmt = userSubjectRemainAmt;
    }

    public String getPresentationYesNo() {
        return presentationYesNo;
    }

    public void setPresentationYesNo(String presentationYesNo) {
        this.presentationYesNo = presentationYesNo;
    }

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

    public int getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(int subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getLimitDay4Transfer() {
        return limitDay4Transfer;
    }

    public void setLimitDay4Transfer(int limitDay4Transfer) {
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

    public BigDecimal getFromInvestmentAmount() {
        return fromInvestmentAmount;
    }

    public void setFromInvestmentAmount(BigDecimal fromInvestmentAmount) {
        this.fromInvestmentAmount = fromInvestmentAmount;
    }

    public BigDecimal getSubjectMaxBuy() {
        return subjectMaxBuy;
    }

    public void setSubjectMaxBuy(BigDecimal subjectMaxBuy) {
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

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getResidueTimestamp() {
        return residueTimestamp;
    }

    public void setResidueTimestamp(String residueTimestamp) {
        this.residueTimestamp = residueTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
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

    public BigDecimal getResidueAmt() {
        return residueAmt;
    }

    public void setResidueAmt(BigDecimal residueAmt) {
        this.residueAmt = residueAmt;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public ArrayList<RegularEarnDetail> getSubjectBar() {
        return subjectBar;
    }

    public void setSubjectBar(ArrayList<RegularEarnDetail> subjectBar) {
        this.subjectBar = subjectBar;
    }

    public ArrayList<RegularProjectMatch> getMatchItem() {
        return matchItem;
    }

    public void setMatchItem(ArrayList<RegularProjectMatch> matchItem) {
        this.matchItem = matchItem;
    }

    public ArrayList<RegularProjectFeature> getSubjectFeature() {
        return subjectFeature;
    }

    public void setSubjectFeature(ArrayList<RegularProjectFeature> subjectFeature) {
        this.subjectFeature = subjectFeature;
    }

    public BigDecimal getActualAmt() {
        return actualAmt;
    }

    public void setActualAmt(BigDecimal actualAmt) {
        this.actualAmt = actualAmt;
    }

    public BigDecimal getPredictIncome() {
        return predictIncome;
    }

    public void setPredictIncome(BigDecimal predictIncome) {
        this.predictIncome = predictIncome;
    }

    public String getOriginalSubjectName() {
        return originalSubjectName;
    }

    public void setOriginalSubjectName(String originalSubjectName) {
        this.originalSubjectName = originalSubjectName;
    }

    public String getOriginalSubjectId() {
        return originalSubjectId;
    }

    public void setOriginalSubjectId(String originalSubjectId) {
        this.originalSubjectId = originalSubjectId;
    }

    public String getFestival88() {
        return festival88;
    }

    public void setFestival88(String festival88) {
        this.festival88 = festival88;
    }

    public String getFestival88_url() {
        return festival88_url;
    }

    public void setFestival88_url(String festival88_url) {
        this.festival88_url = festival88_url;
    }

    public String getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(String transferFlag) {
        this.transferFlag = transferFlag;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
}
