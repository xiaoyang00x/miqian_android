package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 15/10/13.
 */
public class HomePageInfoResult extends Meta {

    private ArrayList<HomePageInfo> data;

    public ArrayList<HomePageInfo> getData() {
        return data;
    }

    public void setData(ArrayList<HomePageInfo> data) {
        this.data = data;
    }
}
