package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 2017/7/4.
 * Email: gwzheng521@163.com
 * Description:
 */

public class ProductRegularBaseInfo extends ProductBaseInfo {

    public static final int ITEM_TYPE_CARD  = 0; // 卡片标
    public static final int ITEM_TYPE_TITLE = 1; // 栏目标题
    public static final int ITEM_TYPE_LIST  = 2; // 栏目下的标列表

    private String subscript;                                   //右上角角标
    protected int type; // 条目类型

    private String title;
    private String jumpUrl;
    private String name;
    private String iconUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSubscript() {
        return subscript;
    }

    public void setSubscript(String subscript) {
        this.subscript = subscript;
    }
}
