package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/21.
 */
public class CapitalItem {

//    private String traDt;//操作日期
//    private String traTm;//操作时间
//    private String traOpNm;//操作类型
//    private String traAmt;//交易金额
//    private String peerCustLoginNm;//操作方
//    private String rem;//备注
//    private String saChgDire;//金额变动方向
//    private String traFundNm;//资金类型名称
//    private String traFundCd;//资金类型编码

      private Long traTime;//操作时间
      private String traAmt;//交易金额
      private String interest;//利息
      private String fee;//手续费
      private String productType;//产品类型1 定期项目2 定期计划3 秒钱宝 99 老活期
      private String productName;//产品名 比如秒钱宝一期
      private String status;//状态  00: 处理中 01: 成功 02: 失败
      private String promotionName;//促销名 （红包，秒钱卡）只有认购的返回中有
      private String promotionAmount;//促销金额  只有认购的返回中有
      private String rechargeTypeName;//充值方式名 只有充值的返回中有

    public Long getTraTime() {
        return traTime;
    }

    public void setTraTime(Long traTime) {
        this.traTime = traTime;
    }

    public String getTraAmt() {
        return traAmt;
    }

    public void setTraAmt(String traAmt) {
        this.traAmt = traAmt;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(String promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public String getRechargeTypeName() {
        return rechargeTypeName;
    }

    public void setRechargeTypeName(String rechargeTypeName) {
        this.rechargeTypeName = rechargeTypeName;
    }
}