package com.miqian.mq.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;

/**
 * Created by Joy on 2015/9/4.
 */
public class RegisterActivity extends BaseActivity {

    private EditText mEt_Telephone;
    private EditText mEt_Captcha;
    private EditText mEt_Password;
    private EditText mEt_Invite;
    private Button mBtn_sendCaptcha;
    private String phone;
    private boolean isTimer;// 是否可以计时
    private MyRunnable myRunnable;
    private Thread thread;
    private MyHandler handler = new MyHandler(this);
    private static final int NUM_TYPE_CAPTCHA = 1;
    private static final int NUM_TYPE_SUMMIT = 2;
    private static final int NUM_TYPE_TOAST = 3;

    @Override
    public void obtainData() {
    }

    public Button getBtn_sendCaptcha() {
        return mBtn_sendCaptcha;
    }

    private static class MyHandler extends Handler {

        private final WeakReference<RegisterActivity> mActivity;

        public MyHandler(RegisterActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;
            }
            Button button = mActivity.get().getBtn_sendCaptcha();
            if (null == button) {
                return;
            }
            String timeInfo = msg.getData().getString("time");
            button.setText(timeInfo + "秒后重新获取");
            if ("0".equals(timeInfo)) {
                button.setEnabled(true);
                button.setText("获取验证码");
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void initView() {

        mEt_Telephone = (EditText) findViewById(R.id.et_account_telephone);
        mEt_Captcha = (EditText) findViewById(R.id.et_account_captcha);
        mEt_Invite = (EditText) findViewById(R.id.et_account_invite);
        mEt_Password = (EditText) findViewById(R.id.et_account_password);
        mBtn_sendCaptcha = (Button) findViewById(R.id.btn_send);

        mEt_Telephone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    watchEdittext(NUM_TYPE_TOAST);
                }
            }
        });

        mBtn_sendCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "1049");
                watchEdittext(NUM_TYPE_CAPTCHA);
            }
        });

    }

    private void watchEdittext(int type) {
        phone = mEt_Telephone.getText().toString();
        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                switch (type) {
                    case NUM_TYPE_CAPTCHA:
                        //发送验证码
                        sendMessage();
                        break;
                }
            } else {
                Uihelper.showToast(this, R.string.phone_noeffect);
            }
        } else {
            Uihelper.showToast(this, R.string.phone_null);
        }

    }

    private void sendMessage() {
        begin();
        HttpRequest.getCaptcha(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                end();
                mBtn_sendCaptcha.setEnabled(false);
                myRunnable = new MyRunnable();
                thread = new Thread(myRunnable);
                thread.start(); // 启动线程，进行倒计时
                isTimer = true;
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);

            }
        }, phone, TypeUtil.CAPTCHA_REGISTER,"");

    }

    public void btn_click(View v) {

        String captcha = mEt_Captcha.getText().toString();
        String invite = mEt_Invite.getText().toString();
        String password = mEt_Password.getText().toString();
        phone = mEt_Telephone.getText().toString();

        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                if (!TextUtils.isEmpty(captcha)) {
                    if (captcha.length() < 6) {

                        Uihelper.showToast(mActivity, R.string.capthcha_num);
                    } else {
                        if (TextUtils.isEmpty(invite)) {
                            summit(captcha, invite, password);
                        } else {
                            if (invite.length() < 4) {
                                Uihelper.showToast(mActivity, R.string.invite_num);
                            } else {
                                summit(captcha, invite, password);
                            }
                        }
                    }

                } else {
                    Uihelper.showToast(this, R.string.tip_captcha);
                }
            } else {
                Uihelper.showToast(this, R.string.phone_noeffect);
            }

        } else {
            Uihelper.showToast(this, R.string.phone_null);
        }


    }

    public void textLawCick(View v) {
        MobclickAgent.onEvent(mContext, "1051");
        WebActivity.startActivity(mActivity, Urls.web_register_law);
    }

    private void summit(final String captcha, final String invite, final String password) {


        if (!TextUtils.isEmpty(password)) {

            if (password.length() < 6 || password.length() > 16) {
                Uihelper.showToast(this, R.string.tip_password);
            } else {
                begin();
                HttpRequest.register(RegisterActivity.this, new ICallback<RegisterResult>() {
                    @Override
                    public void onSucceed(RegisterResult result) {
                        MobclickAgent.onEvent(mContext, "1053");
                        end();
                        Uihelper.showToast(mActivity, "注册成功");
                        UserInfo userInfo = result.getData();
                        UserUtil.saveUserInfo(getApplicationContext(), userInfo);

                        if (Pref.getBoolean(Pref.GESTURESTATE, getBaseContext(), true)) {
                            GestureLockSetActivity.startActivity(getBaseContext(),null,true);
                        }else {
                            OpenHuiFuActivity.startActivity(mActivity, TypeUtil.TYPE_OPENHF_REGISTER);
                        }
                        finish();
                    }

                    @Override
                    public void onFail(String error) {
                        end();
                        Uihelper.showToast(mActivity, error);
                    }
                }, phone, captcha, password, invite);
            }

        } else {
            Uihelper.showToast(this, "密码不能为空");
        }
    }

    @Override
    protected String getPageName() {
        return "注册";
    }

    private class MyRunnable implements Runnable {

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
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("注册");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTimer = false;
    }
}
