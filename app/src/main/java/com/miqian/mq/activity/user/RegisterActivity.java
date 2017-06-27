package com.miqian.mq.activity.user;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.CaptchaResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.ProgressDialogView;
import com.miqian.mq.views.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 * Created by Tlt on 2017/6/9.
 */
public class RegisterActivity extends Activity {

    @BindView(R.id.edit_telephone)
    EditText editPhone;
    @BindView(R.id.et_password)
    EditText editPassword;
    @BindView(R.id.et_account_captcha)
    EditText editCaptcha;
    @BindView(R.id.btn_send)
    Button btnSendCaptcha;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private Dialog mWaitingDialog;
    private static final int NUM_TYPE_CAPTCHA = 1;
    private static final int NUM_TYPE_TOAST = 3;
    private String phone;
    public static boolean isTimer;// 是否可以计时
    private static Handler handler;
    private MyRunnable myRunnable;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);
        if (MobileOS.isKitOrNewer()) {
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setStatusBarTintResource(R.color.base_background);
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mWaitingDialog = ProgressDialogView.create(this);
        btnSendCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(RegisterActivity.this, "1049");
                watchEdittext(NUM_TYPE_CAPTCHA);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                btnSendCaptcha.setEnabled(false);
                btnSendCaptcha.setTextColor(ContextCompat.getColor(RegisterActivity.this,R.color.mq_b5_v2));
                String timeInfo = msg.getData().getString("time");
                btnSendCaptcha.setText(timeInfo + "秒后重新获取");
                if ("0".equals(timeInfo)) {
                    btnSendCaptcha.setEnabled(true);
                    btnSendCaptcha.setTextColor(ContextCompat.getColor(RegisterActivity.this,R.color.mq_r1_v2));
                    btnSendCaptcha.setText("获取验证码");
                }
                super.handleMessage(msg);
            }
        };
    }

    private void register() {

        String captcha = editCaptcha.getText().toString();
        String password = editPassword.getText().toString();
        phone = editPhone.getText().toString();

        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                if (!TextUtils.isEmpty(captcha)) {
                    if (captcha.length() < 6) {

                        Uihelper.showToast(RegisterActivity.this, R.string.capthcha_num);
                    } else {
                        summit(captcha, password);
                    }

                } else {
                    Uihelper.showToast(RegisterActivity.this, R.string.tip_captcha);
                }
            } else {
                Uihelper.showToast(RegisterActivity.this, R.string.phone_noeffect);
            }

        } else {
            Uihelper.showToast(RegisterActivity.this, R.string.phone_null);
        }
    }

    private void summit(String captcha, String password) {

        if (!TextUtils.isEmpty(password)) {
            if (password.length() < 6 || password.length() > 16) {
                Uihelper.showToast(RegisterActivity.this, R.string.tip_password);
            } else {
                if (password.matches(FormatUtil.PATTERN_PASSWORD)) {
                    begin();
                    HttpRequest.register(RegisterActivity.this, new ICallback<RegisterResult>() {
                        @Override
                        public void onSucceed(RegisterResult result) {
                            MobclickAgent.onEvent(RegisterActivity.this, "1053");
                            end();
                            Uihelper.showToast(RegisterActivity.this, "注册成功");
                            UserInfo userInfo = result.getData();
                            UserUtil.saveUserInfo(RegisterActivity.this, userInfo);
                            if (Pref.getBoolean(Pref.GESTURESTATE, RegisterActivity.this, true)) {
                                GestureLockSetActivity.startActivity(RegisterActivity.this, null);
                            }
                            ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.LOGIN_SUCCESS, null);
                            finish();
                        }

                        @Override
                        public void onFail(String error) {
                            end();
                            Uihelper.showToast(RegisterActivity.this, error);
                        }
                    }, phone, captcha, password, "");
                }else {
                    Uihelper.showToast(RegisterActivity.this, "密码须是数字、字母、字符组合");
                }
            }

        } else {
            Uihelper.showToast(RegisterActivity.this, "密码不能为空");
        }
    }

    private void watchEdittext(int type) {
        phone = editPhone.getText().toString();
        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                switch (type) {
                    case NUM_TYPE_CAPTCHA:
                        //发送验证码
                        sendMessage();
                        break;
                }
            } else {
                Uihelper.showToast(RegisterActivity.this, R.string.phone_noeffect);
            }
        } else {
            Uihelper.showToast(RegisterActivity.this, R.string.phone_null);
        }

    }

    private void sendMessage() {
        begin();
        HttpRequest.getCaptcha(RegisterActivity.this, new ICallback<CaptchaResult>() {
            @Override
            public void onSucceed(CaptchaResult result) {
                end();
                btnSendCaptcha.setEnabled(false);
                myRunnable = new MyRunnable();
                thread = new Thread(myRunnable);
                thread.start(); // 启动线程，进行倒计时
                isTimer = true;
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(RegisterActivity.this, error);

            }
        }, phone, TypeUtil.CAPTCHA_REGISTER);

    }

    @OnClick(R.id.btn_back) //返回按钮
    public void back() {
        this.finish();
    }

    @OnClick(R.id.btn_law) //秒钱注册协议
    public void law() {
        MobclickAgent.onEvent(RegisterActivity.this, "1051");
        WebActivity.startActivity(RegisterActivity.this, Urls.web_register_law);
    }

    @OnClick(R.id.btn_tologin) //已有账号去登录
    public void toLogin() {
       LoginActivity.start(RegisterActivity.this);
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_anim_scenic_no, R.anim.activity_anim_scenic_out);
    }

    /**
     * 显示 loading 对话框
     */
    protected void begin() {
        if (mWaitingDialog != null) {
            mWaitingDialog.show();

        }
    }

    /**
     * 隐藏 loading 对话框
     */
    protected void end() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {

            for (int i = 60; i >= 0; i--) {
                if (isTimer) {
                    Bundle bundle = new Bundle();
                    bundle.putString("time", i + "");
                    Message message = Message.obtain();
                    message.setData(bundle);
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000); // 休眠1秒钟
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            isTimer = false;

        }
    }

    @Override
    protected void onDestroy() {
        isTimer = false;
        super.onDestroy();
    }
}
