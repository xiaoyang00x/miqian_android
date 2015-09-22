package com.miqian.mq.activity;

import com.miqian.mq.R;
import com.miqian.mq.entity.BankInfo;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;

/**
 * Created by Joy on 2015/9/10.
 */
public class IntoActivity extends BaseActivity {
    private ArrayList<BankInfo> bankList;

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("充值");

    }
}
