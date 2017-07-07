package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.rollin.IntoActivity;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.entity.ProducedOrder;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.entity.SubscribeOrder;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jackie on 2015/9/19.
 */
public class CurrentInvestment extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.bt_law1)
    Button btLaw1;//xxx服务协议
    @BindView(R.id.bt_law2)
    Button btLaw2;//xxx债权转让合同
    @BindView(R.id.bt_law3)
    Button btLaw3;//xxx收益转让合同

    private TextView expectMoney;
    private TextView textPromote;
    private TextView textPromoteType;
    private TextView textPromoteMoney;
    private TextView textPromoteUnit;

    private RelativeLayout frameExpect; //预期收益
    private RelativeLayout frameRedPackage; //红包/卡
    private RelativeLayout frameContract; //查看合同

    private TextView textProjectType;//项目名称
    private TextView textProjectInfo;//项目年化利率及期限
    private TextView textOrderMoney;//认购金额
    private TextView textBalance;//余额

    private Button btPay;
    private Button btRollin;

    private RelativeLayout frameTip;
    private TextView textTip;
    private MySwipeRefresh swipeRefresh;

    private ProducedOrder producedOrder;
    private List<Promote> promList;
    private String promoteId = "";
    private String promoteType = "";

    private String money;
    private String productType; //1:定期项目 2:定期计划 3:秒钱宝
    private String productCode; //标的id，活期默认为0
    //    private String interestRateString; //年化收益和期限
    private int position = -1;//使用的红包位置，用于获取list
//    private PayOrder payOrder;

    private BigDecimal orderMoney;//订单金额
    private BigDecimal promoteMoney = BigDecimal.ZERO;//优惠金额： 红包、拾财券
    private BigDecimal increaseMoney = BigDecimal.ZERO;//加息金额
    //    private BigDecimal payMoney;//需支付的金额
    private BigDecimal bFlag = BigDecimal.ZERO;

    public static final int REQUEST_CODE_ROLLIN = 1;
    private static final int REQUEST_CODE_REDPACKET = 2;
    private static final int REQUEST_CODE_PAYMODE = 3;
    private static final int REQUEST_CODE_PASSWORD = 4;

    public static final int FAIL = 0;
    public static final int SUCCESS = 1;
    public static final int PROCESSING = 2;

    public static final String PRODID_REGULAR = "1";
    public static final String PRODID_REGULAR_PLAN = "2";
    public static final String PRODID_CURRENT = "3";

    private CustomDialog packageTips;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        orderMoney = new BigDecimal(money);
        productType = intent.getStringExtra("productType");
//        productCode = intent.getStringExtra("productCode");
        productCode = "MQBXSB11707051748001";
//        productCode = "DQJHPTB1707032039001";
//        interestRateString = intent.getStringExtra("interestRateString");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        refreshData();
    }

    private void refreshData() {
        position = -1;
        promoteId = "";
        promoteType = "";
        promoteMoney = BigDecimal.ZERO;
        increaseMoney = BigDecimal.ZERO;
        if (!swipeRefresh.isRefreshing()) {
            begin();
        }
        HttpRequest.getProduceOrder(mActivity, new ICallback<MqResult<ProducedOrder>>() {
            @Override
            public void onSucceed(MqResult<ProducedOrder> result) {
                end();
                swipeRefresh.setRefreshing(false);
                if ("996633".equals(result.getCode())) {
                    showTips(true, result);
                } else {
                    showTips(false, result);
                    producedOrder = result.getData();
                    promList = producedOrder.getPromList();
                    refreshView(true);
                }
            }

            @Override
            public void onFail(String error) {
                end();
                swipeRefresh.setRefreshing(false);
                btPay.setEnabled(false);
                Uihelper.showToast(mActivity, error);
            }
        }, money, productCode);
    }

    private void showTips(boolean flag, MqResult<ProducedOrder> result) {
        if (flag) {
            frameTip.setVisibility(View.VISIBLE);
            btPay.setEnabled(false);
            textTip.setText(result.getMessage());
        } else {
            frameTip.setVisibility(View.GONE);
            btPay.setEnabled(true);
        }
    }

    private void refreshView(boolean initFlag) {
        textBalance.setText(FormatUtil.formatAmount(producedOrder.getBalance()));
        expectMoney.setText(producedOrder.getPredictIncome());

        initPayMode(initFlag);
        refreshPromoteView();
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
                textPromote.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b1));
                textPromote.setText("少支付");
                textPromoteMoney.setText("" + promoteMoney);
                textPromoteUnit.setText("元");
            } else if (Promote.TYPE.JX.getValue().equals(promoteType)) {
                Promote promote = promList.get(position);
                textPromoteType.setText(promote.getUsableAmt() + "%加息卡");
                textPromote.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b1));
                textPromote.setText("收益增加");
                textPromoteMoney.setText("" + increaseMoney);
                textPromoteUnit.setText("元");
            } else if (Promote.TYPE.SK.getValue().equals(promoteType)) {
                textPromoteType.setText("双倍收益卡");
                textPromote.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b1));
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
            textPromote.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b2));
            textPromote.setText("无可用");
            textPromoteMoney.setText("");
            textPromoteUnit.setText("");
        }
    }

    /**
     * 根据服务端返回数据判断支付状态
     */
    private void initPayMode(boolean initFlag) {
        if (orderMoney.subtract(promoteMoney).compareTo(producedOrder.getBalance()) > 0) {
            btPay.setEnabled(false);
        } else {
            btPay.setEnabled(true);
        }
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        getmTitle().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        textTip = (TextView) findViewById(R.id.text_tip);
        frameTip = (RelativeLayout) findViewById(R.id.frame_tip);

        textProjectType = (TextView) findViewById(R.id.text_project_type);
        textProjectInfo = (TextView) findViewById(R.id.text_project_info);

        textOrderMoney = (TextView) findViewById(R.id.order_money);
        textOrderMoney.setText(FormatUtil.formatAmountStr(money));

        frameExpect = (RelativeLayout) findViewById(R.id.frame_expect);
        expectMoney = (TextView) findViewById(R.id.expect_money);

        frameRedPackage = (RelativeLayout) findViewById(R.id.frame_red_package);
        frameRedPackage.setOnClickListener(this);
        textPromote = (TextView) findViewById(R.id.text_promote);
        textPromoteType = (TextView) findViewById(R.id.text_promote_type);
        textPromoteMoney = (TextView) findViewById(R.id.text_promote_money);
        textPromoteUnit = (TextView) findViewById(R.id.text_promote_unit);

        textBalance = (TextView) findViewById(R.id.text_balance);

        frameContract = (RelativeLayout) findViewById(R.id.frame_contract);
        frameContract.setOnClickListener(this);

        if (PRODID_CURRENT.equals(productType)) {
            frameExpect.setVisibility(View.GONE);
            frameRedPackage.setVisibility(View.GONE);
            frameContract.setVisibility(View.GONE);

            btLaw1.setText("《秒钱宝服务协议》、");
            btLaw2.setText("《秒钱宝债权转让合同》、");
            btLaw3.setText("《秒钱宝收益转让合同》");
//            textProjectType.setText("");
//            textProjectInfo.setText("");
        } else {
            frameExpect.setVisibility(View.VISIBLE);
            frameRedPackage.setVisibility(View.VISIBLE);
            frameContract.setVisibility(View.VISIBLE);
            if (PRODID_REGULAR.equals(productType)) {
                btLaw1.setText("《定期计划服务协议》、");
                btLaw2.setText("《定期项目债权转让合同》、");
                btLaw3.setText("《定期项目收益转让合同》");
            } else {
                btLaw1.setText("《定期计划服务协议》、");
                btLaw2.setText("《定期计划债权转让合同》、");
                btLaw3.setText("《定期计划收益转让合同》");
            }
//            textProjectType.setText("");
//            textProjectInfo.setText("");
        }

        btPay = (Button) findViewById(R.id.bt_pay);
        btPay.setOnClickListener(this);
        btRollin = (Button) findViewById(R.id.bt_rollin);
        btRollin.setOnClickListener(this);

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

    //服务协议
    public void textLaw1(View v) {
        if (PRODID_CURRENT.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_current_law_server);
        } else if (PRODID_REGULAR.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_regular_law_server);
        } else if (PRODID_REGULAR_PLAN.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_plan_law_server);
        }
    }

    //债权转让合同
    public void textLaw2(View v) {
        //秒钱宝
        if (PRODID_CURRENT.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_current_law_claims);
        }//定期项目
        else if (PRODID_REGULAR.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_regular_law_claims);
        } //定期计划
        else if (PRODID_REGULAR_PLAN.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_plan_law_claims);
        }
    }

    //收益权转让合同
    public void textLaw3(View v) {
        if (PRODID_CURRENT.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_current_earning);
        } else if (PRODID_REGULAR.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_regular_law_earnings);
        } else if (PRODID_REGULAR_PLAN.equals(productType)) {
            WebActivity.startActivity(mActivity, Urls.web_plan_law_earnings);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pay:
                MobclickAgent.onEvent(mActivity, "1069");
                if ((PRODID_REGULAR_PLAN.equals(productType) || PRODID_REGULAR.equals(productType)) && promList != null && promList.size() > 0 && position < 0) {
                    showPackageTips();
                } else {
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
            case R.id.frame_contract:
                break;
            case R.id.bt_rollin:
                Intent intent = new Intent(CurrentInvestment.this, IntoActivity.class);
                intent.putExtra("rollType", 1);
                startActivityForResult(intent, REQUEST_CODE_ROLLIN);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ROLLIN) {
            if (resultCode == SUCCESS) {
                Uihelper.showToast(mActivity, "充值成功");
                refreshData();
            } else if (resultCode == FAIL) {
                Uihelper.showToast(mActivity, "充值失败，请重新充值");
            }
        } else if (requestCode == REQUEST_CODE_REDPACKET) {
//          红包、拾财券选择
            if (resultCode == SUCCESS) {
                position = data.getIntExtra("position", -1);
                promoteMoney = BigDecimal.ZERO;
                increaseMoney = BigDecimal.ZERO;
                promoteId = "";
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
                    promoteId = promote.getId();
                }
                refreshView(false);
            }
        }
    }

    private void showPackageTips() {
        if (packageTips == null) {
            packageTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {
                @Override
                public void positionBtnClick() {
                    payOrder();
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
        begin();
        HttpRequest.subscribeOrder(mActivity, new ICallback<MqResult<SubscribeOrder>>() {
            @Override
            public void onSucceed(MqResult<SubscribeOrder> result) {
                end();
                SubscribeOrder subscribeOrder = result.getData();
                if (result.getCode().equals("996633")) {
                    Uihelper.showToast(mActivity, result.getMessage());
                } else {
                    Intent intent = new Intent(CurrentInvestment.this, SubscribeResult.class);
                    if (result.getCode().equals("000000")) {
                        intent.putExtra("status", 1);
                        intent.putExtra("productType", productType);
                    } else {
                        intent.putExtra("status", 0);
                        intent.putExtra("errorReason", result.getMessage());
                    }
                    intent.putExtra("subscribeOrder", JSON.toJSONString(subscribeOrder));
                    startActivity(intent);
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, money, productCode, promoteId);
    }

    @Override
    public void onBackPressed() {
        MobclickAgent.onEvent(mActivity, "1070");
        finish();
    }
}
