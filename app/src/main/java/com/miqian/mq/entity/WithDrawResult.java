package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WithDrawResult extends Meta {

    private WithdrawItem  data;

    public WithdrawItem getData() {
        return data;
    }

    public void setData(WithdrawItem data) {
        this.data = data;
    }
}
