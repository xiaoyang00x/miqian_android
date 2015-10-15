package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/10/15.
 */
public class ProjectInfo {

    private int serNo;//流水号
    private String bdId;
    private String bdNm;//名称
    private String buyAmt;//匹配金额

    public int getSerNo() {
        return serNo;
    }

    public void setSerNo(int serNo) {
        this.serNo = serNo;
    }

    public String getBdId() {
        return bdId;
    }

    public void setBdId(String bdId) {
        this.bdId = bdId;
    }

    public String getBdNm() {
        return bdNm;
    }

    public void setBdNm(String bdNm) {
        this.bdNm = bdNm;
    }

    public String getBuyAmt() {
        return buyAmt;
    }

    public void setBuyAmt(String buyAmt) {
        this.buyAmt = buyAmt;
    }
}
