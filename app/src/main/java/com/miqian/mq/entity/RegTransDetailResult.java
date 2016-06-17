package com.miqian.mq.entity;

/**
 * Created by Administrator on 2016/6/15.
 *
 * 定期转让详情
 */
public class RegTransDetailResult  extends Meta{

     private RegTransDetail data;

    public RegTransDetail getData() {
        return data;
    }

    public void setData(RegTransDetail data) {
        this.data = data;
    }
}
