package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class UserMessageData {

    private List<MessageInfo>  msgList;
    private int nextStartId;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MessageInfo> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MessageInfo> msgList) {
        this.msgList = msgList;
    }

    public int getNextStartId() {
        return nextStartId;
    }

    public void setNextStartId(int nextStartId) {
        this.nextStartId = nextStartId;
    }
}
