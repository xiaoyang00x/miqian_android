package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/24.
 * <p>
 * 红包、拾财券
 */
public class Promote {

    public enum TYPE { // 促销类型
        SC("SC"), // 拾财券
        HB("HB"), //红包
        TY("TY"), //体验金
        JX("JX"), //加息券
        FXQ("FXQ"), //分享券
        DK("DK"), //抵扣券
        SK("SK"); //双倍收益卡

        private final String value;

        TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 促销产品类别的ID(核心cs_prom_prod_c表的ID)
     */
    private String couponsId;
    /**
     * 促销产品类别的名称
     */
    private String promProdName;
    /**
     * 促销类型：SC:拾财券 HB:红包 JF:积分 LP:礼品卡 TY:体验金 JX:加息券 FXQ:分享券 DK:抵扣券  SK:双倍收益卡
     */
    private String type;
    /**
     * 总额(单位不一定是RMB，有可能是积分什么的，协同转化比率才能算出RMB)
     */
    private BigDecimal totalAmt;
    /**
     * 可用余额
     */
    private BigDecimal useableAmt;
    /**
     * 抵用比率
     */
    private String toUseRate;
    /**
     * 加息券额外收益
     */
    private BigDecimal extraIncome;
    /**
     * 开始时间
     */
    private String startTimestamp;
    /**
     * 结束时间
     */
    private String endTimestamp;
    /**
     * 认购时，如果选择本促销，将抵用的实际金额（根据客户认购金额，促销可用余额等计算出来）
     */
    private BigDecimal willUseAmt;
    /**
     * 促销状态
     **/
    private String status;

    private String minBuyAmtOrPerc;
    private String fitBdTermOrYrt;
    private String fitProdOrBdType;
    /**
     * 是否跳转状态:到h5
     */
    private String promState;
    /**
     * 跳转地址
     **/
    private String promUrl;

    public String getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(String couponsId) {
        this.couponsId = couponsId;
    }

    public String getPromProdName() {
        return promProdName;
    }

    public void setPromProdName(String promProdName) {
        this.promProdName = promProdName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public BigDecimal getUseableAmt() {
        return useableAmt;
    }

    public void setUseableAmt(BigDecimal useableAmt) {
        this.useableAmt = useableAmt;
    }

    public String getToUseRate() {
        return toUseRate;
    }

    public void setToUseRate(String toUseRate) {
        this.toUseRate = toUseRate;
    }

    public BigDecimal getExtraIncome() {
        return extraIncome;
    }

    public void setExtraIncome(BigDecimal extraIncome) {
        this.extraIncome = extraIncome;
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

    public BigDecimal getWillUseAmt() {
        return willUseAmt;
    }

    public void setWillUseAmt(BigDecimal willUseAmt) {
        this.willUseAmt = willUseAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMinBuyAmtOrPerc() {
        return minBuyAmtOrPerc;
    }

    public void setMinBuyAmtOrPerc(String minBuyAmtOrPerc) {
        this.minBuyAmtOrPerc = minBuyAmtOrPerc;
    }

    public String getFitBdTermOrYrt() {
        return fitBdTermOrYrt;
    }

    public void setFitBdTermOrYrt(String fitBdTermOrYrt) {
        this.fitBdTermOrYrt = fitBdTermOrYrt;
    }

    public String getFitProdOrBdType() {
        return fitProdOrBdType;
    }

    public void setFitProdOrBdType(String fitProdOrBdType) {
        this.fitProdOrBdType = fitProdOrBdType;
    }

    public String getPromUrl() {
        return promUrl;
    }

    public void setPromUrl(String promUrl) {
        this.promUrl = promUrl;
    }

    public String getPromState() {
        return promState;
    }

    public void setPromState(String promState) {
        this.promState = promState;
    }
}
