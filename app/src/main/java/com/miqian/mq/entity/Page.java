package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/12.
 */
public class Page {

    private int total;//共几页
    private int count;//总记录数
    private String sortField;//排序字段
    private int start;//开始查询页码
    private String sortOrder;//排序属性

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
