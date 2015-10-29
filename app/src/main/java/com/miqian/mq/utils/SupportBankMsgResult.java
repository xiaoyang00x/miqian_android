package com.miqian.mq.utils;

import com.miqian.mq.entity.Meta;

/**
 * Created by Jackie on 2015/10/29.
 */
public class SupportBankMsgResult extends Meta {

    private SupportBankMsg data;

    public SupportBankMsg getData() {
        return data;
    }

    public void setData(SupportBankMsg data) {
        this.data = data;
    }
}
