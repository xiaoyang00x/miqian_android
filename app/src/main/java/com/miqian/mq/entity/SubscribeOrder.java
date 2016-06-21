package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/29.
 */
public class SubscribeOrder {

    private String orderNo;
    private String goldCoin;
    private String goldCoin_url;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(String goldCoin) {
        this.goldCoin = goldCoin;
    }

    public String getGoldCoin_url() {
        return goldCoin_url;
    }

    public void setGoldCoin_url(String goldCoin_url) {
        this.goldCoin_url = goldCoin_url;
    }
}
