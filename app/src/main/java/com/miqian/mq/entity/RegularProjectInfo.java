package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: 定期项目信息
 * @email: cswangduo@163.com
 * @date: 16/5/27
 */
public class RegularProjectInfo {

    private int limit; // 期限

    private String subjectStatus;

    private String prodId; // 产品类型

    private int yearInterest; // 年化收益率

    private int personTime; // 认购人次

    private double fromInvestmentAmount; // 最低认购金额/起投金额

    private double continueInvestmentLimit; // 续投金额

    private double subjectMaxBuy; // 最大可以认购金额

    private String subjectId; // 标的ID

    private String subjectName; // 标的名称

    private String startTimestamp; // 开始时间戳

    private String endTimestamp; // 结束时间戳

    private double subjectTotalPrice; // 标的总额

    private double purchasePrice; // 已投总额

    private double residueAmt; // 剩余额度

}
