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
    private String curAmt;//活期总额
    private String bindCardStatus;//银行卡绑定状态 0未绑定 1 已绑定
    private String usableSa;//可用余额
    private String idCard;//身份证号
    private String totalProfit;//历史收益
    private String regTotal;//定期认购笔数
    private String bankCardNo;//银行卡号
    private String totalAsset;//总资产
    private String bankCode;//绑定银行代码
    private String bankName;//绑定银行名称
    private String bankUrlSmall;//绑定银行图标url
    private String bankUrlBig;//绑定银行图标url
    private String canRedeem;//可赎回金额
    private int totalPromotion;//优惠券总数
    private String ydayProfit;//昨日收益
    private String custLevel;//新增用户等级
    private String token;

    private String status;//用户状态 active 激活 close 未激活
    private String hfAccountStatus;//汇付账户状态 unaccount 未开户 success 成功开始 process 进行中
    private String cardInfo;//银行卡信息(对象)


// TODO: 2016/10/16
    /**
     * 需要删除的
     */
    private String supportStatus;//是否支持连连绑卡
    private String singleAmtLimit; //单笔限额
    private String dayAmtLimit; // 每日限额
    private String monthAmtLimit ; // 每月限额
    private String payPwdStatus;//支付密码是否设置 0 未设置 1 已设置
    private String realNameStatus;//实名认证状态
    private String addRechargeMinValue;//充值最小额度

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

//    public String getLoginName() {
//        return loginName;
//    }
//
//    public void setLoginName(String loginName) {
//        this.loginName = loginName;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getNickName() {
//        return nickName;
//    }
//
//    public void setNickName(String nickName) {
//        this.nickName = nickName;
//    }

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

    //    public String getBankNo() {
//        return bankNo;
//    }
//
//    public void setBankNo(String bankNo) {
//        this.bankNo = bankNo;
//    }

    public String getPayPwdStatus() {
        return "0";
    }

    public void setPayPwdStatus(String payPwdStatus) {
        this.payPwdStatus = payPwdStatus;
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

    public String getBindCardStatus() {
        return bindCardStatus;
    }

    public void setBindCardStatus(String bindCardStatus) {
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

//    public String getWealthTicket() {
//        return wealthTicket;
//    }
//
//    public void setWealthTicket(String wealthTicket) {
//        this.wealthTicket = wealthTicket;
//    }

    public String getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(String realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

//    public String getRedBag() {
//        return redBag;
//    }
//
//    public void setRedBag(String redBag) {
//        this.redBag = redBag;
//    }

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

    public String getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(String supportStatus) {
        this.supportStatus = supportStatus;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHfAccountStatus() {
        return hfAccountStatus;
    }

    public void setHfAccountStatus(String hfAccountStatus) {
        this.hfAccountStatus = hfAccountStatus;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }
}
