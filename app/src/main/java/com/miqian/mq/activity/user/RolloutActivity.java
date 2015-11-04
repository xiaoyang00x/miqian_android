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
import com.miqian.mq.activity.setting.BankBranchActivity;
import com.miqian.mq.activity.setting.BindCardActivity;
import com.miqian.mq.activity.setting.CityListActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.Meta;
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
import com.miqian.mq.views.DialogTradePassword;
import com.miqian.mq.views.WFYTitle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
            textTotalMoney,
            tv_arrivetime,
            tv_bank_province;
    private ImageView iconBank;
    private View frame_bindbranch,
            frame_bank_branch,
            frame_bank_province;
    private EditText editMoney;
    private String bankOpenName,
            city,
            province,
            branch,
            moneyString,
            cardNum,
            totalMoney;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private CustomDialog dialogTips, dialogTipsReput;
    private DialogTradePassword dialogTradePassword_set;
    private DialogTradePassword dialogTradePassword_input;
    private boolean isChooseCity;
    private BankCard bankCard;
    private boolean isSuccessBindBranch;

    @Override
    public void obtainData() {

        HttpRequest.getUserBankCard(mActivity, new ICallback<BankCardResult>() {
            @Override
            public void onSucceed(BankCardResult result) {
                bankCard = result.getData();
                if (bankCard != null) {
                    bankOpenName = bankCard.getBankOpenName();
                }
                initBindBranchView();
            }

            @Override
            public void onFail(String error) {

            }
        });

    }

    @Override
    public void initView() {

        findView();

        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        if (userInfo == null) {
            return;
        }

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
        }

    }

    private void initBindView() {
        String bindCardStatus = userInfo.bindCardStatus;
        //未绑定
        if ("0".equals(bindCardStatus)) {
            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {

                @Override
                public void positionBtnClick() {
                    //跳到绑定银行卡的页面
                    dismiss();
                    Intent intent_bind = new Intent(mActivity, BindCardActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("userInfo", userInfo);
                    intent_bind.putExtras(extra);
                    startActivity(intent_bind);
                    finish();
                }

                @Override
                public void negativeBtnClick() {
                    dismiss();
                    finish();
                }
            };
            dialogTips.setNegative(View.VISIBLE);
            dialogTips.setRemarks("    您尚未绑定银行卡，请先绑定银行卡");
            dialogTips.setNegative("取消");
            dialogTips.show();
            dialogTips.setCancelable(false);
        } else {
            if (!TextUtils.isEmpty(userInfo.getBankNo())) {
                cardNum = RSAUtils.decryptByPrivate(userInfo.getBankNo());
                bindBankId.setText("**** **** **** " + cardNum.substring(cardNum.length() - 4, cardNum.length()));
            }
            if (!TextUtils.isEmpty(userInfo.getBankName())) {
                bindBankName.setText(userInfo.getBankName());
            }

            if (!TextUtils.isEmpty(userInfo.getBalance())) {
                totalMoney = userInfo.getBalance();
                textTotalMoney.setText(totalMoney + "");
                editMoney.setHint("最多可转" + totalMoney + "元");
            }

            if (!TextUtils.isEmpty(userInfo.getBankUrlSmall())) {
                imageLoader.displayImage(userInfo.getBankUrlSmall(), iconBank, options);
            }
        }

    }

    private void findView() {
        bindBankId = (TextView) findViewById(R.id.bind_bank_id);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        textBranch = (TextView) findViewById(R.id.tv_bank_branch);
        tv_bank_province = (TextView) findViewById(R.id.tv_bank_province);
        tv_arrivetime = (TextView) findViewById(R.id.tv_arrivetime);
        iconBank = (ImageView) findViewById(R.id.icon_bank);


        frame_bindbranch = findViewById(R.id.frame_bindbranch);
        frame_bank_province = findViewById(R.id.frame_bank_province);
        frame_bank_branch = findViewById(R.id.frame_bank_branch);


        textTotalMoney = (TextView) findViewById(R.id.total_money);
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

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(0)).build();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rollout;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("提现");
        mTitle.setRightText("转出说明");
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
        } else if (moneyCurrent.compareTo(BigDecimal.TEN) < 0) {
            initTipDialog(0);
            dialogTips.setRemarks("转出金额不能小于10元");
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

        if (dialogTradePassword_set == null && type == DialogTradePassword.TYPE_SETPASSWORD) {
            dialogTradePassword_set = new DialogTradePassword(mActivity, DialogTradePassword.TYPE_SETPASSWORD) {

                @Override
                public void positionBtnClick(String password) {

                    //设置交易密码
                    mWaitingDialog.show();
                    HttpRequest.setPayPassword(mActivity, new ICallback<Meta>() {
                        @Override
                        public void onSucceed(Meta result) {
                            mWaitingDialog.dismiss();
                            dismiss();//设置状态
                            userInfo.setPayPwdStatus("1");
                            Uihelper.showToast(mActivity, "设置成功");
                            rollOutHttp();

                        }

                        @Override
                        public void onFail(String error) {
                            mWaitingDialog.dismiss();
                            dismiss();
                            Uihelper.showToast(mActivity, error);
                        }
                    }, password, password);


                }
            };
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
        }
    }

    private void rollOut(String password) {
        mWaitingDialog.show();
        HttpRequest.withdrawCash(mActivity, new ICallback<RollOutResult>() {
            @Override
            public void onSucceed(RollOutResult result) {
                mWaitingDialog.dismiss();
                RollOut rollOut = result.getData();
                if (rollOut == null) {
                    return;
                }
                rollOut.setBankName(userInfo.getBankName());
                if (!TextUtils.isEmpty(cardNum)) {
                    rollOut.setCardNum(cardNum);
                }
                Intent intent = new Intent(mActivity, RollOutResultActivity.class);
                Bundle extra = new Bundle();
                extra.putSerializable("rollOutResult", rollOut);
                intent.putExtras(extra);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, moneyString, userInfo.getBankCode(), cardNum, password);


    }


    private void rollOutHttp() {
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
}