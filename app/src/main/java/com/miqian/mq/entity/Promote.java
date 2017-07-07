package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/24.
 * <p>
 * 红包、拾财券
 */
public class Promote {

    public enum TYPE { // 促销类型
        HB("0"), //红包
        SC("1"), //秒钱卡
        JX("2"), //加息券
        SK("3"), //双倍加息卡
        CA("4"); //现金

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
     * 促销Id
     */
    private String id;
    /**
     * 促销产品类别的名称
     */
    private String name;
    /**
     * 促销类型：SC:拾财券 HB:红包 JF:积分 LP:礼品卡 TY:体验金 JX:加息券 FXQ:分享券 DK:抵扣券  SK:双倍收益卡
     */
    private String type;
    /**
     * 总额度(单位不一定是RMB，有可能是积分什么的，协同转化比率才能算出RMB)
     */
    private BigDecimal totalAmt;
    /**
     * 可用余额
     */
    private BigDecimal usableAmt;
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
     * 促销状态
     * 0 可使用 1 使用完 2 已过期
     **/
    private String status;

    private String minBuyAmtOrPerc;//促销最小使用金额和抵用比例
    private String fitBdTermOrYrt;//使用期限和适用利率
    private String fitProdOrBdType;//促销产品适用产品类型和标的类型
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
