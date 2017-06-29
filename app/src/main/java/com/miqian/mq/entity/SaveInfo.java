package com.miqian.mq.entity;

/**
 * Created by Jackie on 2017/6/29.
 */

public class SaveInfo {

    private String jxPayPwdStatus;      //是否设置支付密码 1 已设置 0 未设置
    private String jxId;            //江西银行用户ID
    private String jxAccountStatus;     //是否开通存管账户 1已开通 0 未开通
    private String jxAutoClaimsTransferStatus;  //是否开通自动债权转让 1已开通 0 未开通
    private String jxAutoSubscribeStatus;       //是否开通自动投标 1已开通 0未开通
    private String custId;
    private String mobile;          //手机号
    private String idCard;          //身份证号
    private String userName;        //用户姓名
    private String warmthTips;      //温馨提示

    public String getJxPayPwdStatus() {
        return jxPayPwdStatus;
    }

    public void setJxPayPwdStatus(String jxPayPwdStatus) {
        this.jxPayPwdStatus = jxPayPwdStatus;
    }

    public String getJxId() {
        return jxId;
    }

    public void setJxId(String jxId) {
        this.jxId = jxId;
    }

    public String getJxAccountStatus() {
        return jxAccountStatus;
    }

    public void setJxAccountStatus(String jxAccountStatus) {
        this.jxAccountStatus = jxAccountStatus;
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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWarmthTips() {
        return warmthTips;
    }

    public void setWarmthTips(String warmthTips) {
        this.warmthTips = warmthTips;
    }
}
