package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 2017/7/12.
 * Email: gwzheng521@163.com
 * Description: 我的秒钱宝交易记录
 * https://tower.im/projects/4972a2ee14f74845af2164ca92b7698b/docs/9d59d7821cd9480b92f2e33458a795f2/
 */

public class NewCurrentFundFlow {

    private Page pageInfo;
    private ArrayList<NewCurrentRecords> list;

    public Page getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Page pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ArrayList<NewCurrentRecords> getList() {
        return list;
    }

    public void setList(ArrayList<NewCurrentRecords> list) {
        this.list = list;
    }

    /**
     * 交易记录
     *
         "traTime": 2313132131,
         "amt": "1000",
         "interest": "6.6",
         "productName":"秒钱宝一期",
         "operateName":"认购",
         "remark":"投标资金"
         "status":"01"
     */
    public class NewCurrentRecords {

        /**
         * 状态 00: 处理中 01: 成功 02: 失败
         */
        public static final String STATUS_00 = "00";
        public static final String STATUS_01 = "01";
        public static final String STATUS_02 = "02";

        /**
         * 操作时间
         */
        private long traTime;

        /**
         * 交易金额
         */
        private String amt;

        /**
         * 利息
         */
        private String interest;

        /**
         * 产品名
         */
        private String productName;

        /**
         * 操作名
         */
        private String operateName;

        /**
         * 备注
         */
        private String remark;

        /**
         * 状态 00: 处理中 01: 成功 02: 失败
         */
        private String status;

        public long getTraTime() {
            return traTime;
        }

        public void setTraTime(long traTime) {
            this.traTime = traTime;
        }

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getOperateName() {
            return operateName;
        }

        public void setOperateName(String operateName) {
            this.operateName = operateName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
