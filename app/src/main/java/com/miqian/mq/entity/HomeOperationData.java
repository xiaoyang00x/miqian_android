package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 16/5/20.
 * 首页运营数据
 */
public class HomeOperationData {

    public static final int TYPE_REGISTER = 1;              //累计注册人数
    public static final int TYPE_VOLUME = 2;                //成交额
    public static final int TYPE_EARNINGS = 3;              //赚取收益
    public static final int TYPE_OPERATION_DAYS = 4;        //安全运营天数

    private int type;           //类型
    private String value;       //运营数据

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
