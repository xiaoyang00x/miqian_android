package com.miqian.mq.entity;

/**
 * Created by Administrator on 2016/6/15.
 */
public class Operation {
    private String operationDt;//操作时间
    private String operationContent;//操作内容
    private int state;// 0:未发生的操作  1:已发生的操作

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOperationDt() {
        return operationDt;
    }

    public void setOperationDt(String operationDt) {
        this.operationDt = operationDt;
    }

    public String getOperationContent() {
        return operationContent;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }
}
