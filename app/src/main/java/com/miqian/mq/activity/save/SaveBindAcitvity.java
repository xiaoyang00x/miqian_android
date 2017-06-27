package com.miqian.mq.activity.save;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2017/6/15.
 * 江西银行存管
 * 绑定银行卡
 */

public class SaveBindAcitvity extends BaseActivity implements View.OnClickListener {

    private EditText editName;
    private EditText editIdcard;
    private EditText editBank;
    private EditText editMobile;
    private EditText editCaptcha;
    private Button btCaptcha;
    private Button btSubmit;

    private String authCode;
    private String mobile;

    private MyRunnable myRunnable;
    private Thread thread;
    public static boolean isTimer;// 是否可以计时
    private static Handler handler;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_captcha:
                sendMessage();
                break;
            case R.id.bt_submit:
                open();
                break;
        }
    }

    @Override
    public void obtainData() {

    }

    private boolean isMobile() {

        mobile = editMobile.getText().toString();

        if (!TextUtils.isEmpty(mobile)) {
            if (MobileOS.isMobileNO(mobile) && mobile.length() == 11) {
                return true;
//                if (!TextUtils.isEmpty(captcha)) {
//                    if (captcha.length() < 6) {
//                        Uihelper.showToast(this, R.string.capthcha_num);
//                    } else {
//                        summit(captcha, password);
//                    }
//
//                } else {
//                    Uihelper.showToast(this, R.string.tip_captcha);
//                }
            } else {
                Uihelper.showToast(this, R.string.phone_noeffect);
            }

        } else {
            Uihelper.showToast(this, R.string.phone_null);
        }
        return false;
    }

    private void open() {
        String userName = editName.getText().toString();
        String idCard = editIdcard.getText().toString();
        String bankCardNo = editBank.getText().toString();
//        String mobile = editMobile.getText().toString();
        String captcha = editCaptcha.getText().toString();

        begin();
        HttpRequest.openJx(this, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                end();
                startActivity(new Intent(SaveBindAcitvity.this, SavePasswordAcitvity.class));
            }

            @Override
            public void onFail(String error) {
                end();
            }
        }, idCard, userName, mobile, bankCardNo, authCode, captcha);
    }

    private void sendMessage() {
        if (!isMobile()) {
            return;
        }
        editMobile.setEnabled(false);
        begin();
        HttpRequest.getCaptcha(this, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                end();
                btCaptcha.setEnabled(false);
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
        }, editMobile.getText().toString(), TypeUtil.CAPTCHA_REGISTER);

    }

    public class MyRunnable implements Runnable {

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
    public void initView() {
        editName = (EditText) findViewById(R.id.edit_name);
        editIdcard = (EditText) findViewById(R.id.edit_idcard);
        editBank = (EditText) findViewById(R.id.edit_bank);
        editMobile = (EditText) findViewById(R.id.edit_mobile);
        editCaptcha = (EditText) findViewById(R.id.edit_captcha);
        btCaptcha = (Button) findViewById(R.id.bt_captcha);
        btCaptcha.setOnClickListener(this);
        btSubmit = (Button) findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                btCaptcha.setEnabled(false);
                btCaptcha.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                String timeInfo = msg.getData().getString("time");
                btCaptcha.setText(timeInfo + "秒后重新获取");
                if ("0".equals(timeInfo)) {
                    btCaptcha.setEnabled(true);
                    btCaptcha.setText("获取验证码");
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_jx_bind;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("绑定银行卡");
    }

    @Override
    protected String getPageName() {
        return "绑定银行卡";
    }

    @Override
    protected void onDestroy() {
        isTimer = false;
        super.onDestroy();
    }
}
