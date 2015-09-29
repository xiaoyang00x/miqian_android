package com.miqian.mq.activity.user;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by TuLiangTan on 2015/9/28.
 */

public class RollOutResultActivity extends BaseActivity implements View.OnClickListener {
    private Button btBack;
    private TextView tvMoney,tvOrderNum,tvCardNum;

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvOrderNum = (TextView) findViewById(R.id.tv_ordernum);
        tvCardNum = (TextView) findViewById(R.id.tv_cardnum);

        btBack = (Button) findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

    }

    @Override
    public void onClick(View v) {


    }
}
