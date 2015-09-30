import com.alibaba.fastjson.JSON;

/**
 * Created by sunyong on 9/25/15.
 */
public class Main {

  public static void main(String[] args) {
    String str =
        "{\n" + "    \"message\": \"成功\",\n" + "    \"data\": {\n" + "        \"subjectInfo\": [\n"
            + "            {\n" + "                \"subjectId\": \"20150921000607\",\n"
            + "                \"subjectName\": \"27\",\n"
            + "                \"startTimestamp\": \"1442820713000\",\n"
            + "                \"endTimestamp\": \"1450627199000\",\n"
            + "                \"purchasePercent\": \"5.6\",\n"
            + "                \"personTime\": \"15\",\n"
            + "                \"subjectType\": \"00\",\n"
            + "                \"subjectStatus\": \"01\",\n"
            + "                \"subjectTotalPrice\": \"2000000\",\n"
            + "                \"subjectBidAmt\": \"112600\",\n"
            + "                \"bxbzf\": \"天津鑫海融资租赁有限公司\",\n"
            + "                \"ddbzf\": \"本息盾（厦门）金融技术服务有限公司\",\n"
            + "                \"presentationYesNo\": \"N\",\n"
            + "                \"presentationYearInterest\": \"0\",\n"
            + "                \"fromInvestmentAmount\": \"100\",\n"
            + "                \"promotionDesc\": \"满1万元送100元红包\",\n"
            + "                \"schemeList\": [\n" + "                    {\n"
            + "                        \"limit\": \"86\",\n"
            + "                        \"yearInterest\": \"9\",\n"
            + "                        \"payMode\": \"月付息，到期还本\"\n" + "                    }\n"
            + "                ]\n" + "            }\n" + "        ],\n" + "        \"adImgs\": [\n"
            + "            {\n"
            + "                \"imgUrl\": \"http://res.shicaidai.com:80/ocp-war/20150908/0c9338e7946f407c918375e78379733e.png\",\n"
            + "                \"id\": 5,\n" + "                \"orderNo\": 2,\n"
            + "                \"jumpUrl\": \"http://www.baidu.com\",\n"
            + "                \"description\": \"asf\"\n" + "            },\n" + "            {\n"
            + "                \"imgUrl\": \"http://res.shicaidai.com:80/ocp-war/20150915/0b12bb91d01d4fd08b6b6fa375f9f5db.png\",\n"
            + "                \"id\": 9,\n" + "                \"orderNo\": 9,\n"
            + "                \"jumpUrl\": \"\",\n" + "                \"description\": \"测试\"\n"
            + "            },\n" + "            {\n"
            + "                \"imgUrl\": \"http://res.shicaidai.com:80/ocp-war/20150915/b386bcc843df45d0891ebbd939a578e7.gif\",\n"
            + "                \"id\": 10,\n" + "                \"orderNo\": 10,\n"
            + "                \"jumpUrl\": \"http://www.youku.com\",\n"
            + "                \"description\": \"对的\"\n" + "            },\n" + "            {\n"
            + "                \"imgUrl\": \"http://res.shicaidai.com:80/ocp-war/20150923/61eaeb65f6664a54ac7a3aa7541c753a.png\",\n"
            + "                \"id\": 11,\n" + "                \"orderNo\": 11,\n"
            + "                \"jumpUrl\": \"http://www.shicaidai.com\",\n"
            + "                \"description\": \"ss\"\n" + "            }\n" + "        ],\n"
            + "        \"newCustomer\": [\n" + "            {\n"
            + "                \"subjectId\": \"20150831000008\",\n"
            + "                \"subjectName\": \"上海富汇融资租赁-扬子江租赁飞机耗材项目\",\n"
            + "                \"startTimestamp\": \"1440986400000\",\n"
            + "                \"endTimestamp\": \"1448812799000\",\n"
            + "                \"purchasePercent\": \"99.3\",\n"
            + "                \"personTime\": \"708\",\n"
            + "                \"subjectType\": \"01\",\n"
            + "                \"subjectStatus\": \"01\",\n"
            + "                \"subjectTotalPrice\": \"100000\",\n"
            + "                \"subjectBidAmt\": \"99300\",\n"
            + "                \"bxbzf\": \"上海富汇融资租赁有限公司\",\n"
            + "                \"ddbzf\": \"本息盾（厦门）金融技术服务有限公司\",\n"
            + "                \"presentationYesNo\": \"N\",\n"
            + "                \"presentationYearInterest\": \"0\",\n"
            + "                \"fromInvestmentAmount\": \"100\",\n"
            + "                \"promotionDesc\": null,\n" + "                \"schemeList\": [\n"
            + "                    {\n" + "                        \"limit\": \"65\",\n"
            + "                        \"yearInterest\": \"9\",\n"
            + "                        \"payMode\": \"月付息，到期还本\"\n" + "                    }\n"
            + "                ]\n" + "            }\n" + "        ]\n" + "    },\n"
            + "    \"code\": \"000000\"\n" + "}";

    Root root = JSON.parseObject(str, Root.class);

    System.out.print("");
    System.out.print("");
    System.out.print("");
    System.out.print("");
    System.out.print("");
  }

  public static class Root {
    String code;
    String message;
    Data data;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public Data getData() {
      return data;
    }

    public void setData(Data data) {
      this.data = data;
    }
  }

  public static class Data {
    private AdvertisementImg[] adImgs;
    private Item newCustomer;
    private Item[] subjectInfo;

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

  public static class AdvertisementImg {
    private int orderNo;//
    private String imgUrl;
    private String jumpUrl;
    private int id;
    private String description;

    public int getOrderNo() {
      return orderNo;
    }

    public void setOrderNo(int orderNo) {
      this.orderNo = orderNo;
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

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }

  public static class Item {
    //／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／
    private String subjectId, subjectName, startTimestamp, endTimestamp, purchasePercent,
        personTime, subjectType, subjectStatus, subjectTotalPrice, subjectBidAmt, bxbzf, ddbzf,
        presentationYesNo, presentationYearInterest, fromInvestmentAmount, promotionDesc;

    private SchemeList[] schemeList;

    public String getSubjectId() {
      return subjectId;
    }

    public void setSubjectId(String subjectId) {
      this.subjectId = subjectId;
    }

    public String getSubjectName() {
      return subjectName;
    }

    public void setSubjectName(String subjectName) {
      this.subjectName = subjectName;
    }

    public String getStartTimestamp() {
      return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
      this.startTimestamp = startTimestamp;
    }

    public String getEndTimestamp() {
      return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
      this.endTimestamp = endTimestamp;
    }

    public String getPersonTime() {
      return personTime;
    }

    public void setPersonTime(String personTime) {
      this.personTime = personTime;
    }

    public String getSubjectType() {
      return subjectType;
    }

    public void setSubjectType(String subjectType) {
      this.subjectType = subjectType;
    }

    public String getSubjectStatus() {
      return subjectStatus;
    }

    public void setSubjectStatus(String subjectStatus) {
      this.subjectStatus = subjectStatus;
    }

    public String getSubjectTotalPrice() {
      return subjectTotalPrice;
    }

    public void setSubjectTotalPrice(String subjectTotalPrice) {
      this.subjectTotalPrice = subjectTotalPrice;
    }

    public String getSubjectBidAmt() {
      return subjectBidAmt;
    }

    public void setSubjectBidAmt(String subjectBidAmt) {
      this.subjectBidAmt = subjectBidAmt;
    }

    public String getBxbzf() {
      return bxbzf;
    }

    public void setBxbzf(String bxbzf) {
      this.bxbzf = bxbzf;
    }

    public String getDdbzf() {
      return ddbzf;
    }

    public void setDdbzf(String ddbzf) {
      this.ddbzf = ddbzf;
    }

    public String getPresentationYesNo() {
      return presentationYesNo;
    }

    public void setPresentationYesNo(String presentationYesNo) {
      this.presentationYesNo = presentationYesNo;
    }

    public String getPresentationYearInterest() {
      return presentationYearInterest;
    }

    public void setPresentationYearInterest(String presentationYearInterest) {
      this.presentationYearInterest = presentationYearInterest;
    }

    public String getFromInvestmentAmount() {
      return fromInvestmentAmount;
    }

    public void setFromInvestmentAmount(String fromInvestmentAmount) {
      this.fromInvestmentAmount = fromInvestmentAmount;
    }

    public String getPromotionDesc() {
      return promotionDesc;
    }

    public void setPromotionDesc(String promotionDesc) {
      this.promotionDesc = promotionDesc;
    }

    public SchemeList[] getSchemeList() {
      return schemeList;
    }

    public void setSchemeList(SchemeList[] schemeList) {
      this.schemeList = schemeList;
    }

    //／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／
    public String getPurchasePercent() {
      return purchasePercent;
    }

    public void setPurchasePercent(String purchasePercent) {
      this.purchasePercent = purchasePercent;
    }
  }

  public static class SchemeList {
    private String limit, yearInterest, payMode;

    public String getLimit() {
      return limit;
    }

    public void setLimit(String limit) {
      this.limit = limit;
    }

    public String getYearInterest() {
      return yearInterest;
    }

    public void setYearInterest(String yearInterest) {
      this.yearInterest = yearInterest;
    }

    public String getPayMode() {
      return payMode;
    }

    public void setPayMode(String payMode) {
      this.payMode = payMode;
    }
  }
}
