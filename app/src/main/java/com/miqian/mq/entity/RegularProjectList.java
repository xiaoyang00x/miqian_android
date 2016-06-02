package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目
 * @email: cswangduo@163.com
 * @date: 16/5/26
 */
public class RegularProjectList {

    private ArrayList<RegularProjectInfo> featureData;

    private ArrayList<RegularProjectData> regularData;

    public ArrayList<RegularProjectInfo> getFeatureData() {
        return featureData;
    }

    public void setFeatureData(ArrayList<RegularProjectInfo> featureData) {
        this.featureData = featureData;
    }

    public ArrayList<RegularProjectData> getRegularData() {
        return regularData;
    }

    public void setRegularData(ArrayList<RegularProjectData> regularData) {
        this.regularData = regularData;
    }

}
