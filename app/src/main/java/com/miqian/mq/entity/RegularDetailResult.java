package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: 定期项目详情
 * @email: cswangduo@163.com
 * @date: 16/6/2
 */
public class RegularDetailResult extends Meta {

    private RegularDetail data;

    public RegularDetail getData() {
        return data;
    }

    public void setData(RegularDetail data) {
        this.data = data;
    }

}