package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2015/10/15.
 */
public class ProjectInfoResult extends Meta {

    private ProjectMatch data;

    public ProjectMatch getData() {
        return data;
    }

    public void setData(ProjectMatch data) {
        this.data = data;
    }
}
