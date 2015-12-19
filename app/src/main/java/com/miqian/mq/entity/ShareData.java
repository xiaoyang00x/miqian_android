package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 15/12/18.
 * 提供给 h5页面用于分享的数据
 */
public class ShareData {
    private String title;       //分享标题
    private String content;     //分享文本
    private String contentUrl;  //网址链接
    private String imageUrl;    //图片链接

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

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
