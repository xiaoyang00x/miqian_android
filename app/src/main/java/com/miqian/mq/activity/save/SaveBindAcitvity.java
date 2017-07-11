package com.miqian.mq.activity.save;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.CaptchaResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.entity.SaveInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jackie on 2017/6/15.
 * 江西银行存管
 * 绑定银行卡
 */

public class SaveBindAcitvity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_idcard)
    EditText editIdcard;
    @BindView(R.id.edit_bank)
    EditText editBank;
    @BindView(R.id.edit_mobile)
    EditText editMobile;
    @BindView(R.id.edit_captcha)
    EditText editCaptcha;
    @BindView(R.id.bt_captcha)
    Button btCaptcha;
    @BindView(R.id.bt_submit)
    Button btSubmit;

    @BindView(R.id.text_tip)
    TextView textTip;

    @BindView(R.id.check_law)
    CheckBox checkLaw;
    @BindView(R.id.bt_law)
    Button btLaw;
    @BindView(R.id.bt_law2)
    Button btLaw2;

    private String authCode;
    private String mobile;

    private MyRunnable myRunnable;
    private Thread thread;
    public static boolean isTimer;// 是否可以计时
    private static Handler handler;

    private SaveInfo saveInfo;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_captcha:
                sendMessage();
                break;
            case R.id.bt_submit:
                open();
                break;
            case R.id.bt_law:
                WebActivity.startActivity(this, Urls.web_authority_law);
                break;
            case R.id.bt_law2:
                WebActivity.startActivity(this, Urls.web_otth_law);
                break;
        }
    }

    @Override
    public void obtainData() {
        begin();
        HttpRequest.openJxPreprocess(this, new ICallback<MqResult<SaveInfo>>() {
            @Override
            public void onSucceed(MqResult<SaveInfo> result) {
                end();
                saveInfo = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(SaveBindAcitvity.this, error);
            }
        });
    }

    private void refreshView() {
        textTip.setText(saveInfo.getWarmthTips());
        if (!TextUtils.isEmpty(saveInfo.getUserName()) && !TextUtils.isEmpty(saveInfo.getIdCard())) {
            editName.setText(saveInfo.getUserName());
            editIdcard.setText(saveInfo.getIdCard());
            editName.setEnabled(false);
            editIdcard.setEnabled(false);
        }
    }

    private boolean isMobile() {
        mobile = editMobile.getText().toString();
        if (!TextUtils.isEmpty(mobile)) {
            if (MobileOS.isMobileNO(mobile) && mobile.length() == 11) {
                return true;
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
        String captcha = editCaptcha.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            Uihelper.showToast(mActivity, "姓名不能为空");
            return;
        }
        if (userName.length() < 2) {
            Uihelper.showToast(mActivity, "请输入正确的姓名");
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            Uihelper.showToast(mActivity, "身份证号码不能为空");
            return;
        }

        if (!idCard.matches(FormatUtil.PATTERN_IDCARD)) {
            Uihelper.showToast(mActivity, "身份证号码不正确");
            return;
        }

        if (TextUtils.isEmpty(bankCardNo)) {
            Uihelper.showToast(mActivity, "银行卡号不能为空");
            return;
        }
        if (!isMobile()) {
            return;
        }

        if (!TextUtils.isEmpty(captcha)) {
            if (captcha.length() < 6) {
                Uihelper.showToast(this, R.string.capthcha_num);
                return;
            }
        } else {
            Uihelper.showToast(this, R.string.tip_captcha);
            return;
        }

        if (TextUtils.isEmpty(authCode)) {
            Uihelper.showToast(this, "请获取验证码");
            return;
        }

        begin();
        HttpRequest.openJx(this, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                end();
                startActivity(new Intent(SaveBindAcitvity.this, SaveResultAcitvity.class));
                SaveBindAcitvity.this.finish();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(SaveBindAcitvity.this, error);
            }
        }, idCard, userName, mobile, bankCardNo, authCode, captcha);
    }

    private void sendMessage() {
        if (!isMobile()) {
            return;
        }
        begin();
        HttpRequest.getCaptcha(this, new ICallback<CaptchaResult>() {
            @Override
            public void onSucceed(CaptchaResult result) {
                end();
                authCode = result.getData().getAuthCode();
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
        }, editMobile.getText().toString(), TypeUtil.CAPTCHA_OPENACCOUNT);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            btSubmit.setEnabled(true);
        } else {
            btSubmit.setEnabled(false);
        }
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
        ButterKnife.bind(this);

        checkLaw.setOnCheckedChangeListener(this);
        btLaw.setOnClickListener(this);
        btLaw2.setOnClickListener(this);

        btCaptcha.setOnClickListener(this);
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
                    btCaptcha.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                }
                super.handleMessage(msg);
            }
        };
        if (UserUtil.isSaveBefore(this)) {
            getmTitle().setIvLeftVisiable(View.GONE);
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (UserUtil.isSaveBefore(this) && keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
