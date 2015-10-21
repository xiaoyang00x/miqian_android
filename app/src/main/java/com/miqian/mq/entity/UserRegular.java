package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by Jackie on 2015/10/12.
 */
public class UserRegular {

    private Page page;
    private List<RegInvest> regInvest;
    private Reg reg;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
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
