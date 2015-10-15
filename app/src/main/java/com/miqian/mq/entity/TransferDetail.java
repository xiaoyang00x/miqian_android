package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2015/10/15.
 */
public class TransferDetail {

    private TransferInfo transAmt;
    private List<TranferDetailInfo> translist;

    public TransferInfo getTransAmt() {
        return transAmt;
    }

    public void setTransAmt(TransferInfo transAmt) {
        this.transAmt = transAmt;
    }

    public List<TranferDetailInfo> getTranslist() {
        return translist;
    }

    public void setTranslist(List<TranferDetailInfo> translist) {
        this.translist = translist;
    }

    public class TransferInfo {

        private String transed;//已转让金额
        private String cantransed;//可转让金额
        private String transing;//转让中金额

        public String getTransed() {
            return transed;
        }

        public void setTransed(String transed) {
            this.transed = transed;
        }

        public String getCantransed() {
            return cantransed;
        }

        public void setCantransed(String cantransed) {
            this.cantransed = cantransed;
        }

        public String getTransing() {
            return transing;
        }

        public void setTransing(String transing) {
            this.transing = transing;
        }
    }

    public class TranferDetailInfo {

        private String discountRt;//折让率 百分比
        private String discountAmt;//折让金额
        private String applDt;//申请日期
        private String prin;//转让本金
        private String buyAmt;//已转让金额
        private String discount;//折让状态 （平价，加价：+1%，降价：-1%）
        private String transState;//转让状态

        public String getDiscountRt() {
            return discountRt;
        }

        public void setDiscountRt(String discountRt) {
            this.discountRt = discountRt;
        }

        public String getDiscountAmt() {
            return discountAmt;
        }

        public void setDiscountAmt(String discountAmt) {
            this.discountAmt = discountAmt;
        }

        public String getApplDt() {
            return applDt;
        }

        public void setApplDt(String applDt) {
            this.applDt = applDt;
        }

        public String getPrin() {
            return prin;
        }

        public void setPrin(String prin) {
            this.prin = prin;
        }

        public String getBuyAmt() {
            return buyAmt;
        }

        public void setBuyAmt(String buyAmt) {
            this.buyAmt = buyAmt;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getTransState() {
            return transState;
        }

        public void setTransState(String transState) {
            this.transState = transState;
        }
    }
}
