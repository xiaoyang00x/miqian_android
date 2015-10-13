package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class  Redpaper{

    private Page page;
    private List<CustPromotion> custPromotion;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<CustPromotion> getCustPromotion() {
        return custPromotion;
    }

    public void setCustPromotion(List<CustPromotion> custPromotion) {
        this.custPromotion = custPromotion;
    }

    public class CustPromotion {

        private String sta;//促销状态CS 初始状态JH 激活状态YW 用完状态GQ 过期状态ZS  赠送状态
        private String prnUsePerc;//折让比例
        private String promAmt;//总额度
        private String promTypCd;//余额类型SC 拾财券HB 红包 JF 积分LP 礼品卡TY 体验金
        private String startTimestamp;//开始日期
        private String usedPromAmt;//已抵用额度
        private String endTimestamp;//结束时间
        private String canUseAmt;//可用额度
        private String prodPromId;//id
        private String limitMsg;//使用范围

        public String getSta() {
            return sta;
        }

        public void setSta(String sta) {
            this.sta = sta;
        }

        public String getPrnUsePerc() {
            return prnUsePerc;
        }

        public void setPrnUsePerc(String prnUsePerc) {
            this.prnUsePerc = prnUsePerc;
        }

        public String getPromAmt() {
            return promAmt;
        }

        public void setPromAmt(String promAmt) {
            this.promAmt = promAmt;
        }

        public String getPromTypCd() {
            return promTypCd;
        }

        public void setPromTypCd(String promTypCd) {
            this.promTypCd = promTypCd;
        }

        public String getStartTimestamp() {
            return startTimestamp;
        }

        public void setStartTimestamp(String startTimestamp) {
            this.startTimestamp = startTimestamp;
        }

        public String getUsedPromAmt() {
            return usedPromAmt;
        }

        public void setUsedPromAmt(String usedPromAmt) {
            this.usedPromAmt = usedPromAmt;
        }

        public String getEndTimestamp() {
            return endTimestamp;
        }

        public void setEndTimestamp(String endTimestamp) {
            this.endTimestamp = endTimestamp;
        }

        public String getCanUseAmt() {
            return canUseAmt;
        }

        public void setCanUseAmt(String canUseAmt) {
            this.canUseAmt = canUseAmt;
        }

        public String getProdPromId() {
            return prodPromId;
        }

        public void setProdPromId(String prodPromId) {
            this.prodPromId = prodPromId;
        }

        public String getLimitMsg() {
            return limitMsg;
        }

        public void setLimitMsg(String limitMsg) {
            this.limitMsg = limitMsg;
        }
    }

}