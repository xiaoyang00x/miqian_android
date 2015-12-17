package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class PushData {

    private List<MessageInfo>  pushList;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MessageInfo> getPushList() {
        return pushList;
    }

    public void setPushList(List<MessageInfo> pushList) {
        this.pushList = pushList;
    }
}
