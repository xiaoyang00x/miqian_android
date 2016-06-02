package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: 标的信息(定期 定期转让公共信息)
 * @email: cswangduo@163.com
 * @date: 16/5/30
 */
public class ProjectInfo {

    // 标的状态
    public static final String STATE_00 = "00"; // 待开标
    public static final String STATE_01 = "01"; // 已开标
    public static final String STATE_02 = "02"; // 已满标（已售罄）
    public static final String STATE_03 = "03"; // 已到期
    public static final String STATE_04 = "04"; // 已撤销
    public static final String STATE_05 = "05"; // 已流标
    public static final String STATE_06 = "06"; // 审批中
    public static final String STATE_07 = "07"; // 还款中
    public static final String STATE_08 = "08"; // 正常还完


}
