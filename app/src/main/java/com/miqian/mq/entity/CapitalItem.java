package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/21.
 */
public class CapitalItem {

//    private String traDt;//操作日期
//    private String traTm;//操作时间
//    private String traOpNm;//操作类型
//    private String traAmt;//交易金额
//    private String peerCustLoginNm;//操作方
//    private String rem;//备注
//    private String saChgDire;//金额变动方向
//    private String traFundNm;//资金类型名称
//    private String traFundCd;//资金类型编码

      private Long traTime;//操作时间
      private String amt;//交易金额
      private String operateName;//资金操作名
      private String fromName;//
      private String remark;//备注

    public Long getTraTime() {
        return traTime;
    }

    public void setTraTime(Long traTime) {
        this.traTime = traTime;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}