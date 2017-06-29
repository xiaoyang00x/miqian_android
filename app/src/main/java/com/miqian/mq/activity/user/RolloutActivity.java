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

public class RolloutActivity extends BaseActivity {

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
    EditText tvBankUnion;
    @BindView(R.id.btn_summit)
    Button btnSummit;

    @BindView(R.id.layout_below5amt)
    View layoutBelow5amt;
    @BindView(R.id.layout_aboveamt)
    View layoutAboveamt;
    @BindView(R.id.btn_openbank)
    Button btnOpenBank;


    public String inputAmt;
    private int monthRemain;
    private int dayRemain;
    private BigDecimal amt;
    private WithDrawInit data;
    private DialogTipSave disableDialog;
    private DialogTipSave feeDialog;
    private DialogTipSave openBankDialog;

    @Override
    public void obtainData() {
        //提现初始化
        begin();
        HttpRequest.withDrawInit(mActivity, new ICallback<WithDrawInitReSult>() {
            @Override
            public void onSucceed(WithDrawInitReSult result) {
                end();
                data = result.getData();
                if (data != null) {
                    amt = data.getAmt();
                    setdata(data);
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        });

    }

    private void setdata(WithDrawInit data) {

        tvAmt.setText(amt + "元");
        tvRemainCount.setText("本月可免费提现" + data.getMonthRemain() + "次");
        String bankUrlSmall = data.getBankUrlSmall();
        if (!TextUtils.isEmpty(bankUrlSmall)) {
            imageLoader.displayImage(bankUrlSmall, ivBankIcon, options);
        }
        String bankNo = data.getBankNo();
        if (!TextUtils.isEmpty(bankNo)) {
            tvCardNum.setText(bankNo.substring(bankNo.length() - 4, bankNo.length()));
        }

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        etAmt.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputAmt = s.toString();
                handledata(inputAmt);
            }


        });

    }

    private void handledata(String inputAmt) {
        //UI回复原样
        layoutBankUnion.setVisibility(View.GONE);
        btnOpenBank.setVisibility(View.GONE);
        tvBankUnion.setVisibility(View.GONE);
        layoutBelow5amt.setVisibility(View.GONE);
        layoutAboveamt.setVisibility(View.GONE);
        btnSummit.setEnabled(false);

        BigDecimal inputmoney;
        data.getBankUnionNumber();
        if (!TextUtils.isEmpty(inputAmt)) {
            inputmoney = new BigDecimal(inputAmt);
            if (amt.compareTo(amt) < 0) {//提现金额大于可提现金额
                Uihelper.showToast(mActivity, "提现金额大于可提现金额，请重新输入");
                return;
            }
            if (inputmoney.compareTo(new BigDecimal(50000)) < 0) {//小额提现
                layoutBelow5amt.setVisibility(View.VISIBLE);
                btnSummit.setEnabled(true);
            } else {//大于5万的大额提现 分情况处理
                HttpRequest.withDrawPreprocess(mActivity, inputAmt + "", new ICallback<WithDrawPrepressResult>() {
                    @Override
                    public void onSucceed(WithDrawPrepressResult result) {
                        WithDrawPrepress data = result.getData();
                        if (data.isEnable()) {
                            if (data.isBindBankStatus()) {// 是绑卡状态
                                layoutBankUnion.setVisibility(View.VISIBLE);
                                tvBankUnion.setText(data.getBankUnionNumber());
                                layoutAboveamt.setVisibility(View.VISIBLE);
                                if (feeDialog == null) {
                                    feeDialog = new DialogTipSave(mActivity, "温馨提示", "此次提现收取手续费" + data.getFeeAmt() + "元") {
                                        @Override
                                        public void positionBtnClick() {
                                            btnSummit.setEnabled(true);
                                        }
                                    };
                                }
                                feeDialog.show();

                            } else {//未绑卡状态
                                layoutBankUnion.setVisibility(View.VISIBLE);
                                btnOpenBank.setVisibility(View.VISIBLE);
                                if (openBankDialog == null) {
                                    openBankDialog = new DialogTipSave(mActivity, "温馨提示", getResources().getText(R.string.rollout_tip_openbank).toString()) {
                                        @Override
                                        public void positionBtnClick() {
                                            dismiss();
                                        }
                                    };
                                }
                                openBankDialog.show();

                            }

                        } else {//非工作日不可提现
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

                    @Override
                    public void onFail(String error) {
                        Uihelper.showToast(mActivity, error);
                    }
                });

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

    }

    //确认
    @OnClick(R.id.btn_summit)
    public void summit() {

          startActivity(new Intent(mActivity, SetBankActivity.class));

    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("提现");
    }

    @Override
    protected String getPageName() {
        return "提现";
    }
}
