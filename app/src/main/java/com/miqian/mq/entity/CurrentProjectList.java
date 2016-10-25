package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/10/19
 */
public class CurrentProjectList {

    private ArrayList<CurrentProjectInfo> currentList;

    public ArrayList<CurrentProjectInfo> getCurrentList() {
        return currentList;
    }

    public void setCurrentList(ArrayList<CurrentProjectInfo> currentList) {
        this.currentList = currentList;
    }

}
