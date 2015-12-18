package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.activity.TradePsCaptchaActivity;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;


public class SecuritySettingActivity extends BaseActivity implements OnClickListener {

    private String payPwdStatus;

    private ImageView iv_switch;

    @Override
    public void obtainData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        payPwdStatus = intent.getStringExtra("payPwdStatus");

        iv_switch = (ImageView) findViewById(R.id.iv_switch);
        findViewById(R.id.password_login).setOnClickListener(this);
        findViewById(R.id.password_transaction).setOnClickListener(this);
        iv_switch.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeGestureSwitchState();
    }

    private void changeGestureSwitchState() {
        iv_switch.setImageResource(isGestureLockOpen() ?
                R.drawable.gesture_swith_open : R.drawable.gesture_switch_close);
    }

    // 手势密码是否开启
    private boolean isGestureLockOpen() {
        return Pref.getBoolean(Pref.GESTURESTATE, getBaseContext(), false);
    }

    // 设置 手势密码 开关状态
    private void setGestureLockState(boolean isOpen) {
        Pref.saveBoolean(Pref.GESTURESTATE, isOpen, getBaseContext());
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
                MobclickAgent.onEvent(mActivity, "1027");
                SendCaptchaActivity.enterActivity(mActivity, TypeUtil.SENDCAPTCHA_FORGETPSW, true);

                break;
            case R.id.password_transaction:
                MobclickAgent.onEvent(mActivity, "1028");
                Intent intent = new Intent(mActivity, TradePsCaptchaActivity.class);
                intent.putExtra("payPwdStatus", payPwdStatus);
                startActivity(intent);
                break;
            case R.id.iv_switch:
                if (isGestureLockOpen()) {
                    setGestureLockState(false);
                    changeGestureSwitchState();
                } else {
                    GestureLockSetActivity.startActivity(getBaseContext(), null);
                }
                break;

            default:
                break;
        }

    }

    @Override
    protected String getPageName() {
        return "安全设置";
    }
}
