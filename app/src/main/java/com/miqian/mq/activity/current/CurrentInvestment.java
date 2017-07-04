package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.rollin.IntoResultActivity;
import com.miqian.mq.entity.PayOrder;
import com.miqian.mq.entity.ProducedOrder;
import com.miqian.mq.entity.ProducedOrderResult;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.entity.SubscribeOrder;
import com.miqian.mq.entity.SubscribeOrderResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CustomDialog;
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
    private TextView textProjectInfo;
    private Button btPay;
    private TextView textOrderMoney;
    private TextView expectMoney;
    private TextView textPromote;
    private TextView textPromoteType;
    private TextView textPromoteMoney;
    private TextView textPromoteUnit;
    private RelativeLayout frameExpect; // 预期收益
//    private RelativeLayout frameFact;
    private RelativeLayout frameRedPackage; // 红包/卡
//    private RelativeLayout frameLianPay;
//    private TextView textLian;
//    private RelativeLayout framePayChoose;
    private TextView textPayType;
//    private TextView textPayTip;
    private TextView textPayMoney;
    private TextView factMoney;
//    private TextView textErrorLian;
    private ImageView imageType;

    private RelativeLayout frameTip;
//    private View frameSpace;
    private TextView textTip;
    private MySwipeRefresh swipeRefresh;

//    private DialogTradePassword dialogTradePasswordInput;

    private ProducedOrder producedOrder;
    private List<Promote> promList;
    private String promListString = "";
    private String promoteType = "";

    private String money;
    private String realMoney;
    private String productType; //1:定期项目 2:定期计划 3:秒钱宝
    private String subjectId; //标的id，活期默认为0
    private String interestRateString; //年化收益和期限
    private int position = -1;//使用的红包位置，用于获取list
    private PayOrder payOrder;
//    private String bankNumber;

    private BigDecimal orderMoney;//订单金额
    private BigDecimal promoteMoney = BigDecimal.ZERO;//优惠金额： 红包、拾财券
    private BigDecimal increaseMoney = BigDecimal.ZERO;//加息金额
    private BigDecimal payMoney;//需支付的金额
    private BigDecimal bFlag = BigDecimal.ZERO;

    public static final int REQUEST_CODE_ROLLIN = 1;
    private static final int REQUEST_CODE_REDPACKET = 2;
    private static final int REQUEST_CODE_PAYMODE = 3;
    private static final int REQUEST_CODE_PASSWORD = 4;

    public static final int FAIL = 0;
    public static final int SUCCESS = 1;
    public static final int PROCESSING = 2;

    //  支付方式: 余额、银行卡、活期、连连支付
    public static final int PAY_MODE_BALANCE = 0;
//    public static final int PAY_MODE_BANK = 1;
//    public static final int PAY_MODE_CURRENT = 2;
//    public static final int PAY_MODE_LIAN = 3;

    public static final String PRODID_REGULAR = "1";
    public static final String PRODID_REGULAR_PLAN = "2";
    public static final String PRODID_CURRENT = "3";

    public static final String SUBJECTID_CURRENT = "0";

    public int payModeState = 0;
    private CustomDialog dialogTips;
    private CustomDialog packageTips;
//    private DialogTip mDialogTip;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        orderMoney = new BigDecimal(money);
        realMoney = intent.getStringExtra("realMoney");
        productType = intent.getStringExtra("productType");
//        if (!TextUtils.isEmpty(realMoney) && (prodId.equals(PRODID_REGULAR_TRANSFER) || prodId.equals(PRODID_REGULAR_PLAN_TRANSFER))) {
//            orderMoney = new BigDecimal(realMoney);
//        }
        subjectId = intent.getStringExtra("subjectId");
        interestRateString = intent.getStringExtra("interestRateString");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        refreshData();
    }

    private void refreshData() {
        position = -1;
        promListString = "";
        promoteType = "";
        promoteMoney = BigDecimal.ZERO;
        increaseMoney = BigDecimal.ZERO;
        if (!swipeRefresh.isRefreshing()) {
            mWaitingDialog.show();
        }
//        HttpRequest.getProduceOrder(mActivity, new ICallback<ProducedOrderResult>() {
//            @Override
//            public void onSucceed(ProducedOrderResult result) {
//                mWaitingDialog.dismiss();
//                swipeRefresh.setRefreshing(false);
//                if ("996633".equals(result.getCode())) {
//                    showTips(true, result);
//                } else {
//                    showTips(false, result);
//                    producedOrder = result.getData();
//                    promList = producedOrder.getPromList();
//                    refreshView(true);
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                mWaitingDialog.dismiss();
//                swipeRefresh.setRefreshing(false);
//                btPay.setEnabled(false);
//                Uihelper.showToast(mActivity, error);
//            }
//        }, money, subjectId, prodId);
    }

    private void showTips(boolean flag, ProducedOrderResult result) {
        if (flag) {
            frameTip.setVisibility(View.VISIBLE);
//            frameSpace.setVisibility(View.GONE);
            btPay.setEnabled(false);
            textTip.setText(result.getMessage());
        } else {
            frameTip.setVisibility(View.GONE);
//            frameSpace.setVisibility(View.VISIBLE);
            btPay.setEnabled(true);
        }
    }

    private void refreshView(boolean initFlag) {
        expectMoney.setText(producedOrder.getPredictIncome());
        factMoney.setText(realMoney);
        initPayMode(initFlag);
        refreshPromoteView();
        refreshPayView();
    }

    /**
     * 红包列显示
     */
    private void refreshPromoteView() {
        if (promList != null && promList.size() > 0) {
            if (promoteMoney.compareTo(bFlag) > 0) {
                Promote promote = promList.get(position);
                if (Promote.TYPE.HB.getValue().equals(promoteType)) {
                    textPromoteType.setText("红包");
                } else {
                    textPromoteType.setText(promote.getToUseRate() + "%秒钱卡");
                }
                textPromote.setTextColor(getResources().getColor(R.color.mq_b1));
                textPromote.setText("少支付");
                textPromoteMoney.setText("" + promoteMoney);
                textPromoteUnit.setText("元");
            } else if (Promote.TYPE.JX.getValue().equals(promoteType)) {
                Promote promote = promList.get(position);
                textPromoteType.setText(promote.getUseableAmt() + "%加息卡");
                textPromote.setTextColor(getResources().getColor(R.color.mq_b1));
                textPromote.setText("收益增加");
                textPromoteMoney.setText("" + increaseMoney);
                textPromoteUnit.setText("元");
            } else if (Promote.TYPE.SK.getValue().equals(promoteType)) {
                textPromoteType.setText("双倍收益卡");
                textPromote.setTextColor(getResources().getColor(R.color.mq_b1));
                textPromote.setText("收益增加");
                textPromoteMoney.setText("" + increaseMoney);
                textPromoteUnit.setText("元");
            } else {
                textPromoteType.setText("红包/卡");
                textPromote.setText("");
                textPromoteMoney.setText("" + promList.size());
                textPromoteUnit.setText("张可用");
            }
        } else {
            textPromoteType.setText("红包/卡");
            textPromote.setTextColor(getResources().getColor(R.color.mq_b2));
            textPromote.setText("无可用");
            textPromoteMoney.setText("");
            textPromoteUnit.setText("");
        }
    }

    /**
     * 根据支付状态刷新view
     */
    private void refreshPayView() {
//        textErrorLian.setVisibility(View.GONE);
//        if (payModeState == PAY_MODE_LIAN) {
////            showLianView(true);
////            textLian.setText("" + payMoney);
//        } else {
//            showLianView(false);
            textPayMoney.setText("" + payMoney);
            textPayMoney.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
            textPayType.setTextColor(ContextCompat.getColor(this, R.color.mq_b1_v2));
//            if (payModeState == PAY_MODE_BALANCE) {
//                textPayType.setText("账户余额");
//                textPayTip.setText("可用" + producedOrder.getBalance() + "元");
//                imageType.setImageResource(R.drawable.balance_enable);
//                showErrorView(producedOrder.getBalance());
//            } else if (payModeState == PAY_MODE_BANK) {
//                String bankNo = bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
//                textPayType.setText(producedOrder.getBankName() + "(" + bankNo + ")");
//                textPayTip.setText("单笔限额" + producedOrder.getSingleAmtLimit() + "元， 单日限额" + producedOrder.getDayAmtLimit() + "元");
//                imageType.setImageResource(R.drawable.icon_bank);
//                imageLoader.displayImage(producedOrder.getBankUrlSmall(), imageType, options);
//            }
//        }
    }

    /**
     * 活期或余额不足的提示
     */
    private void showErrorView(BigDecimal balance) {
        if (payMoney.compareTo(balance) > 0) {
//            textErrorLian.setVisibility(View.VISIBLE);
            textPayType.setTextColor(ContextCompat.getColor(this, R.color.mq_b5_v2));
            textPayMoney.setTextColor(ContextCompat.getColor(this, R.color.mq_b5_v2));
            if (payModeState == PAY_MODE_BALANCE) {
                imageType.setImageResource(R.drawable.balance_disable);
//                textErrorLian.setText("账户余额不足，请充值或更换支付方式后，再进行认购");
            }
        }
    }

    /**
     * 活期或余额不足的toast提示
     */
    private boolean insufficeBalance() {
        if (payModeState == PAY_MODE_BALANCE) {
            if (payMoney.compareTo(producedOrder.getBalance()) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据服务端返回数据判断支付状态
     */
    private void initPayMode(boolean initFlag) {
        payMoney = needPayMoney();
        if (!initFlag) {
            return;
        }
        if (producedOrder != null) {
//            if ("1".equals(producedOrder.getSupportStatus()) && "1".equals(producedOrder.getBindCardStatus())) {
//                bankNumber = RSAUtils.decryptByPrivate(producedOrder.getBankCardNo());
//                if (payMoney.compareTo(producedOrder.getBalance()) > 0) {
//                    payModeState = PAY_MODE_BANK;
//                } else {
//                    payModeState = PAY_MODE_BALANCE;
//                }
//            } else {
//                payModeState = PAY_MODE_LIAN;
//            }
        } else {
            payModeState = PAY_MODE_BALANCE;
        }
    }

    private BigDecimal needPayMoney() {
        if (orderMoney.compareTo(promoteMoney) > 0) {
            return orderMoney.subtract(promoteMoney);
        } else {
            return BigDecimal.ZERO;
        }
    }

//    private void showLianView(boolean isShow) {
//        if (isShow) {
//            frameLianPay.setVisibility(View.VISIBLE);
//            framePayChoose.setVisibility(View.GONE);
//        } else {
//            frameLianPay.setVisibility(View.GONE);
//            framePayChoose.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public void initView() {
        getmTitle().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        textTip = (TextView) findViewById(R.id.text_tip);
        frameTip = (RelativeLayout) findViewById(R.id.frame_tip);
//        frameSpace = findViewById(R.id.frame_space);
//        frameLianPay = (RelativeLayout) findViewById(R.id.frame_lian_pay);
//        frameFact = (RelativeLayout) findViewById(R.id.frame_fact);
//        factMoney = (TextView) findViewById(R.id.fact_money);
        frameExpect = (RelativeLayout) findViewById(R.id.frame_expect);
//        textLian = (TextView) findViewById(R.id.text_lian);
        textPayType = (TextView) findViewById(R.id.text_pay_type);
        textPayMoney = (TextView) findViewById(R.id.text_pay_money);
//        textPayTip = (TextView) findViewById(R.id.text_pay_tip);
        imageType = (ImageView) findViewById(R.id.image_type);

        textProjectType = (TextView) findViewById(R.id.text_project_type);
        textProjectInfo = (TextView) findViewById(R.id.text_project_info);
        frameRedPackage = (RelativeLayout) findViewById(R.id.frame_red_package);

        frameRedPackage.setOnClickListener(this);
//        if (PRODID_CURRENT.equals(prodId)) {
//            frameRedPackage.setVisibility(View.GONE);
//            frameExpect.setVisibility(View.GONE);
//            textProjectType.setText("活期赚");
//            if (TextUtils.isEmpty(interestRateString)) {
//                textInterestRate.setText("年化收益：8%");
//            } else {
//                textInterestRate.setText("年化收益：" + interestRateString);
//            }
//        } else if (PRODID_REGULAR.equals(prodId)) {
//            textProjectType.setText("定期项目");
//            textInterestRate.setText("年化收益：" + interestRateString);
//        } else if (PRODID_REGULAR_PLAN.equals(prodId)) {
//            textProjectType.setText("定期计划");
//            textInterestRate.setText("年化收益：" + interestRateString);
//        } else if (PRODID_REGULAR_TRANSFER.equals(prodId)) {
//            frameRedPackage.setVisibility(View.GONE);
//            textProjectType.setText("定期项目转让");
//            textInterestRate.setText("预期年化：" + interestRateString);
////            frameFact.setVisibility(View.VISIBLE);
//        } else if (PRODID_REGULAR_PLAN_TRANSFER.equals(prodId)) {
//            frameRedPackage.setVisibility(View.GONE);
//            textProjectType.setText("定期计划转让");
//            textInterestRate.setText("预期年化：" + interestRateString);
////            frameFact.setVisibility(View.VISIBLE);
//        }


        btPay = (Button) findViewById(R.id.bt_pay);
        btPay.setOnClickListener(this);
        textOrderMoney = (TextView) findViewById(R.id.order_money);
        textOrderMoney.setText(money);
        expectMoney = (TextView) findViewById(R.id.expect_money);
        textPromote = (TextView) findViewById(R.id.text_promote);
        textPromoteType = (TextView) findViewById(R.id.text_promote_type);
        textPromoteMoney = (TextView) findViewById(R.id.text_promote_money);
        textPromoteUnit = (TextView) findViewById(R.id.text_promote_unit);
//        textErrorLian = (TextView) findViewById(R.id.text_error_lian);

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

    //产品协议
    public void textLawCickProduct(View v) {
        //活期
        if (PRODID_CURRENT.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_current_law);

        }//定期
        else if (PRODID_REGULAR.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_regular_law);

        } //定期计划
        else if (PRODID_REGULAR_PLAN.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_regplan_law);

        }
    }

    //资金管理协议
    public void textLawCickMoney(View v) {
        WebActivity.startActivity(mActivity, Urls.web_recharge_law);
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        if (PRODID_CURRENT.equals(productType)) {
            mTitle.setTitleText("秒钱宝认购");
        } else if (PRODID_REGULAR.equals(productType)) {
            mTitle.setTitleText("定期项目认购");
        } else if (PRODID_REGULAR_PLAN.equals(productType)) {
            mTitle.setTitleText("定期计划认购");
        } else {
            mTitle.setTitleText("确认订单");
        }
    }

    @Override
    protected String getPageName() {
        return "确认订单";
    }

    private void clickToPayOrder() {
//        if (payModeState == PAY_MODE_LIAN && payMoney != BigDecimal.ZERO) {
//            MobclickAgent.onEvent(mContext, "1067");
//            Intent intent = new Intent(CurrentInvestment.this, IntoActivity.class);
//            intent.putExtra("rollType", 1);
//            intent.putExtra("money", payMoney.toString());
//            startActivityForResult(intent, REQUEST_CODE_ROLLIN);
//        } else if (payModeState == PAY_MODE_BANK && payMoney != BigDecimal.ZERO) {
//            rollIn();
//        } else {
            MobclickAgent.onEvent(mContext, "1068");
            payOrder();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pay:
                MobclickAgent.onEvent(mActivity, "1069");
//                if (insufficeBalance()) {
//                    String tipString = "";
//                    if (payModeState == PAY_MODE_BALANCE) {
//                        tipString = "账户余额不足，请充值或更换支付方式后，再进行认购";
//                    }
//                    if (mDialogTip==null){
//                        mDialogTip=new DialogTip(mActivity) {};
//                    }
//                    mDialogTip.setInfo(tipString);
//                    mDialogTip.show();
//                    return;
//                }
                if ((PRODID_REGULAR_PLAN.equals(productType) || PRODID_REGULAR.equals(productType)) && promList != null && promList.size() > 0 && position < 0) {
                    showPackageTips();
                } else {
                    clickToPayOrder();
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
            if (resultCode == SUCCESS) {
                String orderNo = data.getStringExtra("orderNo");
//                payQuickOrder(orderNo);
//            } else if (resultCode == PROCESSING) {
//                CurrentInvestment.this.finish();
            } else if (resultCode == FAIL) {
                Uihelper.showToast(mActivity, "充值失败，请重新充值");
            }
        } else if (requestCode == REQUEST_CODE_REDPACKET) {
//          红包、拾财券选择
            if (resultCode == SUCCESS) {
                position = data.getIntExtra("position", -1);
                promoteMoney = BigDecimal.ZERO;
                increaseMoney = BigDecimal.ZERO;
                promListString = "";
                promoteType = "";
                if (position >= 0) {
                    Promote promote = promList.get(position);
                    promoteType = promote.getType();
                    if (Promote.TYPE.JX.getValue().equals(promote.getType())) {
                        increaseMoney = promote.getExtraIncome();
                    } else if (Promote.TYPE.SK.getValue().equals(promote.getType())) {
                        increaseMoney = promote.getExtraIncome();
                    } else {
                        promoteMoney = promote.getExtraIncome();
                    }
                    List<Promote> promListParam = new ArrayList<>();
                    promListParam.add(promote);
                    promListString = JSON.toJSONString(promListParam, true);
                }
                refreshView(false);
            }
//        } else if (requestCode == REQUEST_CODE_PAYMODE) {
//            if (resultCode == SUCCESS) {
//                payModeState = data.getIntExtra("payModeState", payModeState);
//                String balanceTemp = data.getStringExtra("balanceMoney");
//                if (!TextUtils.isEmpty(balanceTemp)) {
//                    producedOrder.setBalance(new BigDecimal(balanceTemp));
//                }
//                refreshPayView();
//            } else if (resultCode == PROCESSING) {
//                CurrentInvestment.this.finish();
//            }
//        } else if (requestCode == REQUEST_CODE_PASSWORD) {
//            if (resultCode == TypeUtil.TRADEPASSWORD_SETTING_SUCCESS) {
//                showTradeDialog(DialogTradePassword.TYPE_INPUTPASSWORD);
//            }
        }
    }

    private void showPackageTips() {
        if (packageTips == null) {
            packageTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {
                @Override
                public void positionBtnClick() {
                    clickToPayOrder();
                    dismiss();
                }

                @Override
                public void negativeBtnClick() {
                    frameRedPackage.performClick();
                }
            };
//            packageTips.setNegative(View.VISIBLE);
//            packageTips.setRemarksVisibility(View.GONE);
            packageTips.setNegative("选择红包/卡");
            packageTips.setPositive("认购");
            packageTips.setTitle("选择红包/卡");
            packageTips.setRemarks("您有未使用的红包/卡");
            packageTips.setCanceledOnTouchOutside(false);
        }
        packageTips.show();
    }

    private void payOrder() {
        HttpRequest.subscribeOrder(mActivity, new ICallback<SubscribeOrderResult>() {
            @Override
            public void onSucceed(SubscribeOrderResult result) {
                mWaitingDialog.dismiss();
                SubscribeOrder subscribeOrder = result.getData();
                if (result.getCode().equals("996633")) {
                    Uihelper.showToast(mActivity, result.getMessage());
                } else {
                    Intent intent = new Intent(CurrentInvestment.this, SubscribeResult.class);
                    intent.putExtra("money", money);
                    intent.putExtra("payMoney", payMoney.toString());
                    intent.putExtra("payModeState", payModeState);
                    intent.putExtra("promoteMoney", promoteMoney.toString());
                    if (result.getCode().equals("000000")) {
                        intent.putExtra("status", 1);
                        intent.putExtra("subscribeOrder", JSON.toJSONString(subscribeOrder));
                    } else {
                        intent.putExtra("status", 0);
                    }
                    startActivity(intent);
                    if (result.getCode().equals("000000")) {
                        CurrentInvestment.this.finish();
                    }
                }
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, money, subjectId, promListString);
    }


//        int status = Pref.getInt(UserUtil.getPrefKey(mActivity, Pref.PAY_STATUS), mActivity, 0);
//        if (status == 0) {
//            mWaitingDialog.show();
//            HttpRequest.getUserInfo(mActivity, new ICallback<UserInfoResult>() {
//                @Override
//                public void onSucceed(UserInfoResult result) {
//                    mWaitingDialog.dismiss();
//                    int status = Integer.parseInt(result.getData().getPayPwdStatus());
//                    showTradeDialog(status);
//                }
//
//                @Override
//                public void onFail(String error) {
//                    mWaitingDialog.dismiss();
//                    Uihelper.showToast(mActivity, error);
//                }
//            });
//        } else {
//            showTradeDialog(status);
//        }
//    }

//    private void showTradeDialog(int type) {
//        if (type == DialogTradePassword.TYPE_SETPASSWORD) {
//            Intent intent = new Intent(CurrentInvestment.this, SetPasswordActivity.class);
//            intent.putExtra("type", TypeUtil.TRADEPASSWORD_FIRST_SETTING);
//            startActivityForResult(intent, REQUEST_CODE_PASSWORD);
//            Uihelper.showToast(mActivity, "保障交易安全，请先设置交易密码");
//        } else {
//            initDialogTradePassword();
//            dialogTradePasswordInput.show();
//        }
//    }

//    private void initDialogTradePassword() {
//        if (dialogTradePasswordInput == null) {
//            dialogTradePasswordInput = new DialogTradePassword(mActivity, DialogTradePassword.TYPE_INPUTPASSWORD) {
//
//                @Override
//                public void positionBtnClick(String payPassword) {
//                    dismiss();
//                    //支付
//                    mWaitingDialog.show();
//                    HttpRequest.subscribeOrder(mActivity, new ICallback<SubscribeOrderResult>() {
//                        @Override
//                        public void onSucceed(SubscribeOrderResult result) {
//                            mWaitingDialog.dismiss();
//                            SubscribeOrder subscribeOrder = result.getData();
//                            if (result.getCode().equals("996633")) {
//                                Uihelper.showToast(mActivity, result.getMessage());
//                            } else if (result.getCode().equals("999992")) {
//                                showPwdError4Dialog(result.getMessage());
//                            } else {
//                                Intent intent = new Intent(CurrentInvestment.this, SubscribeResult.class);
//                                intent.putExtra("money", money);
//                                intent.putExtra("payMoney", payMoney.toString());
//                                intent.putExtra("payModeState", payModeState);
//                                intent.putExtra("promoteMoney", promoteMoney.toString());
//                                if (result.getCode().equals("000000")) {
//                                    intent.putExtra("status", 1);
//                                    intent.putExtra("subscribeOrder", JSON.toJSONString(subscribeOrder));
//                                } else {
//                                    intent.putExtra("status", 0);
//                                }
//                                startActivity(intent);
//                                if (result.getCode().equals("000000")) {
//                                    CurrentInvestment.this.finish();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFail(String error) {
//                            mWaitingDialog.dismiss();
//                            Uihelper.showToast(mActivity, error);
//                        }
//                    }, money, prodId, subjectId, promListString, prodListString);
//                }
//            };
//        }
//    }

//    private void showPwdError4Dialog(String message) {
//
//        if (dialogTips == null) {
//            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {
//                @Override
//                public void positionBtnClick() {
//                    MobclickAgent.onEvent(mActivity, "1028");
//                    Intent intent = new Intent(mActivity, TradePsCaptchaActivity.class);
//                    intent.putExtra("realNameStatus", "1");
//                    startActivity(intent);
//                    dismiss();
//                }
//
//                @Override
//                public void negativeBtnClick() {
//                    showTradeDialog(DialogTradePassword.TYPE_INPUTPASSWORD);
//                }
//            };
//            dialogTips.setNegative(View.VISIBLE);
//            dialogTips.setRemarks(message);
//            dialogTips.setNegative("继续尝试");
//            dialogTips.setPositive("找回密码");
//            dialogTips.setTitle("交易密码错误");
//            dialogTips.setCanceledOnTouchOutside(false);
//        }
//        dialogTips.show();
//
//    }

//    private void payQuickOrder(String orderNo) {
//        begin();
//        HttpRequest.subscribeQuickOrder(mActivity, new ICallback<SubscribeOrderResult>() {
//            @Override
//            public void onSucceed(SubscribeOrderResult result) {
//                end();
//                Intent intent = new Intent(CurrentInvestment.this, SubscribeResult.class);
//                SubscribeOrder subscribeOrder = result.getData();
//                if (result.getCode().equals("996633")) {
//                    Uihelper.showToast(mActivity, result.getMessage());
//                } else {
//                    intent.putExtra("money", money);
//                    intent.putExtra("payMoney", payMoney.toString());
//                    intent.putExtra("payModeState", payModeState);
//                    intent.putExtra("promoteMoney", promoteMoney.toString());
//                    if (result.getCode().equals("000000")) {
//                        intent.putExtra("status", 1);
//                        intent.putExtra("subscribeOrder", JSON.toJSONString(subscribeOrder));
//                    } else {
//                        intent.putExtra("status", 0);
//                    }
//                    startActivity(intent);
//                    if (result.getCode().equals("000000")) {
//                        CurrentInvestment.this.finish();
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//                end();
//                Uihelper.showToast(mActivity, error);
//            }
//        }, money, prodId, orderNo, subjectId, promListString, prodListString);
//    }

//    class MyHandler extends Handler {
//        WeakReference<CurrentInvestment> weakActivity;
//
//        public MyHandler(CurrentInvestment activity) {
//            weakActivity = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            textErrorLian.setVisibility(View.GONE);
//            String strRet = (String) msg.obj;
//            switch (msg.what) {
//                case Constants.RQF_PAY:
//                    JSONObject objContent = BaseHelper.string2JSON(strRet);
//                    String retCode = objContent.optString("ret_code");
//                    String retMsg = objContent.optString("ret_msg");
//                    String orderNo = payOrder.getNo_order();
//                    // //先判断状态码，状态码为 成功或处理中 的需要 验签
//                    if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
//                        String resulPay = objContent.optString("result_pay");
//                        if (Constants.RESULT_PAY_SUCCESS.equalsIgnoreCase(resulPay)) {
//                            // 支付成功后续处理
//                            payQuickOrder(orderNo);
//                        } else {
//                            Uihelper.showToast(mActivity, retMsg);
//                        }
//                    } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
//                        String resulPay = objContent.optString("result_pay");
//                        if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
//                            jumpToResult(CurrentInvestment.PROCESSING, money, orderNo);
//                        }
//                    } else if (retCode.equals("1006")) {
//                        Uihelper.showToast(mActivity, "您已取消当前交易");
//                    } else {
//                        IntoActivity.rollInError(mActivity, orderNo, strRet);
//                        textErrorLian.setVisibility(View.VISIBLE);
//                        String errorString = IntoActivity.showErrorString(mActivity, retCode);
//                        if (TextUtils.isEmpty(errorString)) {
//                            errorString = retMsg;
//                        }
//                        textErrorLian.setText(errorString);
//                    }
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    }

    /**
     * 跳转充值结果
     *
     * @param orderNo
     */
    private void jumpToResult(int status, String money, String orderNo) {
        Intent intent = new Intent(CurrentInvestment.this, IntoResultActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("money", money);
        intent.putExtra("orderNo", orderNo);
        startActivity(intent);
        CurrentInvestment.this.finish();
    }

    @Override
    public void onBackPressed() {
        MobclickAgent.onEvent(mActivity, "1070");
        finish();
    }
}
