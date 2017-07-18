package com.miqian.mq.activity.user;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.setting.SetBankActivity;
import com.miqian.mq.entity.WithDrawInit;
import com.miqian.mq.entity.WithDrawInitReSult;
import com.miqian.mq.entity.WithDrawPrepress;
import com.miqian.mq.entity.WithDrawPrepressResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.DialogTipSave;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/6/29.
 */

public class RolloutActivity extends BaseActivity implements ExtendOperationController.ExtendOperationListener {

    @BindView(R.id.tv_amt)
    TextView tvAmt;
    @BindView(R.id.tv_remain_count)
    TextView tvRemainCount;
    @BindView(R.id.iv_bankicon)
    ImageView ivBankIcon;
    @BindView(R.id.tv_cardnum)
    TextView tvCardNum;
    @BindView(R.id.et_amt)
    EditText etAmt;
    @BindView(R.id.layout_bankunion)
    View layoutBankUnion;
    @BindView(R.id.tv_bankunion)
    TextView tvBankUnion;
    @BindView(R.id.btn_summit)
    Button btnSummit;

    @BindView(R.id.layout_below5amt)
    View layoutBelow5amt;
    @BindView(R.id.layout_aboveamt)
    View layoutAboveamt;
    @BindView(R.id.layout_no_work)
    View layoutNoWork;
    @BindView(R.id.layout_nobranch)
    View layoutNoBranch;
    @BindView(R.id.btn_openbank)
    Button btnOpenBank;

    @BindView(R.id.tv_tip1)
    TextView tvTip1;
    @BindView(R.id.tv_tip2)
    TextView tvTip2;

    public String inputAmt;
    private int monthRemain;
    private int dayRemain;
    private BigDecimal amt;
    private WithDrawInit initData;
    private DialogTipSave disableDialog;
    private DialogTipSave feeDialog;
    private DialogTipSave openBankDialog;
    private BigDecimal amtMinLimit;
    private BigDecimal amtMaxLimit;
    private ExtendOperationController operationController;
    private String bankUnionNumber;
    private BigDecimal inputmoney;

    @Override
    public void obtainData() {
        //提现初始化
        begin();
        HttpRequest.withDrawInit(mActivity, new ICallback<WithDrawInitReSult>() {
            @Override
            public void onSucceed(WithDrawInitReSult result) {
                end();
                initData = result.getData();
                if (initData != null) {
                    amt = initData.getAmt();
                    amtMinLimit = initData.getAmtMinLimit();
                    amtMaxLimit = initData.getBigWithdrawAmt();
                    bankUnionNumber = initData.getBankUnionNumber();
                    setdata(initData);
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        });

    }


    private void setdata(WithDrawInit initData) {

        tvAmt.setText(amt + "元");
        etAmt.setHint("请输入提现金额，" + amtMinLimit + "元起");
        tvRemainCount.setText("本月可免费提现" + initData.getMonthRemain() + "次");
        String bankUrlSmall = initData.getBankUrlSmall();
        if (!TextUtils.isEmpty(bankUrlSmall)) {
            imageLoader.displayImage(bankUrlSmall, ivBankIcon, options);
        }
        String bankNo = initData.getBankNo();
        if (!TextUtils.isEmpty(bankNo)) {
            tvCardNum.setText("尾号" + bankNo.substring(bankNo.length() - 4, bankNo.length()));
        }
        tvTip1.setVisibility(View.VISIBLE);
        tvTip2.setVisibility(View.VISIBLE);
        tvTip1.setText("1.普通提现：支持单笔" + amtMaxLimit + "(含" + amtMaxLimit + ")以下资金提现，实时到账。");
        tvTip2.setText("2.大额提现：支持单笔" + amtMaxLimit + "以上资金提现。可以大额提现时间");

    }

    @Override
    protected void onDestroy() {
        operationController.unRegisterExtendOperationListener(this);
        super.onDestroy();
    }

    @Override
    public void initView() {
        operationController = ExtendOperationController.getInstance();
        operationController.registerExtendOperationListener(this);
        ButterKnife.bind(this);
        etAmt.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //UI回复原样
                layoutBankUnion.setVisibility(View.GONE);
                btnOpenBank.setVisibility(View.GONE);
                tvBankUnion.setVisibility(View.GONE);
                layoutBelow5amt.setVisibility(View.GONE);
                layoutAboveamt.setVisibility(View.GONE);
                layoutNoBranch.setVisibility(View.GONE);
                layoutNoWork.setVisibility(View.GONE);
                btnSummit.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) return;
                String string = s.toString().replaceAll(",", "");
                if (count != before && string.matches(FormatUtil.PATTERN_MONEY)) {
                    etAmt.setText(FormatUtil.formatAmtForInto(string));
                }
                etAmt.setSelection(etAmt.getText().length());
            }

            @Override
            public void afterTextChanged(final Editable s) {
                inputAmt = s.toString().replaceAll(",", "");
                if (TextUtils.isEmpty(inputAmt)) {
                    return;
                }
                if (inputAmt.matches(FormatUtil.PATTERN_MONEY)) {
                    handledata(inputAmt);
                    return;
                }
                s.delete(s.toString().length() - 1, s.toString().length());
            }
        });
    }

    private void handledata(final String inputAmt) {
        if (!TextUtils.isEmpty(inputAmt)) {
            inputmoney = new BigDecimal(inputAmt);
            if (amt.compareTo(inputmoney) < 0) {//提现金额大于可提现金额
                Uihelper.showToast(mActivity, "提现金额大于可提现金额，请重新输入");
                return;
            }
            if ((inputmoney.compareTo(amtMinLimit)) < 0) {
                Uihelper.showToast(mActivity, "小于最小提现金额，请重新输入");
                return;
            }
            if (inputmoney.compareTo(amtMaxLimit) <= 0) {//小额提现  等于也是小额
                layoutBelow5amt.setVisibility(View.VISIBLE);
                btnSummit.setEnabled(true);
            } else {
                if (!TextUtils.isEmpty(bankUnionNumber)) {
                    layoutBankUnion.setVisibility(View.VISIBLE);
                    tvBankUnion.setVisibility(View.VISIBLE);
                    tvBankUnion.setText(bankUnionNumber);
                    layoutAboveamt.setVisibility(View.VISIBLE);
                    btnSummit.setEnabled(true);
                } else {//未开通联行号
                    layoutBankUnion.setVisibility(View.VISIBLE);
                    layoutNoBranch.setVisibility(View.VISIBLE);
                    btnOpenBank.setVisibility(View.VISIBLE);
                }
            }


        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rollout;
    }

    //开通联行号
    @OnClick(R.id.btn_openbank)
    public void openBank() {
        String bankName = initData.getBankName();
        if (!TextUtils.isEmpty(bankName)) {
            Intent intent = new Intent(RolloutActivity.this, SetBankActivity.class);
            intent.putExtra("bankName", initData.getBankName());
            startActivity(intent);
        }
    }

    //确认
    @OnClick(R.id.btn_summit)
    public void summit() {
        if (dayRemain==0){
            Uihelper.showToast(mActivity, "今日提现次数已达上限");
            return;
        }
        begin();
        HttpRequest.withDrawPreprocess(mActivity, inputAmt + "", new ICallback<WithDrawPrepressResult>() {
            @Override
            public void onSucceed(WithDrawPrepressResult result) {
                end();
                WithDrawPrepress data = result.getData();
                String code = result.getCode();
                if ("200016".equals(code)) {// 还没有绑定银行卡
                    Uihelper.showToast(mActivity, result.getMessage());

                } else if ("400007".equals(code)) {//支行信息不全
                    layoutBankUnion.setVisibility(View.VISIBLE);
                    layoutNoBranch.setVisibility(View.VISIBLE);
                    btnOpenBank.setVisibility(View.VISIBLE);
                    if (openBankDialog == null) {
                        openBankDialog = new DialogTipSave(mActivity, "提示", getResources().getText(R.string.rollout_tip_openbank).toString()) {
                            @Override
                            public void positionBtnClick() {
                                dismiss();
                                openBank();
                            }
                        };
                    }
                    openBankDialog.show();

                } else if ("101006".equals(code)) {//小于最小提现金额
                    Uihelper.showToast(mActivity, result.getMessage());

                } else if ("101001".equals(code)) {//余额不足（提现金额大于可提现金额）
                    Uihelper.showToast(mActivity, result.getMessage());

                } else {
                    if (data.isEnable()) {
                        String feeAmt = data.getFeeAmt();
                        if (feeDialog == null) {
                            feeDialog = new DialogTipSave(mActivity, "温馨提示", "此次提现收取手续费" + data.getFeeAmt() + "元") {
                                @Override
                                public void positionBtnClick() {
                                    dismiss();
                                    HttpRequest.autoWithdraw(mActivity, inputAmt);
                                }
                            };
                        }
                        if (!TextUtils.isEmpty(feeAmt)) {
                            if (new BigDecimal(feeAmt).compareTo(BigDecimal.ZERO) > 0) {
                                feeDialog.show();
                            } else {
                                HttpRequest.autoWithdraw(mActivity, inputAmt);
                            }
                        } else {
                            HttpRequest.autoWithdraw(mActivity, inputAmt);
                        }
                    } else {//非工作日不可提现
                        layoutNoWork.setVisibility(View.VISIBLE);
                        if (disableDialog == null) {
                            disableDialog = new DialogTipSave(mActivity, "温馨提示", getResources().getText(R.string.rollout_tip_enable).toString()) {
                                @Override
                                public void positionBtnClick() {
                                    dismiss();
                                }
                            };
                        }

                        disableDialog.show();
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
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("提现");
    }

    @Override
    protected String getPageName() {
        return "提现";
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {

        if (operationKey == ExtendOperationController.OperationKey.BANK_UNIONNUM) {
            String bankUnionNumber = (String) data;
            if (!TextUtils.isEmpty(bankUnionNumber)) {
                btnOpenBank.setVisibility(View.GONE);
                layoutNoBranch.setVisibility(View.GONE);
                layoutAboveamt.setVisibility(View.VISIBLE);
                tvBankUnion.setVisibility(View.VISIBLE);
                tvBankUnion.setText(bankUnionNumber);
                btnSummit.setEnabled(true);
            }
        }

    }
}
