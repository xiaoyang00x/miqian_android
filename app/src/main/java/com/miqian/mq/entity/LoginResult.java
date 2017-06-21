package com.miqian.mq.entity;

/**
 * Created by Administrator on 2015/9/6.
 */
public class LoginResult extends Meta {
    private Login data;

    public Login getData() {
        return data;
    }

    public void setData(Login data) {
        this.data = data;
    }
}
