package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/10/10.
 */
public class CurrentRecordResult extends Meta{

    private RecordCurrent data;

    public RecordCurrent getData() {
        return data;
    }

    public void setData(RecordCurrent data) {
        this.data = data;
    }
}
