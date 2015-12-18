package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.IntoActivity;
import com.miqian.mq.entity.Amt;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.ProducedOrder;
import com.miqian.mq.entity.ProducedOrderResult;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.entity.SubscribeOrder;
import com.miqian.mq.entity.SubscribeOrderResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.DialogTradePassword;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2015/9/19.
 */
public class CurrentInvestment extends BaseActivity implements View.OnClickListener {

    private TextView textProjectType;
    private TextView textInterestRate;
    private Button btPay;
    private TextView textOrderMoney;
    private TextView textBalance;
    private TextView textBest;
    private TextView textBestMoney;
    //    private TextView textBankPayMoney;
    private RelativeLayout frameCurrentPay;
    private RelativeLayout frameRedPackage;
    private EditText editCurrentBalance;
    private RelativeLayout frameTip;
    private View frameSpace;
    private TextView textTip;
    private MySwipeRefresh swipeRefresh;

    private DialogTradePassword dialogTradePasswordSet;
    private DialogTradePassword dialogTradePasswordInput;


    private ProducedOrder producedOrder;
    private List<Promote> promList;
    private String promListString = "";
    private String prodListString = "";

    private String money;
    private String prodId; //0:充值产品 1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
    private String subjectId; //标的id，活期默认为0
    private String interestRateString; //年化利率和期限
    private int position = -1;//使用的红包位置，用于获取list

    private BigDecimal orderMoney;//订单金额
    private BigDecimal promoteMoney = BigDecimal.ZERO;//优惠金额： 红包、拾财券
    private BigDecimal currentMoney = BigDecimal.ZERO;//活转定中使用的活期余额
    private BigDecimal balanceMoney;//账户余额
    private BigDecimal balancePay;//需余额支付额度
    private BigDecimal rollinMoney;//需转入金额
    private BigDecimal bFlag = BigDecimal.ZERO;

    private static final int REQUEST_CODE_ROLLIN = 1;
    private static final int REQUEST_CODE_REDPACKET = 2;

    public static final int FAIL = 0;
    public static final int SUCCESS = 1;
    public static final int PROCESSING = 2;


    public static final String PRODID_CURRENT = "1";
    public static final String PRODID_REGULAR = "3";
    public static final String PRODID_REGULAR_PLAN = "5";

    public static final String SUBJECTID_CURRENT = "0";

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        prodId = intent.getStringExtra("prodId");
        subjectId = intent.getStringExtra("subjectId");
        interestRateString = intent.getStringExtra("interestRateString");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        refreshData();
    }

    private void refreshData() {
        if (!swipeRefresh.isRefreshing()) {
            mWaitingDialog.show();
        }
        HttpRequest.getProduceOrder(mActivity, new ICallback<ProducedOrderResult>() {
            @Override
            public void onSucceed(ProducedOrderResult result) {
                mWaitingDialog.dismiss();
                swipeRefresh.setRefreshing(false);
                if ("996633".equals(result.getCode())) {
                    showTips(true, result);
                } else {
                    showTips(false, result);
                    producedOrder = result.getData();
                    promList = producedOrder.getPromList();
                    refreshView();
                }
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                swipeRefresh.setRefreshing(false);
                btPay.setEnabled(false);
                Uihelper.showToast(mActivity, error);
            }
        }, money, subjectId, prodId);
    }

    private void showTips(boolean flag, ProducedOrderResult result) {
        if (flag) {
            frameTip.setVisibility(View.VISIBLE);
            frameSpace.setVisibility(View.GONE);
            btPay.setEnabled(false);
            textTip.setText(result.getMessage());
        } else {
            frameTip.setVisibility(View.GONE);
            frameSpace.setVisibility(View.VISIBLE);
            btPay.setEnabled(true);
        }
    }

    private void refreshView() {
        refreshCurrentView();
        editCurrentBalance.setHint("最多可支付" + producedOrder.getBalanceCurrent() + "元");
        textOrderMoney.setText(money + "元");

        if (promList != null && promList.size() > 0) {
            if (promoteMoney.compareTo(bFlag) > 0) {
                textBest.setTextColor(getResources().getColor(R.color.mq_b1));
                textBest.setText("抵用");
                textBestMoney.setText(promoteMoney + "元");
            } else {
                textBest.setTextColor(getResources().getColor(R.color.mq_b2));
                textBest.setText("最多可抵");
                textBestMoney.setText(producedOrder.getBest() + "元");
            }
        } else {
            textBest.setTextColor(getResources().getColor(R.color.mq_b2));
            textBest.setText("无可用");
            textBestMoney.setText("");
        }
    }

    //活期余额
    private void refreshCurrentView() {
        if (isNeedRollin()) {
            btPay.setText("充值" + rollinMoney + " 元");
        } else {
            btPay.setText("支付");
        }
        textBalance.setText(balancePay + "元");
    }

    @Override
    public void initView() {
        textTip = (TextView) findViewById(R.id.text_tip);
        frameTip = (RelativeLayout) findViewById(R.id.frame_tip);
        frameSpace = (View) findViewById(R.id.frame_space);
        frameCurrentPay = (RelativeLayout) findViewById(R.id.frame_current_pay);

        textProjectType = (TextView) findViewById(R.id.text_project_type);
        textInterestRate = (TextView) findViewById(R.id.text_interest_rate);
        frameRedPackage = (RelativeLayout) findViewById(R.id.frame_red_package);
        editCurrentBalance = (EditText) findViewById(R.id.edit_current_balance);
        editCurrentBalance.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void myAfterTextChanged(Editable s) {
                try {
                    String temp = s.toString();
                    if (TextUtils.isEmpty(temp) || temp.matches(FormatUtil.PATTERN_MONEY)) {
                        refreshCurrentView();
                        return;
                    }
                    s.delete(temp.length() - 1, temp.length());
                } catch (Exception e) {
                }
            }
        });


        frameRedPackage.setOnClickListener(this);
        if (PRODID_CURRENT.equals(prodId)) {
            frameRedPackage.setVisibility(View.GONE);
            frameCurrentPay.setVisibility(View.GONE);
            textProjectType.setText("期赚");
            textProjectType.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_type_current), null, null, null);
            textInterestRate.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.investment_interest), null);
        } else if (PRODID_REGULAR.equals(prodId)) {
            textProjectType.setText("期赚");
            textProjectType.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_type_regular), null, null, null);
            textInterestRate.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            textInterestRate.setText(interestRateString);
        } else if (PRODID_REGULAR_PLAN.equals(prodId)) {
            textProjectType.setText("期计划");
            textProjectType.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_type_regular), null, null, null);
            textInterestRate.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            textInterestRate.setText(interestRateString);
        }

        btPay = (Button) findViewById(R.id.bt_pay);
        btPay.setOnClickListener(this);
        textOrderMoney = (TextView) findViewById(R.id.order_money);
        textBalance = (TextView) findViewById(R.id.text_balance);
        textBest = (TextView) findViewById(R.id.text_best);
        textBestMoney = (TextView) findViewById(R.id.text_best_money);
//        textBankPayMoney = (TextView) findViewById(R.id.text_bank_pay_money);
//        frameBankPay = (RelativeLayout) findViewById(R.id.frame_bank_pay);

        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
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
    protected String getPageName() {
        return "确认订单";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pay:
                if (isNeedRollin()) {
                    MobclickAgent.onEvent(mContext, "1067");
                    Intent intent = new Intent(CurrentInvestment.this, IntoActivity.class);
                    intent.putExtra("rollType", 1);
                    intent.putExtra("money", rollinMoney.toString());
                    startActivityForResult(intent, REQUEST_CODE_ROLLIN);
                } else {
                    MobclickAgent.onEvent(mContext, "1068");
                    payOrder();
                }
                break;
            case R.id.frame_red_package:
                MobclickAgent.onEvent(mContext, "1066");
                if (promList != null && promList.size() > 0) {
                    Intent intent = new Intent(CurrentInvestment.this, ActivityRedPacket.class);
                    intent.putExtra("producedOrder", JSON.toJSONString(producedOrder));
                    intent.putExtra("position", position);
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
            if (resultCode == SUCCESS) {
                btPay.setText("支付");
            } else if (resultCode == PROCESSING) {
                Uihelper.showToast(mActivity, "正在处理中，请稍后重试");
            } else if (resultCode == FAIL) {
                Uihelper.showToast(mActivity, "充值失败，请重新充值");
            }
        } else if (requestCode == REQUEST_CODE_REDPACKET) {
//          红包、拾财券选择
            if (resultCode == SUCCESS) {
                position = data.getIntExtra("position", -1);
                if (position >= 0) {
                    Promote promote = promList.get(position);
                    List<Promote> promListParam = new ArrayList<>();
                    promListParam.add(promote);
                    promoteMoney = promote.getWillUseAmt();
                    promListString = JSON.toJSONString(promListParam, true);
                } else {
                    promoteMoney = BigDecimal.ZERO;
                    promListString = "";
                }
                refreshView();
            }
        }
    }

    private boolean isNeedRollin() {
        if (producedOrder != null) {
            orderMoney = new BigDecimal(money);
            balanceMoney = new BigDecimal(producedOrder.getBalance());
            String currentString = editCurrentBalance.getText().toString();
            if (!TextUtils.isEmpty(currentString)) {
                currentMoney = new BigDecimal(editCurrentBalance.getText().toString());
            } else {
                currentMoney = BigDecimal.ZERO;
            }

            BigDecimal tempPromoteMoney = currentMoney.add(promoteMoney);

            float needPay = orderMoney.subtract(tempPromoteMoney).floatValue();
            if (needPay > 0) {
                if (tempPromoteMoney.add(balanceMoney).compareTo(orderMoney) > 0) {
                    balancePay = orderMoney.subtract(tempPromoteMoney);
                } else {
                    balancePay = balanceMoney;
                }
            } else {
                balancePay = new BigDecimal(0);
            }
            rollinMoney = orderMoney.subtract(tempPromoteMoney).subtract(balancePay);
            if (rollinMoney.compareTo(bFlag) > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean isCurrentFit() {
        BigDecimal tempBig = orderMoney.subtract(promoteMoney);
        if (tempBig.compareTo(producedOrder.getBalanceCurrent()) > 0) {
            if (currentMoney.compareTo(producedOrder.getBalanceCurrent()) > 0) {
                Uihelper.showToast(mActivity, "请确认您的活期支付金额");
                editCurrentBalance.setText(producedOrder.getBalanceCurrent().toString());
                return false;
            }
        } else {
            if (currentMoney.compareTo(tempBig) > 0) {
                Uihelper.showToast(mActivity, "请确认您的活期支付金额");
                editCurrentBalance.setText(tempBig.toString());
                return false;
            }
        }
        return true;
    }


    private void payOrder() {
        if (!isCurrentFit()) {
            return;
        }
        if (currentMoney.compareTo(bFlag) > 0) {
            List<Amt> prodListParam = new ArrayList<>();
            Amt amt = new Amt();
            amt.setAmt(currentMoney);
            prodListParam.add(amt);
            prodListString = JSON.toJSONString(prodListParam, true);
        } else {
            prodListString = "";
        }

        mWaitingDialog.show();
        HttpRequest.getUserInfo(mActivity, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                mWaitingDialog.dismiss();
                int status = Integer.parseInt(result.getData().getPayPwdStatus());
                showTradeDialog(status);
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    private void showTradeDialog(int type) {
        initDialogTradePassword(type);
        if (type == DialogTradePassword.TYPE_SETPASSWORD) {
            dialogTradePasswordSet.show();
        } else {
            dialogTradePasswordInput.show();
        }
    }

    private void initDialogTradePassword(int type) {

        if (dialogTradePasswordSet == null && type == DialogTradePassword.TYPE_SETPASSWORD) {
            dialogTradePasswordSet = new DialogTradePassword(mActivity, DialogTradePassword.TYPE_SETPASSWORD) {

                @Override
                public void positionBtnClick(String s) {
                    //设置交易密码
                    mWaitingDialog.show();
                    dismiss();
                    HttpRequest.setPayPassword(mActivity, new ICallback<Meta>() {
                        @Override
                        public void onSucceed(Meta result) {
                            mWaitingDialog.dismiss();
                            Uihelper.showToast(mActivity, "设置成功");
                            showTradeDialog(DialogTradePassword.TYPE_INPUTPASSWORD);
                        }

                        @Override
                        public void onFail(String error) {
                            mWaitingDialog.dismiss();
                            Uihelper.showToast(mActivity, error);
                        }
                    }, s, s);
                }
            };
        } else {
            if (dialogTradePasswordInput == null) {
                dialogTradePasswordInput = new DialogTradePassword(mActivity, DialogTradePassword.TYPE_INPUTPASSWORD) {

                    @Override
                    public void positionBtnClick(String payPassword) {
                        dismiss();
                        //支付
                        mWaitingDialog.show();
                        HttpRequest.subjectIdOrder(mActivity, new ICallback<SubscribeOrderResult>() {
                            @Override
                            public void onSucceed(SubscribeOrderResult result) {
                                mWaitingDialog.dismiss();
                                Intent intent = new Intent(CurrentInvestment.this, SubscribeResult.class);
                                SubscribeOrder subscribeOrder = result.getData();
                                if (result.getCode().equals("996633")) {
                                    Uihelper.showToast(mActivity, result.getMessage());
                                } else {
                                    if (result.getCode().equals("000000")) {
                                        intent.putExtra("status", 1);
                                        intent.putExtra("orderNo", subscribeOrder.getOrderNo());
                                        intent.putExtra("addTime", subscribeOrder.getAddTime());
                                    } else {
                                        intent.putExtra("status", 0);
                                    }
                                    intent.putExtra("money", orderMoney.toString());
                                    intent.putExtra("balance", balancePay.toString());
                                    intent.putExtra("promoteMoney", promoteMoney.toString());
                                    startActivity(intent);
                                    CurrentInvestment.this.finish();
                                }
                            }

                            @Override
                            public void onFail(String error) {
                                mWaitingDialog.dismiss();
                                Uihelper.showToast(mActivity, error);
                            }
                        }, money, prodId, payPassword, subjectId, promListString, prodListString);
                    }
                };
            }
        }
    }
}
