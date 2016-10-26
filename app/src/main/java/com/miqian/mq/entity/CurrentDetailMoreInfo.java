package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/10/23
 */
public class CurrentDetailMoreInfo {

    private ArrayList<CurrentDetailMoreInfoItem> more;

    private ArrayList<CurrentDetailMoreInfoItem> list;

    public ArrayList<CurrentDetailMoreInfoItem> getMore() {
        return more;
    }

    public void setMore(ArrayList<CurrentDetailMoreInfoItem> more) {
        this.more = more;
    }

    public ArrayList<CurrentDetailMoreInfoItem> getList() {
        return list;
    }

    public void setList(ArrayList<CurrentDetailMoreInfoItem> list) {
        this.list = list;
    }

}
