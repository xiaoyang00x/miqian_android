package com.miqian.mq.activity.save;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.rollin.IntoActivity;
import com.miqian.mq.activity.rollin.IntoCheckAcitvity;
import com.miqian.mq.net.Urls;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2017/6/15.
 * 江西银行存管
 * 签署协议
 */

public class SaveAutoAcitvity extends BaseActivity implements View.OnClickListener {

    private Button btSubmit;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                startActivity(new Intent(this, IntoActivity.class));
                break;
        }
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        btSubmit = (Button) findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_jx_auto;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("开通存管账户");
    }

    @Override
    protected String getPageName() {
        return "开通存管账户";
    }
}
