package com.miqian.mq.entity;

/**
 * Created by wgl on 5/20/16.
 * 首页新闻动态数据
 */
public class HomeNewsData {
  private int id;
  private String imgUrl;            //图片链接
  private String jumpUrl;           //跳转链接
  private String desc;              //新闻描述
  private String title;             //标题

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
