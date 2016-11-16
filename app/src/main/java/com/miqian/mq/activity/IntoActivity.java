package com.miqian.mq.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.activity.user.HfUpdateActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
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
    private EditText editMoneyBound;
    private ImageView imageBank;
//    private EditText editBankNumber;
    private TextView bindBankName;
    private TextView bindBankNumber;
//    private LinearLayout frameRealName;
//    private EditText editName;
//    private EditText editCardId;
    private TextView textLimit;
//    private TextView textErrorLian;
    private LinearLayout frameBind;
    private LinearLayout frameBound;

//    private MyHandler mHandler;
    private UserInfo userInfo;

    private String money;
    private String bankNumber;
//    private String realName;
//    private String idCard;
    private int rollType;//为1时传入充值金额，为0时不传
    private int bindStatus;
//    private String relaNameStatus;

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getUserInfo(mActivity, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                end();
                showContentView();
                userInfo = result.getData();
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
        editMoneyBound = (EditText) findViewById(R.id.edit_money_bound);
        editMoneyBound.addTextChangedListener(new MyTextWatcher() {

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
        if (rollType == 1 || rollType == 2) {
            money = intent.getStringExtra("money");
            editMoney.setText(money);
            editMoney.setEnabled(false);
            editMoneyBound.setText(money);
            editMoneyBound.setEnabled(false);
        }
        imageBank = (ImageView) findViewById(R.id.image_bank);
//        editBankNumber = (EditText) findViewById(R.id.edit_bank_number);
//        editBankNumber.addTextChangedListener(textWatcher);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        bindBankNumber = (TextView) findViewById(R.id.bind_bank_number);
        textLimit = (TextView) findViewById(R.id.text_limit);
//        textErrorLian = (TextView) findViewById(R.id.text_error_lian);

//        frameRealName = (LinearLayout) findViewById(R.id.frame_real_name);
//        editName = (EditText) frameRealName.findViewById(R.id.edit_name);
//        editCardId = (EditText) frameRealName.findViewById(R.id.edit_card_id);
//        editName.addTextChangedListener(textWatcherLian);
//        editCardId.addTextChangedListener(textWatcherLian);
//        mHandler = new MyHandler(this);
        frameBind = (LinearLayout) findViewById(R.id.frame_bind);
        frameBound = (LinearLayout) findViewById(R.id.frame_bound);
    }

    private void refreshView() {
        btRollin.setEnabled(true);

//        relaNameStatus = userInfo.getRealNameStatus();
//        if ("1".equals(relaNameStatus)) {
//            editCardId.setEnabled(false);
//            editName.setEnabled(false);
//            realName = RSAUtils.decryptByPrivate(userInfo.getUserName());
//            idCard = RSAUtils.decryptByPrivate(userInfo.getIdCard());
//            editName.setText(realName);
//            editCardId.setText(idCard);
//        } else {
//            frameRealName.setVisibility(View.VISIBLE);
//        }
        if (userInfo.isBindCardStatus()) {
            bindStatus = 1;
        }
        if (bindStatus == 1) {
//            frameRealName.setVisibility(View.GONE);
            frameBind.setVisibility(View.GONE);
            frameBound.setVisibility(View.VISIBLE);
            bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankCardNo());
            if (!TextUtils.isEmpty(bankNumber) && bankNumber.length() > 4) {
                bankNumber = "**** **** **** " + bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
            }
            bindBankNumber.setText(bankNumber);
            bindBankName.setText(userInfo.getBankName());
            textLimit.setText("单笔限额" + userInfo.getSingleAmtLimit() + "元， 单日限额" + userInfo.getDayAmtLimit() + "元");
            imageLoader.displayImage(userInfo.getBankUrlSmall(), imageBank, options);
        } else {
//            frameRealName.setVisibility(View.VISIBLE);
            frameBind.setVisibility(View.VISIBLE);
            frameBound.setVisibility(View.GONE);
//            showBankLimit();
        }
    }

    private void rollIn() {
        if (rollType == 0) {
            if (bindStatus == 0) {
                money = editMoney.getText().toString();
            } else {
                money = editMoneyBound.getText().toString();
            }
            if (TextUtils.isEmpty(money)) {
                Uihelper.showToast(mActivity, "转入金额不能为空");
                return;
            }
            BigDecimal tempMoney = new BigDecimal(money);
            BigDecimal minMoney = BigDecimal.ZERO;
            if (!TextUtils.isEmpty(userInfo.getAddRechargeMinValue())) {
                minMoney = new BigDecimal(userInfo.getAddRechargeMinValue());
            }
            if (minMoney.compareTo(tempMoney) > 0) {
                Uihelper.showToast(mActivity, "转入金额不能为小于" + minMoney + "元");
                return;
            }
        }
        HttpRequest.rollinHf(mActivity, RSAUtils.decryptByPrivate(userInfo.getHfCustId()), money);

//        if (bindStatus == 0) {
//            bankNumber = editBankNumber.getText().toString().replaceAll(" ", "");
//            if (TextUtils.isEmpty(bankNumber)) {
//                Uihelper.showToast(mActivity, "卡号不能为空");
//                return;
//            }
//        } else {
//            bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankCardNo());
//        }

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
//        begin();

//        HttpRequest.rollIn(mActivity, new ICallback<String>() {
//            @Override
//            public void onSucceed(String result) {
//                end();
//                Meta meta = JsonUtil.parseObject(result, Meta.class);
//                if ("000000".equals(meta.getCode())) {
//                    PayOrderResult payOrderResult = JsonUtil.parseObject(result, PayOrderResult.class);
//                    payOrder = constructPreCardPayOrder(payOrderResult.getData());
//                    String content4Pay = JSON.toJSONString(payOrder);
//                    MobileSecurePayer msp = new MobileSecurePayer();
//                    msp.pay(content4Pay, mHandler, Constants.RQF_PAY, mActivity, false);
//                } else if ("999991".equals(meta.getCode())) {
//                    SupportBankMsgResult supportBankMsgResult = JsonUtil.parseObject(result, SupportBankMsgResult.class);
//                    Uihelper.showToast(mActivity, supportBankMsgResult.getMessage());
//                } else if ("996633".equals(meta.getCode())) {
//                    Uihelper.showToast(mActivity, meta.getMessage());
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                end();
//                Uihelper.showToast(mActivity, error);
//            }
//        }, money, bankNumber, realName, idCard);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("充值");
    }

//    private void showBankLimit() {
//        mTitle.setRightText("银行列表");
//        mTitle.setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WebActivity.startActivity(mActivity, Urls.web_support_bank);
//            }
//        });
//    }

    public void textLawCick(View v) {
        WebActivity.startActivity(mActivity, Urls.web_recharge_law);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HfUpdateActivity.REQUEST_CODE_ROLLIN) {
            if (resultCode == CurrentInvestment.SUCCESS) {
                finish();
            } else {
                Uihelper.showToast(mActivity, "充值失败");
            }
        }
    }

    @Override
    protected String getPageName() {
        return "充值";
    }
}
