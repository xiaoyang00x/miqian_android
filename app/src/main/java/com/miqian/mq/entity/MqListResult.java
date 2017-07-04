package com.miqian.mq.entity;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 2017/6/27.
 * Email: gwzheng521@163.com
 * Description: 秒钱通用返回结果
 */

public class MqListResult<T> {

    private ArrayList<T> data;
    private String code;
    private String message;

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
