package com.miqian.mq.entity;

import java.io.Serializable;
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
    private String subjectStatus; //标的状态
    private String subjectTotalPrice; //标的总额
    private String purchasePrice; //已认购金额
    private String bxbzf; //本息保障方
    private String ddbzf; //兜底保障方
    private String presentationYesNo; // "N",//是否赠送收益
    private String presentationYearInterest; //赠送年利率
    private String fromInvestmentAmount; //起投金额
    private String promotionDesc; //"满1万元送100元红包"//促销描述
    private ArrayList<RegularEarnSubInfo> schemeList;


    /**
     * 获取第一个子标的年化收益
     * @return
     */
    public String getYearInterest() {
        if(schemeList != null && schemeList.size() > 0) {
            return schemeList.get(0).getYearInterest();
        }
        return "";
    }


    /**
     * 获取第一个子标的期限
     * @return
     */
    public String getLimit() {
        if(schemeList != null && schemeList.size() > 0) {
            return schemeList.get(0).getLimit();
        }
        return "";
    }

    /**
     * 获取第一个子标的还款方式
     * @return
     */
    public String getPayMode() {
        if(schemeList != null && schemeList.size() > 0) {
            return schemeList.get(0).getPayMode();
        }
        return "";
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

    public ArrayList<RegularEarnSubInfo> getSchemeList() {
        return schemeList;
    }

    public void setSchemeList(ArrayList<RegularEarnSubInfo> schemeList) {
        this.schemeList = schemeList;
    }
}
