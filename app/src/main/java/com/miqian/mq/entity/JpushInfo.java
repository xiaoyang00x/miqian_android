package com.miqian.mq.entity;

/**
 * Created by Joy on 2015/8/31.
 */
public class JpushInfo {

    private String id;
    private String title;
    private String  content;
    private String time;
    private String url;
    private String uriType;
    private String state; // 1为未读，2为已读
    private String userId;
    private String pushSource;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPushSource() {
        return pushSource;
    }

    public void setPushSource(String pushSource) {
        this.pushSource = pushSource;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUriType() {
        return uriType;
    }

    public void setUriType(String uriType) {
        this.uriType = uriType;
    }
}
