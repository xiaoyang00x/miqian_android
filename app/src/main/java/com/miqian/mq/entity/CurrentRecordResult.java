package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 16/10/28.
 * 资金记录服务器返回结果
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
