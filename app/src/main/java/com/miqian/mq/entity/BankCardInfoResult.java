package com.miqian.mq.entity;

/**
 * Created by Administrator on 2017/6/27.
 */

public class BankCardInfoResult extends Meta{

    private    BankCardInfo  data;

    public BankCardInfo getData() {
        return data;
    }

    public void setData(BankCardInfo data) {
        this.data = data;
    }
}
