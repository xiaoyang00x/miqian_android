package com.miqian.mq.entity;

import android.text.TextUtils;

/**
 * Created by guolei_wang on 15/12/18.
 * 提供给 h5页面用于分享的数据
 */
public class ShareData {
    public static final int TYPE_SHARE = 1;
    public static final int TYPE_LINKS = 2;
    public static final String PLATFORM_WECHAT= "1";
    public static final String PLATFORM_WECHAT_MOMENTS= "2";
    public static final String PLATFORM_QQ= "3";
    public static final String PLATFORM_SINA= "4";
    public static final String PLATFORM_SHORT_MSG= "5";
    public static final String PLATFORM_EMAIL= "6";
    public static final String PLATFORM_QQ_ZONE= "7";
    private String title;       //分享标题
    private String content;     //分享文本
    private String contentUrl;  //网址链接
    private String imageUrl;    //图片链接


    //以下四个字段为兼容新版本(与IOS保持一致)
    private String share_title;             //分享标题
    private String share_content;           //分享文本
    private String share_url;               //网址链接
    private String share_imagebase64;       //图片链接

    private int type;                       //1 分享  2 链接
    private String share_platform;          // share平台对应：1 -- 微信好友  2 -- 微信朋友圈 3 -- QQ好友 4 -- 新浪微博 5 -- 短信 6 -- 邮件  7 qq空间
    private String right_name;
    private String links;



    public String getTitle() {
        return TextUtils.isEmpty(title)? share_title : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return TextUtils.isEmpty(content)? share_content : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUrl() {
        return TextUtils.isEmpty(contentUrl)? share_url : contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getImageUrl() {
        return TextUtils.isEmpty(imageUrl)? share_imagebase64 : imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_imagebase64() {
        return share_imagebase64;
    }

    public void setShare_imagebase64(String share_imagebase64) {
        this.share_imagebase64 = share_imagebase64;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShare_platform() {
        return share_platform;
    }

    public void setShare_platform(String share_platform) {
        this.share_platform = share_platform;
    }

    public String getRight_name() {
        return right_name;
    }

    public void setRight_name(String right_name) {
        this.right_name = right_name;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }
}
