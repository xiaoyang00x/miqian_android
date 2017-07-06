package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2015/10/12.
 */
public class UserRegular {

    private Page pageInfo;
    private List<RegInvest> regInvest;
    private Reg reg;

    public Page getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Page pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<RegInvest> getRegInvest() {
        return regInvest;
    }

    public void setRegInvest(List<RegInvest> regInvest) {
        this.regInvest = regInvest;
    }

    public Reg getReg() {
        return reg;
    }

    public void setReg(Reg reg) {
        this.reg = reg;
    }
}
