package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2015/10/21.
 */
public class ProjectMatch {

    private List<ProjectInfo> matchsubList;
    private Page page;

    public List<ProjectInfo> getMatchsubList() {
        return matchsubList;
    }

    public void setMatchsubList(List<ProjectInfo> matchsubList) {
        this.matchsubList = matchsubList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}