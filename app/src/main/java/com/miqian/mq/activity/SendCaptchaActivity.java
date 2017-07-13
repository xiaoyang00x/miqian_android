package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.setting.SetPasswordActivity;
import com.miqian.mq.entity.CaptchaResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
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
    private static Handler handler;
    private TextView tvPhone;
    private int type;
    private View frameTelephone;
    private String authCode;


    @Override
    public void obtainData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mBtn_sendCaptcha.setEnabled(false);
                mBtn_sendCaptcha.setTextColor(ContextCompat.getColor(SendCaptchaActivity.this, R.color.mq_b5_v2));
                String timeInfo = msg.getData().getString("time");
                mBtn_sendCaptcha.setText(timeInfo + "秒后重新获取");
                if ("0".equals(timeInfo)) {
                    mBtn_sendCaptcha.setEnabled(true);
                    mBtn_sendCaptcha.setTextColor(ContextCompat.getColor(SendCaptchaActivity.this, R.color.mq_r1_v2));
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

        tvPhone = (TextView) findViewById(R.id.tv_phone);
        frameTelephone = findViewById(R.id.frame_telephone);
        mEt_Telephone = (EditText) findViewById(R.id.et_account_telephone);
        mEt_Captcha = (EditText) findViewById(R.id.et_account_captcha);
        mBtn_sendCaptcha = (Button) findViewById(R.id.btn_send);

        mBtn_sendCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCaptcha();
            }
        });
        if (type == TypeUtil.CAPTHCA_MODIFYLOGINPW || type == TypeUtil.CAPTCHA_TRADE_PW) {
            tvPhone.setVisibility(View.VISIBLE);
            phone = Pref.getString(Pref.TELEPHONE, mActivity, "");
            if (!TextUtils.isEmpty(phone)) {
                tvPhone.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
            }
        } else {
            frameTelephone.setVisibility(View.VISIBLE);
        }

    }

    private void sendCaptcha() {
        if (type == TypeUtil.CAPTCHA_FINDPASSWORD) {
            phone = mEt_Telephone.getText().toString();
        }
        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                //发送验证码
                sendMessage();
            } else {
                Uihelper.showToast(this, R.string.phone_noeffect);
            }
        } else {
            Uihelper.showToast(this, R.string.phone_null);
        }

    }

    private void sendMessage() {
        begin();
        HttpRequest.getCaptcha(mActivity, new ICallback<CaptchaResult>() {
            @Override
            public void onSucceed(CaptchaResult result) {
                end();
                if (result != null && result.getData() != null) {
                    authCode = result.getData().getAuthCode();
                }
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
        }, phone, type);

    }

    public void btn_click(View v) {

        String captcha = mEt_Captcha.getText().toString();
        if (type == TypeUtil.CAPTCHA_FINDPASSWORD) {
            phone = mEt_Telephone.getText().toString();
        }

        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                //检验验证码
                if (!TextUtils.isEmpty(captcha)) {

                    if (type == TypeUtil.CAPTCHA_TRADE_PW) {
                        //跳转江西银行网页
                        if (!TextUtils.isEmpty(authCode)) {
                            HttpRequest.changejxTradePwd(mActivity, authCode, captcha);
                            finish();
                        }

                    } else {
                        //验证验证码
                        checkCaptcha(phone, captcha);
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

    private void checkCaptcha(final String phone, final String captcha) {
        begin();
        HttpRequest.checkCaptcha(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                end();
                summit(phone, captcha);
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);

            }
        }, phone, type, captcha);
    }

    private void summit(final String phone, final String captcha) {
        Intent intent = new Intent(mActivity, SetPasswordActivity.class);
        intent.putExtra("captcha", captcha);
        intent.putExtra("phone", phone);
        intent.putExtra("type", type);
        startActivity(intent);
        finish();
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
        if (type == TypeUtil.CAPTCHA_FINDPASSWORD) {
            mTitle.setTitleText("忘记密码");
        } else if (type == TypeUtil.CAPTCHA_TRADE_PW) {
            mTitle.setTitleText("修改交易密码");
        } else {
            mTitle.setTitleText("修改登录密码");
        }

    }

    @Override
    protected String getPageName() {
        if (type == TypeUtil.CAPTCHA_FINDPASSWORD) {
            return "忘记密码";
        } else if (type == TypeUtil.CAPTCHA_TRADE_PW) {
            return "修改交易密码";
        } else {
            return "修改登录密码";
        }
    }

    public static void enterActivity(Context context, int type) {

        Intent intent = new Intent(context, SendCaptchaActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTimer = false;
    }
}
