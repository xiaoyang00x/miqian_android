package com.miqian.mq.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by guolei_wang on 15/12/15.
 * 首页返回的定期数据
 */
public class SubjectCategoryData implements Serializable {

    private String subjectCategoryName;
    private String subjectCategoryIconUrl;
    private String subjectCategoryDesc;
    private String subjectCategoryDescUrl;
    private ArrayList<RegularBaseData> subjectInfo;

    public String getSubjectCategoryName() {
        return subjectCategoryName;
    }

    public void setSubjectCategoryName(String subjectCategoryName) {
        this.subjectCategoryName = subjectCategoryName;
    }

    public String getSubjectCategoryDesc() {
        return subjectCategoryDesc;
    }

    public void setSubjectCategoryDesc(String subjectCategoryDesc) {
        this.subjectCategoryDesc = subjectCategoryDesc;
    }

    public String getSubjectCategoryDescUrl() {
        return subjectCategoryDescUrl;
    }

    public void setSubjectCategoryDescUrl(String subjectCategoryDescUrl) {
        this.subjectCategoryDescUrl = subjectCategoryDescUrl;
    }

    public ArrayList<RegularBaseData> getSubjectInfo() {
        if(subjectInfo == null) {
            subjectInfo = new ArrayList<>();
        }
        return subjectInfo;
    }

    public void setSubjectInfo(ArrayList<RegularBaseData> subjectInfo) {
        this.subjectInfo = subjectInfo;
    }

    public String getSubjectCategoryIconUrl() {
        return subjectCategoryIconUrl;
    }

    public void setSubjectCategoryIconUrl(String subjectCategoryIconUrl) {
        this.subjectCategoryIconUrl = subjectCategoryIconUrl;
    }
}
