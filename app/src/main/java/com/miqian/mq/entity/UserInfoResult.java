package com.miqian.mq.entity;

/**
 * Created by Administrator on 2017/6/19.
 */

public class UserInfoResult extends Meta{
     UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
