package com.miqian.mq.entity;

/**
 * Created by Administrator on 2016/6/15.
 */
public class Operation {
    private String operationDt;//操作时间
    private String operationContent;//操作内容

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
