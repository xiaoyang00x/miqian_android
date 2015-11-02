package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/21.
 */
public class CustPromotion {

    private String status;//促销状态CS 初始状态JH 激活状态YW 用完状态GQ 过期状态ZS  赠送状态
    private String toUseRate;//折让比例
    private String totalAmt;//总额度
    private String type;//余额类型SC 拾财券HB 红包 JF 积分LP 礼品卡TY 体验金
    private String startTimestamp;//开始日期
    private String usedAmt;//已抵用额度
    private String endTimestamp;//结束时间
    private String canUseAmt;//可用额度
    private String prodPromId;//id
    private String limitMsg;//使用范围

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToUseRate() {
        return toUseRate;
    }

    public void setToUseRate(String toUseRate) {
        this.toUseRate = toUseRate;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getUsedAmt() {
        return usedAmt;
    }

    public void setUsedAmt(String usedAmt) {
        this.usedAmt = usedAmt;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getCanUseAmt() {
        return canUseAmt;
    }

    public void setCanUseAmt(String canUseAmt) {
        this.canUseAmt = canUseAmt;
    }

    public String getProdPromId() {
        return prodPromId;
    }

    public void setProdPromId(String prodPromId) {
        this.prodPromId = prodPromId;
    }

    public String getLimitMsg() {
        return limitMsg;
    }

    public void setLimitMsg(String limitMsg) {
        this.limitMsg = limitMsg;
    }
}