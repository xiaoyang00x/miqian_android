package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.QQprojectRegister;
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
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2015/9/1.
 */
public abstract class Dialog_Register extends Dialog implements View.OnClickListener {

    public int type;
    private final Context mContext;

    private EditText mEt_Telephone;
    private EditText mEt_Captcha;
    private EditText mEt_Password;
    private Button mBtn_sendCaptcha;
    private Dialog mWaitingDialog;

    private String phone;
    private static final int NUM_TYPE_CAPTCHA = 1;
    private static final int NUM_TYPE_TOAST = 3;

    private boolean isTimer;// 是否可以计时
    private MyRunnable myRunnable;
    private Thread thread;
    private static Handler handler;
    private RegisterListenerFromDialog mRegistListener;

    public Dialog_Register(Context context) {
        super(context, R.style.Dialog);
        mContext = context;
        this.setContentView(R.layout.dialog_register);
        initView();
        obtainData();
    }

    public void obtainData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String timeInfo = msg.getData().getString("time");
                mBtn_sendCaptcha.setText(timeInfo + "秒后重新获取");
                if ("0".equals(timeInfo)) {
                    mBtn_sendCaptcha.setEnabled(true);
                    mBtn_sendCaptcha.setText("获取验证码");
                }
                super.handleMessage(msg);
            }
        };
    }

    public interface RegisterListenerFromDialog {
        void registerSuccessfromDialog();
    }

    public void setLoginLister(RegisterListenerFromDialog registListener) {
        this.mRegistListener = registListener;
    }

    private void initView() {
        mWaitingDialog = ProgressDialogView.create(mContext);
        mEt_Telephone = (EditText) findViewById(R.id.et_account_telephone);
        mEt_Captcha = (EditText) findViewById(R.id.et_account_captcha);
        mEt_Password = (EditText) findViewById(R.id.et_account_password);
        mBtn_sendCaptcha = (Button) findViewById(R.id.btn_send);
        final Button mBtn_register = (Button) findViewById(R.id.btn_register);
        Button mBtn_TextLaw = (Button) findViewById(R.id.tv_law);
        Button mBtn_TextLaw_Net = (Button) findViewById(R.id.text_law_net);
        Button mBtntoLogin = (Button) findViewById(R.id.btn_tologin);
        CheckBox checkBoxLaw = (CheckBox)findViewById(R.id.check_law_dialog);

        mBtn_register.setOnClickListener(this);
        mBtn_TextLaw.setOnClickListener(this);
        mBtntoLogin.setOnClickListener(this);
        mBtn_TextLaw_Net.setOnClickListener(this);

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

        checkBoxLaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mBtn_register.setEnabled(true);
                } else {
                    mBtn_register.setEnabled(false);
                }
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
                Uihelper.showToast(mContext, R.string.phone_noeffect);
            }
        } else {
            Uihelper.showToast(mContext, R.string.phone_null);
        }

    }

    public void click() {

        String captcha = mEt_Captcha.getText().toString();
        String password = mEt_Password.getText().toString();
        phone = mEt_Telephone.getText().toString();

        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                if (!TextUtils.isEmpty(captcha)) {
                    if (captcha.length() < 6) {

                        Uihelper.showToast(mContext, R.string.capthcha_num);
                    } else {
                        summit(captcha, password);
                    }

                } else {
                    Uihelper.showToast(mContext, R.string.tip_captcha);
                }
            } else {
                Uihelper.showToast(mContext, R.string.phone_noeffect);
            }

        } else {
            Uihelper.showToast(mContext, R.string.phone_null);
        }
    }

    private void summit(final String captcha, final String password) {
        if (!TextUtils.isEmpty(password)) {
            if (password.length() < 6 || password.length() > 16) {
                Uihelper.showToast(mContext, R.string.tip_password);
            } else {
                begin();
                HttpRequest.register(mContext, new ICallback<RegisterResult>() {
                    @Override
                    public void onSucceed(RegisterResult result) {
                        MobclickAgent.onEvent(mContext, "1053");
                        Uihelper.showToast(mContext, "注册成功");
                        UserInfo userInfo = result.getData();
                        UserUtil.saveUserInfo(mContext, userInfo);
                        registerSuccess();
                        if(mRegistListener != null) {
                            mRegistListener.registerSuccessfromDialog();
                        }
                        end();
                    }

                    @Override
                    public void onFail(String error) {
                        end();
                        Uihelper.showToast(mContext, error);
                    }
                }, phone, captcha, password, "");
            }

        } else {
            Uihelper.showToast(mContext, "密码不能为空");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register://注册
                click();

                break;
            case R.id.tv_law:
                MobclickAgent.onEvent(mContext, "1051");
                WebActivity.startActivity(mContext, Urls.web_register_law);

                break;
            case R.id.text_law_net://网络借贷协议
                WebActivity.startActivity(mContext, Urls.web_register_law_net);

                break;
            case R.id.btn_tologin:
                toLogin();
                break;
            default:
                break;
        }
    }

    public abstract void toLogin();

    public abstract void registerSuccess();

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

    private void sendMessage() {
        begin();
        HttpRequest.getCaptcha(mContext, new ICallback<Meta>() {
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
                Uihelper.showToast(mContext, error);

            }
        }, phone, TypeUtil.CAPTCHA_REGISTER);

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
     * 显示 loading 对话框
     */
    protected void end() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
    }

    @Override
    public void dismiss() {
        isTimer = false;
        super.dismiss();
    }
}
