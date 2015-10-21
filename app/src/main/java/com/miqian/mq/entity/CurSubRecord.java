package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/21.
 */
public class CurSubRecord {

    private String type;//认购赎回类型
    private String amt;//金额
    private String crtDt;//操作日期
    private String interest; //赎回利息
    private String traCd;//交易码SR01 认购交易SS01 赎回交易SZ01  转让交易为空表示是活期认购

    public String getTraCd() {
        return traCd;
    }

    public void setTraCd(String traCd) {
        this.traCd = traCd;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(String crtDt) {
        this.crtDt = crtDt;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
