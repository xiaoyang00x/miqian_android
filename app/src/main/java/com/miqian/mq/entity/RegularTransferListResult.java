package com.miqian.mq.entity;

/**
 * @author wangduo
 * @description: 定期项目转让 返回结果
 * @email: cswangduo@163.com
 * @date: 16/5/26
 */
public class RegularTransferListResult extends Meta{

    private RegularTransferList data;

    public RegularTransferList getData() {
        return data;
    }

    public void setData(RegularTransferList data) {
        this.data = data;
    }

}
