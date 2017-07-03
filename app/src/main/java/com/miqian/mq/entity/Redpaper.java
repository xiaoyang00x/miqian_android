package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class Redpaper {

    private Page pageInfo;
    private List<Promote> promList;
    private CountInfo countInfo;

    public Page getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Page pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Promote> getPromList() {
        return promList;
    }

    public void setPromList(List<Promote> promList) {
        this.promList = promList;
    }

    public CountInfo getCountInfo() {
        return countInfo;
    }

    public void setCountInfo(CountInfo countInfo) {
        this.countInfo = countInfo;
    }
}