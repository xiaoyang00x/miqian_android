package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 16/5/20.
 * 首页新手活动
 */
public class HomeNewActivitiesData {

    private String id;
    private String imgUrl;          //图片链接
    private String jumpUrl;         //跳转链接

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
