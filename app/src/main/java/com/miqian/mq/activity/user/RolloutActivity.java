package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.TradePsCaptchaActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.setting.BankBranchActivity;
import com.miqian.mq.activity.setting.CityListActivity;
import com.miqian.mq.activity.setting.SetPasswordActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.RollOut;
import com.miqian.mq.entity.RollOutResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.entity.WithDrawResult;
import com.miqian.mq.entity.WithdrawItem;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.DialogTradePassword;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Joy on 2015/9/22.
 */
public class RolloutActivity extends BaseActivity {
    private UserInfo userInfo;
    private TextView
            bindBankId,
            bindBankName,
            textBranch,
            tv_bank_province;
    private View frame_bindbranch,
            frame_bank_branch,
            frame_bank_province;
    private EditText editMoney;
    private String bankOpenName,
            city,
            province,
            moneyString,
            cardNum,
            totalMoney;
    private CustomDialog dialogTips, dialogTipsReput;
    private DialogTradePassword dialogTradePassword_input;
    private boolean isChooseCity;
    private BankCard bankCard;
    private boolean isSuccessBindBranch;
    private View btnRollout;
    private BigDecimal mLimitLowestMoney = BigDecimal.TEN;  //最低提现金额，默认10元
    private TextView tvTips;

    @Override
    public void onCreate(Bundle arg0) {
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getUserBankCard(mActivity, new ICallback<BankCardResult>() {
            @Override
            public void onSucceed(BankCardResult result) {
                end();
                btnRollout.setEnabled(true);
                bankCard = result.getData();
                if (bankCard != null) {
                    bankOpenName = bankCard.getBankOpenName();
                    String limitLowestMoney = bankCard.getWithdrawLimitLowestAmt();
                    if (!TextUtils.isEmpty(limitLowestMoney)) {
                        mLimitLowestMoney = new BigDecimal(limitLowestMoney);
                    }
                    String limitTips = bankCard.getWithdrawLimitPrompt();
                    limitTips = limitTips.replace("|n", "\n");
                    tvTips.setText(limitTips);
                }
                initBindBranchView();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        });

    }

    @Override
    public void initView() {
        findView();
        initBindView();

    }

    private void initBindBranchView() {
        if (TextUtils.isEmpty(bankOpenName)) {
            frame_bindbranch.setVisibility(View.VISIBLE);
            frame_bank_province.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_city = new Intent(mActivity, CityListActivity.class);
                    startActivityForResult(intent_city, 0);
                }
            });

            frame_bank_branch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isChooseCity) {
                        Intent intent_branch = new Intent(mActivity, BankBranchActivity.class);
                        intent_branch.putExtra("city", city);
                        intent_branch.putExtra("province", province);
                        intent_branch.putExtra("bankcode", bankCard.getBankCode());
                        intent_branch.putExtra("fromsetting", false);
                        intent_branch.putExtra("bankName", userInfo.getBankName());
                        intent_branch.putExtra("bankCard", cardNum);
                        startActivityForResult(intent_branch, 0);

                    } else {
                        Uihelper.showToast(mActivity, "请先选择城市");
                    }

                }
            });

        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == 2) {
            isSuccessBindBranch = true;
            String branch = data.getStringExtra("branchbank");
            if (!TextUtils.isEmpty(branch)) {
                textBranch.setText(branch);
            }

        } else if (resultCode == 0) {
            isChooseCity = true;
            city = data.getStringExtra("city");
            province = data.getStringExtra("province");
            if (!TextUtils.isEmpty(city)) {
                tv_bank_province.setText(city);
            }
            //设置交易密码成功
        } else if (resultCode == TypeUtil.TRADEPASSWORD_SETTING_SUCCESS) {
            userInfo.setPayPwdStatus("1");
            rollOutHttp();
        }
    }

    private void initBindView() {
        if (userInfo == null) {
            return;
        }
        if (!TextUtils.isEmpty(userInfo.getBankNo())) {
            cardNum = RSAUtils.decryptByPrivate(userInfo.getBankNo());
            bindBankId.setText("**** **** **** " + cardNum.substring(cardNum.length() - 4, cardNum.length()));
        }
        if (!TextUtils.isEmpty(userInfo.getBankName())) {
            bindBankName.setText(userInfo.getBankName());
        }

        if (!TextUtils.isEmpty(userInfo.getBalance())) {
            totalMoney = userInfo.getBalance();
            editMoney.setHint("本次最多可提现" + totalMoney + "元");
        }

    }

    private void findView() {
        bindBankId = (TextView) findViewById(R.id.bind_bank_number);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        textBranch = (TextView) findViewById(R.id.tv_bank_branch);
        tv_bank_province = (TextView) findViewById(R.id.tv_bank_province);
        tvTips = (TextView) findViewById(R.id.tv_rollout_tip);


        frame_bindbranch = findViewById(R.id.frame_bindbranch);
        frame_bank_province = findViewById(R.id.frame_bank_province);
        frame_bank_branch = findViewById(R.id.frame_bank_branch);

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
                } catch (Exception e) {
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
    protected String getPageName() {
        return "提现";
    }

    public void btn_click(View v) {
        moneyString = editMoney.getText().toString();
        if (TextUtils.isEmpty(moneyString)) {
            Uihelper.showToast(mActivity, "交易金额不能为空");
            return;
        }
        if (TextUtils.isEmpty(bankOpenName) && !isSuccessBindBranch) {
            Uihelper.showToast(mActivity, "未绑定支行");
        } else {
            handleData();
        }
    }

    private void handleData() {
        BigDecimal moneyCurrent = new BigDecimal(moneyString);
        BigDecimal moneyTotal = new BigDecimal(totalMoney);
        if (moneyCurrent.compareTo(moneyTotal) > 0) {
            initTipDialog(0);
            dialogTips.setRemarks("转出金额超限");
            dialogTips.show();
            return;
        } else if (moneyCurrent.compareTo(mLimitLowestMoney) < 0) {
            initTipDialog(0);
            dialogTips.setRemarks("转出金额不能小于" + mLimitLowestMoney + "元");
            dialogTips.show();
            return;
        } else {
            //提现预处理
            HttpRequest.withdrawPreprocess(mActivity, new ICallback<WithDrawResult>() {
                @Override
                public void onSucceed(WithDrawResult result) {
                    List<WithdrawItem> data = result.getData();
                    StringBuilder tip = new StringBuilder();
                    for (WithdrawItem item : data) {
                        if (!"0".equals(item.getFeeAmt())) {
                            tip.append("；" + item.getName()).append(item.getFeeAmt() + "元");
                        }
                    }
                    if (!TextUtils.isEmpty(tip)) {
                        tip.deleteCharAt(0);
                        initTipDialog(1);
                        dialogTipsReput.setNegative(View.VISIBLE);
                        dialogTipsReput.setRemarks(tip.toString());
                        dialogTipsReput.setNegative("取消");
                        dialogTipsReput.show();
                    } else {
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


    public void initTipDialog(int code) {

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

    private void initDialogTradePassword(int type) {

        if (type == DialogTradePassword.TYPE_SETPASSWORD) {

            Intent intent = new Intent(mActivity, SetPasswordActivity.class);
            intent.putExtra("type", TypeUtil.TRADEPASSWORD_FIRST_SETTING);
            startActivityForResult(intent, 0);
            Uihelper.showToast(mActivity, "保障交易安全，请先设置交易密码");

        } else {
            if (dialogTradePassword_input == null) {
                dialogTradePassword_input = new DialogTradePassword(mActivity, DialogTradePassword.TYPE_INPUTPASSWORD) {
                    @Override
                    public void positionBtnClick(String s) {
                        dismiss();
                        //提现
                        rollOut(s);
                    }
                };
            }
            dialogTradePassword_input.show();
        }
    }

    private void rollOut(String password) {
        mWaitingDialog.show();
        HttpRequest.withdrawCash(mActivity, new ICallback<RollOutResult>() {
            @Override
            public void onSucceed(RollOutResult result) {
                mWaitingDialog.dismiss();
                String resultCode = result.getCode();
                //999999为失败
                if (resultCode.equals("999999")) {
                    RollOut rollOut = new RollOut();
                    rollOut.setBankName(userInfo.getBankName());
                    if (!TextUtils.isEmpty(cardNum)) {
                        rollOut.setCardNum(cardNum);
                    }
                    rollOut.setMoneyOrder(moneyString);
                    rollOut.setState("0");//失败
                    Intent intent = new Intent(mActivity, RollOutResultActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("rollOutResult", rollOut);
                    intent.putExtras(extra);
                    startActivity(intent);
                    finish();
                } else if (resultCode.equals("000000")) {
                    RollOut rollOut = result.getData();
                    if (rollOut == null) {
                        return;
                    }
                    rollOut.setBankName(userInfo.getBankName());
                    if (!TextUtils.isEmpty(cardNum)) {
                        rollOut.setCardNum(cardNum);
                    }
                    rollOut.setState("1");//成功
                    Intent intent = new Intent(mActivity, RollOutResultActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("rollOutResult", rollOut);
                    intent.putExtras(extra);
                    startActivity(intent);
                    finish();
                }//交易密码错误4次提示框
                else if (resultCode.equals("999992")) {
                    showPwdError4Dialog(result.getMessage());
                } else {
                    Uihelper.showToast(mActivity, result.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, moneyString, userInfo.getBankCode(), cardNum, password);


    }

    private void showPwdError4Dialog(String message) {

        if (dialogTips == null) {
            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {
                @Override
                public void positionBtnClick() {
                    MobclickAgent.onEvent(mActivity, "1028");
                    Intent intent = new Intent(mActivity, TradePsCaptchaActivity.class);
                    intent.putExtra("realNameStatus", userInfo.getRealNameStatus());
                    startActivity(intent);
                    dismiss();
                }

                @Override
                public void negativeBtnClick() {
                    rollOutHttp();
                }
            };
            dialogTips.setNegative(View.VISIBLE);
            dialogTips.setRemarks(message);
            dialogTips.setNegative("继续尝试");
            dialogTips.setPositive("找回密码");
            dialogTips.setTitle("交易密码错误");
            dialogTips.setCanceledOnTouchOutside(false);
        }
        dialogTips.show();

    }

    private void rollOutHttp() {
        if (userInfo.getPayPwdStatus() != null) {
            int state = Integer.parseInt(userInfo.getPayPwdStatus());
            initDialogTradePassword(state);
        }

    }
}