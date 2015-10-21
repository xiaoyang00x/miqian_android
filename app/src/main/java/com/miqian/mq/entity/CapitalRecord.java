package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/13.
 */
public class CapitalRecord {

    private Page page;
    private List<CapitalItem> assetRecord;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<CapitalItem> getAssetRecord() {
        return assetRecord;
    }

    public void setAssetRecord(List<CapitalItem> assetRecord) {
        this.assetRecord = assetRecord;
    }
}
