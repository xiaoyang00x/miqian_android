package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/6.
 */
public class UserInfo  implements Serializable{

    public String custId;//客户id
    public String mobilePhone;//手机号
    public String token;//
    public String loginName;//登录名
    public String realName;//真实姓名
    public String nickName;//昵称
    public String idCard;//身份证号
    public String bankCardNo;//银行卡号
    public String payPwdStatus;//支付密码是否设置 0 未设置 1 已设置
    public String balance;//可用余额
    public String curAmt;//活期总额
    public String bindCardStatus;//银行卡绑定状态 0未绑定 1 已绑定
    public String totalProfit;//历史收益
    public String regTotal;//定期认购笔数
    public String totalAsset;//总资产
    public String wealthTicket;//拾财券
    public String realNameStatus;//实名认证状态
    public String redBag;//红包数
    public String bankName;//绑定银行名称
    public String bankUrlSmall;//绑定银行图标url
    public String  bankUrlBig;//绑定银行图标url
    public String bankCode;//绑定银行代码
    public String supportStatus;//是否支持连连绑卡

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

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
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
}
