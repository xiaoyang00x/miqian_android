package com.miqian.mq.activity;

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
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Joy on 2015/9/4.
 */
public class TradePsCaptchaActivity extends BaseActivity {


    private EditText mEt_Captcha;
    private Button mBtn_sendCaptcha;
    private boolean isTimer;// 是否可以计时
    private MyRunnable myRunnable;
    private Thread thread;
    private static Handler handler;
    private EditText mEtRealname;
    private TextView tv_phone;
    private String telephone;
    private boolean isModifyPhone;
    private String realNameStatus;
    private TextView tv_tip;

    @Override
    public void onCreate(Bundle arg0) {
        Intent intent = getIntent();
        isModifyPhone = intent.getBooleanExtra("isModifyPhone", false);
        realNameStatus = intent.getStringExtra("realNameStatus");
        super.onCreate(arg0);
    }

    @Override
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

    @Override
    public void initView() {

        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        telephone = Pref.getString(Pref.TELEPHONE, mActivity, "");
        tv_phone.setText(telephone);
        mEt_Captcha = (EditText) findViewById(R.id.et_account_captcha);
        if (!isModifyPhone) {
            mEtRealname = (EditText) findViewById(R.id.et_realname);
        }
        mBtn_sendCaptcha = (Button) findViewById(R.id.btn_send);

        mBtn_sendCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "1049");
                sendMessage();
            }
        });
        reFreshView();

    }

    private void reFreshView() {

        if (isModifyPhone) {
            findViewById(R.id.frament_modifytradepassword).setVisibility(View.GONE);
            findViewById(R.id.view_1).setVisibility(View.GONE);
            mTitle.setTitleText("修改手机号码");
            tv_tip.setText("修改手机号码,请先验证手机");
        }
        if (!TextUtils.isEmpty(realNameStatus)) {
            //未实名认证
            if ("0".equals(realNameStatus)) {
                findViewById(R.id.frament_modifytradepassword).setVisibility(View.GONE);
                findViewById(R.id.view_1).setVisibility(View.GONE);
                tv_tip.setText("修改交易密码,请先验证手机");
            } else {
                tv_tip.setText("修改交易密码,请先验证手机和身份证");
            }
        }
    }

    private void sendMessage() {
        int captchaType;
        if (isModifyPhone) {
            captchaType = TypeUtil.CAPTCHA_BINDTEL_FIRST;
        } else {
            captchaType = TypeUtil.CAPTCHA_TRADEPASSWORD;

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
        }, telephone, captchaType);

    }

    public void btn_click(View v) {
        final String captcha = mEt_Captcha.getText().toString();
        if (isModifyPhone) {
            if (TextUtils.isEmpty(captcha)) {
                Uihelper.showToast(this, R.string.tip_captcha);
            } else {
                if (captcha.length() < 6) {
                    Uihelper.showToast(mActivity, R.string.capthcha_num);
                    return;
                }
                begin();
                HttpRequest.checkCaptcha(mActivity, new ICallback<Meta>() {
                    @Override
                    public void onSucceed(Meta result) {
                        end();
                        Intent intent = new Intent(mActivity, SendCaptchaActivity.class);
                        intent.putExtra("type", TypeUtil.MODIFY_PHONE);
                        intent.putExtra("captcha", captcha);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFail(String error) {
                        end();
                        Uihelper.showToast(mActivity, error);
                    }
                }, telephone, TypeUtil.CAPTCHA_BINDTEL_FIRST, captcha);
            }
        } else {

            if (!TextUtils.isEmpty(realNameStatus)) {
                //未实名认证
                if ("0".equals(realNameStatus)) {
                    summit("", captcha);
                } else {
                    String idCard = mEtRealname.getText().toString();
                    if (!TextUtils.isEmpty(idCard)) {
                        if (idCard.matches(FormatUtil.PATTERN_IDCARD)) {
                            if (TextUtils.isEmpty(captcha)) {
                                Uihelper.showToast(this, R.string.tip_captcha);
                            } else {
                                if (captcha.length() < 6) {
                                    Uihelper.showToast(mActivity, R.string.capthcha_num);
                                } else {
                                    summit(idCard, captcha);
                                }
                            }
                        } else {
                            Uihelper.showToast(mActivity, "身份证号码不正确");
                        }
                    } else {
                        Uihelper.showToast(mActivity, "身份证号码不能为空");
                    }
                }
            }
        }

    }

    private void summit(final String idCard, final String captcha) {

        HttpRequest.changePayPassword(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                Intent intent = new Intent(mActivity, SetPasswordActivity.class);
                intent.putExtra("captcha", captcha);
                intent.putExtra("idCard", idCard);
                intent.putExtra("telephone", telephone);
                intent.putExtra("type", TypeUtil.PASSWORD_TRADE);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String error) {

                Uihelper.showToast(mActivity, error);

            }
        }, "SXJ0", idCard, telephone, captcha, "", "");

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
        return R.layout.activity_sendcaptcha_tradeps;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("修改交易密码");

    }

    @Override
    protected String getPageName() {
        return "修改交易密码";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTimer = false;
    }
}
