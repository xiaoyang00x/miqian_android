package com.miqian.mq.entity;

/**
 * Created by Jackie on 2017/7/12.
 */

public class RedeemMqbInfo {

    private int dayMaxCount;//每日最大赎回次数
    private int dayRemainCount;//用户今天剩余可赎回次数
    private int monthMaxCount;//每月最大赎回次数
    private int monthRemainCount;//用户本月剩余可赎回次数
    private boolean enable;//是否可赎回
    private String mobile;


    public int getDayMaxCount() {
        return dayMaxCount;
    }

    public void setDayMaxCount(int dayMaxCount) {
        this.dayMaxCount = dayMaxCount;
    }

    public int getDayRemainCount() {
        return dayRemainCount;
    }

    public void setDayRemainCount(int dayRemainCount) {
        this.dayRemainCount = dayRemainCount;
    }

    public int getMonthMaxCount() {
        return monthMaxCount;
    }

    public void setMonthMaxCount(int monthMaxCount) {
        this.monthMaxCount = monthMaxCount;
    }

    public int getMonthRemainCount() {
        return monthRemainCount;
    }

    public void setMonthRemainCount(int monthRemainCount) {
        this.monthRemainCount = monthRemainCount;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
