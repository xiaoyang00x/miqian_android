package com.miqian.mq.entity;


/**
 * Created by Jackie on 2015/9/11.
 */
public class MaintenanceResult extends Meta {

    private String title;//标题
    private String content;//内容
    private String inscription;//碑铭

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

    public String getInscription() {
        return inscription;
    }

    public void setInscription(String inscription) {
        this.inscription = inscription;
    }
}
