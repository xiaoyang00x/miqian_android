package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/21.
 */
public class RegInvest {

    private String id;//投资ID(投资编号)
    private String prodId;//产品ID 定期计划 “4”  定期赚“3”
    private String prodName;//产品名称
    private String bdNm;//标的名称
    private String bdTyp;//标的类型  00  标准标的 01  新手专属 02  众人拾财专属 03  老财主专享回馈 04 定向大额投资人 88 88专属
    private String bdTypeName;//标的类型名称
    private String sta;//项目状态    审核SH ；计息中JH；已到期JQ ；关闭GB
    private String dueDt;//计息到期日期
    private String crtDt;//创建日期
    private String realInterest;//实际利率
    private String prnTransSa;//投资可转让金额
    private String transSta;//转让状态  WZ:未转让 ZR:转让中
    private String transing;//转让中金额
    private String transed;//已转让金额
    private String hasTransOper;//是否有转让标识
    private String payMeansName;//月付息，到期还本"
    private String limitCnt;//定期计划期限
    private String projectState;//项目状态 0未结息 1转让中 2已转让 3已到期
    private String bearingStatus;//结息状态 Y:已结息 N：未结息
    private String regAmt;//待收本金
    private String regIncome;//待收收益
    private String prnAmt;//总投资本金

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

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getBdNm() {
        return bdNm;
    }

    public void setBdNm(String bdNm) {
        this.bdNm = bdNm;
    }

    public String getBdTyp() {
        return bdTyp;
    }

    public void setBdTyp(String bdTyp) {
        this.bdTyp = bdTyp;
    }

    public String getBdTypeName() {
        return bdTypeName;
    }

    public void setBdTypeName(String bdTypeName) {
        this.bdTypeName = bdTypeName;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
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

    public String getTransSta() {
        return transSta;
    }

    public void setTransSta(String transSta) {
        this.transSta = transSta;
    }

    public String getTransing() {
        return transing;
    }

    public void setTransing(String transing) {
        this.transing = transing;
    }

    public String getTransed() {
        return transed;
    }

    public void setTransed(String transed) {
        this.transed = transed;
    }

    public String getHasTransOper() {
        return hasTransOper;
    }

    public void setHasTransOper(String hasTransOper) {
        this.hasTransOper = hasTransOper;
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

    public String getPrnAmt() {
        return prnAmt;
    }

    public void setPrnAmt(String prnAmt) {
        this.prnAmt = prnAmt;
    }
}
