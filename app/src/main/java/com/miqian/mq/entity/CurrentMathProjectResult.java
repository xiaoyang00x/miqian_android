package com.miqian.mq.entity;

/**
 * Created by wgl on 2016/11/16.
 * 我的秒钱宝匹配项目  服务器返回结果
 */
public class CurrentMathProjectResult extends Meta {

    private CurrentMathProjectData data;

    public CurrentMathProjectData getData() {
        return data;
    }

    public void setData(CurrentMathProjectData data) {
        this.data = data;
    }
}
