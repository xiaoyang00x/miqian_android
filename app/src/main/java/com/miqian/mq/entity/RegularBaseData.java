package com.miqian.mq.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by guolei_wang on 15/12/15.
 * 定期赚和定期计划的基础数据结构
 */
public class RegularBaseData implements Serializable {

    protected String subjectId;//标的ID
    protected int prodId;//产品类型：定期赚 3、定期计划4
    protected String subjectName;//标的名称
    protected long startTimestamp; //开始时间
    protected long endTimestamp; //结束时间
    protected float purchasePercent; //认购进度
    protected int subjectStatus; //标的状态  投标状态编码: 99待开标不显示
    protected int presentationYesNo; // "0",//是否赠送收益  1 赠送
    protected String presentationYearInterest; //赠送年利率
    protected String subjectType;

    private BigDecimal residueAmt;
    private BigDecimal subjectTotalPrice;

    protected String yearInterest; //年化收益
    protected String limit; //期限

    //以下数据为本地设置，不是服务器返回数据
    private boolean showLable;  //是否展示首页 lable，
    private String subjectCategoryName;
    private String subjectCategoryIconUrl;
    private String subjectCategoryDesc;
    private String subjectCategoryDescUrl;


    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
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

    public int getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(int subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public int getPresentationYesNo() {
        return presentationYesNo;
    }

    public void setPresentationYesNo(int presentationYesNo) {
        this.presentationYesNo = presentationYesNo;
    }

    public String getPresentationYearInterest() {
        return presentationYearInterest;
    }

    public void setPresentationYearInterest(String presentationYearInterest) {
        this.presentationYearInterest = presentationYearInterest;
    }

    public String getYearInterest() {
        return yearInterest;
    }

    public void setYearInterest(String yearInterest) {
        this.yearInterest = yearInterest;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public boolean isShowLable() {
        return showLable;
    }

    public void setShowLable(boolean showLable) {
        this.showLable = showLable;
    }

    public String getSubjectCategoryName() {
        return subjectCategoryName;
    }

    public void setSubjectCategoryName(String subjectCategoryName) {
        this.subjectCategoryName = subjectCategoryName;
    }

    public String getSubjectCategoryDesc() {
        return subjectCategoryDesc;
    }

    public void setSubjectCategoryDesc(String subjectCategoryDesc) {
        this.subjectCategoryDesc = subjectCategoryDesc;
    }

    public String getSubjectCategoryDescUrl() {
        return subjectCategoryDescUrl;
    }

    public void setSubjectCategoryDescUrl(String subjectCategoryDescUrl) {
        this.subjectCategoryDescUrl = subjectCategoryDescUrl;
    }

    public String getSubjectCategoryIconUrl() {
        return subjectCategoryIconUrl;
    }

    public void setSubjectCategoryIconUrl(String subjectCategoryIconUrl) {
        this.subjectCategoryIconUrl = subjectCategoryIconUrl;
    }

    public BigDecimal getResidueAmt() {
        return residueAmt;
    }

    public void setResidueAmt(BigDecimal residueAmt) {
        this.residueAmt = residueAmt;
    }

    public BigDecimal getSubjectTotalPrice() {
        return subjectTotalPrice;
    }

    public void setSubjectTotalPrice(BigDecimal subjectTotalPrice) {
        this.subjectTotalPrice = subjectTotalPrice;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
}
