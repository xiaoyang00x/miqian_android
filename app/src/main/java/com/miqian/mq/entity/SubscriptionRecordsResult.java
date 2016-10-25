package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 16/10/25.
 * 我的秒钱宝认购记录服务器返回
 */
public class SubscriptionRecordsResult extends Meta {

    private SubscriptionRecords data;

    public SubscriptionRecords getData() {
        return data;
    }

    public void setData(SubscriptionRecords data) {
        this.data = data;
    }
}
