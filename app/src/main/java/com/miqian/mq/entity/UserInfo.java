package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/6.
 */
public class UserInfo implements Serializable {

    private String custId;//客户id
    private String hfCustId;//客户汇付id
    private String userName;//真实姓名
    private String mobile;//手机号
    private boolean status;//用户状态 true 激活 false 未激活
    private String curAmt;//活期总额
    private boolean bindCardStatus;//银行卡绑定状态 false未绑定 true 已绑定
    private String usableSa;//可用余额
    private String totalProfit;//历史收益
    private String regTotal;//定期认购笔数
    private String bankCardNo;//银行卡号
    private String totalAsset;//总资产
    private String nickName;//昵称
    private String bankCode;//绑定银行代码
    private String bankName;//绑定银行名称
    private String bankUrlSmall;//绑定银行图标url
    private String bankUrlBig;//绑定银行图标url
    private String canRedeem;//可赎回金额
    private int totalPromotion;//优惠券总数
    private String ydayProfit;//昨日收益
    private String custLevel;//新增用户等级
    private String addRechargeMinValue;//充值最小额度
    private boolean hfAccountStatus;//汇付账户状态 true 开通 false 未开通
    private boolean hfAutoTenderPlanStatus;//汇付自动投标计划状态 true 开通 false 未开通
    private String singleAmtLimit; //单笔限额
    private String dayAmtLimit; // 每日限额
    private String monthAmtLimit ; // 每月限额
    private String token;


// TODO: 2016/10/16
    /**
     * 需要删除的
     */
//    private String supportStatus;//是否支持连连绑卡
    private String realNameStatus;//实名认证状态
    private String idCard;//身份证号

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public String getCanRedeem() {
        return canRedeem;
    }

    public void setCanRedeem(String canRedeem) {
        this.canRedeem = canRedeem;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getHfCustId() {
        return hfCustId;
    }

    public void setHfCustId(String hfCustId) {
        this.hfCustId = hfCustId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getUsableSa() {
        return usableSa;
    }

    public void setUsableSa(String usableSa) {
        this.usableSa = usableSa;
    }

    public String getCurAmt() {
        return curAmt;
    }

    public void setCurAmt(String curAmt) {
        this.curAmt = curAmt;
    }

    public boolean isBindCardStatus() {
        return bindCardStatus;
    }

    public void setBindCardStatus(boolean bindCardStatus) {
        this.bindCardStatus = bindCardStatus;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getRegTotal() {
        return regTotal;
    }

    public void setRegTotal(String regTotal) {
        this.regTotal = regTotal;
    }

    public String getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(String totalAsset) {
        this.totalAsset = totalAsset;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(String realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    public int getTotalPromotion() {
        return totalPromotion;
    }

    public void setTotalPromotion(int totalPromotion) {
        this.totalPromotion = totalPromotion;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankUrlSmall() {
        return bankUrlSmall;
    }

    public void setBankUrlSmall(String bankUrlSmall) {
        this.bankUrlSmall = bankUrlSmall;
    }

    public String getBankUrlBig() {
        return bankUrlBig;
    }

    public void setBankUrlBig(String bankUrlBig) {
        this.bankUrlBig = bankUrlBig;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

//    public String getSupportStatus() {
//        return supportStatus;
//    }
//
//    public void setSupportStatus(String supportStatus) {
//        this.supportStatus = supportStatus;
//    }

    public String getAddRechargeMinValue() {
        return addRechargeMinValue;
    }

    public void setAddRechargeMinValue(String addRechargeMinValue) {
        this.addRechargeMinValue = addRechargeMinValue;
    }

    public String getSingleAmtLimit() {
        return singleAmtLimit;
    }

    public void setSingleAmtLimit(String singleAmtLimit) {
        this.singleAmtLimit = singleAmtLimit;
    }

    public String getDayAmtLimit() {
        return dayAmtLimit;
    }

    public void setDayAmtLimit(String dayAmtLimit) {
        this.dayAmtLimit = dayAmtLimit;
    }

    public String getMonthAmtLimit() {
        return monthAmtLimit;
    }

    public void setMonthAmtLimit(String monthAmtLimit) {
        this.monthAmtLimit = monthAmtLimit;
    }

    public String getYdayProfit() {
        return ydayProfit;
    }

    public void setYdayProfit(String ydayProfit) {
        this.ydayProfit = ydayProfit;
    }

    public boolean isHfAutoTenderPlanStatus() {
        return hfAutoTenderPlanStatus;
    }

    public void setHfAutoTenderPlanStatus(boolean hfAutoTenderPlanStatus) {
        this.hfAutoTenderPlanStatus = hfAutoTenderPlanStatus;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isHfAccountStatus() {
        return hfAccountStatus;
    }

    public void setHfAccountStatus(boolean hfAccountStatus) {
        this.hfAccountStatus = hfAccountStatus;
    }
}
