package com.miqian.mq.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by guolei_wang on 15/9/17.
 * 定期赚
 */
public class RegularEarn implements Serializable {

    private String subjectId;//标的ID
    private String subjectName;//标的名称
    private long startTimestamp; //开始时间
    private long endTimestamp; //结束时间
    private float purchasePercent; //认购进度
    private String personTime; //认购人次
    private String subjectType; //标的类型
    private String subjectStatus; //标的状态  投标状态编码: 99待开标不显示   00待开标   01已开标   02已满标（已售罄）  03已到期  04已撤销  05已流标   06审批中    07还款中    08正常还完
    private BigDecimal subjectTotalPrice; //标的总额
    private BigDecimal purchasePrice; //已认购金额
    private String bxbzf; //本息保障方
    private String ddbzf; //兜底保障方
    private String presentationYesNo; // "N",//是否赠送收益
    private String presentationYearInterest; //赠送年利率
    private BigDecimal fromInvestmentAmount; //起投金额
    private String promotionDesc; //"满1万元送100元红包"//促销描述
    private ArrayList<RegularEarnSubInfo> schemeList;

    //以下字段只有再取详情的时候才有
    private String yearInterest; //年化收益
    private String limit; //期限

    public BigDecimal getSubjectMaxBuy() {
        return subjectMaxBuy;
    }

    public void setSubjectMaxBuy(BigDecimal subjectMaxBuy) {
        this.subjectMaxBuy = subjectMaxBuy;
    }

    private BigDecimal subjectMaxBuy; //最大认购金额


    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public void setYearInterest(String yearInterest) {
        this.yearInterest = yearInterest;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    private String payMode; //还款方式


    /**
     * 获取第一个子标的年化收益
     *
     * @return
     */
    public String getYearInterest() {
        if (schemeList != null && schemeList.size() > 0) {
            return schemeList.get(0).getYearInterest();
        }
        return yearInterest;
    }


    /**
     * 获取第一个子标的期限
     *
     * @return
     */
    public String getLimit() {
        if (schemeList != null && schemeList.size() > 0) {
            return schemeList.get(0).getLimit();
        }
        return limit;
    }

    /**
     * 获取第一个子标的还款方式
     *
     * @return
     */
    public String getPayMode() {
        if (schemeList != null && schemeList.size() > 0) {
            return schemeList.get(0).getPayMode();
        }
        return payMode;
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

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public float getPurchasePercent() {
        return purchasePercent;
    }

    public void setPurchasePercent(float purchasePercent) {
        this.purchasePercent = purchasePercent;
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

    public BigDecimal getFromInvestmentAmount() {
        return fromInvestmentAmount;
    }

    public void setFromInvestmentAmount(BigDecimal fromInvestmentAmount) {
        this.fromInvestmentAmount = fromInvestmentAmount;
    }

    public String getPromotionDesc() {
        return promotionDesc;
    }

    public void setPromotionDesc(String promotionDesc) {
        this.promotionDesc = promotionDesc;
    }

    public ArrayList<RegularEarnSubInfo> getSchemeList() {
        return schemeList;
    }

    public void setSchemeList(ArrayList<RegularEarnSubInfo> schemeList) {
        this.schemeList = schemeList;
    }

}
