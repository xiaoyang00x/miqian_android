package com.miqian.mq.test;

import java.io.Serializable;

/**
 * 活动实体类【单独类】
 */
public class ActivityEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    // 长宽比
    public static final String JK_LENGTH_AND_WIDTH = "lengthAndWidth";

    public String activityImgUrl;
    public String activityName;
    public String activityId;
    public String activityType;
    public String activityTag;
    public String discountRate;
    public String activityHtmlUrl;
    public String startDate;
    public String endDate;
    public String activityRule;
    public String relatedID;
    public String longLabel;
    public String additionalName;//首页改版二期楼层用到（活动说明）
    public String shareDesc;
    public int dataType;//新版首页品类区用到
    public String activityTitle;//首页展示用
    public long delayEndTime;
    public boolean showDay;
    public boolean showTime;
    // 长宽比例
    public float scaleWidth;
    public float scaleHeight;
    public boolean isLoadImage;

    @Override
    public String toString() {
        return "ActivityEntity [activityImgUrl=" + activityImgUrl + ", activityName=" + activityName + ", activityId="
                + activityId + ", activityType=" + activityType + ", activityHtmlUrl=" + activityHtmlUrl
                + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
    
}
