package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/12.
 */
public class Page {

    private String total;//共几页
    private String count;//总记录数
    private String sortField;//排序字段
    private String start;//开始查询页码
    private String sortOrder;//排序属性

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
