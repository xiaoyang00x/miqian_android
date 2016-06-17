package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.TradePsCaptchaActivity;
import com.miqian.mq.activity.setting.SetPasswordActivity;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.Redeem;
import com.miqian.mq.entity.RedeemData;
import com.miqian.mq.entity.UserCurrent;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.DialogTradePassword;
import com.miqian.mq.views.TextViewEx;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 赎回
 * Created by TuLiangTan on 2015/10/9.
 */
public class ActivityRedeem extends BaseActivity {
    private EditText editMoney;
    private UserInfo userInfo;
    private DialogTradePassword dialogTradePassword_input;
    private String money;
    private Button btnRollout;
    private CustomDialog dialogTips;
    private TextViewEx tvTip;
    private TextViewEx tvExtra;
    private UserCurrent userCurrent;
    private BigDecimal resideMoney;//可赎回金额

    @Override
    public void onCreate(Bundle arg0) {
        userCurrent = (UserCurrent) getIntent().getExtras().getSerializable("userCurrent");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        if (userCurrent != null) {
            BigDecimal balance = userCurrent.getBalance();//活期余额
            BigDecimal curDayResidue = userCurrent.getCurDayResidue();//当日剩余可赎回额度
            if (curDayResidue.compareTo(balance) > 0) {
                resideMoney = balance;
            } else {
                resideMoney = curDayResidue;
            }
            editMoney.setHint("可赎回" + resideMoney + "元");
            btnRollout.setEnabled(true);
            BigDecimal curMonthAmt = userCurrent.getCurMonthAmt();//本月已赎回的金额
            BigDecimal lmtMonthAmt = userCurrent.getLmtMonthAmt();//本月限制赎回额度
            BigDecimal curResidue = lmtMonthAmt.subtract(curMonthAmt);//剩余可赎回额度

            DecimalFormat df = new java.text.DecimalFormat("#.00");
            String textCurResidue = df.format(curResidue);
            String textCurMonthAmt = df.format(curMonthAmt);
            if (textCurResidue.equals(".00")) {
                textCurResidue = "0";
            }
            if (textCurMonthAmt.equals(".00")) {
                textCurMonthAmt = "0";
            }

            tvExtra.setText("您剩余可赎回额度" + textCurResidue + "元" + "(本月已经赎回" + textCurMonthAmt + "元)");
            String tip = userCurrent.getWarmPrompt();//温馨提示
            if (!TextUtils.isEmpty(tip)) {
                String limitTips = tip.replace("|n", "\n");
                tvTip.setText(limitTips, true);
            }
        }
        begin();
        HttpRequest.getUserInfo(mActivity, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                end();
                userInfo = result.getData();
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
        editMoney = (EditText) findViewById(R.id.edit_money);
        btnRollout = (Button) findViewById(R.id.bt_redeem);
        tvExtra = (TextViewEx) findViewById(R.id.tv_extra);
        tvTip = (TextViewEx) findViewById(R.id.tv_tip);
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

        if (userInfo == null) {
            return;
        }
        money = editMoney.getText().toString();
        if (!TextUtils.isEmpty(money)) {
            BigDecimal moneyCurrent = new BigDecimal(money);
            if (moneyCurrent.compareTo(resideMoney) > 0) {
                Uihelper.showToast(mActivity, "超出可赎回的金额");
            } else if (moneyCurrent.compareTo(BigDecimal.ZERO) == 0) {
                Uihelper.showToast(mActivity, "金额不能小于0.01");
            } else {
                if (!TextUtils.isEmpty(userInfo.getPayPwdStatus())) {
                    int state = Integer.parseInt(userInfo.getPayPwdStatus());
                    initDialogTradePassword(state);
                }
            }
        } else {
            Uihelper.showToast(mActivity, "金额不能为空");
        }
    }

    private void initDialogTradePassword(int type) {

        if (type == DialogTradePassword.TYPE_SETPASSWORD) {

            Intent intent = new Intent(ActivityRedeem.this, SetPasswordActivity.class);
            intent.putExtra("type", TypeUtil.TRADEPASSWORD_FIRST_SETTING);
            startActivityForResult(intent, 0);
            Uihelper.showToast(mActivity, "保障交易安全，请先设置交易密码");

        } else {
            if (dialogTradePassword_input == null) {
                dialogTradePassword_input = new DialogTradePassword(mActivity, DialogTradePassword.TYPE_INPUTPASSWORD) {

                    @Override
                    public void positionBtnClick(String s) {
                        dismiss();

                        //赎回
                        redoom(s);

                    }
                };
            }
            dialogTradePassword_input.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //设置交易密码成功
        if (resultCode == TypeUtil.TRADEPASSWORD_SETTING_SUCCESS) {
            userInfo.setPayPwdStatus("1");
            initDialogTradePassword(DialogTradePassword.TYPE_INPUTPASSWORD);
        }
    }

    private void redoom(String password) {
        mWaitingDialog.show();
        HttpRequest.redeem(mActivity, new ICallback<RedeemData>() {
            @Override
            public void onSucceed(RedeemData result) {
                mWaitingDialog.dismiss();
                String code = result.getCode();

                Intent intent = new Intent(mActivity, RedeemResult.class);

                if (code.equals("999993") || code.equals("999988") || code.equals("996633")) {
                    Uihelper.showToast(mActivity, result.getMessage());
                }//交易密码错误4次提示框
                else if (code.equals("999992")) {
                    showPwdError4Dialog(result.getMessage());
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
            }
        }, money, password);

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
                    initDialogTradePassword(DialogTradePassword.TYPE_INPUTPASSWORD);
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

}