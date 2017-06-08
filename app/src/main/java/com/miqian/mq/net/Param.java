package com.miqian.mq.net;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/4.
 */
public class Param implements Serializable {

    public Param(String key, String value) {
        this.key = key;
        this.value = value;
    }

    String key;
    String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
