package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class Redpaper {

    private Page page;
    private List<Promote> custPromotion;
    private String expireState;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Promote> getCustPromotion() {
        return custPromotion;
    }

    public void setCustPromotion(List<Promote> custPromotion) {
        this.custPromotion = custPromotion;
    }

    public String getExpireState() {
        return expireState;
    }

    public void setExpireState(String expireState) {
        this.expireState = expireState;
    }
}