package com.miqian.mq.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.OrderLian;
import com.miqian.mq.entity.OrderLianResult;
import com.miqian.mq.entity.PayOrder;
import com.miqian.mq.entity.PayOrderResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.pay.BaseHelper;
import com.miqian.mq.pay.MobileSecurePayer;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by Joy on 2015/9/10.
 */
public class IntoActivity extends BaseActivity implements View.OnClickListener {

    private Button btRollin;
    private RelativeLayout frameMoney;
    private RelativeLayout frameBank;
    private RelativeLayout frameBankInput;
    private RelativeLayout frameTip;
    private EditText editMoney;
    private EditText editBankNumber;
    private TextView bindBankName;
    private TextView bindBankNumber;
    private ImageView iconBank;

    private MyHandler mHandler;
    private UserInfo userInfo;

    private String money;
    private String bankNumber;
    private int rollType;
    private String bindStatus;

    @Override
    public void obtainData() {
        mWaitingDialog.show();
        HttpRequest.getUserInfo(mActivity, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                mWaitingDialog.dismiss();
                userInfo = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        rollType = intent.getIntExtra("rollType", 0);
        btRollin = (Button) findViewById(R.id.bt_rollin);
        btRollin.setOnClickListener(this);
        frameMoney = (RelativeLayout) findViewById(R.id.frame_money);
        if (rollType == 1) {
            frameMoney.setVisibility(View.GONE);
            money = intent.getStringExtra("money");
        }
        frameBankInput = (RelativeLayout) findViewById(R.id.frame_bank_input);
        frameBank = (RelativeLayout) findViewById(R.id.frame_bank);
        frameTip = (RelativeLayout) findViewById(R.id.frame_tip);
        editMoney = (EditText) findViewById(R.id.edit_money);
        editMoney.addTextChangedListener(new MyTextWatcher() {

            @Override
            public void myAfterTextChanged(Editable s) {
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
        editBankNumber = (EditText) findViewById(R.id.edit_bank_number);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        bindBankNumber = (TextView) findViewById(R.id.bind_bank_number);
        iconBank = (ImageView) findViewById(R.id.icon_bank);
        mHandler = new MyHandler(this);
    }

    private void refreshView() {
        bindStatus = userInfo.bindCardStatus;
        if (bindStatus.equals("0")) {
            frameBank.setVisibility(View.GONE);
            frameBankInput.setVisibility(View.VISIBLE);
            editBankNumber.requestFocus();
            frameTip.setVisibility(View.GONE);
        } else if (bindStatus.equals("1")) {
            if (userInfo.getSupportStatus().equals("1")) {
                frameBankInput.setVisibility(View.GONE);
                frameTip.setVisibility(View.GONE);
                if (rollType == 1) {
                    btRollin.performClick();
                }
            } else {
                frameBank.setVisibility(View.VISIBLE);
                frameBankInput.setVisibility(View.VISIBLE);
                frameTip.setVisibility(View.VISIBLE);
                editBankNumber.requestFocus();
            }
            bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankNo());
            if (!TextUtils.isEmpty(bankNumber) && bankNumber.length() > 4) {
                bankNumber = "**** **** **** " + bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
            }
            bindBankNumber.setText(bankNumber);
            bindBankName.setText(userInfo.getBankName());

            if (!TextUtils.isEmpty(userInfo.getBankUrlSmall())) {
                imageLoader.displayImage(userInfo.getBankUrlSmall(), iconBank, options);
            }
        }
    }

    private void rollIn() {
        if (rollType == 0) {
            money = editMoney.getText().toString();
            if (TextUtils.isEmpty(money)) {
                Uihelper.showToast(mActivity, "转入金额不能为空");
                return;
            }
            float tempMoney = Float.parseFloat(money);
            float minMoney = Float.parseFloat(userInfo.getAddRechargeMinValue());
            if (tempMoney < minMoney) {
                Uihelper.showToast(mActivity, "转入金额不能为小于" + minMoney + "元");
                return;
            }
        }

        if (bindStatus.equals("0")) {
            bankNumber = editBankNumber.getText().toString();
            if (TextUtils.isEmpty(bankNumber)) {
                Uihelper.showToast(mActivity, "卡号不能为空");
                return;
            }
        } else {
            bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankNo());
        }

        mWaitingDialog.show();
        HttpRequest.rollIn(mActivity, new ICallback<PayOrderResult>() {
            @Override
            public void onSucceed(PayOrderResult payOrderResult) {
                mWaitingDialog.dismiss();
                PayOrder newPayOrder = constructPreCardPayOrder(payOrderResult.getData());
                String content4Pay = JSON.toJSONString(newPayOrder);
                Log.e("", "content4Pay---------- : " + content4Pay);
                MobileSecurePayer msp = new MobileSecurePayer();
                msp.pay(content4Pay, mHandler, Constants.RQF_PAY, mActivity, false);
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, money, bankNumber);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("充值");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_rollin:
                rollIn();
                break;
            default:
                break;
        }
    }

    private PayOrder constructPreCardPayOrder(PayOrder payOrder) {
        payOrder.setAcct_name(RSAUtils.decryptByPrivate(payOrder.getAcct_name()));
        payOrder.setCard_no(RSAUtils.decryptByPrivate(payOrder.getCard_no()));
        payOrder.setUser_id(RSAUtils.decryptByPrivate(payOrder.getUser_id()));
        payOrder.setId_no(RSAUtils.decryptByPrivate(payOrder.getId_no()));
        return payOrder;
    }

    class MyHandler extends Handler {
        WeakReference<IntoActivity> weakActivity;

        public MyHandler(IntoActivity activity) {
            weakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            String strRet = (String) msg.obj;
            switch (msg.what) {
                case Constants.RQF_PAY:
                    JSONObject objContent = BaseHelper.string2JSON(strRet);
                    String retCode = objContent.optString("ret_code");
                    String retMsg = objContent.optString("ret_msg");
                    //                    String money = objContent.optString("money_order");
                    String orderNo = objContent.optString("no_order");
                    // //先判断状态码，状态码为 成功或处理中 的需要 验签
                    if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                        String resulPay = objContent.optString("result_pay");
                        if (Constants.RESULT_PAY_SUCCESS.equalsIgnoreCase(resulPay)) {
                            Log.e("", "rollInResult ------- " + resulPay);
                            // 支付成功后续处理
                            checkOrder(orderNo);
                        } else {
                            Uihelper.showToast(mActivity, retMsg);
                        }
                    } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                        String resulPay = objContent.optString("result_pay");
                        if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
                            checkOrder(orderNo);
                        }
                    } else if (retCode.equals("1006")) {
                        Uihelper.showToast(mActivity, "您已取消当前交易");
                    } else if (retCode.equals("1004")) {
                        Uihelper.showToast(mActivity, "您的银行卡号有误");
                    } else {
                        Uihelper.showToast(mActivity, retMsg);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void checkOrder(String orderNo) {
        mWaitingDialog.show();
        HttpRequest.rollInResult(mActivity, new ICallback<OrderLianResult>() {
            @Override
            public void onSucceed(OrderLianResult orderLianResult) {
                mWaitingDialog.dismiss();
                OrderLian orderLian = orderLianResult.getData();
                if (orderLianResult.getCode().equals("000000")) {
                    if (rollType == 1) {
                        setResult(CurrentInvestment.SUCCESS);
                    } else {
                        Intent intent = new Intent(IntoActivity.this, IntoResultActivity.class);
                        intent.putExtra("status", CurrentInvestment.SUCCESS);
                        intent.putExtra("money", orderLian.getAmt());
                        intent.putExtra("orderNo", orderLian.getOrderNo());
                        startActivity(intent);
                    }
                } else if (orderLianResult.getCode().equals("100096")) {
                    if (rollType == 1) {
                        setResult(CurrentInvestment.FAIL);
                    } else {
                        Intent intent = new Intent(IntoActivity.this, IntoResultActivity.class);
                        intent.putExtra("status", CurrentInvestment.FAIL);
                        intent.putExtra("money", orderLian.getAmt());
                        intent.putExtra("orderNo", orderLian.getOrderNo());
                        startActivity(intent);
                    }
                } else if (orderLianResult.getCode().equals("100097")) {
                    if (rollType == 1) {
                        setResult(CurrentInvestment.PROCESSING);
                    } else {
                        Intent intent = new Intent(IntoActivity.this, IntoResultActivity.class);
                        intent.putExtra("status", CurrentInvestment.PROCESSING);
                        intent.putExtra("money", orderLian.getAmt());
                        intent.putExtra("orderNo", orderLian.getOrderNo());
                        startActivity(intent);
                    }
                }
                IntoActivity.this.finish();
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, orderNo);
    }
}
