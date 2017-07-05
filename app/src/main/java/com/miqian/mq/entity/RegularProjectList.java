package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目
 * @email: cswangduo@163.com
 * @date: 16/5/26
 */
public class RegularProjectList {

    private ProductRegularBaseInfo recommendProductDto;

    private ArrayList<RegularProjectData> regularDatas;

    public ProductRegularBaseInfo getRecommendProductDto() {
        return recommendProductDto;
    }

    public void setRecommendProductDto(ProductRegularBaseInfo recommendProductDto) {
        this.recommendProductDto = recommendProductDto;
    }

    public ArrayList<RegularProjectData> getRegularDatas() {
        return regularDatas;
    }

    public void setRegularDatas(ArrayList<RegularProjectData> regularDatas) {
        this.regularDatas = regularDatas;
    }
}
