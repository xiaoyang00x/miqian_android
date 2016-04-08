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
        DK("DK"); //抵扣券

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
    private String promProdId;
    /**
     * 促销产品类别的名称
     */
    private String promProdName;
    /**
     * 客户促销余额的ID(核心cs_cust_prom_sa_b表的ID)
     */
    private String id;
    /**
     * 促销类型：SC:拾财券 HB:红包 JF:积分 LP:礼品卡 TY:体验金 JX:加息券 FXQ:分享券 DK:抵扣券
     */
    private String type;
    /**
     * 转化比率
     */
    private String transformRate;
    /**
     * 总额(单位不一定是RMB，有可能是积分什么的，协同转化比率才能算出RMB)
     */
    private BigDecimal totalAmt;
    /**
     * 已用额度
     */
    private BigDecimal usedAmt;
    /**
     * 可用余额
     */
    private BigDecimal canUseAmt;
    /**
     * 抵用比率
     */
    private String toUseRate;
    /**
     * 加息券浮动利率
     */
    private String giveYrt;
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
     * 促销使用的限制范围信息
     */
    private String limitMsg;
    /**
     * 适用最小金额信息
     */
    private String minBuyAmtMsg;
    /**
     * 促销状态
     **/
    private String status;

    private String minBuyAmtOrPerc;
    private String fitBdTermOrYrt;
    private String fitProdOrBdType;
    private String shareUrl;
    /**
     * 是否跳转状态:到h5
     */
    private String promState;
    /**
     * 跳转地址
     **/
    private String promUrl;

    public String getPromProdId() {
        return promProdId;
    }

    public void setPromProdId(String promProdId) {
        this.promProdId = promProdId;
    }

    public String getPromProdName() {
        return promProdName;
    }

    public void setPromProdName(String promProdName) {
        this.promProdName = promProdName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransformRate() {
        return transformRate;
    }

    public void setTransformRate(String transformRate) {
        this.transformRate = transformRate;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public BigDecimal getUsedAmt() {
        return usedAmt;
    }

    public void setUsedAmt(BigDecimal usedAmt) {
        this.usedAmt = usedAmt;
    }

    public BigDecimal getCanUseAmt() {
        return canUseAmt;
    }

    public void setCanUseAmt(BigDecimal canUseAmt) {
        this.canUseAmt = canUseAmt;
    }

    public String getToUseRate() {
        return toUseRate;
    }

    public void setToUseRate(String toUseRate) {
        this.toUseRate = toUseRate;
    }

    public String getGiveYrt() {
        return giveYrt;
    }

    public void setGiveYrt(String giveYrt) {
        this.giveYrt = giveYrt;
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

    public String getLimitMsg() {
        return limitMsg;
    }

    public void setLimitMsg(String limitMsg) {
        this.limitMsg = limitMsg;
    }

    public String getMinBuyAmtMsg() {
        return minBuyAmtMsg;
    }

    public void setMinBuyAmtMsg(String minBuyAmtMsg) {
        this.minBuyAmtMsg = minBuyAmtMsg;
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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
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
