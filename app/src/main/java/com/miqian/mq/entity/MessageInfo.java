package com.miqian.mq.entity;

/**
 * Created by Joy on 2015/9/16.
 */
public class MessageInfo {


    private  String content;  //内容
    private  String id;      //id
    private  String pushSource;
    private  String title;
    private  String jumpUrl;
    private  String sendTime;
    private  String msgType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPushSource() {
        return pushSource;
    }

    public void setPushSource(String pushSource) {
        this.pushSource = pushSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
