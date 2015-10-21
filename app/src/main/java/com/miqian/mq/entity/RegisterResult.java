package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/9/6.
 */
public class RegisterResult extends Meta {
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
