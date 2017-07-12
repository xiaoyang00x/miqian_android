package com.miqian.mq.activity.current;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.CaptchaResult;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.entity.NewCurrentFoundFlow;
import com.miqian.mq.entity.RedeemMqbInfo;
import com.miqian.mq.entity.RedeemResultInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Joy on 2015/9/10.
 * 充值页面
 */
public class ActivityRedeemMqb extends BaseActivity {

    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_time)
    TextView textTime;
    @BindView(R.id.text_money)
    TextView textMoney;
    @BindView(R.id.text_interest_rate)
    TextView textInterestRate;
    @BindView(R.id.text_mobile)
    TextView textMobile;
    @BindView(R.id.et_account_captcha)
    EditText editCaptcha;
    @BindView(R.id.btn_send)
    Button btnSendCaptcha;
    @BindView(R.id.bt_redeem)
    Button btRedeem;
    @BindView(R.id.text_tip)
    TextView textTip;

    private RedeemMqbInfo redeemMqbInfo;

    private NewCurrentFoundFlow.InvestInfo investInfo;
    private String transSeqNo;

    private MyRunnable myRunnable;
    private Thread thread;
    public static boolean isTimer;// 是否可以计时
    private static Handler handler;

    @Override
    public void obtainData() {
        begin();
        HttpRequest.redeemPreprocessMqb(mActivity, new ICallback<MqResult<RedeemMqbInfo>>() {
            @Override
            public void onSucceed(MqResult<RedeemMqbInfo> result) {
                end();
                redeemMqbInfo = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
                btRedeem.setEnabled(false);
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    public static void startActivity(Context context, NewCurrentFoundFlow.InvestInfo info) {
        Intent intent = new Intent(context, ActivityRedeemMqb.class);
        intent.putExtra("InvestInfo", JSON.toJSONString(info));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String s = intent.getStringExtra("InvestInfo");
        investInfo = JsonUtil.parseObject(s, NewCurrentFoundFlow.InvestInfo.class);

        if (investInfo != null) {
            textName.setText(investInfo.getProductName());
            textTime.setText("认购时间： " + Uihelper.timestampToDateStr_other(investInfo.getStartTime()));
            textMoney.setText(investInfo.getPurchaseAmount());
            textInterestRate.setText(investInfo.getProductRate());
            SpannableString spannableString = new SpannableString("%");
            spannableString.setSpan(new TextAppearanceSpan(this, R.style.f3_R1_V2), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textInterestRate.append(spannableString);


            transSeqNo = investInfo.getPurchaseSeqno();
        }
//        rollType = intent.getIntExtra("rollType", 0);
//        SpannableString ss = new SpannableString("请输入金额");//定义hint的值
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(16, true);//设置字体大小 true表示单位是sp
//        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        bindBankNumber = (TextView) findViewById(R.id.bind_bank_number);
//        textMobile = (TextView) findViewById(R.id.text_mobile);
//        textLimit = (TextView) findViewById(R.id.text_limit);
//        textLimit.setOnClickListener(this);
//        textTip1 = (TextView) findViewById(R.id.text_tip1);
//        textTip2 = (TextView) findViewById(R.id.text_tip2);
//        editCaptcha = (EditText) findViewById(R.id.et_account_captcha);
//        btnSendCaptcha = (Button) findViewById(R.id.btn_send);
//        btnSendCaptcha.setOnClickListener(this);
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
    }

    private void refreshView() {
        textTip.setText("最多赎回，" + redeemMqbInfo.getDayMaxCount() + "笔/日，" + redeemMqbInfo.getMonthMaxCount() + "笔/月");
        btRedeem.setEnabled(true);
        textMobile.setText(redeemMqbInfo.getMobile());
    }

    @OnClick(R.id.bt_redeem)//赎回
    public void redeem() {
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

        begin();
        HttpRequest.redeemMqb(mActivity, new ICallback<MqResult<RedeemResultInfo>>() {
            @Override
            public void onSucceed(MqResult<RedeemResultInfo> result) {
                end();
                RedeemResultInfo redeemResultInfo = result.getData();
                jumpToResult(redeemResultInfo);
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, transSeqNo, captcha);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_redeem_mqb;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("在线快捷充值");
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_rollin:
//                rollIn();
//                break;
//            case R.id.btn_send:
//                sendMessage();
//                break;
//            case R.id.text_limit:
//                WebActivity.startActivity(mActivity, Urls.web_support_bank);
//                break;
//            default:
//                break;
//        }
//    }

    @OnClick(R.id.btn_send)//发送验证码
    public void sendMessage() {
        begin();
        HttpRequest.getCaptcha(this, new ICallback<CaptchaResult>() {
            @Override
            public void onSucceed(CaptchaResult result) {
                end();
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
        }, redeemMqbInfo.getMobile(), TypeUtil.CAPTCHA_REDEEM_MIAOQIANBAO);

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

//    public void showTip2(String temp) {
//        BigDecimal tempMoney = new BigDecimal(temp);
//        if (tempMoney.compareTo(limitMoney) > 0) {
//            textTip2.setVisibility(View.VISIBLE);
//            textTip2.setText("已超过快捷充值限额" + userInfo.getAmtPerLimit() + "元， 建议使用转账充值");
//            btRollin.setEnabled(false);
//        } else {
//            textTip2.setVisibility(View.GONE);
//            btRollin.setEnabled(true);
//        }
//    }

    /**
     * 跳转充值结果
     *
     * @param redeemResultInfo
     */
    private void jumpToResult(RedeemResultInfo redeemResultInfo) {
//        if (rollType != 1) {
//            Intent intent = new Intent(ActivityRedeemMqb.this, IntoResultActivity.class);
//            intent.putExtra("status", orderRecharge.getStatus());
//            intent.putExtra("money", orderRecharge.getAmt());
//            intent.putExtra("orderNo", orderRecharge.getOrderNo());
//            intent.putExtra("bankNo", orderRecharge.getBankNo());
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent();
//            setResult(CurrentInvestment.SUCCESS, intent);
//        }
        ActivityRedeemMqb.this.finish();
    }

    @Override
    protected String getPageName() {
        return "秒钱宝赎回";
    }

    @Override
    protected void onDestroy() {
        isTimer = false;
        super.onDestroy();
    }
}
