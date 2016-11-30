package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by guolei_wang on 15/9/17.
 * 定期赚子标信息
 */
class RegularEarnSubInfo implements Serializable {

    private String yearInterest; //年化收益
    private String limit; //期限
    private String payMode; //还款方式

    public String getYearInterest() {
        return yearInterest;
    }

    public void setYearInterest(String yearInterest) {
        this.yearInterest = yearInterest;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }
}
