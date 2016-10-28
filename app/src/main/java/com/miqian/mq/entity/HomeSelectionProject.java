package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by guolei_wang on 16/5/21.
 * 首页精选项目数据
 */
public class HomeSelectionProject {

    private long startTimestamp;
    private long endTimestamp;
    private String jumpProjectUrl;                              //跳转到项目的H5页面
    private String subjectName;                                 //项目名称
    private String limit;                                       //项目期限
    private String presentationYearInterest;                    //赠送年化收益
    private String yearInterest;                                //年化收益
    private String presentationYesNo;                              //是否赠送收益  Y 赠送  N不赠送
    private String subjectId;                                   //标的ID
    private int prodId;                                      //产品类型：定期赚 3、定期计划4
    private String subscript;                                   //右上角角标
    private BigDecimal subjectTotalPrice;                       //标的总额
    private BigDecimal residueAmt;                              //标的剩余额度
    private String subjectStatus;                               //标的状态  投标状态编码: 99待开标不显示   00待开标   01已开标   02已满标（已售罄）  03已到期  04已撤销  05已流标   06审批中    07还款中    08正常还完
    private String subjectType; // 标的类型 00：标准标的 01：新手专属 02：众人拾财专属 03：老财主专享回馈 04：定向大额投资人 05：拼iPhone干货 06：加息专场 07：双倍收益标 0IP6：0元购iPhone6 88：88专属

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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
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

    public String getPresentationYesNo() {
        return presentationYesNo;
    }

    public void setPresentationYesNo(String presentationYesNo) {
        this.presentationYesNo = presentationYesNo;
    }

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

    public String getSubscript() {
        return subscript;
    }

    public void setSubscript(String subscript) {
        this.subscript = subscript;
    }

    public BigDecimal getSubjectTotalPrice() {
        return subjectTotalPrice;
    }

    public void setSubjectTotalPrice(BigDecimal subjectTotalPrice) {
        this.subjectTotalPrice = subjectTotalPrice;
    }

    public BigDecimal getResidueAmt() {
        return residueAmt;
    }

    public void setResidueAmt(BigDecimal residueAmt) {
        this.residueAmt = residueAmt;
    }

    public String getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(String subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public String getJumpProjectUrl() {
        return jumpProjectUrl;
    }

    public void setJumpProjectUrl(String jumpProjectUrl) {
        this.jumpProjectUrl = jumpProjectUrl;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
}
