package com.miqian.mq.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.miqian.mq.R;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2017/6/15.
 * 充值方式选择页面
 */

public class IntoModeAcitvity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout frameOnline;
    private RelativeLayout frameOffline;
    private RelativeLayout frameCheck;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_online:
                startActivity(new Intent(this, IntoActivity.class));
                break;
            case R.id.frame_offline:
                break;
            case R.id.frame_check:
                break;
        }
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        frameOnline = (RelativeLayout) findViewById(R.id.frame_online);
        frameOnline.setOnClickListener(this);
        frameOffline = (RelativeLayout) findViewById(R.id.frame_offline);
        frameOffline.setOnClickListener(this);
        frameCheck = (RelativeLayout) findViewById(R.id.frame_check);
        frameCheck.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into_mode;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("充值");
    }

    @Override
    protected String getPageName() {
        return "充值";
    }
}
