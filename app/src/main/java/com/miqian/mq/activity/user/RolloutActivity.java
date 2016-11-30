package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.entity.WithDrawResult;
import com.miqian.mq.entity.WithdrawItem;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.TextViewEx;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;

/**
 * Created by Joy on 2015/9/22.
 */
public class RolloutActivity extends BaseActivity {
    private UserInfo userInfo;
    private TextView bindBankId;
    private TextView bindBankName;
    private EditText editMoney;
    private String moneyString;
    private String cardNum;
    private String totalMoney;
    private CustomDialog dialogTips, dialogTipsReput;
    private View btnRollout;
    private BigDecimal mLimitLowestMoney = BigDecimal.ONE;  //最低提现金额，默认1元
    private TextViewEx tvTips;
    private ImageView imageBank;

    @Override
    public void onCreate(Bundle arg0) {
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        tvTips.setText(getResources().getString(R.string.rollout_rule), true);

    }

    @Override
    public void initView() {
        findView();
        initBindView();

    }

    private void initBindView() {
        if (userInfo == null) {
            return;
        }
        if (!TextUtils.isEmpty(userInfo.getBankCardNo())) {
            cardNum = RSAUtils.decryptByPrivate(userInfo.getBankCardNo());
            bindBankId.setText("**** **** **** " + cardNum.substring(cardNum.length() - 4, cardNum.length()));
        }
        if (!TextUtils.isEmpty(userInfo.getBankName())) {
            bindBankName.setText(userInfo.getBankName());
        }
        imageLoader.displayImage(userInfo.getBankUrlSmall(), imageBank, options);

        if (!TextUtils.isEmpty(userInfo.getUsableSa())) {
            totalMoney = userInfo.getUsableSa();
            editMoney.setHint("本次最多可提现" + totalMoney + "元");
        }

    }

    private void findView() {
        bindBankId = (TextView) findViewById(R.id.bind_bank_number);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        tvTips = (TextViewEx) findViewById(R.id.tv_rollout_tip);
        imageBank = (ImageView) findViewById(R.id.image_bank);
        btnRollout = findViewById(R.id.bt_rollout);

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
                } catch (Exception ignored) {
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rollout;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("提现");
        mTitle.setRightText("提现说明");
        mTitle.setOnRightClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WebActivity.startActivity(mActivity, Urls.web_rollout);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HfUpdateActivity.REQUEST_CODE_ROLLOUT) {
            if (resultCode == CurrentInvestment.SUCCESS) {
                finish();
            } else {
                Uihelper.showToast(mActivity, "提现失败");
            }
        }
    }

    @Override
    protected String getPageName() {
        return "提现";
    }

    public void btn_click(View v) {
        moneyString = editMoney.getText().toString();
        if (TextUtils.isEmpty(moneyString)) {
            Uihelper.showToast(mActivity, "交易金额不能为空");
            return;
        }
        handleData();
    }

    private void handleData() {
        BigDecimal moneyCurrent = new BigDecimal(moneyString);
        BigDecimal moneyTotal = new BigDecimal(totalMoney);
        if (moneyCurrent.compareTo(moneyTotal) > 0) {
            initTipDialog(0);
            dialogTips.setRemarks("转出金额超限");
            dialogTips.show();
        } else if (moneyCurrent.compareTo(mLimitLowestMoney) < 0) {
            initTipDialog(0);
            dialogTips.setRemarks("转出金额不能小于" + mLimitLowestMoney + "元");
            dialogTips.show();
        } else {
            //提现预处理
            HttpRequest.withdrawPreprocess(mActivity, new ICallback<WithDrawResult>() {
                @Override
                public void onSucceed(WithDrawResult result) {
                    WithdrawItem data = result.getData();
                    StringBuilder tip = new StringBuilder();
                    String feeAmt = data.getFeeAmt();
                    BigDecimal bdFeeamt=null;
                    if(!TextUtils.isEmpty(feeAmt)){
                        bdFeeamt=new BigDecimal(feeAmt);
                        if (bdFeeamt.compareTo(BigDecimal.ZERO)>0){
                            tip.append("提现手续费").append(feeAmt).append("元");
                            initTipDialog(1);
                            dialogTipsReput.setRemarks(tip.toString());
                            dialogTipsReput.show();
                        }else {
                            rollOutHttp();
                        }
                    }else {
                        rollOutHttp();
                    }
                }

                @Override
                public void onFail(String error) {
                    Uihelper.showToast(mActivity, error);

                }
            }, moneyString);
        }
    }


    private void initTipDialog(int code) {

        if (code == 0 && dialogTips == null) {
            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {

                @Override
                public void positionBtnClick() {
                    dismiss();
                }

                @Override
                public void negativeBtnClick() {

                }
            };
        } else {
            dialogTipsReput = new CustomDialog(this, CustomDialog.CODE_TIPS) {

                @Override
                public void positionBtnClick() {
                    rollOutHttp();
                    dismiss();
                }

                @Override
                public void negativeBtnClick() {

                }
            };
        }
    }

    private void rollOutHttp() {
        HttpRequest.rolloutHf(mActivity, moneyString);

    }
}