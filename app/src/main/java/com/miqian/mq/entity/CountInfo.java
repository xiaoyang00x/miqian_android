package com.miqian.mq.entity;

/**
 * Created by Jackie on 2017/7/3.
 */

public class CountInfo {

    private String unuseCount;
    private String usedCount;
    private String expireCount;

    public String getUnuseCount() {
        return unuseCount;
    }

    public void setUnuseCount(String unuseCount) {
        this.unuseCount = unuseCount;
    }

    public String getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(String usedCount) {
        this.usedCount = usedCount;
    }

    public String getExpireCount() {
        return expireCount;
    }

    public void setExpireCount(String expireCount) {
        this.expireCount = expireCount;
    }
}
