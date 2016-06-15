package com.miqian.mq.entity;

/**
 * Created by Administrator on 2016/6/15.
 */
public class OperationResult extends Meta {

     private RecordInfo data;

    public RecordInfo getData() {
        return data;
    }

    public void setData(RecordInfo data) {
        this.data = data;
    }
}
