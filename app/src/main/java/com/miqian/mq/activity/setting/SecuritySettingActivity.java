package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.activity.TradePsCaptchaActivity;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;


public class SecuritySettingActivity extends BaseActivity implements OnClickListener, ExtendOperationController.ExtendOperationListener {

    private String payPwdStatus;

    private ImageView iv_switch;
    private String realNameStatus;
    private ExtendOperationController extendOperationController;

    @Override
    public void obtainData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        payPwdStatus = intent.getStringExtra("payPwdStatus");
        realNameStatus = intent.getStringExtra("realNameStatus");

        iv_switch = (ImageView) findViewById(R.id.iv_switch);
        findViewById(R.id.password_login).setOnClickListener(this);
        findViewById(R.id.password_transaction).setOnClickListener(this);
        iv_switch.setOnClickListener(this);

        extendOperationController = ExtendOperationController.getInstance();
        extendOperationController.registerExtendOperationListener(this);
    }

    @Override
    protected void onDestroy() {
        if (extendOperationController != null) {
            extendOperationController.unRegisterExtendOperationListener(this);
        }
        super.onDestroy();
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
                if ("0".equals(payPwdStatus)) {//未设置
                    Intent intent = new Intent(mActivity, SetPasswordActivity.class);
                    intent.putExtra("type", TypeUtil.TRADEPASSWORD_FIRST_SETTING);
                    startActivity(intent);
                } else {
                    MobclickAgent.onEvent(mActivity, "1028");
                    Intent intent = new Intent(mActivity, TradePsCaptchaActivity.class);
                    intent.putExtra("realNameStatus", realNameStatus);
                    startActivity(intent);
                }
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

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {

        switch (operationKey) {
            case ExtendOperationController.OperationKey.SETTRADPASSWORD_SUCCESS:
                payPwdStatus = "1";
                break;
            default:
                break;
        }

    }
}
