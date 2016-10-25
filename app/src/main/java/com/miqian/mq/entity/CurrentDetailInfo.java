package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/10/23
 */

public class CurrentDetailInfo {

    private CurrentProjectInfo currentInfo; // 标的信息

    private FestivalInfo festival; // 活动信息

    private CurrentDetailMoreInfo subjectBar; // 其它信息

    public CurrentProjectInfo getCurrentInfo() {
        return currentInfo;
    }

    public void setCurrentInfo(CurrentProjectInfo currentInfo) {
        this.currentInfo = currentInfo;
    }

    public FestivalInfo getFestival() {
        return festival;
    }

    public void setFestival(FestivalInfo festival) {
        this.festival = festival;
    }

    public CurrentDetailMoreInfo getSubjectBar() {
        return subjectBar;
    }

    public void setSubjectBar(CurrentDetailMoreInfo subjectBar) {
        this.subjectBar = subjectBar;
    }

}
