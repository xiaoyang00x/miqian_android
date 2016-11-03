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
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

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
    private static final int NUM_TYPE_CAPTCHA = 1;
    private static final int NUM_TYPE_SUMMIT = 2;
    private static final int NUM_TYPE_TOAST = 3;
    private TextView tv_phone;
    private int type;   //忘记密码，绑定手机号
    private boolean isModifyPhone;
    private String oldCaptcha;
    private Intent intent;
    private View layoutPhone;
    private View layoutInputPhone;
    private boolean isModify;

    @Override
    public void obtainData() {
        intent = getIntent();
        type = intent.getIntExtra("type", 0);
        oldCaptcha = intent.getStringExtra("captcha");
        isModify = intent.getBooleanExtra("isModify", false);
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
        if (type == TypeUtil.SENDCAPTCHA_FORGETPSW && isModify) {
            layoutPhone.setVisibility(View.VISIBLE);
            layoutInputPhone.setVisibility(View.GONE);
            phone = Pref.getString(Pref.TELEPHONE, mActivity, "");
            if (!TextUtils.isEmpty(phone)) {
                tv_phone.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
            }
        }
    }

    @Override
    public void initView() {
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        mEt_Telephone = (EditText) findViewById(R.id.et_account_telephone);
        mEt_Captcha = (EditText) findViewById(R.id.et_account_captcha);
        mBtn_sendCaptcha = (Button) findViewById(R.id.btn_send);
        layoutInputPhone = findViewById(R.id.layout_input_phone);
        layoutPhone = findViewById(R.id.layout_phone);


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
                if (type == TypeUtil.SENDCAPTCHA_FORGETPSW && isModify) {
                    sendMessage();
                } else {
                    watchEdittext(NUM_TYPE_CAPTCHA);
                }
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
                if (isModify) {
                    //修改手机号码
                    summitType = TypeUtil.CAPTCHA_CHANGPWD;
                } else {
                    //找回密码
                    summitType = TypeUtil.CAPTCHA_FINDPASSWORD;
                }
                MobclickAgent.onEvent(mContext, "1049");
                break;
            case TypeUtil.MODIFY_PHONE:
                MobclickAgent.onEvent(mContext, "1050");
                summitType = TypeUtil.CAPTCHA_BINTTEL_SECOND;
                break;
            default:
                break;
        }
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
        }, phone, summitType,"");

    }

    public void btn_click(View v) {
        if (!(type == TypeUtil.SENDCAPTCHA_FORGETPSW && isModify)) {
            phone = mEt_Telephone.getText().toString();
        }
        String captcha = mEt_Captcha.getText().toString();
        if (!TextUtils.isEmpty(phone)) {
            if (MobileOS.isMobileNO(phone) && phone.length() == 11) {
                //检验验证码
                if (!TextUtils.isEmpty(captcha)) {
                    //验证验证码
                    checkCaptcha(phone, captcha);

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
        int type = 0;
        if (isModifyPhone) {
            summit(phone, captcha);
        } else {
            if (isModify){
                //修改登录密码
                type = TypeUtil.CAPTCHA_CHANGPWD;
            }else {
                //忘记登录密码
                type = TypeUtil.CAPTCHA_FINDPASSWORD;
            }
            HttpRequest.checkCaptcha(mActivity, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {
                    summit(phone, captcha);
                }

                @Override
                public void onFail(String error) {

                    Uihelper.showToast(mActivity, error);

                }
            }, phone, type, captcha);
        }

    }

    private void summit(final String phone, final String captcha) {

        if (isModifyPhone) {
            //绑定新手机号码
            String oldPhone = Pref.getString(Pref.TELEPHONE, mActivity, "");
            if (!TextUtils.isEmpty(oldPhone) && !TextUtils.isEmpty(captcha)) {
                begin();
                HttpRequest.changePhone(mActivity, new ICallback<Meta>() {
                    @Override
                    public void onSucceed(Meta result) {
                        end();
                        Uihelper.showToast(mActivity, "绑定成功");
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.MODIFYPHONE, phone);
                        finish();
                    }

                    @Override
                    public void onFail(String error) {
                        end();
                        Uihelper.showToast(mActivity, error);

                    }
                }, oldPhone, oldCaptcha, phone, captcha);
            }

        } else {
            Intent intent = new Intent(mActivity, SetPasswordActivity.class);
            intent.putExtra("captcha", captcha);
            intent.putExtra("phone", phone);
            intent.putExtra("isModify", isModify);
            startActivity(intent);
            finish();
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
        return R.layout.activity_sendcaptcha;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        if (type == TypeUtil.SENDCAPTCHA_FORGETPSW) {
            mTitle.setTitleText("忘记密码");
            if (isModify) {
                mTitle.setTitleText("修改登录密码");
            }
        } else if (type == TypeUtil.MODIFY_PHONE) {
            mTitle.setTitleText("绑定手机号码");
            isModifyPhone = true;
        }
    }

    @Override
    protected String getPageName() {
        String pageName = "";
        if (type == TypeUtil.SENDCAPTCHA_FORGETPSW) {
            pageName = "忘记密码";
            if (isModify) {
                pageName = "修改登录密码";
            }
        } else if (type == TypeUtil.MODIFY_PHONE) {
            pageName = "绑定手机号码";
        }
        return pageName;
    }

    public static void enterActivity(Context context, int type, boolean isModify) {

        Intent intent = new Intent(context, SendCaptchaActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("isModify", isModify);
        context.startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTimer = false;
    }
}
