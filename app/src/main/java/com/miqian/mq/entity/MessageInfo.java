package com.miqian.mq.entity;

/**
 * Created by Joy on 2015/9/16.
 */
public class MessageInfo {


    private  String content;  //内容
    private  String title;
    private  String sendTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

}
