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
        private String endTm;//结束时间
        private String oldId;//原来的ID(当有做赠送时有值)（原来的编号）
        private String sta;//促销状态CS 初始状态JH 激活状态YW 用完状态GQ 过期状态ZS  赠送状态
        private String promNo;//促销序列号
        private String recCustId;//接收者的客户Id
        private String endDt;//结束日期
        private String prodId;//使用的产品ID 0 :充值产品,1:活期赚2:活期转让赚3:定期赚4:定期转让赚5: 定期计划6: 计划转让如:1,2 表示:活期赚, 活期转让赚
        private String prnUsePerc;//折让比例
        private String promAmt;//总额度
        private String id;//促销余额编号
        private String prodScope;//产品适用范围(默认0)-1 所有 0 未转让1 转让 (现已弃用)
        private String promTypCd;//余额类型SC 拾财券HB 红包 JF 积分LP 礼品卡TY 体验金
        private String startTm;//开始日期
        private String usedPromAmt;//已抵用额度
        private String startDt;//开始时间
        private String recLoginNm;//接收者的客户登录名
        private String giveCustId;//赠送者的客户Id
        private String giveLoginNm;//赠送者的客户登录名
        private String allowDonation;//是否允许赠送（默认Y） Y-允许，N-不允许
        private String prodPromId;//产品促销id


        public String getEndTm() {
            return endTm;
        }

        public void setEndTm(String endTm) {
            this.endTm = endTm;
        }

        public String getOldId() {
            return oldId;
        }

        public void setOldId(String oldId) {
            this.oldId = oldId;
        }

        public String getSta() {
            return sta;
        }

        public void setSta(String sta) {
            this.sta = sta;
        }

        public String getPromNo() {
            return promNo;
        }

        public void setPromNo(String promNo) {
            this.promNo = promNo;
        }

        public String getRecCustId() {
            return recCustId;
        }

        public void setRecCustId(String recCustId) {
            this.recCustId = recCustId;
        }

        public String getEndDt() {
            return endDt;
        }

        public void setEndDt(String endDt) {
            this.endDt = endDt;
        }

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProdScope() {
            return prodScope;
        }

        public void setProdScope(String prodScope) {
            this.prodScope = prodScope;
        }

        public String getPromTypCd() {
            return promTypCd;
        }

        public void setPromTypCd(String promTypCd) {
            this.promTypCd = promTypCd;
        }

        public String getStartTm() {
            return startTm;
        }

        public void setStartTm(String startTm) {
            this.startTm = startTm;
        }

        public String getUsedPromAmt() {
            return usedPromAmt;
        }

        public void setUsedPromAmt(String usedPromAmt) {
            this.usedPromAmt = usedPromAmt;
        }

        public String getStartDt() {
            return startDt;
        }

        public void setStartDt(String startDt) {
            this.startDt = startDt;
        }

        public String getRecLoginNm() {
            return recLoginNm;
        }

        public void setRecLoginNm(String recLoginNm) {
            this.recLoginNm = recLoginNm;
        }

        public String getGiveCustId() {
            return giveCustId;
        }

        public void setGiveCustId(String giveCustId) {
            this.giveCustId = giveCustId;
        }

        public String getGiveLoginNm() {
            return giveLoginNm;
        }

        public void setGiveLoginNm(String giveLoginNm) {
            this.giveLoginNm = giveLoginNm;
        }

        public String getAllowDonation() {
            return allowDonation;
        }

        public void setAllowDonation(String allowDonation) {
            this.allowDonation = allowDonation;
        }

        public String getProdPromId() {
            return prodPromId;
        }

        public void setProdPromId(String prodPromId) {
            this.prodPromId = prodPromId;
        }

    }

    public class Page {

        private String total;
        private String count;
        private String sortField;
        private String start;
        private String sortOrder;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getSortField() {
            return sortField;
        }

        public void setSortField(String sortField) {
            this.sortField = sortField;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }
    }
}