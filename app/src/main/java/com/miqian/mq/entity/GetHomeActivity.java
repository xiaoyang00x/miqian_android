package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 15/12/17.
 * 首页运营活动返回数据
 */
public class GetHomeActivity {

    public static final String FLAG_UNSHOW = "0";   //常量 不弹窗
    public static final String FLAG_SHOW = "1";    //常量 弹窗
    private String activityId = "";                //活动 ID
    private String activityPlanId = "";            //活动计划 ID
    private String backgroundCase;            //文本描述
    private String jumpUrl;                   //活动跳转链接
    private String showFlag;                     //是否弹窗标示0:不弹窗 1：弹窗
    private String enterCase;                  //参加活动按钮文本
    private String titleCase;                  //对话框标题
    private long sysDate;                      //系统当前时间
    private long beginTime;                      //活动开始时间
    private long endTime;                      //活动结束时间

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityPlanId() {
        return activityPlanId;
    }

    public void setActivityPlanId(String activityPlanId) {
        this.activityPlanId = activityPlanId;
    }

    public String getBackgroundCase() {
        return backgroundCase;
    }

    public void setBackgroundCase(String backgroundCase) {
        this.backgroundCase = backgroundCase;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public String getEnterCase() {
        return enterCase;
    }

    public void setEnterCase(String enterCase) {
        this.enterCase = enterCase;
    }

    public String getTitleCase() {
        return titleCase;
    }

    public void setTitleCase(String titleCase) {
        this.titleCase = titleCase;
    }

    public long getSysDate() {
        return sysDate;
    }

    public void setSysDate(long sysDate) {
        this.sysDate = sysDate;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
