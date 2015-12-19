package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 15/12/17.
 * 首页运营活动返回数据
 */
public class GetHomeActivity {
    private String backgroundUrl;             //背景图
    private String activityId;                //活动 ID
    private String jumpUrl;                   //活动跳转链接
    private int showFlag;                     //
    private String enterUrl;                  //参加活动按钮图片
    private String closeUrl;                  //关闭按钮

    private ArrayList<HomeActivityTime> activityTime;

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public int getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(int showFlag) {
        this.showFlag = showFlag;
    }

    public String getEnterUrl() {
        return enterUrl;
    }

    public void setEnterUrl(String enterUrl) {
        this.enterUrl = enterUrl;
    }

    public String getCloseUrl() {
        return closeUrl;
    }

    public void setCloseUrl(String closeUrl) {
        this.closeUrl = closeUrl;
    }

    public ArrayList<HomeActivityTime> getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(ArrayList<HomeActivityTime> activityTime) {
        this.activityTime = activityTime;
    }
}
