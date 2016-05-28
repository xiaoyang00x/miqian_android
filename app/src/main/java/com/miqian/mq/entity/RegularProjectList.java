package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目
 * @email: cswangduo@163.com
 * @date: 16/5/26
 */
public class RegularProjectList {

    private Page page;

    private ArrayList<RegularProjectInfo> subjectData;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public ArrayList<RegularProjectInfo> getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(ArrayList<RegularProjectInfo> subjectData) {
        this.subjectData = subjectData;
    }
}
