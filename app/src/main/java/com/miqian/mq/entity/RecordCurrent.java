package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by guolei_wang on 16/10/28.
 * 资金记录服务器返回结果Data
 */
public class RecordCurrent {

    private List<FundFlow> fundFlowList;


    public List<FundFlow> getFundFlowList() {
        return fundFlowList;
    }

    public void setFundFlowList(List<FundFlow> fundFlowList) {
        this.fundFlowList = fundFlowList;
    }
}
