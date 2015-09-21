package com.miqian.mq.test;

import java.io.Serializable;

/**
 * 活动实体类【单独类】
 */
public class ActivityEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    // 长宽比
    public static final String JK_LENGTH_AND_WIDTH = "lengthAndWidth";

    public String imgUrl;
    public String activityName;
    public String activityId;
    public String activityType;
    public String htmlUrl;
    public String startDate;
    public String endDate;

}
