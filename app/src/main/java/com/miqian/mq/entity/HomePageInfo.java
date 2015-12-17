package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by sunyong on 9/14/15.
 */
public class HomePageInfo {

    private ArrayList<AdvertisementImg> adImgs;
    private ArrayList<SubjectCategoryData> subjectData;

    public ArrayList<AdvertisementImg> getAdImgs() {
        return adImgs;
    }

    public void setAdImgs(ArrayList<AdvertisementImg> adImgs) {
        this.adImgs = adImgs;
    }

    public ArrayList<SubjectCategoryData> getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(ArrayList<SubjectCategoryData> subjectData) {
        this.subjectData = subjectData;
    }
}
