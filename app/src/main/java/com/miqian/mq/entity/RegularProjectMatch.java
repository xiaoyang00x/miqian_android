package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * @author wangduo
 * @description: 定期项目匹配项目
 * @email: cswangduo@163.com
 * @date: 16/6/4
 */
public class RegularProjectMatch {

    private String name; // 项目名称
    private BigDecimal occupyAmount; // 金额

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOccupyAmount() {
        return occupyAmount;
    }

    public void setOccupyAmount(BigDecimal occupyAmount) {
        this.occupyAmount = occupyAmount;
    }
}
