package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/24.
 *
 * 红包、拾财券
 */
public class Promote {

    private String usedAmt;//促销已用户金额
    private String totalAmt;//促销总金额
    private String id;//客户促销余额ID
    private String willUseAmt;//本次认购选择本促销时可抵用的金额
    private String toUseRate;//抵用比率
    private String startTimestamp;//开始时间
    private String endTimestamp;//到期时间
    private String type;//促销类型 SC：拾财券  HB：红包 JF：积分 LP：礼品卡 TY：体验金
    private String promProdId;//促销类型的ID
    private String limitMsg;//限制范围
    private String canUseAmt;//红包余额

    public String getUsedAmt() {
        return usedAmt;
    }

    public void setUsedAmt(String usedAmt) {
        this.usedAmt = usedAmt;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWillUseAmt() {
        return willUseAmt;
    }

    public void setWillUseAmt(String willUseAmt) {
        this.willUseAmt = willUseAmt;
    }

    public String getToUseRate() {
        return toUseRate;
    }

    public void setToUseRate(String toUseRate) {
        this.toUseRate = toUseRate;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPromProdId() {
        return promProdId;
    }

    public void setPromProdId(String promProdId) {
        this.promProdId = promProdId;
    }

    public String getLimitMsg() {
        return limitMsg;
    }

    public void setLimitMsg(String limitMsg) {
        this.limitMsg = limitMsg;
    }

    public String getCanUseAmt() {
        return canUseAmt;
    }

    public void setCanUseAmt(String canUseAmt) {
        this.canUseAmt = canUseAmt;
    }
}
