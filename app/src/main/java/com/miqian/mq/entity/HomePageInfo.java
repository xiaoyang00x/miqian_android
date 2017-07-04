package com.miqian.mq.entity;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by sunyong on 9/14/15.
 */
public class HomePageInfo {
    public static final int MODULE_LOOP = 1;                    //轮播图
    public static final int MODULE_BULLETIN = 2;                //公告
    public static final int MODULE_OPERATION_DATA = 3;          //运营数据
    public static final int MODULE_HOT_RECOMMEND = 4;           //热门推荐
    public static final int MODULE_SELECTION = 5;               //精选项目
    public static final int MODULE_NEW_ACTIVITIES = 6;          //新手活动
    public static final int MODULE_NEWS = 7;                    //新闻动态
    public static final int MODULE_TEL = 8;                     //客服电话

    private int module;
    private String title;
    private ArrayList<HomeAdData> bsAdListData;                                 //轮播图
    private MessageInfo bsPushData;                                             //公告
    private ArrayList<HomeOperationData> operationDatas;                        //运营数据
    private ArrayList<HomeRecommendData> hotRecommendData;                      //热门推荐
    private ArrayList<ProductRegularBaseInfo> subjectInfoData;                    //精选项目
    private ArrayList<HomeNewActivitiesData> hotActivityData;                   //新手活动
    private ArrayList<HomeNewsData> hotNewsData;                                //新闻动态
    private String consumerHotLine;                                             //客服电话

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<HomeAdData> getBsAdListData() {
        return bsAdListData;
    }

    public void setBsAdListData(ArrayList<HomeAdData> bsAdListData) {
        this.bsAdListData = bsAdListData;
    }

    public MessageInfo getBsPushData() {
        return bsPushData;
    }

    public void setBsPushData(MessageInfo bsPushData) {
        this.bsPushData = bsPushData;
    }

    public ArrayList<HomeOperationData> getOperationDatas() {
        return operationDatas;
    }

    public void setOperationDatas(ArrayList<HomeOperationData> operationDatas) {
        this.operationDatas = operationDatas;
    }

    public ArrayList<HomeRecommendData> getHotRecommendData() {
        return hotRecommendData;
    }

    public void setHotRecommendData(ArrayList<HomeRecommendData> hotRecommendData) {
        this.hotRecommendData = hotRecommendData;
    }

    public ArrayList<ProductRegularBaseInfo> getSubjectInfoData() {
        return subjectInfoData;
    }

    public void setSubjectInfoData(ArrayList<ProductRegularBaseInfo> subjectInfoData) {
        this.subjectInfoData = subjectInfoData;
    }

    public ArrayList<HomeNewActivitiesData> getHotActivityData() {
        return hotActivityData;
    }

    public void setHotActivityData(ArrayList<HomeNewActivitiesData> hotActivityData) {
        this.hotActivityData = hotActivityData;
    }

    public ArrayList<HomeNewsData> getHotNewsData() {
        return hotNewsData;
    }

    public void setHotNewsData(ArrayList<HomeNewsData> hotNewsData) {
        this.hotNewsData = hotNewsData;
    }

    public String getConsumerHotLine() {
        return consumerHotLine;
    }

    public void setConsumerHotLine(String consumerHotLine) {
        this.consumerHotLine = consumerHotLine;
    }
}
