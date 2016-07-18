package com.miqian.mq.entity;

/**
 * Created by Jackie on 2016/7/14.
 */
public class Advert {

    private String jumpUrl;
    private String imgUrl_android1x;
    private String imgUrl_android2x;
    private int isShow;

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getImgUrl_android1x() {
        return imgUrl_android1x;
    }

    public void setImgUrl_android1x(String imgUrl_android1x) {
        this.imgUrl_android1x = imgUrl_android1x;
    }

    public String getImgUrl_android2x() {
        return imgUrl_android2x;
    }

    public void setImgUrl_android2x(String imgUrl_android2x) {
        this.imgUrl_android2x = imgUrl_android2x;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}
