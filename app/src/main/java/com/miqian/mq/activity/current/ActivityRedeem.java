package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.Redeem;
import com.miqian.mq.entity.RedeemData;
import com.miqian.mq.entity.UserCurrentData;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.DialogTip;
import com.miqian.mq.views.TextViewEx;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 赎回
 * Created by TuLiangTan on 2015/10/9.
 */
public class ActivityRedeem extends BaseActivity implements View.OnClickListener {
    private EditText editMoney;
    private String money;
    private Button btnRollout;
    private TextViewEx tvTip;
    private TextViewEx tvExtra;
    private UserCurrentData userCurrentData;
    private BigDecimal resideMoney;//可赎回金额
    private BigDecimal curResidue;//本月剩余可赎回额度
    private DialogTip mDialogTip;
    private TextView tvPhone;
    private Button mBtn_sendCaptcha;
    private EditText mEt_Captcha;
    private boolean isTimer;// 是否可以计时
    private MyRunnable myRunnable;
    private Thread thread;
    private static Handler handler;
    private String phone;

    @Override
    public void onCreate(Bundle arg0) {
        userCurrentData = (UserCurrentData) getIntent().getExtras().getSerializable("userCurrentData");
        super.onCreate(arg0);
    }

    @Override
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
        phone = Pref.getString(Pref.TELEPHONE, mActivity, "");
        if (!TextUtils.isEmpty(phone)) {
            tvPhone.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
        }
        DecimalFormat df = new java.text.DecimalFormat("0.00");
        if (userCurrentData != null && userCurrentData.getUserRedeem() != null && userCurrentData.getUserCurrent() != null) {
            BigDecimal balance = userCurrentData.getUserCurrent().getPrnAmt();//活期待收金额
            BigDecimal curDayResidue = userCurrentData.getUserRedeem().getCurDayResidue();//当日剩余可赎回额度
            if (curDayResidue.compareTo(balance) > 0) {
                resideMoney = balance;
            } else {
                resideMoney = curDayResidue;
            }
            String format = df.format(resideMoney);
            if (format.equals(".00")) {
                editMoney.setHint("可赎回0.00元");
            } else {
                editMoney.setHint("可赎回" + format + "元");
            }

            btnRollout.setEnabled(true);
            BigDecimal curMonthAmt = userCurrentData.getUserRedeem().getCurMonthAmt();//本月已赎回的金额
            BigDecimal lmtMonthAmt = userCurrentData.getUserRedeem().getLmtMonthAmt();//本月限制赎回额度
            curResidue = lmtMonthAmt.subtract(curMonthAmt);//剩余可赎回额度


            String textCurResidue = df.format(curResidue);
            String textCurMonthAmt = df.format(curMonthAmt);
            if (textCurResidue.equals(".00")) {
                textCurResidue = "0";
            }
            if (textCurMonthAmt.equals(".00")) {
                textCurMonthAmt = "0";
            }

            tvExtra.setText("您剩余可赎回额度" + textCurResidue + "元" + "(本月已经赎回" + textCurMonthAmt + "元)");
            String tip = userCurrentData.getUserRedeem().getWarmPrompt();//温馨提示
            if (!TextUtils.isEmpty(tip)) {
                String limitTips = tip.replace("|n", "\n");
                tvTip.setText(limitTips, true);
            }
        }
    }

    @Override
    public void initView() {
        editMoney = (EditText) findViewById(R.id.edit_money);
        btnRollout = (Button) findViewById(R.id.bt_redeem);
        tvExtra = (TextViewEx) findViewById(R.id.tv_extra);
        tvTip = (TextViewEx) findViewById(R.id.tv_tip);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        mBtn_sendCaptcha = (Button) findViewById(R.id.btn_send);
        mEt_Captcha = (EditText) findViewById(R.id.et_captcha);
        mBtn_sendCaptcha.setOnClickListener(this);

        editMoney.addTextChangedListener(new MyTextWatcher() {

            @Override
            public void myAfterTextChanged(Editable s) {
//				setRollEnabled();
                try {
                    String temp = s.toString();
                    if (temp.matches(FormatUtil.PATTERN_MONEY)) {
                        return;
                    }
                    s.delete(temp.length() - 1, temp.length());
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_redeem;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("赎回");

    }

    @Override
    protected String getPageName() {
        return "赎回";
    }

    public void btn_click(View v) {
        String captcha = mEt_Captcha.getText().toString();
        money = editMoney.getText().toString();
        if (!TextUtils.isEmpty(money)) {
            BigDecimal moneyCurrent = new BigDecimal(money);

            if (moneyCurrent.compareTo(resideMoney) > 0) {
                initDialog();
                mDialogTip.setInfo("您当日赎回金额已超限\n      请修改赎回金额");
                mDialogTip.setSureInfo("我知道了");
                mDialogTip.show();

            } else if (moneyCurrent.compareTo(BigDecimal.ZERO) == 0) {
                initDialog();
                mDialogTip.setInfo("金额不能小于0.01");
                mDialogTip.setSureInfo("我知道了");
                mDialogTip.show();
            } else {
                if (!TextUtils.isEmpty(captcha)) {
                    if (captcha.length() < 6) {
                        Uihelper.showToast(mActivity, R.string.capthcha_num);
                    } else {
                        summit(captcha, money);

                    }

                } else {
                    Uihelper.showToast(this, R.string.tip_captcha);
                }

            }
        } else {
            initDialog();
            mDialogTip.setInfo("赎回金额不可为空\n  请修改赎回金额");
            mDialogTip.setSureInfo("我知道了");
            mDialogTip.show();
        }
    }

    private void summit(String captcha, final String money) {

        mWaitingDialog.show();
        HttpRequest.redeem(mActivity, new ICallback<RedeemData>() {
            @Override
            public void onSucceed(RedeemData result) {
                mWaitingDialog.dismiss();
                String code = result.getCode();
                Intent intent = new Intent(mActivity, RedeemResult.class);
                if (code.equals("999993") || code.equals("999988") || code.equals("996633")) {
                    initDialog();
                    mDialogTip.setInfo(result.getMessage());
                    mDialogTip.show();
                } else {
                    if (code.equals("000000")) {
                        //刷新我的活期数据
                        ExtendOperationController.getInstance().
                                doNotificationExtendOperation(ExtendOperationController.OperationKey.REFRESH_CURRENTINFO, null);
                        intent.putExtra("state", 1);
                        Redeem redeem = result.getData();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("redeemData", redeem);
                        intent.putExtras(bundle);
                    } else {
                        intent.putExtra("state", 0);
                        intent.putExtra("errormessage", result.getMessage());
                        intent.putExtra("capital", money);
                    }
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(ActivityRedeem.this, error);
            }
        }, money, phone, captcha);

    }

    private void redoom(String password) {


    }

    private void initDialog() {
        if (mDialogTip == null) {
            mDialogTip = new DialogTip(ActivityRedeem.this) {
            };
        }
    }


    private void sendMessage() {
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
        }, phone, TypeUtil.CAPTCHA_REDEEM);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send) {
            sendMessage();
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
    protected void onDestroy() {
        super.onDestroy();
        isTimer = false;
    }

}