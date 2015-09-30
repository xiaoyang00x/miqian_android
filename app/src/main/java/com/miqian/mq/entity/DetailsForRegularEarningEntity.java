package com.miqian.mq.entity;

/**
 * Created by sunyong on 9/24/15.
 */
public class DetailsForRegularEarningEntity {
  String code;
  String message;
  Item data;

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

  public Item getData() {
    return data;
  }

  public void setData(Item data) {
    this.data = data;
  }

  public static class Item {
    //待收本金，年化收益率，项目期限，项目认购日期，项目结束日期。待收收益，还款方式，所属活动（88节）
    String unreceived_capital;//待收本金
    String annualized;//年化收益率
    String limit;//项目期限
    String start_time;//项目认购日期
    String end_time;//项目结束日期
    String unreceiverd_earning;//待收收益
    String repayment;//还款方式
    String promotion;//所属活动（88节）

    String type;//定期赚   定期计划

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getUnreceived_capital() {
      return unreceived_capital;
    }

    public void setUnreceived_capital(String unreceived_capital) {
      this.unreceived_capital = unreceived_capital;
    }

    public String getAnnualized() {
      return annualized;
    }

    public void setAnnualized(String annualized) {
      this.annualized = annualized;
    }

    public String getLimit() {
      return limit;
    }

    public void setLimit(String limit) {
      this.limit = limit;
    }

    public String getStart_time() {
      return start_time;
    }

    public void setStart_time(String start_time) {
      this.start_time = start_time;
    }

    public String getEnd_time() {
      return end_time;
    }

    public void setEnd_time(String end_time) {
      this.end_time = end_time;
    }

    public String getUnreceiverd_earning() {
      return unreceiverd_earning;
    }

    public void setUnreceiverd_earning(String unreceiverd_earning) {
      this.unreceiverd_earning = unreceiverd_earning;
    }

    public String getRepayment() {
      return repayment;
    }

    public void setRepayment(String repayment) {
      this.repayment = repayment;
    }

    public String getPromotion() {
      return promotion;
    }

    public void setPromotion(String promotion) {
      this.promotion = promotion;
    }
  }
}
