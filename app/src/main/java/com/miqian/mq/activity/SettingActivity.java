package com.miqian.mq.activity;

import android.content.Intent;
import android.view.View;

import com.miqian.mq.R;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Administrator on 2015/9/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {


    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

        View frame_setting_name = findViewById(R.id.frame_setting_name);
        View frame_setting_bindphone = findViewById(R.id.frame_setting_bindphone);
        View frame_setting_security = findViewById(R.id.frame_setting_security);
        View frame_setting_helpcenter = findViewById(R.id.frame_setting_helpcenter);

        View frame_setting_suggest = findViewById(R.id.frame_setting_suggest);
        View frame_setting_about = findViewById(R.id.frame_setting_about);
        View frame_setting_telephone = findViewById(R.id.frame_setting_telephone);


        frame_setting_name.setOnClickListener(this);
        frame_setting_bindphone.setOnClickListener(this);
        frame_setting_security.setOnClickListener(this);
        frame_setting_helpcenter.setOnClickListener(this);
        frame_setting_suggest.setOnClickListener(this);
        frame_setting_about.setOnClickListener(this);
        frame_setting_telephone.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
             mTitle.setTitleText("设置");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //姓名
            case R.id.frame_setting_name:
                break;
            //绑定手机
            case R.id.frame_setting_bindphone:
                break;
            //安全设置
            case R.id.frame_setting_security:
                startActivity(new Intent(mActivity,SecuritySettingActivity.class));
                break;
            //帮助中心
            case R.id.frame_setting_helpcenter:
                break;
            //意见反馈
            case R.id.frame_setting_suggest:
                break;
            //关于咪钱
            case R.id.frame_setting_about:
                break;
            //联系客服
            case R.id.frame_setting_telephone:
                break;
        }

    }
}
