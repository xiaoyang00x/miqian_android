package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/30.
 */
public class UserCurrentResult extends Meta {

    private UserCurrentData data;

    public UserCurrentData getData() {
        return data;
    }

    public void setData(UserCurrentData data) {
        this.data = data;
    }
}
