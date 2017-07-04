package com.miqian.mq.entity;

/**
 * Created by Administrator on 2017/6/27.
 */

public class CaptchaResult extends Meta {
    public Authcode getData() {
        return data;
    }

    public void setData(Authcode data) {
        this.data = data;
    }

    private Authcode data;
}
