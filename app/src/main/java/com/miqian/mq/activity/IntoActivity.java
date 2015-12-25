package com.miqian.mq.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.OrderLian;
import com.miqian.mq.entity.OrderLianResult;
import com.miqian.mq.entity.PayOrder;
import com.miqian.mq.entity.PayOrderResult;
import com.miqian.mq.entity.SupportBankMsgResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.pay.BaseHelper;
import com.miqian.mq.pay.MobileSecurePayer;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;

/**
 * Created by Joy on 2015/9/10.
 * 充值页面
 */
public class IntoActivity extends BaseActivity implements View.OnClickListener {

    private Button btRollin;
    private LinearLayout frameBank;
    private RelativeLayout frameBankInput;
    private EditText editMoney;
    private EditText editBankNumber;
    private TextView bindBankName;
    private TextView bindBankNumber;
    private LinearLayout frameRealName;
    private EditText editName;
    private EditText editCardId;
    private TextView textTip;
    private TextView textLimit;

    private MyHandler mHandler;
    private UserInfo userInfo;
    private PayOrder payOrder;

    private String money;
    private String bankNumber;
    private String realName;
    private String idCard;
    private int rollType;//为1时传入充值金额，为0时不传
    private String bindStatus;
    private String bindSupportStatus;
    private String relaNameStatus;

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getUserInfo(mActivity, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                end();
                userInfo = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
                btRollin.setEnabled(false);
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
        if (rollType == 1) {
            money = intent.getStringExtra("money");
            editMoney.setText(money);
            editMoney.setEnabled(false);
        }
        frameBankInput = (RelativeLayout) findViewById(R.id.frame_bank_input);
        frameBank = (LinearLayout) findViewById(R.id.frame_bank);
        editBankNumber = (EditText) findViewById(R.id.edit_bank_number);
        editBankNumber.addTextChangedListener(textWatcher);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        bindBankNumber = (TextView) findViewById(R.id.bind_bank_number);
        textLimit = (TextView) findViewById(R.id.text_limit);

        frameRealName = (LinearLayout) findViewById(R.id.frame_real_name);
        editName = (EditText) frameRealName.findViewById(R.id.edit_name);
        editCardId = (EditText) frameRealName.findViewById(R.id.edit_card_id);
        textTip = (TextView) findViewById(R.id.text_tip);
        mHandler = new MyHandler(this);
    }

    private void refreshView() {
        btRollin.setEnabled(true);
        bindStatus = userInfo.getBindCardStatus();
        bindSupportStatus = userInfo.getSupportStatus();
        relaNameStatus = userInfo.getRealNameStatus();
        if ("1".equals(relaNameStatus)) {
            editCardId.setEnabled(false);
            editName.setEnabled(false);
            realName = RSAUtils.decryptByPrivate(userInfo.getRealName());
            idCard = RSAUtils.decryptByPrivate(userInfo.getIdCard());
            editName.setText(realName);
            editCardId.setText(idCard);
        } else {
            frameRealName.setVisibility(View.VISIBLE);
        }
        if (bindStatus.equals("0")) {
            frameRealName.setVisibility(View.VISIBLE);
            frameBank.setVisibility(View.GONE);
            frameBankInput.setVisibility(View.VISIBLE);
            textTip.setVisibility(View.VISIBLE);
            showBankLimit();
        } else if (bindStatus.equals("1")) {
            if (userInfo.getSupportStatus().equals("1")) {
                frameBank.setVisibility(View.VISIBLE);
                frameBankInput.setVisibility(View.GONE);
                if (rollType == 1) {
                    btRollin.performClick();
                }
            } else {
                frameRealName.setVisibility(View.VISIBLE);
                frameBank.setVisibility(View.GONE);
                frameBankInput.setVisibility(View.VISIBLE);
                textTip.setVisibility(View.VISIBLE);
                showBankLimit();
            }
            bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankNo());
            if (!TextUtils.isEmpty(bankNumber) && bankNumber.length() > 4) {
                bankNumber = "**** **** **** " + bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
            }
            bindBankNumber.setText(bankNumber);
            bindBankName.setText(userInfo.getBankName());
            textLimit.setText("单笔限额" + userInfo.getSingleAmtLimit() + "元， 单日限额" + userInfo.getDayAmtLimit() + "元");
        }
    }

    private void rollIn() {
        if (rollType == 0) {
            money = editMoney.getText().toString();
            if (TextUtils.isEmpty(money)) {
                Uihelper.showToast(mActivity, "转入金额不能为空");
                return;
            }
            BigDecimal tempMoney = new BigDecimal(money);
            BigDecimal minMoney = new BigDecimal(userInfo.getAddRechargeMinValue());
            if (minMoney.compareTo(tempMoney) > 0) {
                Uihelper.showToast(mActivity, "转入金额不能为小于" + minMoney + "元");
                return;
            }
        }

        if (bindStatus.equals("0") || bindSupportStatus.equals("0")) {
            bankNumber = editBankNumber.getText().toString().replaceAll(" ", "");
            if (TextUtils.isEmpty(bankNumber)) {
                Uihelper.showToast(mActivity, "卡号不能为空");
                return;
            }
        } else {
            bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankNo());
        }

        if ("0".equals(relaNameStatus)) {
            realName = editName.getText().toString();
            idCard = editCardId.getText().toString();
            if (TextUtils.isEmpty(realName)) {
                Uihelper.showToast(mActivity, "姓名不能为空");
                return;
            }
            if (realName.length() < 2) {
                Uihelper.showToast(mActivity, "姓名输入有误");
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
        }
        begin();
        HttpRequest.rollIn(mActivity, new ICallback<String>() {
            @Override
            public void onSucceed(String result) {
                end();
                Meta meta = JsonUtil.parseObject(result, Meta.class);
                if ("000000".equals(meta.getCode())) {
                    PayOrderResult payOrderResult = JsonUtil.parseObject(result, PayOrderResult.class);
                    payOrder = constructPreCardPayOrder(payOrderResult.getData());
                    String content4Pay = JSON.toJSONString(payOrder);
                    MobileSecurePayer msp = new MobileSecurePayer();
                    msp.pay(content4Pay, mHandler, Constants.RQF_PAY, mActivity, false);
                } else if ("999991".equals(meta.getCode())) {
                    SupportBankMsgResult supportBankMsgResult = JsonUtil.parseObject(result, SupportBankMsgResult.class);
                    Uihelper.showToast(mActivity, supportBankMsgResult.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, money, bankNumber, realName, idCard);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("充值");
    }

    private void showBankLimit() {
        mTitle.setRightText("银行列表");
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(mActivity, Urls.web_support_bank);
            }
        });
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
        begin();
        HttpRequest.rollInResult(mActivity, new ICallback<OrderLianResult>() {
            @Override
            public void onSucceed(OrderLianResult orderLianResult) {
                end();
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
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, orderNo);
    }

    @Override
    protected String getPageName() {
        return "充值";
    }

    //  银行卡4位1空格
    TextWatcher textWatcher = new TextWatcher() {
        int beforeTextLength = 0;
        int onTextLength = 0;
        boolean isChanged = false;

        int location = 0;// 记录光标的位置
        private char[] tempChar;
        private StringBuffer buffer = new StringBuffer();
        int konggeNumberB = 0;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextLength = s.length();
            buffer.append(s.toString());
            if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                isChanged = false;
                return;
            }
            isChanged = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeTextLength = s.length();
            if (buffer.length() > 0) {
                buffer.delete(0, buffer.length());
            }
            konggeNumberB = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    konggeNumberB++;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChanged) {
                location = editBankNumber.getSelectionEnd();
                int index = 0;
                while (index < buffer.length()) {
                    if (buffer.charAt(index) == ' ') {
                        buffer.deleteCharAt(index);
                    } else {
                        index++;
                    }
                }

                index = 0;
                int konggeNumberC = 0;
                while (index < buffer.length()) {
                    if (index % 5 == 4) {
                        buffer.insert(index, ' ');
                        konggeNumberC++;
                    }
                    index++;
                }

                if (konggeNumberC > konggeNumberB) {
                    location += (konggeNumberC - konggeNumberB);
                }

                tempChar = new char[buffer.length()];
                buffer.getChars(0, buffer.length(), tempChar, 0);
                String str = buffer.toString();
                if (location > str.length()) {
                    location = str.length();
                } else if (location < 0) {
                    location = 0;
                }

                editBankNumber.setText(str);
                Editable etable = editBankNumber.getText();
                Selection.setSelection(etable, location);
                isChanged = false;
            }
        }
    };
}
