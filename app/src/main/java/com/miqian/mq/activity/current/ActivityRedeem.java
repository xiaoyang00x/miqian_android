package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.setting.SetPasswordActivity;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.Redeem;
import com.miqian.mq.entity.RedeemData;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.TypeUtil;
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
    private UserInfo userInfo;
    private DialogTradePassword dialogTradePassword_input;
    private String money;
    private Button btnRollout;

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getUserInfo(mActivity, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                end();
                userInfo = result.getData();
                if (userInfo != null) {
                    capital = userInfo.getCanRedeem();
                    if (!TextUtils.isEmpty(capital)) {
                        editMoney.setHint("可赎回" + capital + "元");
                        btnRollout.setEnabled(true);
                    }
                }
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

            if (TextUtils.isEmpty(capital)) {
                return;
            }
            BigDecimal moneyTotal = new BigDecimal(capital);
            if (moneyCurrent.compareTo(moneyTotal) > 0) {
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

        if ( type == DialogTradePassword.TYPE_SETPASSWORD) {

            Intent intent = new Intent(mActivity, SetPasswordActivity.class);
            intent.putExtra("type", TypeUtil.TRADEPASSWORD_FIRST_SETTING);
            startActivityForResult(intent, 0);
            
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TypeUtil.TRADEPASSWORD_SETTING_SUCCESS) {
            userInfo.setPayPwdStatus("1");
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
