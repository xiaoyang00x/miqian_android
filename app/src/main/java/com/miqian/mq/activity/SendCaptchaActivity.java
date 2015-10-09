package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.setting.SetPasswordActivity;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Joy on 2015/9/4.
 */
public class SendCaptchaActivity extends BaseActivity {


    private EditText mEt_Telephone, mEt_Captcha;
    private Button mBtn_sendCaptcha;
    private String phone;
    private boolean isTimer;// 是否可以计时
    private MyRunnable myRunnable;
    private Thread thread;
    private boolean isSending;
    private static Handler handler;
    private static final int NUM_TYPE_CAPTCHA = 1;
    private static final int NUM_TYPE_SUMMIT = 2;
    private static final int NUM_TYPE_TOAST = 3;
    private TextView tv_phone;
    private int type;   //忘记密码，修改绑定手机


    @Override
    public void obtainData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mBtn_sendCaptcha.setEnabled(false);
                String timeInfo = msg.getData().getString("time");
                mBtn_sendCaptcha.setText(timeInfo + "秒后重新获取");
                if ("0".equals(timeInfo)) {
                    isSending = false;
                    mBtn_sendCaptcha.setEnabled(true);
                    mBtn_sendCaptcha.setText("获取验证码");
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if (type == TypeUtil.SENDCAPTCHA_FORGETPSW) {
            mTitle.setTitleText("修改登录密码");
        }

        tv_phone = (TextView) findViewById(R.id.tv_modifyphone_captcha);

        mEt_Telephone = (EditText) findViewById(R.id.et_account_telephone);
        mEt_Captcha = (EditText) findViewById(R.id.et_account_captcha);
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
                watchEdittext(NUM_TYPE_CAPTCHA);
            }
        });

    }

    private void watchEdittext(int type) {
        phone = mEt_Telephone.getText().toString();
        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                switch (type) {
                    case NUM_TYPE_TOAST:
                        break;
                    case NUM_TYPE_CAPTCHA:
                        //发送验证码
                        sendMessage();

                        break;
                    case NUM_TYPE_SUMMIT:
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
        int summitType = 0;
        switch (type) {
            case TypeUtil.SENDCAPTCHA_FORGETPSW:
                summitType = TypeUtil.CAPTCHA_FINDPASSWORD;
                break;
        }

        HttpRequest.getCaptcha(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                Uihelper.trace("captcha:" + result.getCode());
            }

            @Override
            public void onFail(String error) {

            }
        }, phone, summitType);


        mBtn_sendCaptcha.setEnabled(false);
        myRunnable = new MyRunnable();
        thread = new Thread(myRunnable);
        thread.start(); // 启动线程，进行倒计时
        isTimer = true;
    }

    public void btn_click(View v) {

        String captcha = mEt_Captcha.getText().toString();
        phone = mEt_Telephone.getText().toString();


        if (!TextUtils.isEmpty(phone)) {
            if (!TextUtils.isEmpty(captcha)) {

                summit(phone, captcha);

            } else {
                Uihelper.showToast(this, R.string.tip_captcha);
            }
            ;
        } else {
            Uihelper.showToast(this, R.string.phone_null);
        }


    }

    private void summit(final String phone, final String captcha) {
        String userId;
        if (UserUtil.hasLogin(mActivity)) {
            userId = UserUtil.getUserId(mActivity);
        } else {
            userId = "";
        }

        int summitType = 0;
        switch (type) {
            case TypeUtil.SENDCAPTCHA_FORGETPSW:
                summitType = TypeUtil.CAPTCHA_FINDPASSWORD;
                break;
        }
        HttpRequest.checkCaptcha(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                Intent intent = new Intent(mActivity, SetPasswordActivity.class);
                intent.putExtra("captcha", captcha);
                intent.putExtra("phone", phone);
                intent.putExtra("type", TypeUtil.PASSWORD_LOGIN);
                startActivity(intent);
            }

            @Override
            public void onFail(String error) {

                Uihelper.showToast(mActivity, error);

            }
        }, phone, summitType, captcha);

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
    public int getLayoutId() {
        return R.layout.activity_sendcaptcha;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("注册");

    }

    public static void enterActivity(Context context, int type) {

        Intent intent = new Intent(context, SendCaptchaActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);

    }
}
