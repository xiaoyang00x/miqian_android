package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/21.
 */
public class TransferInfo {

    private String transed;//已转让金额
    private String cantransed;//可转让金额
    private String transing;//转让中金额

    public String getTransed() {
        return transed;
    }

    public void setTransed(String transed) {
        this.transed = transed;
    }

    public String getCantransed() {
        return cantransed;
    }

    public void setCantransed(String cantransed) {
        this.cantransed = cantransed;
    }

    public String getTransing() {
        return transing;
    }

    public void setTransing(String transing) {
        this.transing = transing;
    }
}
