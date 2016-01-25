package com.miqian.mq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.ProducedOrder;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.views.DialogTip;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/1/20.
 */
public class PaymodeActivity extends BaseActivity implements View.OnClickListener {

    private DialogTip dialogTip;
    private TextView textMoney;
    private RelativeLayout frameBank;
    private RelativeLayout frameCurrent;
    private RelativeLayout frameBalance;
    private ImageView ivChooseBank;
    private ImageView ivChooseCurrent;
    private ImageView ivChooseBalance;

    private TextView textBankName;
    private TextView textCurrent;
    private TextView textBalance;
    private ImageView imageBank;
    private ImageView imageCurrent;
    private ImageView imageBalance;
    private TextView textTip;
    private TextView textCurrentMoney;
    private TextView textBalanceMoney;

    private int payModeState;
    private String money;
    private ProducedOrder producedOrder;
    private String bankString;
    private String prodId;

    private BigDecimal payMoney;
    private BigDecimal currentMoney = BigDecimal.ZERO;//活转定中使用的活期余额
    private BigDecimal balanceMoney = BigDecimal.ZERO;//账户余额

    @Override
    public void onCreate(Bundle arg0) {
        Intent intent = getIntent();
        payModeState = intent.getIntExtra("payModeState", 0);
        money = intent.getStringExtra("money");
        prodId = intent.getStringExtra("prodId");
        producedOrder = JsonUtil.parseObject(intent.getStringExtra("producedOrder"), ProducedOrder.class);
        payMoney = new BigDecimal(money);
        if (producedOrder != null) {
            String bankNumber = RSAUtils.decryptByPrivate(producedOrder.getBankCardNo());
            bankNumber = bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
            bankString = producedOrder.getBankName() + "(" + bankNumber + ")";
            currentMoney = producedOrder.getBalanceCurrent();
            balanceMoney = producedOrder.getBalance();
        }
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        textMoney = (TextView) findViewById(R.id.text_money);
        frameBank = (RelativeLayout) findViewById(R.id.frame_bank);
        frameBank.setOnClickListener(this);
        frameCurrent = (RelativeLayout) findViewById(R.id.frame_current);
        frameBalance = (RelativeLayout) findViewById(R.id.frame_balance);

        ivChooseBank = (ImageView) findViewById(R.id.iv_choose_bank);
        ivChooseCurrent = (ImageView) findViewById(R.id.iv_choose_current);
        ivChooseBalance = (ImageView) findViewById(R.id.iv_choose_balance);

        textBankName = (TextView) findViewById(R.id.tv_bankname);
        textCurrent = (TextView) findViewById(R.id.text_current);
        textCurrent.setOnClickListener(this);
        textBalance = (TextView) findViewById(R.id.text_balance);
        imageBank = (ImageView) findViewById(R.id.iv_bank);
        imageCurrent = (ImageView) findViewById(R.id.iv_current);
        imageBalance = (ImageView) findViewById(R.id.iv_balance);
        textTip = (TextView) findViewById(R.id.text_tip);
        textCurrentMoney = (TextView) findViewById(R.id.text_current_money);
        textBalanceMoney = (TextView) findViewById(R.id.text_balance_money);
        initViewByData();
        refreshView();
    }

    private void initViewByData() {
        textMoney.setText("待付款" + money + "元");
        textBankName.setText(bankString);
        textTip.setText("单日限额 ");
        textCurrentMoney.setText("可用" + currentMoney + "元");
        textBalanceMoney.setText("可用" + balanceMoney + "元");
        if (producedOrder != null) {
            textTip.setText("单笔限额" + producedOrder.getSingleAmtLimit() + "元， 单日限额" + producedOrder.getDayAmtLimit() + "元");
            imageLoader.displayImage(producedOrder.getBankUrlSmall(), imageBank, options);
        }
        if (CurrentInvestment.PRODID_CURRENT.equals(prodId)) {
            frameCurrent.setVisibility(View.GONE);
        } else {
            if (currentMoney.compareTo(payMoney) >= 0) {
                frameCurrent.setOnClickListener(this);
            } else {
                textCurrent.setTextColor(getResources().getColor(R.color.mq_b3));
                imageCurrent.setEnabled(false);
            }
        }

        if (balanceMoney.compareTo(payMoney) >= 0) {
            frameBalance.setOnClickListener(this);
        } else {
            textBalance.setTextColor(getResources().getColor(R.color.mq_b3));
            textBalance.setText("余额不足");
            imageBalance.setEnabled(false);
        }
    }


    private void refreshView() {
        ivChooseBank.setVisibility(View.GONE);
        ivChooseCurrent.setVisibility(View.GONE);
        ivChooseBalance.setVisibility(View.GONE);
        if (payModeState == CurrentInvestment.PAY_MODE_BANK) {
            ivChooseBank.setVisibility(View.VISIBLE);
        } else if (payModeState == CurrentInvestment.PAY_MODE_CURRENT) {
            ivChooseCurrent.setVisibility(View.VISIBLE);
        } else if (payModeState == CurrentInvestment.PAY_MODE_BALANCE) {
            ivChooseBalance.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_paymode;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("选择支付方式");
    }

    @Override
    protected String getPageName() {
        return "选择支付方式";
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.text_current:
                if (dialogTip == null) {
                    dialogTip = new DialogTip(mActivity) {
                    };
                }
                dialogTip.show();
                break;
            case R.id.frame_bank:
                if (payModeState != CurrentInvestment.PAY_MODE_BANK) {
                    payModeState = CurrentInvestment.PAY_MODE_BANK;
                    refreshView();
                }
                backSubscribePage();
                break;
            case R.id.frame_current:
                if (payModeState != CurrentInvestment.PAY_MODE_CURRENT) {
                    payModeState = CurrentInvestment.PAY_MODE_CURRENT;
                    refreshView();
                }
                backSubscribePage();
                break;
            case R.id.frame_balance:
                if (payModeState != CurrentInvestment.PAY_MODE_BALANCE) {
                    payModeState = CurrentInvestment.PAY_MODE_BALANCE;
                    refreshView();
                }
                backSubscribePage();
                break;
            default:
                break;
        }
    }

    /**
     * 返回订单页面认购
     *
     */
    private void backSubscribePage() {
        Intent intent = new Intent();
        intent.putExtra("payModeState", payModeState);
        setResult(CurrentInvestment.SUCCESS, intent);
        PaymodeActivity.this.finish();
    }
}
