package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by sunyong on 9/14/15.
 */
public class HomePageInfo {

    private ArrayList<AdvertisementImg> adImgs;
    private ArrayList<RegularEarn> subjectInfo;
    private ArrayList<RegularEarn> newCustomer;

    public ArrayList<RegularEarn> getSubjectInfo() {
        return subjectInfo;
    }

    public void setSubjectInfo(ArrayList<RegularEarn> subjectInfo) {
        this.subjectInfo = subjectInfo;
    }

    public ArrayList<AdvertisementImg> getAdImgs() {
        return adImgs;
    }

    public void setAdImgs(ArrayList<AdvertisementImg> adImgs) {
        this.adImgs = adImgs;
    }

    public ArrayList<RegularEarn> getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(ArrayList<RegularEarn> newCustomer) {
        this.newCustomer = newCustomer;
    }
}
