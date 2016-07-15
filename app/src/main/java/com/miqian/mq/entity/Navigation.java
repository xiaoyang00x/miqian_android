package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2016/7/14.
 */
public class Navigation {

    private String color;
    private String colorClick;
    private boolean navigationOnOff;
    private List<TabInfo> navigationList;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorClick() {
        return colorClick;
    }

    public void setColorClick(String colorClick) {
        this.colorClick = colorClick;
    }

    public boolean isNavigationOnOff() {
        return navigationOnOff;
    }

    public void setNavigationOnOff(boolean navigationOnOff) {
        this.navigationOnOff = navigationOnOff;
    }

    public List<TabInfo> getNavigationList() {
        return navigationList;
    }

    public void setNavigationList(List<TabInfo> navigationList) {
        this.navigationList = navigationList;
    }
}