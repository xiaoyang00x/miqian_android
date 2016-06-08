package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: 标的特色列表
 * @email: cswangduo@163.com
 * @date: 16/6/4
 */
public class RegularProjectFeature {

    private String title; // 标题
    private String imgUrl; // 图标url
    private String jumpUrl; // 点击跳转地址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}
