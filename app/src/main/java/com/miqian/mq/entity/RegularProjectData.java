package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目 - 栏目名称 栏目标列表
 * @email: cswangduo@163.com
 * @date: 16/5/30
 */
public class RegularProjectData {

    private String title;
    private String jumpUrl;
    private String name;
    private String iconUrl;
    private ArrayList<ProductRegularBaseInfo> subjectData;

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

    public ArrayList<ProductRegularBaseInfo> getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(ArrayList<ProductRegularBaseInfo> subjectData) {
        this.subjectData = subjectData;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
