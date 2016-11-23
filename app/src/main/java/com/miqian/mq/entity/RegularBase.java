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

    // 产品id
    public static final int REGULAR_PROJECT = 1; // 定期项目
    public static final int REGULAR_04 = 4; // 定期项目转让
    public static final int REGULAR_PLAN = 2; // 定期计划
    public static final int REGULAR_06 = 6; // 定期计划转让
    public static final int CURRENT_PROJECT = 3; // 秒钱宝

    // 标的状态
    public static final int STATE_1 = 1; // 创建
    public static final int STATE_2 = 2; // 待审核
    public static final int STATE_3 = 3; // 待上线
    public static final int STATE_4 = 4; // 待开标EGULAR_PROJECT
    public static final int STATE_5 = 5; // 开标
    public static final int STATE_6 = 6; // 满标
    public static final int STATE_7 = 7; // 下架

    protected int type; // 条目类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
