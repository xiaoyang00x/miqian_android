package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * Created by guolei_wang on 16/10/28.
 * 资金记录
 */
public class FundFlow {

    /**
     * 资金记录类型 00 全部 01 充值 02 提现 03 认购 04 赎回 05 转让 06 到期还款
     */
    public static final String BILL_TYPE_ALL = "00";
    public static final String BILL_TYPE_CZ = "01";
    public static final String BILL_TYPE_TX = "02";
    public static final String BILL_TYPE_RG = "03";
    public static final String BILL_TYPE_SH = "04";
    public static final String BILL_TYPE_ZR = "05";
    public static final String BILL_TYPE_HK = "06";
    public static final String BILL_TYPE_OTHER = "99";

    private long operateTIme;                   //操作时间
    private String operateName;                 //操作内容
    private String operateUser;                 //操作方
    private String operateRemark;               //备注
    private String operateBillType;             //账单类型
    private BigDecimal operateAmt;              //操作金额
    private BigDecimal capitalAmt;              //本金金额
    private BigDecimal interestAmt;             // 利息金额

    public long getOperateTIme() {
        return operateTIme;
    }

    public void setOperateTIme(long operateTIme) {
        this.operateTIme = operateTIme;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public BigDecimal getOperateAmt() {
        return operateAmt;
    }

    public void setOperateAmt(BigDecimal operateAmt) {
        this.operateAmt = operateAmt;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getOperateRemark() {
        return operateRemark;
    }

    public void setOperateRemark(String operateRemark) {
        this.operateRemark = operateRemark;
    }

    public String getOperateBillType() {
        return operateBillType;
    }

    public void setOperateBillType(String operateBillType) {
        this.operateBillType = operateBillType;
    }

    public BigDecimal getCapitalAmt() {
        return capitalAmt;
    }

    public void setCapitalAmt(BigDecimal capitalAmt) {
        this.capitalAmt = capitalAmt;
    }

    public BigDecimal getInterestAmt() {
        return interestAmt;
    }

    public void setInterestAmt(BigDecimal interestAmt) {
        this.interestAmt = interestAmt;
    }
}
