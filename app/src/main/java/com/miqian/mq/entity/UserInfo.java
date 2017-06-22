package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/6.
 */
public class UserInfo implements Serializable {

    private String custId;//客户id
    private String mobile;//手机号
    private String token;//
    private String loginName;//登录名
    private String nickName;//昵称
    private String idCard;//身份证号
    private String bankNo;//银行卡号
    private String payPwdStatus;//支付密码是否设置 0 未设置 1 已设置
    private String balance;//可用余额
    private String curAmt;//活期总额
    private String bindCardStatus;//银行卡绑定状态 0未绑定 1 已绑定
    private String totalProfit;//历史收益
    private String regTotal;//定期认购笔数
    private String totalAsset;//总资产
    private String wealthTicket;//拾财券
    private String realNameStatus;//实名认证状态
    private String redBag;//红包数
    private int totalPromotion;//优惠券总数
    private String bankName;//绑定银行名称
    private String bankUrlSmall;//绑定银行图标url
    private String bankUrlBig;//绑定银行图标url
    private String bankCode;//绑定银行代码
    private String supportStatus;//银行支持状态
    private String amtMinLimit;//充值最小额度
    private String canRedeem;//可赎回金额
    private String amtPerLimit; //单笔限额
    private String dayAmtLimit; // 每日限额
    private String monthAmtLimit ; // 每月限额
    private String ydayProfit;//昨日收益
    private String custLevel;//新增用户等级
    private String withdrawCashSwitch; //2017手Q 提现开关

    //江西银行存管增加字段 2017.6.2
    private String userName;//真实姓名
    private String userNameStatus;//用户实名状态
    private String jxId; //江西银行用户id
    private String status; //用户状态  激活与未激活
    private String jxAccountStatus; //是否开通存管账户 1已开通 0 未开通
    private String newCurrentAmt; //秒钱宝余额
    private String currentAmt; //老用户活期余额
    private String currentCanRedeem; //活期可赎回金额
    private String newCurrentCanRedeem; //秒钱宝可赎回金额
    private String jxAutoClaimsTransferStatus; //是否开通自动债权转让 1已开通 0 未开通
    private String jxAutoSubscribeStatus; //是否开通自动投标 1为已开通 0为未开通

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameStatus() {
        return userNameStatus;
    }

    public void setUserNameStatus(String userNameStatus) {
        this.userNameStatus = userNameStatus;
    }

    public String getJxId() {
        return jxId;
    }

    public void setJxId(String jxId) {
        this.jxId = jxId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJxAccountStatus() {
        return jxAccountStatus;
    }

    public void setJxAccountStatus(String jxAccountStatus) {
        this.jxAccountStatus = jxAccountStatus;
    }

    public String getNewCurrentAmt() {
        return newCurrentAmt;
    }

    public void setNewCurrentAmt(String newCurrentAmt) {
        this.newCurrentAmt = newCurrentAmt;
    }

    public String getCurrentAmt() {
        return currentAmt;
    }

    public void setCurrentAmt(String currentAmt) {
        this.currentAmt = currentAmt;
    }

    public String getCurrentCanRedeem() {
        return currentCanRedeem;
    }

    public void setCurrentCanRedeem(String currentCanRedeem) {
        this.currentCanRedeem = currentCanRedeem;
    }

    public String getNewCurrentCanRedeem() {
        return newCurrentCanRedeem;
    }

    public void setNewCurrentCanRedeem(String newCurrentCanRedeem) {
        this.newCurrentCanRedeem = newCurrentCanRedeem;
    }

    public String getJxAutoClaimsTransferStatus() {
        return jxAutoClaimsTransferStatus;
    }

    public void setJxAutoClaimsTransferStatus(String jxAutoClaimsTransferStatus) {
        this.jxAutoClaimsTransferStatus = jxAutoClaimsTransferStatus;
    }

    public String getJxAutoSubscribeStatus() {
        return jxAutoSubscribeStatus;
    }

    public void setJxAutoSubscribeStatus(String jxAutoSubscribeStatus) {
        this.jxAutoSubscribeStatus = jxAutoSubscribeStatus;
    }

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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getPayPwdStatus() {
        return payPwdStatus;
    }

    public void setPayPwdStatus(String payPwdStatus) {
        this.payPwdStatus = payPwdStatus;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public String getWealthTicket() {
        return wealthTicket;
    }

    public void setWealthTicket(String wealthTicket) {
        this.wealthTicket = wealthTicket;
    }

    public String getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(String realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    public String getRedBag() {
        return redBag;
    }

    public void setRedBag(String redBag) {
        this.redBag = redBag;
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

    public String getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(String supportStatus) {
        this.supportStatus = supportStatus;
    }

    public String getAmtMinLimit() {
        return amtMinLimit;
    }

    public void setAmtMinLimit(String amtMinLimit) {
        this.amtMinLimit = amtMinLimit;
    }

    public String getAmtPerLimit() {
        return amtPerLimit;
    }

    public void setAmtPerLimit(String amtPerLimit) {
        this.amtPerLimit = amtPerLimit;
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

    public String getWithdrawCashSwitch() {
        return withdrawCashSwitch;
    }

    public void setWithdrawCashSwitch(String withdrawCashSwitch) {
        this.withdrawCashSwitch = withdrawCashSwitch;
    }
}
