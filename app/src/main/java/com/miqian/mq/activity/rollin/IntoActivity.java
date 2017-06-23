package com.miqian.mq.activity.rollin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.OrderRecharge;
import com.miqian.mq.entity.OrderRechargeResult;
import com.miqian.mq.entity.PayOrder;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.entity.UserInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.pay.BaseHelper;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Joy on 2015/9/10.
 * 充值页面
 */
public class IntoActivity extends BaseActivity implements View.OnClickListener {

    private Button btRollin;
    private EditText editMoney;
    private TextView bindBankNumber;
    private TextView textLimit;
    private TextView textErrorLian;
    private EditText editCaptcha;
    private Button btnSendCaptcha;

    private UserInfo userInfo;
    private PayOrder payOrder;

    private String money;
    private String bankNumber;
    //    private String realName;
//    private String idCard;
    private int rollType;//为1时传入充值金额，为0时不传
    private int bindStatus;
//    private String relaNameStatus;

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
                } else if ("999991".equals(meta.getCode())) {
                } else if ("996633".equals(meta.getCode())) {
                    Uihelper.showToast(mActivity, meta.getMessage());
                }
                refreshView();
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
                }
                super.handleMessage(msg);
            }
        };
        Intent intent = getIntent();
        rollType = intent.getIntExtra("rollType", 0);
        btRollin = (Button) findViewById(R.id.bt_rollin);
        btRollin.setOnClickListener(this);
        editMoney = (EditText) findViewById(R.id.edit_money);
        editMoney.addTextChangedListener(new MyTextWatcher() {

            @Override
            public void myAfterTextChanged(Editable s) {
                try {
                    textErrorLian.setVisibility(View.GONE);
                    String temp = s.toString();
                    if (temp.matches(FormatUtil.PATTERN_MONEY)) {
                        return;
                    }
                    s.delete(temp.length() - 1, temp.length());
                } catch (Exception e) {
                }
            }
        });
        if (rollType == 1 || rollType == 2) {
            money = intent.getStringExtra("money");
        }
        bindBankNumber = (TextView) findViewById(R.id.bind_bank_number);
        textLimit = (TextView) findViewById(R.id.text_limit);
        textErrorLian = (TextView) findViewById(R.id.text_error_lian);
        editCaptcha = (EditText) findViewById(R.id.et_account_captcha);
        btnSendCaptcha = (Button) findViewById(R.id.btn_send);
        btnSendCaptcha.setOnClickListener(this);
    }

    private void refreshView() {
        btRollin.setEnabled(true);
//        relaNameStatus = userInfo.getRealNameStatus();
//        if ("1".equals(relaNameStatus)) {
//            realName = RSAUtils.decryptByPrivate(userInfo.getUserName());
//            idCard = RSAUtils.decryptByPrivate(userInfo.getIdCard());
//            editName.setText(realName);
//            editCardId.setText(idCard);
//        } else {
//            frameRealName.setVisibility(View.VISIBLE);
//        }
//        if ("1".equals(userInfo.getBindCardStatus()) && "1".equals(userInfo.getSupportStatus())) {
//            bindStatus = 1;
//        }
//        if (bindStatus == 1) {
//            frameRealName.setVisibility(View.GONE);
//            frameBind.setVisibility(View.GONE);
//            frameBound.setVisibility(View.VISIBLE);
        bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankNo());
        bindBankNumber.setText(bankNumber);
//            if (!TextUtils.isEmpty(bankNumber) && bankNumber.length() > 4) {
//                bankNumber = "**** **** **** " + bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
//            }
//            bindBankName.setText(userInfo.getBankName());
//            imageLoader.displayImage(userInfo.getBankUrlSmall(), imageBank, options);
//        } else {
//            frameRealName.setVisibility(View.VISIBLE);
//            frameBind.setVisibility(View.VISIBLE);
//            frameBound.setVisibility(View.GONE);
//            showBankLimit();
//        }
    }

    private void rollIn() {
//        HttpRequest.rollinJx(this, "amt", "bankNo", "captcha", "authCode");
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
//
//        if (bindStatus == 0) {
//            bankNumber = editBankNumber.getText().toString().replaceAll(" ", "");
//            if (TextUtils.isEmpty(bankNumber)) {
//                Uihelper.showToast(mActivity, "卡号不能为空");
//                return;
//            }
//        } else {
//            bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankNo());
//        }
//
//        if ("0".equals(relaNameStatus)) {
//            realName = editName.getText().toString();
//            idCard = editCardId.getText().toString();
//            if (TextUtils.isEmpty(realName)) {
//                Uihelper.showToast(mActivity, "姓名不能为空");
//                return;
//            }
//            if (realName.length() < 2) {
//                Uihelper.showToast(mActivity, "姓名输入有误");
//                return;
//            }
//            if (TextUtils.isEmpty(idCard)) {
//                Uihelper.showToast(mActivity, "身份证号码不能为空");
//                return;
//            }
//
//            if (!idCard.matches(FormatUtil.PATTERN_IDCARD)) {
//                Uihelper.showToast(mActivity, "身份证号码不正确");
//                return;
//            }
//        }
        String captcha = editCaptcha.getText().toString();
        begin();
        HttpRequest.rollIn(mActivity, new ICallback<OrderRechargeResult>() {
            @Override
            public void onSucceed(OrderRechargeResult orderRechargeResult) {
                end();
                OrderRecharge orderRecharge = orderRechargeResult.getData();
                jumpToResult(orderRecharge);
//                Meta meta = JsonUtil.parseObject(result, Meta.class);
//                if ("000000".equals(meta.getCode())) {
////                    PayOrderResult payOrderResult = JsonUtil.parseObject(result, PayOrderResult.class);
////                    payOrder = constructPreCardPayOrder(payOrderResult.getData());
////                    String content4Pay = JSON.toJSONString(payOrder);
////                    MobileSecurePayer msp = new MobileSecurePayer();
////                    msp.pay(content4Pay, mHandler, Constants.RQF_PAY, mActivity, false);
//                } else if ("999991".equals(meta.getCode())) {
////                    SupportBankMsgResult supportBankMsgResult = JsonUtil.parseObject(result, SupportBankMsgResult.class);
////                    Uihelper.showToast(mActivity, supportBankMsgResult.getMessage());
//                } else if ("996633".equals(meta.getCode())) {
////                    Uihelper.showToast(mActivity, meta.getMessage());
//                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, money, bankNumber, captcha);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("在线快捷充值");
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

    public void textLawCick(View v) {
        WebActivity.startActivity(mActivity, Urls.web_recharge_law);
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
            default:
                break;
        }
    }


    private void sendMessage() {
        begin();
        HttpRequest.getCaptcha(this, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
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
        }, userInfo.getMobile(), TypeUtil.CAPTCHA_REGISTER);

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

    public static PayOrder constructPreCardPayOrder(PayOrder payOrder) {
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
            textErrorLian.setVisibility(View.GONE);
            switch (msg.what) {
                case Constants.RQF_PAY:
                    JSONObject objContent = BaseHelper.string2JSON(strRet);
                    String retCode = objContent.optString("ret_code");
                    String retMsg = objContent.optString("ret_msg");
                    String orderNo = payOrder.getNo_order();
                    // //先判断状态码，状态码为 成功或处理中 的需要 验签
                    if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                        String resulPay = objContent.optString("result_pay");
                        if (Constants.RESULT_PAY_SUCCESS.equalsIgnoreCase(resulPay)) {
                            // 支付成功后续处理
                            if (rollType == 1) {
                                backSubscribePage(orderNo);
                            } else {
                                checkOrder(orderNo);
                            }
                        } else {
                            Uihelper.showToast(mActivity, retMsg);
                        }
                    } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                        String resulPay = objContent.optString("result_pay");
                        if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
                            if (rollType == 1) {
                                setResult(CurrentInvestment.PROCESSING);
//                                jumpToResult(CurrentInvestment.PROCESSING, money, orderNo);
                            } else {
                                checkOrder(orderNo);
                            }
                        }
                    } else if (retCode.equals("1006")) {
                        Uihelper.showToast(mActivity, "您已取消当前交易");
                    } else {
                        rollInError(mActivity, orderNo, strRet);
                        textErrorLian.setVisibility(View.VISIBLE);
                        String errorString = showErrorString(mActivity, retCode);
                        if (TextUtils.isEmpty(errorString)) {
                            errorString = retMsg;
                        }
                        textErrorLian.setText(errorString);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public static void rollInError(Context context, String orderNo, String error) {
        HttpRequest.rollInError(context, orderNo, error);
    }

    public static String showErrorString(Context context, String key) {
        String errorData = Pref.getString(Pref.ERROR_LIAN, context, Constants.ERROR_LIAN_DEFAULT);
        if (!TextUtils.isEmpty(errorData)) {
            Map<String, String> userMap = JSON.parseObject(errorData, new TypeReference<Map<String, String>>() {
            });
            return userMap.get(key);
        }
        return null;
    }

    private void checkOrder(String orderNo) {
        begin();
//        HttpRequest.rollInResult(mActivity, new ICallback<OrderLianResult>() {
//            @Override
//            public void onSucceed(OrderLianResult orderLianResult) {
//                end();
//                OrderLian orderLian = orderLianResult.getData();
//                if (orderLianResult.getCode().equals("000000")) {
//                    jumpToResult(CurrentInvestment.SUCCESS, orderLian.getAmt(), orderLian.getOrderNo());
//                } else if (orderLianResult.getCode().equals("100096")) {
//                    jumpToResult(CurrentInvestment.FAIL, orderLian.getAmt(), orderLian.getOrderNo());
//                } else if (orderLianResult.getCode().equals("100097")) {
//                    jumpToResult(CurrentInvestment.PROCESSING, orderLian.getAmt(), orderLian.getOrderNo());
//                } else {
//                    Uihelper.showToast(mActivity, orderLianResult.getMessage());
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                end();
//                Uihelper.showToast(mActivity, error);
//            }
//        }, orderNo);
    }

    /**
     * 返回订单页面认购
     *
     * @param orderNo
     */
    private void backSubscribePage(String orderNo) {
        Intent intent = new Intent();
        intent.putExtra("orderNo", orderNo);
        setResult(CurrentInvestment.SUCCESS, intent);
        IntoActivity.this.finish();
    }

    /**
     * 跳转充值结果
     *
     * @param orderRecharge
     */
    private void jumpToResult(OrderRecharge orderRecharge) {
//        if (rollType == 0 || status == CurrentInvestment.PROCESSING) {
//            Intent intent = new Intent(IntoActivity.this, IntoResultActivity.class);
//            intent.putExtra("status", status);
//            intent.putExtra("money", money);
//            intent.putExtra("orderNo", orderNo);
//            intent.putExtra("tip", orderNo);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent();
//            intent.putExtra("orderNo", orderNo);
//            setResult(status, intent);
//        }

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

    MyTextWatcher textWatcherLian = new MyTextWatcher() {
        @Override
        public void myAfterTextChanged(Editable arg0) {
            textErrorLian.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onDestroy() {
        isTimer = false;
        super.onDestroy();
    }

    //    //  银行卡4位1空格
//    TextWatcher textWatcher = new TextWatcher() {
//        int beforeTextLength = 0;
//        int onTextLength = 0;
//        boolean isChanged = false;
//
//        int location = 0;// 记录光标的位置
//        private char[] tempChar;
//        private StringBuffer buffer = new StringBuffer();
//        int konggeNumberB = 0;
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            onTextLength = s.length();
//            buffer.append(s.toString());
//            if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
//                isChanged = false;
//                return;
//            }
//            isChanged = true;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            beforeTextLength = s.length();
//            if (buffer.length() > 0) {
//                buffer.delete(0, buffer.length());
//            }
//            konggeNumberB = 0;
//            for (int i = 0; i < s.length(); i++) {
//                if (s.charAt(i) == ' ') {
//                    konggeNumberB++;
//                }
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            if (isChanged) {
//                textErrorLian.setVisibility(View.GONE);
////                location = editBankNumber.getSelectionEnd();
//                int index = 0;
//                while (index < buffer.length()) {
//                    if (buffer.charAt(index) == ' ') {
//                        buffer.deleteCharAt(index);
//                    } else {
//                        index++;
//                    }
//                }
//
//                index = 0;
//                int konggeNumberC = 0;
//                while (index < buffer.length()) {
//                    if (index % 5 == 4) {
//                        buffer.insert(index, ' ');
//                        konggeNumberC++;
//                    }
//                    index++;
//                }
//
//                if (konggeNumberC > konggeNumberB) {
//                    location += (konggeNumberC - konggeNumberB);
//                }
//
//                tempChar = new char[buffer.length()];
//                buffer.getChars(0, buffer.length(), tempChar, 0);
//                String str = buffer.toString();
//                if (location > str.length()) {
//                    location = str.length();
//                } else if (location < 0) {
//                    location = 0;
//                }
//
////                editBankNumber.setText(str);
////                Editable etable = editBankNumber.getText();
////                Selection.setSelection(etable, location);
//                isChanged = false;
//            }
//        }
//    };
}
