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

    protected int type; // 条目类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
