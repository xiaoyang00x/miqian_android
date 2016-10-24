package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/24.
 * <p>
 * 红包、拾财券
 */
public class Promote {

    public enum TYPE { // 促销类型

        TY("TY"), //体验金
        FXQ("FXQ"), //分享券
        DK("DK"), //抵扣券
        HB("0"), //红包
        SC("1"), //秒钱卡
        JX("2"), //加息卡
        SK("3"); //双倍收益卡

        private final String value;

        TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String id;//用户促销券ID
    private String couponsId;//促销券配置ID
    private String name;//促销券名称
    private String startTimestamp;//开始时间
    private String endTimestamp;//结束时间
    private String type;//促销券类型 0：红包 1：秒钱卡 2：加息卡 3：双倍加息卡
    private BigDecimal totalAmt;//总额
    private BigDecimal usedAmt;//已用额度
    private BigDecimal usableAmt;//可用余额
    private String toUseRate;//抵用比率
    private BigDecimal toUseAmt;//抵用金额
    private String addRate;//加息利率
    private BigDecimal addInterest;//加息利息
    private String minBuyAmtOrPerc;//投资最小金额和抵用比率
    private String fitProdOrBdType;//适用渠道、产品类型和标的类型
    private String fitBdTermOrYrt;//适用期限和适用年利率

    private String shareUrl;
    private String promState;//是否跳转状态:到h5
    private String promUrl;//跳转地址

    public String getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(String couponsId) {
        this.couponsId = couponsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getUsableAmt() {
        return usableAmt;
    }

    public void setUsableAmt(BigDecimal usableAmt) {
        this.usableAmt = usableAmt;
    }

    public String getToUseRate() {
        return toUseRate;
    }

    public void setToUseRate(String toUseRate) {
        this.toUseRate = toUseRate;
    }

    public String getAddRate() {
        return addRate;
    }

    public void setAddRate(String addRate) {
        this.addRate = addRate;
    }

    public BigDecimal getAddInterest() {
        return addInterest;
    }

    public void setAddInterest(BigDecimal addInterest) {
        this.addInterest = addInterest;
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

    public BigDecimal getToUseAmt() {
        return toUseAmt;
    }

    public void setToUseAmt(BigDecimal toUseAmt) {
        this.toUseAmt = toUseAmt;
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
