package com.miqian.mq.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.miqian.mq.R;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.views.WFYTitle;


public class SecuritySettingActivity extends BaseActivity implements OnClickListener {
    private int paypwd_status;

    @Override
    public void obtainData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        paypwd_status = intent.getIntExtra("paypwd_status", 0);

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

                Intent intent_change = new Intent(this, ChangePassWordActivity.class);
                intent_change.putExtra("style", TypeUtil.PASSWORD_LOGIN);
                startActivityForResult(intent_change, 0);

                break;
            case R.id.password_transaction:

                Intent intent_change2 = new Intent(this, ChangePassWordActivity.class);
                intent_change2.putExtra("paypwd_status", paypwd_status);
                intent_change2.putExtra("style", TypeUtil.PASSWORD_TRADE);
                startActivityForResult(intent_change2, 0);
                break;

            default:
                break;
        }

    }

}
