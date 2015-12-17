package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/12/9.
 */
public class UserMessageResult extends Meta {

    private UserMessageData data;

    public UserMessageData getData() {
        return data;
    }

    public void setData(UserMessageData data) {
        this.data = data;
    }
}
