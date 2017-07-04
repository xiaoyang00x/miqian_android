package com.miqian.mq.activity.rollin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.CaptchaResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.OrderRecharge;
import com.miqian.mq.entity.OrderRechargeResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.entity.UserInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;

/**
 * Created by Joy on 2015/9/10.
 * 充值页面
 */
public class IntoActivity extends BaseActivity implements View.OnClickListener {

    private Button btRollin;
    private EditText editMoney;
    private TextView bindBankNumber;
    private TextView textMobile;
    private TextView textLimit;
    private TextView textTip1;
    private TextView textTip2;
    private EditText editCaptcha;
    private Button btnSendCaptcha;

    private UserInfo userInfo;

    private String money;
    private String authCode;
    private int rollType;//为1时传入充值金额，为0时不传

    private MyRunnable myRunnable;
    private Thread thread;
    public static boolean isTimer;// 是否可以计时
    private static Handler handler;

    @Override
    public void obtainData() {
        begin();
        HttpRequest.rollInPreprocess(mActivity, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                end();
                showContentView();
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if ("000000".equals(meta.getCode())) {
                    UserInfoResult userInfoResult = JsonUtil.parseObject(result, UserInfoResult.class);
                    userInfo = userInfoResult.getData();
                    refreshView();
                } else if ("999991".equals(meta.getCode())) {
                    textTip2.setVisibility(View.VISIBLE);
                    textTip2.setText(meta.getMessage());
                } else if ("996633".equals(meta.getCode())) {
                    Uihelper.showToast(mActivity, meta.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                end();
                showErrorView();
                btRollin.setEnabled(false);
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    @Override
    public void initView() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                btnSendCaptcha.setEnabled(false);
                btnSendCaptcha.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                String timeInfo = msg.getData().getString("time");
                btnSendCaptcha.setText(timeInfo + "秒后重新获取");
                if ("0".equals(timeInfo)) {
                    btnSendCaptcha.setEnabled(true);
                    btnSendCaptcha.setText("获取验证码");
                    btnSendCaptcha.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                }
                super.handleMessage(msg);
            }
        };
        Intent intent = getIntent();
        rollType = intent.getIntExtra("rollType", 0);
        btRollin = (Button) findViewById(R.id.bt_rollin);
        btRollin.setOnClickListener(this);
        editMoney = (EditText) findViewById(R.id.edit_money);

        SpannableString ss = new SpannableString("请输入金额");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(16, true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editMoney.setHint(new SpannedString(ss));
//        editMoney.addTextChangedListener(new MyTextWatcher() {
//
//            @Override
//            public void myAfterTextChanged(Editable s) {
//                try {
//                    String temp = s.toString();
//                    if (temp.matches(FormatUtil.PATTERN_MONEY)) {
//                        return;
//                    }
//                    s.delete(temp.length() - 1, temp.length());
//                } catch (Exception e) {
//                }
//            }
//        });
        if (rollType == 1 || rollType == 2) {
            money = intent.getStringExtra("money");
        }
        bindBankNumber = (TextView) findViewById(R.id.bind_bank_number);
        textMobile = (TextView) findViewById(R.id.text_mobile);
        textLimit = (TextView) findViewById(R.id.text_limit);
        textLimit.setOnClickListener(this);
        editCaptcha = (EditText) findViewById(R.id.et_account_captcha);
        btnSendCaptcha = (Button) findViewById(R.id.btn_send);
        btnSendCaptcha.setOnClickListener(this);
        textTip1 = (TextView) findViewById(R.id.text_tip1);
        textTip2 = (TextView) findViewById(R.id.text_tip2);
    }

    private void refreshView() {
        textTip1.setText("温馨提示：单笔限额" + userInfo.getAmtPerLimit() + "万， 单日限额" + userInfo.getAmtDayLimit() + "万。若充值金额超过限额您可以选择\"转账充值\"。");
        btRollin.setEnabled(true);
        bindBankNumber.setText(RSAUtils.decryptByPrivate(userInfo.getBankNo()));
        textMobile.setText(userInfo.getMobile());
    }

    private void rollIn() {
        if (rollType == 0) {
            money = editMoney.getText().toString();
            if (TextUtils.isEmpty(money)) {
                Uihelper.showToast(mActivity, "转入金额不能为空");
                return;
            }
            BigDecimal tempMoney = new BigDecimal(money);
            BigDecimal minMoney = new BigDecimal(userInfo.getAmtMinLimit());
            if (minMoney.compareTo(tempMoney) > 0) {
                Uihelper.showToast(mActivity, "转入金额不能为小于" + minMoney + "元");
                return;
            }
        }
        String captcha = editCaptcha.getText().toString();
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
        HttpRequest.rollIn(mActivity, new ICallback<OrderRechargeResult>() {
            @Override
            public void onSucceed(OrderRechargeResult orderRechargeResult) {
                end();
                OrderRecharge orderRecharge = orderRechargeResult.getData();
                jumpToResult(orderRecharge);
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, money, authCode, captcha);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("在线快捷充值");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_rollin:
                rollIn();
                break;
            case R.id.btn_send:
                sendMessage();
                break;
            case R.id.text_limit:
                WebActivity.startActivity(mActivity, Urls.web_support_bank);
                break;
            default:
                break;
        }
    }


    private void sendMessage() {
        begin();
        HttpRequest.getCaptcha(this, new ICallback<CaptchaResult>() {
            @Override
            public void onSucceed(CaptchaResult result) {
                end();
                authCode = result.getData().getAuthCode();
                btnSendCaptcha.setEnabled(false);
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
        }, userInfo.getMobile(), TypeUtil.CAPTCHA_QUICK_RECHARGE);

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

//    public static String showErrorString(Context context, String key) {
//        String errorData = Pref.getString(Pref.ERROR_LIAN, context, Constants.ERROR_LIAN_DEFAULT);
//        if (!TextUtils.isEmpty(errorData)) {
//            Map<String, String> userMap = JSON.parseObject(errorData, new TypeReference<Map<String, String>>() {
//            });
//            return userMap.get(key);
//        }
//        return null;
//    }


    /**
     * 跳转充值结果
     *
     * @param orderRecharge
     */
    private void jumpToResult(OrderRecharge orderRecharge) {
        Intent intent = new Intent(IntoActivity.this, IntoResultActivity.class);
        intent.putExtra("status", orderRecharge.getStatus());
        intent.putExtra("money", orderRecharge.getAmt());
        intent.putExtra("orderNo", orderRecharge.getOrderNo());
        intent.putExtra("bankNo", orderRecharge.getBankNo());
        startActivity(intent);
        IntoActivity.this.finish();
    }

    @Override
    protected String getPageName() {
        return "在线快捷充值";
    }

//    MyTextWatcher textWatcherLian = new MyTextWatcher() {
//        @Override
//        public void myAfterTextChanged(Editable arg0) {
//            textErrorLian.setVisibility(View.GONE);
//        }
//    };

    @Override
    protected void onDestroy() {
        isTimer = false;
        super.onDestroy();
    }
}
