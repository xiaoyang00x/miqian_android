package com.miqian.mq.entity;

/**
 * Created by wgl on 5/20/16.
 * 首页轮播图数据
 */
public class HomeAdData {
  private String id;
  private String imgUrl;            //图片链接
  private String jumpUrl;           //跳转链接

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
