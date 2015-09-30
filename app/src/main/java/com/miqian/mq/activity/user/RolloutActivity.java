package com.miqian.mq.activity.user;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.setting.BankBranchActivity;
import com.miqian.mq.activity.setting.CityListActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.DialogTradePassword;
import com.miqian.mq.views.WFYTitle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Joy on 2015/9/22.
 */
public class RolloutActivity extends BaseActivity {
    private UserInfo userInfo;
    private TextView bindBankId, bindBankName, textBranch, textTotalMoney, tv_arrivetime, tv_bank_province;
    private ImageView iconBank;
    private View frame_bindbranch, frame_bank_branch, frame_bank_province;
    private EditText editMoney;
    private String bankOpenName, city, province, branch;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    // private CustomDialog dialogPassword;
    private CustomDialog dialogTips;
    private CustomDialog dialogTipsReput;
    private String totalMoney;
    private DialogTradePassword dialogTradePassword_set;
    private DialogTradePassword dialogTradePassword_input;
    private String moneyString;
    private String cardNum;

    @Override
    public void obtainData() {

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
        initBindBranchView();

    }

    private void initBindBranchView() {
        bankOpenName = "";
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
                    Intent intent_branch = new Intent(mActivity, BankBranchActivity.class);
                    startActivityForResult(intent_branch, 0);

                }
            });


        } else {
            frame_bindbranch.setVisibility(View.GONE);
        }


//        HttpRequest.getUserBankCard(mActivity, new ICallback<BankCardResult>() {
//            @Override
//            public void onSucceed(BankCardResult result) {
//                BankCard bankCard = result.getData();
//                bankCard.getBankOpenName();
//            }
//
//            @Override
//            public void onFail(String error) {
//
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == 1) {
            branch = data.getStringExtra("branch");
            if (!TextUtils.isEmpty(branch)) {
                textBranch.setText(branch);
            }

        } else if (resultCode == 0) {
            city = data.getStringExtra("city");
            province = data.getStringExtra("province");
            if (!TextUtils.isEmpty(city)) {
                tv_bank_province.setText(city);
            }
        }
         if(branch==null||city==null||province==null||cardNum==null){
             return;
         }
        //绑定银行卡
        HttpRequest.bindBank(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {

            }

            @Override
            public void onFail(String error) {
                Uihelper.showToast(mActivity, error);

            }
        }, cardNum, "XG", userInfo.getBankCode(), userInfo.getBankName(), branch,province, city);

    }

    private void initBindView() {
        String bindCardStatus = userInfo.bindCardStatus;
        //未绑定
        if ("0".equals(bindCardStatus)) {
            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {

                @Override
                public void positionBtnClick() {
                    //跳到绑定支行的页面
                    dismiss();
                    Uihelper.showToast(mActivity, "跳到绑定支行的页面");
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
            if (!TextUtils.isEmpty(userInfo.getBankCardNo())) {
                cardNum = RSAUtils.decryptByPrivate(userInfo.getBankCardNo());
                bindBankId.setText("**** **** **** " + cardNum.substring(cardNum.length() - 4, cardNum.length()));
            }
            if (!TextUtils.isEmpty(userInfo.getBankName())) ;
            bindBankName.setText(userInfo.getBankName());

            if (!TextUtils.isEmpty(userInfo.getBalance())) ;
            totalMoney = userInfo.getBalance();
            textTotalMoney.setText(totalMoney + "");
            editMoney.setHint("最多可转" + totalMoney + "元");

            if (!TextUtils.isEmpty(userInfo.getBankUrlSmall())) ;
            imageLoader.displayImage(userInfo.getBankUrlSmall(), iconBank, options);
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
//                WebViewActivity.doIntent(mActivity, Urls.help_rollout_url, true, null);
            }
        });

    }

    public void btn_click(View v) {
        moneyString = editMoney.getText().toString();
        if (isMoneyMatch(moneyString)) {
            rollOutHttp();
        }
    }

    private boolean isMoneyMatch(String moneyString) {
        if (TextUtils.isEmpty(moneyString)) {
            Uihelper.showToast(mActivity, "交易金额不能为空");
            return false;
        }
        float moneyFloat = Float.parseFloat(moneyString);
        float totalMoneyFloat = Float.parseFloat(totalMoney);
        if (moneyFloat > totalMoneyFloat) {
            initTipDialog(0);
            dialogTips.setRemarks("转出金额超限");
            dialogTips.show();
            return false;
        } else if (moneyFloat < 10) {
            initTipDialog(0);
            dialogTips.setRemarks("转出金额不能小于10元");
            dialogTips.show();
            return false;
        } else if (moneyFloat < 100) {
            initTipDialog(1);
            dialogTipsReput.setNegative(View.VISIBLE);
            dialogTipsReput.setRemarks("转出金额小于100元时，第三方需收取一元手续费");
            dialogTipsReput.show();
            return false;
        }
        return true;
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
                    String moneyString = editMoney.getText().toString().trim();
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

                    if (!TextUtils.isEmpty(password)) {
                        if (password.length() >= 6 && password.length() <= 20) {
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
                            if (s.length() >= 6 && s.length() <= 20) {

                                //提现
                                rollOut(s);

                            } else {
                                Uihelper.showToast(mActivity, R.string.tip_password);
                            }
                        } else {

                            Uihelper.showToast(mActivity, "密码不能为空");

                        }

                    }
                };
            }
        }

    }

    private void rollOut(String password) {


        //HttpRequest.withdrawCash(mActivity, new ICallback<Meta>() {
        //    @Override
        //    public void onSucceed(Meta result) {
        //        Uihelper.showToast(mActivity, "提现成功");
        //    }
        //
        //    @Override
        //    public void onFail(String error) {
        //        Uihelper.showToast(mActivity, error);
        //    }
        //},moneyString,userInfo.getBankCode(),cardNum,password);


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