package com.miqian.mq.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miqian.mq.R;
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
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.ProgressDialogView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by TuLiangTan on 2016/12/6.
 */

public class QQprojectRegister implements View.OnClickListener {


    private final View mRootView;
    private final Context mContext;
    private final MySwipeRefresh mSwipeRefresh;

    private EditText mEt_Telephone;
    private EditText mEt_Captcha;
    private EditText mEt_Password;
    private Button mBtn_sendCaptcha;
    private Dialog mWaitingDialog;

    private String phone;
    private static final int NUM_TYPE_CAPTCHA = 1;
    private static final int NUM_TYPE_TOAST = 3;

    public  static boolean isTimer;// 是否可以计时
    private MyRunnable myRunnable;
    private Thread thread;
    private static Handler handler;
    private LoginLister mLoginLister;

    public QQprojectRegister(Context context, View rootView, MySwipeRefresh swipeRefresh) {

        this.mContext = context;
        this.mRootView = rootView;
        this.mSwipeRefresh = swipeRefresh;
        initView();
        obtainData();
    }
    public interface LoginLister{
        void toLogin();
        void registerSuccessfromNative();
    }

    public void setLoginLister(LoginLister loginLister){
           this.mLoginLister=loginLister;
    }

    public void obtainData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mBtn_sendCaptcha.setEnabled(false);
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

    private void initView() {

        mWaitingDialog = ProgressDialogView.create(mContext);

        mSwipeRefresh.setVisibility(View.GONE);
        mRootView.findViewById(R.id.layout_register).setVisibility(View.VISIBLE);

        mEt_Telephone = (EditText) mRootView.findViewById(R.id.et_account_telephone);
        mEt_Captcha = (EditText) mRootView.findViewById(R.id.et_account_captcha);
        mEt_Password = (EditText) mRootView.findViewById(R.id.et_account_password);
        mBtn_sendCaptcha = (Button) mRootView.findViewById(R.id.btn_send);
        Button mBtn_register = (Button) mRootView.findViewById(R.id.btn_register);
        Button mBtn_TextLaw = (Button) mRootView.findViewById(R.id.text_law);
        Button mBtntoLogin = (Button) mRootView.findViewById(R.id.btn_tologin);

        mBtn_register.setOnClickListener(this);
        mBtn_TextLaw.setOnClickListener(this);
        mBtntoLogin.setOnClickListener(this);

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
                        end();
                        Uihelper.showToast(mContext, "注册成功");
                        UserInfo userInfo = result.getData();
                        UserUtil.saveUserInfo(mContext, userInfo);
                        mLoginLister.registerSuccessfromNative();
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
            case R.id.text_law:
                MobclickAgent.onEvent(mContext, "1051");
                WebActivity.startActivity(mContext, Urls.web_register_law);

                break;
            case R.id.btn_tologin:
                mLoginLister.toLogin();
                break;
            default:
                break;
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

    private void sendMessage() {
        if (mWaitingDialog != null) {
            mWaitingDialog.show();
        }
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

}
