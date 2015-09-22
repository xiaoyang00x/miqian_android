package com.miqian.mq.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.miqian.mq.R;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.views.WFYTitle;


public class SecuritySettingActivity extends BaseActivity implements OnClickListener {

    @Override
    public void obtainData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {

        Intent intent = getIntent();

        findViewById(R.id.password_login).setOnClickListener(this);
        findViewById(R.id.password_transaction).setOnClickListener(this);

    }


    @Override
    public int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_security;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("安全设置");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_login:

                SendCaptchaActivity.enterActivity(mContext, TypeUtil.SENDCAPTCHA_FORGETPSW);

                break;
            case R.id.password_transaction:
              startActivity(new Intent(mActivity,TradePsCaptchaActivity.class));
                break;

            default:
                break;
        }

    }

}
