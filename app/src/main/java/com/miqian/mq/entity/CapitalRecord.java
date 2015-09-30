package com.miqian.mq.entity;

/**
 * 接口文档
 * https://tower.im/projects/dd55cdc7404448b7bc3f101d5fed84e1/docs/3d14a74c188148d186c518fe441ae551/
 * Created by sunyong on 9/23/15.
 */
public class CapitalRecord {
  /**
   * todo 这里参数对应的是res json
   */
  String code;//返回代码
  String message;   //返回代码对应消息
  Page page;//分页信息
  Item[] data;//数据信息
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

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Item[] getData() {
    return data;
  }

  public void setData(Item[] data) {
    this.data = data;
  }

  public static class Page {
    int total;// 23,
    int count;//113
    String sortField;//"sortField": "",
    int start;//0
    String sortOrder;//"sortOrder": ""

    public int getTotal() {
      return total;
    }

    public void setTotal(int total) {
      this.total = total;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public String getSortField() {
      return sortField;
    }

    public void setSortField(String sortField) {
      this.sortField = sortField;
    }

    public int getStart() {
      return start;
    }

    public void setStart(int start) {
      this.start = start;
    }

    public String getSortOrder() {
      return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
      this.sortOrder = sortOrder;
    }
  }

  public static class Item {
    String traTm;//"traTm": "10:30:58",
    String traDt;//    "traDt": "2015-09-09",
    String seqNo;//   "seqNo": "",
    int totalAmt;//   "totalAmt": 0,
    int waitRecvAmt;//  "waitRecvAmt": 0,
    String id;//  "id": "000000000000000000000018585715",
    String appDt;//  "appDt": "",
    int freezeSa;//  "freezeSa": 0,
    String peerFundLYn;//  "peerFundLYn": "",
    String finDt;//"finDt": "2015-09-09",
    String traOpNm;//   "traOpNm": "申请提现",
    String investId;//  "investId": "",
    int usableSa;//  "usableSa": 0,
    String finTm;//  "finTm": "10:30:58",
    String custId;//  "custId": "",
    String bdId;// "bdId": "",
    String appTm;// "appTm": "",
    String traFundNm;// "traFundNm": "资金",
    String traAmt;// "traAmt": 100,
    String prodId;// "prodId": "",
    String freezeYn;//"freezeYn": "Y",
    String transYn;//   "transYn": "",
    String pubBdCustId;//   "pubBdCustId": "",
    String pubBdLoginNm;//    "pubBdLoginNm": "",
    String peerCustLoginNm;//   "peerCustLoginNm": "系统",
    String cutLoginNm;//  "custLoginNm": "",
    String sxtzsNo;//  "sxtzsNo": "",
    String traOpTypNm;//  "traOpTypNm": "",
    int peerAmt;//  "peerAmt": 0,
    String rem;// "rem": "用户申请提现,资金被冻结等待处理",
    String traOpTypCd;//  "traOpTypCd": "",
    String traFundCd;//  "traFundCd": "ZJ",
    String peerCustId;//"peerCustId": "",
    String traCd;//   "traCd": "",
    String saChgDire;//    "saChgDire": "JS"

    public String getTraTm() {
      return traTm;
    }

    public void setTraTm(String traTm) {
      this.traTm = traTm;
    }

    public String getTraDt() {
      return traDt;
    }

    public void setTraDt(String traDt) {
      this.traDt = traDt;
    }

    public String getSeqNo() {
      return seqNo;
    }

    public void setSeqNo(String seqNo) {
      this.seqNo = seqNo;
    }

    public int getTotalAmt() {
      return totalAmt;
    }

    public void setTotalAmt(int totalAmt) {
      this.totalAmt = totalAmt;
    }

    public int getWaitRecvAmt() {
      return waitRecvAmt;
    }

    public void setWaitRecvAmt(int waitRecvAmt) {
      this.waitRecvAmt = waitRecvAmt;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getAppDt() {
      return appDt;
    }

    public void setAppDt(String appDt) {
      this.appDt = appDt;
    }

    public int getFreezeSa() {
      return freezeSa;
    }

    public void setFreezeSa(int freezeSa) {
      this.freezeSa = freezeSa;
    }

    public String getPeerFundLYn() {
      return peerFundLYn;
    }

    public void setPeerFundLYn(String peerFundLYn) {
      this.peerFundLYn = peerFundLYn;
    }

    public String getFinDt() {
      return finDt;
    }

    public void setFinDt(String finDt) {
      this.finDt = finDt;
    }

    public String getTraOpNm() {
      return traOpNm;
    }

    public void setTraOpNm(String traOpNm) {
      this.traOpNm = traOpNm;
    }

    public String getInvestId() {
      return investId;
    }

    public void setInvestId(String investId) {
      this.investId = investId;
    }

    public int getUsableSa() {
      return usableSa;
    }

    public void setUsableSa(int usableSa) {
      this.usableSa = usableSa;
    }

    public String getFinTm() {
      return finTm;
    }

    public void setFinTm(String finTm) {
      this.finTm = finTm;
    }

    public String getCustId() {
      return custId;
    }

    public void setCustId(String custId) {
      this.custId = custId;
    }

    public String getBdId() {
      return bdId;
    }

    public void setBdId(String bdId) {
      this.bdId = bdId;
    }

    public String getAppTm() {
      return appTm;
    }

    public void setAppTm(String appTm) {
      this.appTm = appTm;
    }

    public String getTraFundNm() {
      return traFundNm;
    }

    public void setTraFundNm(String traFundNm) {
      this.traFundNm = traFundNm;
    }

    public String getTraAmt() {
      return traAmt;
    }

    public void setTraAmt(String traAmt) {
      this.traAmt = traAmt;
    }

    public String getProdId() {
      return prodId;
    }

    public void setProdId(String prodId) {
      this.prodId = prodId;
    }

    public String getFreezeYn() {
      return freezeYn;
    }

    public void setFreezeYn(String freezeYn) {
      this.freezeYn = freezeYn;
    }

    public String getTransYn() {
      return transYn;
    }

    public void setTransYn(String transYn) {
      this.transYn = transYn;
    }

    public String getPubBdCustId() {
      return pubBdCustId;
    }

    public void setPubBdCustId(String pubBdCustId) {
      this.pubBdCustId = pubBdCustId;
    }

    public String getPubBdLoginNm() {
      return pubBdLoginNm;
    }

    public void setPubBdLoginNm(String pubBdLoginNm) {
      this.pubBdLoginNm = pubBdLoginNm;
    }

    public String getPeerCustLoginNm() {
      return peerCustLoginNm;
    }

    public void setPeerCustLoginNm(String peerCustLoginNm) {
      this.peerCustLoginNm = peerCustLoginNm;
    }

    public String getCutLoginNm() {
      return cutLoginNm;
    }

    public void setCutLoginNm(String cutLoginNm) {
      this.cutLoginNm = cutLoginNm;
    }

    public String getSxtzsNo() {
      return sxtzsNo;
    }

    public void setSxtzsNo(String sxtzsNo) {
      this.sxtzsNo = sxtzsNo;
    }

    public String getTraOpTypNm() {
      return traOpTypNm;
    }

    public void setTraOpTypNm(String traOpTypNm) {
      this.traOpTypNm = traOpTypNm;
    }

    public int getPeerAmt() {
      return peerAmt;
    }

    public void setPeerAmt(int peerAmt) {
      this.peerAmt = peerAmt;
    }

    public String getRem() {
      return rem;
    }

    public void setRem(String rem) {
      this.rem = rem;
    }

    public String getTraOpTypCd() {
      return traOpTypCd;
    }

    public void setTraOpTypCd(String traOpTypCd) {
      this.traOpTypCd = traOpTypCd;
    }

    public String getTraFundCd() {
      return traFundCd;
    }

    public void setTraFundCd(String traFundCd) {
      this.traFundCd = traFundCd;
    }

    public String getPeerCustId() {
      return peerCustId;
    }

    public void setPeerCustId(String peerCustId) {
      this.peerCustId = peerCustId;
    }

    public String getTraCd() {
      return traCd;
    }

    public void setTraCd(String traCd) {
      this.traCd = traCd;
    }

    public String getSaChgDire() {
      return saChgDire;
    }

    public void setSaChgDire(String saChgDire) {
      this.saChgDire = saChgDire;
    }
  }
}
