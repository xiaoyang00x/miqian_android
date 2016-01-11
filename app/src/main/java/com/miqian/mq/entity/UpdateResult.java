package com.miqian.mq.entity;

/**
 * Created by Jackie on 2016/1/11.
 */
public class UpdateResult extends Meta {

    private UpdateInfo data;

    public UpdateInfo getData() {
        return data;
    }

    public void setData(UpdateInfo data) {
        this.data = data;
    }
}
