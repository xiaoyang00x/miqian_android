package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/6.
 */
public class UserInfo implements Serializable {

    private String custId;//客户id
    private String mobilePhone;//手机号
    private String token;//
    private String loginName;//登录名
    private String realName;//真实姓名
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
    private String bankName;//绑定银行名称
    private String bankUrlSmall;//绑定银行图标url
    private String bankUrlBig;//绑定银行图标url
    private String bankCode;//绑定银行代码
    private String supportStatus;//是否支持连连绑卡
    private String addRechargeMinValue;//充值最小额度
    private String canRedeem;//可赎回金额

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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
}
