package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.Redeem;
import com.miqian.mq.entity.RedeemData;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.DialogTradePassword;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;

/**
 * 赎回
 * Created by TuLiangTan on 2015/10/9.
 */
public class ActivityRedeem extends BaseActivity {
    private EditText editMoney;
    private String capital;
    private String totalEaring;
    private UserInfo userInfo;
    private DialogTradePassword dialogTradePassword_set;
    private DialogTradePassword dialogTradePassword_input;
    private String money;

    @Override
    public void obtainData() {
        Intent intent = getIntent();
        capital = intent.getStringExtra("capital");
        totalEaring = intent.getStringExtra("totalEaring");
        if (!TextUtils.isEmpty(capital)) {
            editMoney.setHint("可赎回" + capital + "元");
        }

        mWaitingDialog.show();
        HttpRequest.getUserInfo(mActivity, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                mWaitingDialog.dismiss();
                userInfo = result.getData();

            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
            }
        });
    }

    @Override
    public void initView() {

        editMoney = (EditText) findViewById(R.id.edit_money);
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

        money = editMoney.getText().toString();
        if (!TextUtils.isEmpty(money)) {

            BigDecimal moneyCurrent = new BigDecimal(money);

            if (TextUtils.isEmpty(capital)) {
                return;
            }
            BigDecimal moneyTotal = new BigDecimal(capital);
            if (moneyCurrent.compareTo(moneyTotal) > 0) {
                Uihelper.showToast(mActivity, "超出可赎回的金额");
            } else if (moneyCurrent.compareTo(BigDecimal.ZERO) == 0) {
                Uihelper.showToast(mActivity, "金额不能小于0.01");
            } else {
                if (userInfo.getPayPwdStatus() != null) {
                    int state = Integer.parseInt(userInfo.getPayPwdStatus());
                    initDialogTradePassword(state);
                    if (state == DialogTradePassword.TYPE_SETPASSWORD) {
                        dialogTradePassword_set.show();
                    } else {
                        dialogTradePassword_input.show();
                    }
                }
            }
        } else {
            Uihelper.showToast(mActivity, "金额不能为空");
        }
    }

    private void initDialogTradePassword(int type) {

        if (dialogTradePassword_set == null && type == DialogTradePassword.TYPE_SETPASSWORD) {
            dialogTradePassword_set = new DialogTradePassword(mActivity, DialogTradePassword.TYPE_SETPASSWORD) {

                @Override
                public void positionBtnClick(String password) {

                    if (!TextUtils.isEmpty(password)) {
                        if (password.length() >= 6 && password.length() <= 16) {
                            //设置交易密码
                            mWaitingDialog.show();
                            HttpRequest.setPayPassword(mActivity, new ICallback<Meta>() {
                                @Override
                                public void onSucceed(Meta result) {
                                    dismiss();
                                    mWaitingDialog.show();
                                    Uihelper.showToast(mActivity, "设置成功");
                                }

                                @Override
                                public void onFail(String error) {
                                    dismiss();
                                    mWaitingDialog.show();
                                    Uihelper.showToast(mActivity, error);
                                }
                            }, password, password);

                        } else {
                            Uihelper.showToast(mActivity, R.string.tip_password);
                        }
                    } else {

                        Uihelper.showToast(mActivity, "密码不能为空");

                    }


                }
            };
        } else {
            if (dialogTradePassword_input == null) {

                dialogTradePassword_input = new DialogTradePassword(mActivity, DialogTradePassword.TYPE_INPUTPASSWORD) {

                    @Override
                    public void positionBtnClick(String s) {
                        dismiss();
                        if (!TextUtils.isEmpty(s)) {
                            if (s.length() >= 6 && s.length() <= 16) {

                                //赎回
                                redoom(s);

                            } else {
                                Uihelper.showToast(mActivity, R.string.tip_password_pc);
                            }
                        } else {

                            Uihelper.showToast(mActivity, "密码不能为空");

                        }


                    }
                };
            }
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

                if (code.equals("999993")||code.equals("999988")) {
                    Uihelper.showToast(mActivity, result.getMessage());
                } else {
                    if (code.equals("000000")) {
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

}
