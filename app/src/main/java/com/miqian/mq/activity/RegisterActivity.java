package com.miqian.mq.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Joy on 2015/9/4.
 */
public class RegisterActivity extends BaseActivity {


    private EditText mEt_Telephone, mEt_Captcha, mEt_Invite, mEt_Password;
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


    @Override
    public void obtainData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
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
        mEt_Captcha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(mEt_Captcha.getText().toString())) {
                        //判断邀请码是否存在


                    } else {
                        Uihelper.showToast(RegisterActivity.this, "请输入正确正确格式的邀请码,4到5位的数字字母组合");
                    }
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
        if (!TextUtils.isEmpty(phone)){
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
        }else {
            Uihelper.showToast(this, R.string.phone_null);
        }

    }

    private void sendMessage() {

        HttpRequest.getCaptcha(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                Uihelper.trace(result.getCode()+"");
            }

            @Override
            public void onFail(String error) {

            }
        },phone,TypeUtil.CAPTCHA_REGISTER,"");


        mBtn_sendCaptcha.setEnabled(false);
        myRunnable = new MyRunnable();
        thread = new Thread(myRunnable);
        thread.start(); // 启动线程，进行倒计时
        isTimer = true;
    }

    public void btn_click(View v) {

        String captcha = mEt_Captcha.getText().toString();
        String invite = mEt_Invite.getText().toString();
        String password = mEt_Password.getText().toString();


       if(!TextUtils.isEmpty(phone)){
           if (!TextUtils.isEmpty(captcha)) {

               if (!TextUtils.isEmpty(password)) {
                   HttpRequest.register(RegisterActivity.this, new ICallback<RegisterResult>() {
                       @Override
                       public void onSucceed(RegisterResult result) {
                           Log.e("Register", result.getData().getBalance());
                       }

                       @Override
                       public void onFail(String error) {
                       }
                   }, phone, captcha, password, invite);

               } else {
                   Uihelper.showToast(this, R.string.tip_password);
               }

           } else {
               Uihelper.showToast(this, R.string.tip_captcha);
           };
       }else {
           Uihelper.showToast(this, R.string.phone_null);
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
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("注册");

    }
}
