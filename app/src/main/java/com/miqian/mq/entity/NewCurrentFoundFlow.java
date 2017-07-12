package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 2017/7/6.
 * Email: gwzheng521@163.com
 * Description: 我的秒钱宝
 *
 * 服务器返回值
     {
     "code":"000000",
     "message":"成功",
     "data":{
     "userRedeem":{
     "enable":true,
     "dayMaxCount":3,
     "dayRemainCount":2,
     "monthMaxCount":10,
     "monthRemainCount":9
     },
     "newCurrent":{
     "prnAmt":"1000",
     "interest":"0.05",
     "yearRate":"5%",
     "lastdayProfit":"200.00"
     },
     "investList":[
     {
     "purchaseSeqno":"1212",
     "productCode":"34",
     "productName":"秒钱宝1期",
     "purchaseAmount":"20000.00",
     "startTime":1444357763445,
     "yearRate":"5%",
     "productStatus":"1"
     }
     ],
     "pageInfo":{
     "totalRecord":0,
     "curPageNo":"1",
     "totalPage":"12",
     "pageSize":20
     }
     }
     }
 */

public class NewCurrentFoundFlow {

    /**
     * 产品状态
     01：认购处理中
     02：赎回处理中
     03：持有中
     */
    public static final String STATUS_01 = "01";
    public static final String STATUS_02 = "02";
    public static final String STATUS_03 = "03";

    private UserRedeem userRedeem;
    private NewCurrent newCurrent;
    private ArrayList<InvestInfo> investList;
    private Page pageInfo;

    public UserRedeem getUserRedeem() {
        return userRedeem;
    }

    public void setUserRedeem(UserRedeem userRedeem) {
        this.userRedeem = userRedeem;
    }

    public NewCurrent getNewCurrent() {
        return newCurrent;
    }

    public void setNewCurrent(NewCurrent newCurrent) {
        this.newCurrent = newCurrent;
    }

    public ArrayList<InvestInfo> getInvestList() {
        return investList;
    }

    public void setInvestList(ArrayList<InvestInfo> investList) {
        this.investList = investList;
    }

    public Page getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Page pageInfo) {
        this.pageInfo = pageInfo;
    }

    public static class InvestInfo {

        /**
         * 秒钱宝在投本金
         */
        private String purchaseSeqno;
        /**
         * 秒钱宝在投本金
         */
        private String productCode;
        /**
         * 秒钱宝在投本金
         */
        private String productName;
        /**
         * 秒钱宝在投本金
         */
        private String purchaseAmount;
        /**
         * 秒钱宝在投本金
         */
        private long startTime;

        /**
         * 秒钱宝在投本金
         */
        private String productRate;

        /**
         * 秒钱宝在投本金
         */
        private String productStatus;

        public String getPurchaseSeqno() {
            return purchaseSeqno;
        }

        public void setPurchaseSeqno(String purchaseSeqno) {
            this.purchaseSeqno = purchaseSeqno;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPurchaseAmount() {
            return purchaseAmount;
        }

        public void setPurchaseAmount(String purchaseAmount) {
            this.purchaseAmount = purchaseAmount;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getProductRate() {
            return productRate;
        }

        public void setProductRate(String productRate) {
            this.productRate = productRate;
        }

        public String getProductStatus() {
            return productStatus;
        }

        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }
    }

    public class NewCurrent {

        /**
         * 秒钱宝在投本金
         */
        private String prnAmt;

        /**
         * 秒钱宝在投收益
         */
        private String interest;

        /**
         * 我的秒钱宝年化收益范围
         */
        private String yearRate;

        /**
         * 秒钱宝昨日收益
         */
        private String lastdayProfit;

        public String getPrnAmt() {
            return prnAmt;
        }

        public void setPrnAmt(String prnAmt) {
            this.prnAmt = prnAmt;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getYearRate() {
            return yearRate;
        }

        public void setYearRate(String yearRate) {
            this.yearRate = yearRate;
        }

        public String getLastdayProfit() {
            return lastdayProfit;
        }

        public void setLastdayProfit(String lastdayProfit) {
            this.lastdayProfit = lastdayProfit;
        }
    }

    public class UserRedeem {

        /**
         * 是否可赎回
         */
        private boolean enable;

        /**
         * 每日可赎回总次数
         */
        private int dayMaxCount;

        /**
         * 用户今日剩余可赎回次数
         */
        private int dayRemainCount;

        /**
         * 每月可赎回总次数
         */
        private int monthMaxCount;

        /**
         * 用户本月剩余可赎回次数
         */
        private int monthRemainCount;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getDayMaxCount() {
            return dayMaxCount;
        }

        public void setDayMaxCount(int dayMaxCount) {
            this.dayMaxCount = dayMaxCount;
        }

        public int getDayRemainCount() {
            return dayRemainCount;
        }

        public void setDayRemainCount(int dayRemainCount) {
            this.dayRemainCount = dayRemainCount;
        }

        public int getMonthMaxCount() {
            return monthMaxCount;
        }

        public void setMonthMaxCount(int monthMaxCount) {
            this.monthMaxCount = monthMaxCount;
        }

        public int getMonthRemainCount() {
            return monthRemainCount;
        }

        public void setMonthRemainCount(int monthRemainCount) {
            this.monthRemainCount = monthRemainCount;
        }
    }
}
