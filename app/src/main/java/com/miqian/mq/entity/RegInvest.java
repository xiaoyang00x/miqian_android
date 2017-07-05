package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by Jackie on 2015/10/21.
 */
public class RegInvest implements Serializable{

//    private String id;//投资ID(投资编号)
//    private String prodId;//产品ID 定期计划 “4”  定期赚“3”
//    private String productName;//产品名称
//    private String bdNm;//标的名称
//    private String bdTyp;//标的类型  00  标准标的 01  新手专属 02  众人拾财专属 03  老财主专享回馈 04 定向大额投资人 88 88专属
//    private String bdTypeName;//标的类型名称
//    private String sta;//项目状态    审核SH ；计息中JH；已到期JQ ；关闭GB
//    private String dueDt;//计息到期日期
//    private String crtDt;//创建日期
//    private String productRate;//产品原始利率
//    private String productPlusRate;//产品促销增加年利率
//    private String prnTransSa;//投资可转让金额
//    private String transSta;//转让状态  WZ:未转让 ZR:转让中
//    private String transing;//转让中金额
//    private String transed;//已转让金额
//    private String hasTransOper;//是否有转让标识
//    private String payMeansName;//月付息，到期还本"
//    private String limitCnt;//定期计划期限
//    private String projectState;//项目状态 0未结息 1转让中 2已转让 3已到期
//    private String bearingStatus;//结息状态 Y:已结息 N：未结息
//    private String regAmt;//待收本金
//    private String regIncome;//待收收益
//    private String prnIncome;//已收收益
//    private String prnAmt;//总投资本金
//    private String subjectType;//标的类型      "88": 88专属  "07":双倍收益卡
//    private String transedAmt;//已转金额


    private String purchaseSeqno;//认购时的交易流水号
    private String productType;//产品类型  1: 定期项目2: 定期计划  98:老的定期计划    97:老的定期项目
    private String bidType;//  标的类型 PTB：普通标  XSB1：新⼿手标1 XSB2：新⼿手标2 QDB：渠道标
    private String productCode;//产品编号
    private String productName;//产品名称
    private String productRate;//产品原始利率
    private String productPlusRate;//产品促销增加年利率
    private String ticketType;//   促销券类型   0: 红包  1: 秒钱卡   2: 加息卡   3: 双倍加息卡   4: 现金
    private String ticketRate;//使用加息券利息
    private String ticketAmount;//使用红包金额
    private String purchaseAmount;//认购金额
    private String dsAmount;//待收本金
    private String dsProfit;//待收利息
    private String ysAmount;//已收本金
    private String ysProfit;//已收利息
    private String startTime;//认购时间
    private String endTime;//到期时间
    private String productTerm;//产品期限(多少天的产品)
    private String status; //    状态   01: 认购处理中 02: 认购成功 03: 计息中 04: 已结清
    private String repayType; // 还款方式

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getPurchaseSeqno() {
        return purchaseSeqno;
    }

    public void setPurchaseSeqno(String purchaseSeqno) {
        this.purchaseSeqno = purchaseSeqno;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductRate() {
        return productRate;
    }

    public void setProductRate(String productRate) {
        this.productRate = productRate;
    }

    public String getProductPlusRate() {
        return productPlusRate;
    }

    public void setProductPlusRate(String productPlusRate) {
        this.productPlusRate = productPlusRate;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getTicketRate() {
        return ticketRate;
    }

    public void setTicketRate(String ticketRate) {
        this.ticketRate = ticketRate;
    }

    public String getTicketAmount() {
        return ticketAmount;
    }

    public void setTicketAmount(String ticketAmount) {
        this.ticketAmount = ticketAmount;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getDsAmount() {
        return dsAmount;
    }

    public void setDsAmount(String dsAmount) {
        this.dsAmount = dsAmount;
    }

    public String getDsProfit() {
        return dsProfit;
    }

    public void setDsProfit(String dsProfit) {
        this.dsProfit = dsProfit;
    }

    public String getYsAmount() {
        return ysAmount;
    }

    public void setYsAmount(String ysAmount) {
        this.ysAmount = ysAmount;
    }

    public String getYsProfit() {
        return ysProfit;
    }

    public void setYsProfit(String ysProfit) {
        this.ysProfit = ysProfit;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProductTerm() {
        return productTerm;
    }

    public void setProductTerm(String productTerm) {
        this.productTerm = productTerm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
