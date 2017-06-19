package com.miqian.mq.entity;

/**
 * Created by Administrator on 2017/6/19.
 */

public class Login {
    private String custId;//客户id
    private String mobile;//手机号
    private String token;

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
