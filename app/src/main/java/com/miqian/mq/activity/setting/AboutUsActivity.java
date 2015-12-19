package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.views.WFYTitle;


public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvVersion;

    @Override
    public void obtainData() {
        tvVersion.setText("v" + MobileOS.getAppVersionName(this));
    }

    @Override
    public void initView() {
        tvVersion = (TextView) findViewById(R.id.tvVersion);

        View frame_introduce = findViewById(R.id.frame_aboutus_introduce);
        View frame_team = findViewById(R.id.frame_aboutus_team);
        View frame_cooperation = findViewById(R.id.frame_aboutus_cooperation);

        frame_introduce.setOnClickListener(this);
        frame_team.setOnClickListener(this);
        frame_cooperation.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_aboutus;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("关于秒钱");

    }

    @Override
    protected String getPageName() {
        return "关于秒钱";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_aboutus_introduce://简介

                break;
            case R.id.frame_aboutus_team://管理团队

                break;
            case R.id.frame_aboutus_cooperation://合作伙伴

                break;
        }

    }
}
