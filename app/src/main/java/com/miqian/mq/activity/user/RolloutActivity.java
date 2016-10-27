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
import com.miqian.mq.encrypt.RSAUtils;
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
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.TextViewEx;
import com.miqian.mq.views.WFYTitle;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Joy on 2015/9/22.
 */
public class RolloutActivity extends BaseActivity {
    private UserInfo userInfo;
    private TextView bindBankId;
    private TextView  bindBankName;
    private EditText editMoney;
    private String    moneyString;
    private String     cardNum;
    private String     totalMoney;
    private CustomDialog dialogTips, dialogTipsReput;
    private View btnRollout;
    private BigDecimal mLimitLowestMoney = BigDecimal.TEN;  //最低提现金额，默认10元
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
        handleData();
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
                            tip.append("；").append(item.getName()).append(item.getFeeAmt()).append("元");
                        }
                    }
                    if (!TextUtils.isEmpty(tip)) {
                        tip.deleteCharAt(0);
                        initTipDialog(1);
                        dialogTipsReput.setRemarks(tip.toString());
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
                }
//                //交易密码错误4次提示框
//                else if (resultCode.equals("999992")) {
//                    showPwdError4Dialog(result.getMessage());
//                }   删除交易密码操作
                else {
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


    private void rollOutHttp() {
//        if (userInfo.getPayPwdStatus() != null) {
//            int state = Integer.parseInt(userInfo.getPayPwdStatus());
//            initDialogTradePassword(state);
//        }    删除交易密码操作

    }
}