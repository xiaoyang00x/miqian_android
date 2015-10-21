package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class  Redpaper{

    private Page page;
    private List<CustPromotion> custPromotion;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<CustPromotion> getCustPromotion() {
        return custPromotion;
    }

    public void setCustPromotion(List<CustPromotion> custPromotion) {
        this.custPromotion = custPromotion;
    }
}