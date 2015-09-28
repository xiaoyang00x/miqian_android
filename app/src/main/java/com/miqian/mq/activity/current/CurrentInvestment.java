package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.IntoActivity;
import com.miqian.mq.entity.ProducedOrder;
import com.miqian.mq.entity.ProducedOrderResult;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jackie on 2015/9/19.
 */
public class CurrentInvestment extends BaseActivity implements View.OnClickListener {

    private Button btPay;
    private TextView textOrderMoney;
    private TextView textBalance;
    private TextView textBest;
    private TextView textBankPay;
    private RelativeLayout frameBankPay;
    private RelativeLayout frameRedPackage;

    private ProducedOrder producedOrder;
    private List<Promote> promList;

    private String money;
    private String prodId; //0:充值产品 1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让

    private BigDecimal orderMoney;//订单金额
    private BigDecimal promoteMoney = new BigDecimal(0);//优惠金额： 红包、拾财券
    private BigDecimal balanceMoney;//账户余额
    private BigDecimal balancePay;//需余额支付额度
    private BigDecimal rollinMoney;//需转入金额
    private BigDecimal bFlag = new BigDecimal(0);

    private static final int REQUEST_CODE_ROLLIN = 1;
    private static final int REQUEST_CODE_REDPACKET = 2;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        money = FormatUtil.getMoneyString(intent.getStringExtra("money"));
        prodId = intent.getStringExtra("prodId");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        refreshData();
    }

    private void refreshData() {
        mWaitingDialog.show();
        HttpRequest.getProduceOrder(mActivity, new ICallback<ProducedOrderResult>() {
            @Override
            public void onSucceed(ProducedOrderResult result) {
                mWaitingDialog.dismiss();
                producedOrder = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, money, prodId);
    }

    private void refreshView() {
        if (isNeedRollin()) {
            frameBankPay.setVisibility(View.VISIBLE);
            btPay.setText("充值");
        } else {
            frameBankPay.setVisibility(View.GONE);
            btPay.setText("支付");
        }
        textOrderMoney.setText(money + "元");
        textBest.setText("最多可抵" + producedOrder.getBest() + "元");
        textBalance.setText(balancePay + "元");
        textBankPay.setText("余额不足，银行卡充值 " + rollinMoney + " 元");
    }

    @Override
    public void initView() {
        btPay = (Button) findViewById(R.id.bt_pay);
        btPay.setOnClickListener(this);
        textOrderMoney = (TextView) findViewById(R.id.order_money);
        textBalance = (TextView) findViewById(R.id.text_balance);
        textBest = (TextView) findViewById(R.id.text_best);
        textBankPay = (TextView) findViewById(R.id.text_bank_pay);
        frameBankPay = (RelativeLayout) findViewById(R.id.frame_bank_pay);
        frameRedPackage = (RelativeLayout) findViewById(R.id.frame_red_package);
        frameRedPackage.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.current_investment;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("确认订单");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pay:
                if (isNeedRollin()) {
                    Intent intent = new Intent(CurrentInvestment.this, IntoActivity.class);
                    intent.putExtra("rollType", 1);
                    intent.putExtra("money", rollinMoney.toString());
                    startActivityForResult(intent, REQUEST_CODE_ROLLIN);
                } else {

                }
                break;
            case R.id.frame_red_package:
                if (producedOrder != null) {
                    promList = producedOrder.getPromList();
                }
                if (promList != null && promList.size() > 0) {
                    Intent intent = new Intent(CurrentInvestment.this, ActivityRedPacket.class);
                    intent.putExtra("producedOrder", JSON.toJSONString(producedOrder));
                    startActivityForResult(intent, REQUEST_CODE_REDPACKET);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ROLLIN) {
            refreshData();
            if (resultCode == IntoActivity.SUCCESS) {
                btPay.setText("支付");
            } else if (resultCode == IntoActivity.PROCESSING) {
                Uihelper.showToast(mActivity, "正在处理中，请稍后重试");
            } else if (resultCode == IntoActivity.FAIL) {
                Uihelper.showToast(mActivity, "充值失败，请重新充值");
            }
        } else if (requestCode == REQUEST_CODE_REDPACKET) {
            refreshView();
        }
        Log.e("", "requestCode : " + requestCode);
        Log.e("", "resultCode : " + resultCode);
    }

    private boolean isNeedRollin() {
        if (producedOrder != null) {
            orderMoney = new BigDecimal(money);
            balanceMoney = new BigDecimal(producedOrder.getBalance());

            float needPay = orderMoney.subtract(promoteMoney).floatValue();
            if (needPay > 0) {
                if (promoteMoney.add(balanceMoney).compareTo(orderMoney) > 0) {
                    balancePay = orderMoney.subtract(promoteMoney);
                } else {
                    balancePay = balanceMoney;
                }
            } else {
                balancePay = new BigDecimal(0);
            }
            rollinMoney = orderMoney.subtract(promoteMoney).subtract(balancePay);
            if (rollinMoney.compareTo(bFlag) > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
