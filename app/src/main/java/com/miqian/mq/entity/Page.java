package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/12.
 */
public class Page {

    private int totalPage;//共几页
    private int totalRecord;//总记录数
    private String curPageNo;//当前页码
    private String pageSize;//每页记录数

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public String getCurPageNo() {
        return curPageNo;
    }

    public void setCurPageNo(String curPageNo) {
        this.curPageNo = curPageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
