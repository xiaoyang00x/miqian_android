package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 15/10/13.
 */
public class HomePageInfoResult extends Meta {

    public HomePageInfo getData() {
        return data;
    }

    public void setData(HomePageInfo data) {
        this.data = data;
    }

    private HomePageInfo data;
}
