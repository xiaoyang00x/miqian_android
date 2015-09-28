package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/24.
 *
 * 红包、拾财券
 */
public class Promote {

    private String promUsedAmt;//促销已用户金额
    private String promTotalAmt;//促销总金额
    private String promSaId;//客户促销余额ID
    private String sa;//本次认购选择本促销时可抵用的金额
    private String toUseRate;//抵用比率
    private String startTimestamp;//开始时间
    private String endTimestamp;//到期时间
    private String promType;//促销类型 SC：拾财券  HB：红包 JF：积分 LP：礼品卡 TY：体验金
    private String prodPromId;//促销类型的ID

    public String getPromUsedAmt() {
        return promUsedAmt;
    }

    public void setPromUsedAmt(String promUsedAmt) {
        this.promUsedAmt = promUsedAmt;
    }

    public String getPromTotalAmt() {
        return promTotalAmt;
    }

    public void setPromTotalAmt(String promTotalAmt) {
        this.promTotalAmt = promTotalAmt;
    }

    public String getPromSaId() {
        return promSaId;
    }

    public void setPromSaId(String promSaId) {
        this.promSaId = promSaId;
    }

    public String getSa() {
        return sa;
    }

    public void setSa(String sa) {
        this.sa = sa;
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

    public String getPromType() {
        return promType;
    }

    public void setPromType(String promType) {
        this.promType = promType;
    }

    public String getProdPromId() {
        return prodPromId;
    }

    public void setProdPromId(String prodPromId) {
        this.prodPromId = prodPromId;
    }
}
