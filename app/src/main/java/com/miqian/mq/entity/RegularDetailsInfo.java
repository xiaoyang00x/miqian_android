package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 2017/7/5.
 * Email: gwzheng521@163.com
 * Description: 定期详情
 */

public class RegularDetailsInfo extends ProductBaseInfo {

    private ArrayList<RegularProjectFeature> regularFeatures;

    public ArrayList<RegularProjectFeature> getRegularFeatures() {
        return regularFeatures;
    }

    public void setRegularFeatures(ArrayList<RegularProjectFeature> regularFeatures) {
        this.regularFeatures = regularFeatures;
    }
}
