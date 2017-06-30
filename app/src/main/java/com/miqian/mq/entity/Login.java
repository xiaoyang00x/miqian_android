package com.miqian.mq.entity;

/**
 * Created by Administrator on 2017/6/19.
 */

public class Login {
    private String custId;//客户id
    private String mobile;//手机号
    private String jxAccountStatus;//江西银行开户状态 1 已开户 0 未开户
    private String isBeforeDepositRegisterStatus;//是否是存管前注册 1 是 0 不是   判断新老用户
    private String token;

    public String getJxAccountStatus() {
        return jxAccountStatus;
    }

    public void setJxAccountStatus(String jxAccountStatus) {
        this.jxAccountStatus = jxAccountStatus;
    }

    public String getIsBeforeDepositRegisterStatus() {
        return isBeforeDepositRegisterStatus;
    }

    public void setIsBeforeDepositRegisterStatus(String isBeforeDepositRegisterStatus) {
        this.isBeforeDepositRegisterStatus = isBeforeDepositRegisterStatus;
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
}
