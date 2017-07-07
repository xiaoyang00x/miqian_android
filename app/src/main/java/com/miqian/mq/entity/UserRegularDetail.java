package com.miqian.mq.entity;

import java.io.Serializable;
import java.util.List;

public class UserRegularDetail implements Serializable {
    List<Operation> operation;

    public List<Operation> getOperation() {
        return operation;
    }

    public void setOperation(List<Operation> operation) {
        this.operation = operation;
    }

    private String status;
    private String ysAmount;
    private String ysProfit;
    private String dsAmount;
    private String dsProfit;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYsAmount() {
        return ysAmount;
    }

    public void setYsAmount(String ysAmount) {
        this.ysAmount = ysAmount;
    }

    public String getYsProfit() {
        return ysProfit;
    }

    public void setYsProfit(String ysProfit) {
        this.ysProfit = ysProfit;
    }

    public String getDsAmount() {
        return dsAmount;
    }

    public void setDsAmount(String dsAmount) {
        this.dsAmount = dsAmount;
    }

    public String getDsProfit() {
        return dsProfit;
    }

    public void setDsProfit(String dsProfit) {
        this.dsProfit = dsProfit;
    }
}
