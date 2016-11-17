package com.miqian.mq.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by wgl on 2016/11/16.
 * 我的秒钱宝匹配项目
 */
public class CurrentMathProjectData implements Serializable{

    private ArrayList<MatchProject> projectList;


    public ArrayList<MatchProject> getProjectList() {
        return projectList;
    }

    public void setProjectList(ArrayList<MatchProject> projectList) {
        this.projectList = projectList;
    }


    /**
     * 匹配项目
     */
    public class MatchProject implements Serializable{
        private long nextRepayDate;                          //下次还款时间
        private String projectCode;                            //项目编号
        private String projectName;                          //项目名
        private BigDecimal remainAmount;                     //本金
        private BigDecimal remainInterest;                   //待收收益

        public long getNextRepayDate() {
            return nextRepayDate;
        }

        public void setNextRepayDate(long nextRepayDate) {
            this.nextRepayDate = nextRepayDate;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
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
    }
}
