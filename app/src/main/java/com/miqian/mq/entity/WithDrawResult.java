package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WithDrawResult extends Meta {

    private List<WithdrawItem>  data;

    public List<WithdrawItem> getData() {
        return data;
    }

    public void setData(List<WithdrawItem> data) {
        this.data = data;
    }
}
