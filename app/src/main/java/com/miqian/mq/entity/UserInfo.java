package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/9/6.
 */
public class UserInfo {

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



}
