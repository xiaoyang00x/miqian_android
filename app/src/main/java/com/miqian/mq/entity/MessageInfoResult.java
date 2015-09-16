package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/9/16.
 */
public class MessageInfoResult extends Meta {


    private  MessageInfo  data;

    public MessageInfo getData() {
        return data;
    }

    public void setData(MessageInfo data) {
        this.data = data;
    }
}
