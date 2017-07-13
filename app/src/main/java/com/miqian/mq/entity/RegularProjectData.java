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
    private ArrayList<ProductRegularBaseInfo> subjectDatas;

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

    public ArrayList<ProductRegularBaseInfo> getSubjectDatas() {
        return subjectDatas;
    }

    public void setSubjectDatas(ArrayList<ProductRegularBaseInfo> subjectDatas) {
        this.subjectDatas = subjectDatas;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
