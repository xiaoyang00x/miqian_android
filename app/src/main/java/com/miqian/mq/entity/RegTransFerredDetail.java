package com.miqian.mq.entity;

/**
 * Created by Administrator on 2016/6/17.
 * 我的转让详情
 */
public class RegTransFerredDetail {

    private String id;    //"id": "000000000000000000000018521919",//投资id
    private String bdId;    //"bdId ":"20151023000042"//标的id
    private String prodId;    //"prodId": "3",//产品id  3定期赚  4定期计划
    private String bdNm;     //"bdNm": "嘉实融资租赁公司/本金保障140003项目",//标的名称
    private String originalSubjectNm;     //"prodNm": "保障140003项目",//这是原项目名称
    private Long bdEndTm;    // 开始时间
    private String originalSubjectId;//原始标的的id
    private String bidLmtminAmt;    // "bidLmtminAmt": "200.00",//单次投标最小金额
    private String bdCiMod;     //"bdCiMod": "次日计息",//计息模式
    private String changeMod;   //  "changeMod": "次日可转",//转让模式
    private String limitCnt;   //  "limitCnt": "276",//项目期限
    private String predictRate;   // 预期年化收益
    private String oldYrt;    // "oldYrt": "12", 原标的年化收益原标的年化收益
    private String bidAmt; //"bidAmt": "20000.00",//可认购本金
    private String realBidAmt;   // "realBidAmt": "19900.00"//实际支付金额
    private String regIncome;    //"regIncome": "20000",//利息收益
    private String projectState;  //  "projectState": "3",//项目状态(1转让中 2 已转让 3转让未结息)
    private String surplusInCome; //  "surplusInCome": "33",  //剩余利息
    private String padInComeDay;    //"padInComeDay": "2016-06-18"  //付息日

    public String getOriginalSubjectNm() {
        return originalSubjectNm;
    }

    public void setOriginalSubjectNm(String originalSubjectNm) {
        this.originalSubjectNm = originalSubjectNm;
    }

    public Long getBdEndTm() {
        return bdEndTm;
    }

    public void setBdEndTm(Long bdEndTm) {
        this.bdEndTm = bdEndTm;
    }

    public String getOriginalSubjectId() {
        return originalSubjectId;
    }

    public void setOriginalSubjectId(String originalSubjectId) {
        this.originalSubjectId = originalSubjectId;
    }

    public String getPredictRate() {
        return predictRate;
    }

    public void setPredictRate(String predictRate) {
        this.predictRate = predictRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBdId() {
        return bdId;
    }

    public void setBdId(String bdId) {
        this.bdId = bdId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getBdNm() {
        return bdNm;
    }

    public void setBdNm(String bdNm) {
        this.bdNm = bdNm;
    }

    public String getBidLmtminAmt() {
        return bidLmtminAmt;
    }

    public void setBidLmtminAmt(String bidLmtminAmt) {
        this.bidLmtminAmt = bidLmtminAmt;
    }

    public String getBdCiMod() {
        return bdCiMod;
    }

    public void setBdCiMod(String bdCiMod) {
        this.bdCiMod = bdCiMod;
    }

    public String getChangeMod() {
        return changeMod;
    }

    public void setChangeMod(String changeMod) {
        this.changeMod = changeMod;
    }

    public String getLimitCnt() {
        return limitCnt;
    }

    public void setLimitCnt(String limitCnt) {
        this.limitCnt = limitCnt;
    }

    public String getOldYrt() {
        return oldYrt;
    }

    public void setOldYrt(String oldYrt) {
        this.oldYrt = oldYrt;
    }

    public String getBidAmt() {
        return bidAmt;
    }

    public void setBidAmt(String bidAmt) {
        this.bidAmt = bidAmt;
    }

    public String getRealBidAmt() {
        return realBidAmt;
    }

    public void setRealBidAmt(String realBidAmt) {
        this.realBidAmt = realBidAmt;
    }

    public String getRegIncome() {
        return regIncome;
    }

    public void setRegIncome(String regIncome) {
        this.regIncome = regIncome;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

    public String getSurplusInCome() {
        return surplusInCome;
    }

    public void setSurplusInCome(String surplusInCome) {
        this.surplusInCome = surplusInCome;
    }

    public String getPadInComeDay() {
        return padInComeDay;
    }

    public void setPadInComeDay(String padInComeDay) {
        this.padInComeDay = padInComeDay;
    }
}
