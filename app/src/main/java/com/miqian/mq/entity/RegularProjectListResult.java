package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: 定期项目列表 返回结果
 * @email: cswangduo@163.com
 * @date: 16/5/26
 */
public class RegularProjectListResult extends Meta{

    private RegularProjectList data;

    public RegularProjectList getData() {
        return data;
    }

    public void setData(RegularProjectList data) {
        this.data = data;
    }

}
