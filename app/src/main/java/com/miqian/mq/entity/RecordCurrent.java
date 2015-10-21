package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/10.
 */
public class RecordCurrent {

    private Page page;
    private List<CurSubRecord> curSubRecord;
    private String curAmt;//历史收益

    public String getCurAmt() {
        return curAmt;
    }

    public void setCurAmt(String curAmt) {
        this.curAmt = curAmt;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<CurSubRecord> getCurSubRecord() {
        return curSubRecord;
    }

    public void setCurSubRecord(List<CurSubRecord> curSubRecord) {
        this.curSubRecord = curSubRecord;
    }
}
