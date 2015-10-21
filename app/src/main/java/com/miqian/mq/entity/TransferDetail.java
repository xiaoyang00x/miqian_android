package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2015/10/15.
 */
public class TransferDetail {

    private TransferInfo transAmt;
    private List<TranferDetailInfo> translist;

    public TransferInfo getTransAmt() {
        return transAmt;
    }

    public void setTransAmt(TransferInfo transAmt) {
        this.transAmt = transAmt;
    }

    public List<TranferDetailInfo> getTranslist() {
        return translist;
    }

    public void setTranslist(List<TranferDetailInfo> translist) {
        this.translist = translist;
    }
}
