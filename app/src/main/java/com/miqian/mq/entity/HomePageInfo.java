package com.miqian.mq.entity;

/**
 * Created by sunyong on 9/14/15.
 */
public class HomePageInfo extends CommonParameters {
  //private String message;//text description
  //private int code;//response code
  private AdvertisementImg[] adImgs;
  private Item newCustomer;
  private Item[] subjectInfo;

  //public static void main(String[] args) {
  //  String jsonStr = "{\n" + "    \"message\": \"成功\",\n" + "    \"code\": \"000000\",\n"
  //      + "    \"adImgs\": [\n" + "        {\n"
  //      + "            \"imgUrl\": \"http://res.shicaidai.com:80/ocp/0c9338e7946f407c918375e78379733e.png\",\n"
  //      + "            \"id\": 5,\n" + "            \"orderNo\": 1,\n"
  //      + "            \"jumpUrl\": \"http://www.baidu.com\",\n"
  //      + "            \"description\": \"asf\"\n" + "        }\n" + "    ],\n"
  //      + "    \"newCustomer\": {\n" + "        \"itemId\": \"JLKWJEOJ687KJHKH\",\n"
  //      + "        \"title\": \"永兴医院医疗设备项目－9\",\n" + "        \"subTitle1\": \"随存随取，灵活安全\",\n"
  //      + "        \"subTitle2\": \"赎回当日到账\",\n" + "        \"subTitle3\": \"赎回当日到账\",\n"
  //      + "        \"annualizedReturn\": \"12%\",\n" + "        \"limit\": \"220天\",\n"
  //      + "        \"purchasers\": 25,\n" + "        \"purchasePercent\": \"75%\"\n" + "    },\n"
  //      + "    \"subjectInfo\": [\n" + "        {\n"
  //      + "            \"itemId\": \"JLKWJEOJ687KJHKH\",\n"
  //      + "            \"title\": \"永兴医院医疗设备项目－9\",\n"
  //      + "            \"subTitle1\": \"随存随取，灵活安全\",\n" + "            \"subTitle2\": \"赎回当日到账\",\n"
  //      + "            \"subTitle3\": \"赎回当日到账\",\n"
  //      + "            \"annualizedReturn\": \"12%\",\n" + "            \"limit\": \"220天\",\n"
  //      + "            \"purchasers\": 25,\n" + "            \"purchasePercent\": \"75%\"\n"
  //      + "        },\n" + "        {\n" + "            \"itemId\": \"JLKWJEOJ687KJHKH\",\n"
  //      + "            \"title\": \"永兴医院医疗设备项目－9\",\n"
  //      + "            \"subTitle1\": \"随存随取，灵活安全\",\n" + "            \"subTitle2\": \"赎回当日到账\",\n"
  //      + "            \"subTitle3\": \"赎回当日到账\",\n"
  //      + "            \"annualizedReturn\": \"12%\",\n" + "            \"limit\": \"220天\",\n"
  //      + "            \"purchasers\": 25,\n" + "            \"purchasePercent\": \"75%\"\n"
  //      + "        }\n" + "    ]\n" + "}";
  //
  //  HomePageInfo group = JSON.parseObject(jsonStr, HomePageInfo.class);
  //  System.out.println();
  //  System.out.println();
  //  System.out.println();
  //  System.out.println();
  //
  //}

  //public String getMessage() {
  //  return message;
  //}
  //
  //public void setMessage(String message) {
  //  this.message = message;
  //}
  //
  //public int getCode() {
  //  return code;
  //}
  //
  //public void setCode(int code) {
  //  this.code = code;
  //}

  public AdvertisementImg[] getAdImgs() {
    return adImgs;
  }

  public void setAdImgs(AdvertisementImg[] adImgs) {
    this.adImgs = adImgs;
  }

  public Item getNewCustomer() {
    return newCustomer;
  }

  public void setNewCustomer(Item newCustomer) {
    this.newCustomer = newCustomer;
  }

  public Item[] getSubjectInfo() {
    return subjectInfo;
  }

  public void setSubjectInfo(Item[] subjectInfo) {
    this.subjectInfo = subjectInfo;
  }
}
