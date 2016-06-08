package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/5/31
 */
public class RegularBase {

    public static final int ITEM_TYPE_CARD  = 0; // 卡片标
    public static final int ITEM_TYPE_TITLE = 1; // 栏目标题
    public static final int ITEM_TYPE_LIST  = 2; // 栏目下的标列表

    public static final int REGULAR_03 = 3; // 定期项目
    public static final int REGULAR_04 = 4; // 定期项目转让
    public static final int REGULAR_05 = 5; // 定期计划
    public static final int REGULAR_06 = 6; // 定期计划转让

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

    protected int type; // 条目类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
