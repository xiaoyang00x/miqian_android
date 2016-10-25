package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: 活期首页
 * @email: cswangduo@163.com
 * @date: 16/10/19
 */
public class CurrentProjectResult extends Meta {

    private CurrentProjectList data;

    public CurrentProjectList getData() {
        return data;
    }

    public void setData(CurrentProjectList data) {
        this.data = data;
    }

}
