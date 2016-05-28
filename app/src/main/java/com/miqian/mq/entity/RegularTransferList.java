package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目转让列表
 * @email: cswangduo@163.com
 * @date: 16/5/26
 */
public class RegularTransferList {

    private Page page;

    private ArrayList<RegularTransferInfo> subjectData;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public ArrayList<RegularTransferInfo> getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(ArrayList<RegularTransferInfo> subjectData) {
        this.subjectData = subjectData;
    }

}
