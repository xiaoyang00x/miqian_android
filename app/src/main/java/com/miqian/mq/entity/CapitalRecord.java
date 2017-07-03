package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/13.
 */
public class CapitalRecord {

    private Page pageInfo;

    public Page getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Page pageInfo) {
        this.pageInfo = pageInfo;
    }

    private List<CapitalItem> list;

    public List<CapitalItem> getList() {
        return list;
    }

    public void setList(List<CapitalItem> list) {
        this.list = list;
    }
}
