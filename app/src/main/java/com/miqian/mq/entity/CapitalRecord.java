package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/13.
 */
public class CapitalRecord {

    private Page page;
    private List<CapitalItem> assetRecord;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<CapitalItem> getAssetRecord() {
        return assetRecord;
    }

    public void setAssetRecord(List<CapitalItem> assetRecord) {
        this.assetRecord = assetRecord;
    }

    public class CapitalItem{

        private String traDt;//操作日期
        private String traTm;//操作时间
        private String traOpNm;//操作类型
        private String traAmt;//交易金额
        private String peerCustLoginNm;//操作方
        private String rem;//备注
        private String saChgDire;//金额变动方向
        private String traFundNm;//资金类型名称
        private String traFundCd;//资金类型编码

        public String getTraDt() {
            return traDt;
        }

        public void setTraDt(String traDt) {
            this.traDt = traDt;
        }

        public String getTraTm() {
            return traTm;
        }

        public void setTraTm(String traTm) {
            this.traTm = traTm;
        }

        public String getTraOpNm() {
            return traOpNm;
        }

        public void setTraOpNm(String traOpNm) {
            this.traOpNm = traOpNm;
        }

        public String getTraAmt() {
            return traAmt;
        }

        public void setTraAmt(String traAmt) {
            this.traAmt = traAmt;
        }

        public String getPeerCustLoginNm() {
            return peerCustLoginNm;
        }

        public void setPeerCustLoginNm(String peerCustLoginNm) {
            this.peerCustLoginNm = peerCustLoginNm;
        }

        public String getRem() {
            return rem;
        }

        public void setRem(String rem) {
            this.rem = rem;
        }

        public String getSaChgDire() {
            return saChgDire;
        }

        public void setSaChgDire(String saChgDire) {
            this.saChgDire = saChgDire;
        }

        public String getTraFundNm() {
            return traFundNm;
        }

        public void setTraFundNm(String traFundNm) {
            this.traFundNm = traFundNm;
        }

        public String getTraFundCd() {
            return traFundCd;
        }

        public void setTraFundCd(String traFundCd) {
            this.traFundCd = traFundCd;
        }
    }
}
