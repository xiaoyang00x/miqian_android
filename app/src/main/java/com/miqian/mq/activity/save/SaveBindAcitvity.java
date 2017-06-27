package com.miqian.mq.activity.save;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * 绑定银行卡
 */

public class SaveBindAcitvity extends BaseActivity implements View.OnClickListener {

    private EditText editName;
    private EditText editIdcard;
    private EditText editBank;
    private EditText editMobile;
    private EditText editCaptcha;
    private Button btCaptcha;
    private Button btSubmit;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_captcha:
                break;
            case R.id.bt_submit:
                break;
        }
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        editName = (EditText) findViewById(R.id.edit_name);
        editIdcard = (EditText) findViewById(R.id.edit_idcard);
        editBank = (EditText) findViewById(R.id.edit_bank);
        editMobile = (EditText) findViewById(R.id.edit_mobile);
        editCaptcha = (EditText) findViewById(R.id.edit_captcha);
        btCaptcha = (Button) findViewById(R.id.bt_captcha);
        btCaptcha.setOnClickListener(this);
        btSubmit = (Button) findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_jx_bind;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("绑定银行卡");
    }

    @Override
    protected String getPageName() {
        return "绑定银行卡";
    }
}
