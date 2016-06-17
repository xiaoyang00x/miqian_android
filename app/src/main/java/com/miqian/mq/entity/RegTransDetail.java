package com.miqian.mq.entity;

/**
 * Created by Administrator on 2016/6/15.
 */
public class RegTransDetail {

    private String bdId;	      //标的id
    private String id;	          //投资ID（投资编号）
    private String prodId;	      //产品ID 定期计划 “4”  定期赚“3”
    private String bdNm;	      //标的名称
    private String dueDt;	      //计息到期日期
    private String crtDt;         //创建日期
    private String realInterest;  //实际利率
    private String prnTransSa;	  //投资可转让余额
    private String payMeansName;   //还款方式
    private String limitCnt;       //定期计划期限
    private String projectState;   //项目状态 0未结息 1转让中 2已转让 3已到期
    private String bearingStatus;  //结息状态 Y:已结息 N：未结息
    private String prnAmt;	       //总投资本金
    private String regAmt;         //待收本金
    private String regIncome;      //待收收益
    private String regAssert;      //已结息收益
    private String presentInterest;      //赠送收益
    private String transFlag;      //转让状态
    private String transDesc;      //转让描述
    private String transferOverTop;      //折让过高临界值
    private String transferOverLow;      //折让过低临界值

    public String getBdId() {
        return bdId;
    }

    public void setBdId(String bdId) {
        this.bdId = bdId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDueDt() {
        return dueDt;
    }

    public void setDueDt(String dueDt) {
        this.dueDt = dueDt;
    }

    public String getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(String crtDt) {
        this.crtDt = crtDt;
    }

    public String getRealInterest() {
        return realInterest;
    }

    public void setRealInterest(String realInterest) {
        this.realInterest = realInterest;
    }

    public String getPrnTransSa() {
        return prnTransSa;
    }

    public void setPrnTransSa(String prnTransSa) {
        this.prnTransSa = prnTransSa;
    }

    public String getPayMeansName() {
        return payMeansName;
    }

    public void setPayMeansName(String payMeansName) {
        this.payMeansName = payMeansName;
    }

    public String getLimitCnt() {
        return limitCnt;
    }

    public void setLimitCnt(String limitCnt) {
        this.limitCnt = limitCnt;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

    public String getBearingStatus() {
        return bearingStatus;
    }

    public void setBearingStatus(String bearingStatus) {
        this.bearingStatus = bearingStatus;
    }

    public String getPrnAmt() {
        return prnAmt;
    }

    public void setPrnAmt(String prnAmt) {
        this.prnAmt = prnAmt;
    }

    public String getRegAmt() {
        return regAmt;
    }

    public void setRegAmt(String regAmt) {
        this.regAmt = regAmt;
    }

    public String getRegIncome() {
        return regIncome;
    }

    public void setRegIncome(String regIncome) {
        this.regIncome = regIncome;
    }

    public String getRegAssert() {
        return regAssert;
    }

    public void setRegAssert(String regAssert) {
        this.regAssert = regAssert;
    }

    public String getPresentInterest() {
        return presentInterest;
    }

    public void setPresentInterest(String presentInterest) {
        this.presentInterest = presentInterest;
    }

    public String getTransFlag() {
        return transFlag;
    }

    public void setTransFlag(String transFlag) {
        this.transFlag = transFlag;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public String getTransferOverTop() {
        return transferOverTop;
    }

    public void setTransferOverTop(String transferOverTop) {
        this.transferOverTop = transferOverTop;
    }

    public String getTransferOverLow() {
        return transferOverLow;
    }

    public void setTransferOverLow(String transferOverLow) {
        this.transferOverLow = transferOverLow;
    }
}
