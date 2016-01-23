package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by Jackie on 2015/9/15.
 */
public class GetRegularInfo {

    private ArrayList<SubjectCategoryData> subjectData;

    private String fitSubjectDesc;  //红包规则描述

    public String getFitSubjectDesc() {
        return fitSubjectDesc;
    }

    public void setFitSubjectDesc(String fitSubjectDesc) {
        this.fitSubjectDesc = fitSubjectDesc;
    }

    public ArrayList<SubjectCategoryData> getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(ArrayList<SubjectCategoryData> subjectData) {
        this.subjectData = subjectData;
    }

}
