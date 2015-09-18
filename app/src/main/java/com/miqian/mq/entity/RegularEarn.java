package com.miqian.mq.entity;

import java.io.Serializable;

/**
 * Created by guolei_wang on 15/9/17.
 */
public class RegularEarn implements Serializable {

    private String bdNm;  //标的名称
    private String bdDesp;  //标的描述
    private String yrt;  //标的预计年化利率

    public RegularEarn(String bdNm, String bdDesp, String yrt) {
        this.bdNm = bdNm;
        this.bdDesp = bdDesp;
        this.yrt = yrt;
    }

    public String getBdNm() {
        return bdNm;
    }

    public void setBdNm(String bdNm) {
        this.bdNm = bdNm;
    }

    public String getBdDesp() {
        return bdDesp;
    }

    public void setBdDesp(String bdDesp) {
        this.bdDesp = bdDesp;
    }

    public String getYrt() {
        return yrt;
    }

    public void setYrt(String yrt) {
        this.yrt = yrt;
    }
}
