package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/5/31
 */
public class RegularProjectHeader extends RegularBase {

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
}
