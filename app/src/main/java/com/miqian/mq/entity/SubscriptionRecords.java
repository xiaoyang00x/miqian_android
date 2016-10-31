package com.miqian.mq.entity;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by guolei_wang on 16/10/25.
 * 秒钱宝认购记录
 */
public class SubscriptionRecords {

    private ArrayList<Products> productList;

    public ArrayList<Products> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Products> productList) {
        this.productList = productList;
    }

    public class Products {
        private String subjectId;                   //产品编号
        private String subjectName;                 //产品名称
        private BigDecimal remainAmount;            //本金
        private BigDecimal remainInterest;          //待收收益
        private long tradeTime;                     //交易时间

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

        public BigDecimal getRemainAmount() {
            return remainAmount;
        }

        public void setRemainAmount(BigDecimal remainAmount) {
            this.remainAmount = remainAmount;
        }

        public BigDecimal getRemainInterest() {
            return remainInterest;
        }

        public void setRemainInterest(BigDecimal remainInterest) {
            this.remainInterest = remainInterest;
        }

        public long getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(long tradeTime) {
            this.tradeTime = tradeTime;
        }
    }
}
