package com.miqian.mq.entity;

/**
 * Created by sunyong on 9/16/15.
 */
public class Item {
  private boolean hasMore;// 为后期的版本做准备。是否显示more按钮
  private String itemId;
  private String title;
  private String subTitleOne;
  private String subTitleTwo;
  private String subTitleThree;
  private String annualizedReturn;
  private String limit;
  private int purchasers;
  private String purchasePercent;

  public boolean isHasMore() {
    return hasMore;
  }

  public void setHasMore(boolean hasMore) {
    this.hasMore = hasMore;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubTitleOne() {
    return subTitleOne;
  }

  public void setSubTitleOne(String subTitleOne) {
    this.subTitleOne = subTitleOne;
  }

  public String getSubTitleTwo() {
    return subTitleTwo;
  }

  public void setSubTitleTwo(String subTitleTwo) {
    this.subTitleTwo = subTitleTwo;
  }

  public String getSubTitleThree() {
    return subTitleThree;
  }

  public void setSubTitleThree(String subTitleThree) {
    this.subTitleThree = subTitleThree;
  }

  public String getAnnualizedReturn() {
    return annualizedReturn;
  }

  public void setAnnualizedReturn(String annualizedReturn) {
    this.annualizedReturn = annualizedReturn;
  }

  public String getLimit() {
    return limit;
  }

  public void setLimit(String limit) {
    this.limit = limit;
  }

  public int getPurchasers() {
    return purchasers;
  }

  public void setPurchasers(int purchasers) {
    this.purchasers = purchasers;
  }

  public String getPurchasePercent() {
    return purchasePercent;
  }

  public void setPurchasePercent(String purchasePercent) {
    this.purchasePercent = purchasePercent;
  }
}