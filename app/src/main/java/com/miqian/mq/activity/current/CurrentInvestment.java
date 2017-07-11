package com.miqian.mq.activity.current;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
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
import com.miqian.mq.entity.ProductBaseInfo;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.entity.SubscribeOrder;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.JsonUtil;
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

    @BindView(R.id.expect_money)
    TextView expectMoney;
    @BindView(R.id.text_promote)
    TextView textPromote;
    @BindView(R.id.text_promote_type)
    TextView textPromoteType;
    @BindView(R.id.text_promote_money)
    TextView textPromoteMoney;
    @BindView(R.id.text_promote_unit)
    TextView textPromoteUnit;

    @BindView(R.id.frame_expect)
    RelativeLayout frameExpect; //预期收益
    @BindView(R.id.frame_red_package)
    RelativeLayout frameRedPackage; //红包/卡

    @BindView(R.id.text_project_type)
    TextView textProjectType;//项目名称
    @BindView(R.id.text_project_info)
    TextView textProjectInfo;//项目年化利率及期限
    @BindView(R.id.order_money)
    TextView textOrderMoney;//认购金额
    @BindView(R.id.text_balance)
    TextView textBalance;//余额

    @BindView(R.id.bt_pay)
    Button btPay;
    @BindView(R.id.bt_rollin)
    Button btRollin;

    @BindView(R.id.frame_tip)
    RelativeLayout frameTip;
    @BindView(R.id.text_tip)
    TextView textTip;

    @BindView(R.id.swipe_refresh)
    MySwipeRefresh swipeRefresh;

    private ProducedOrder producedOrder;
    private List<Promote> promList;
    private String promoteId = "";
    private String promoteType = "";

    private String money;
    private int productType; //1:定期项目 2:定期计划 3:秒钱宝
    private String productCode; //标的id
    //    private String interestRateString; //年化收益和期限
    private int position = -1;//使用的红包位置，用于获取list
    private ProductBaseInfo productInfo;

    private BigDecimal orderMoney;//订单金额
    private BigDecimal promoteMoney = BigDecimal.ZERO;//优惠金额： 红包、拾财券
    private BigDecimal increaseMoney = BigDecimal.ZERO;//加息金额
    //    private BigDecimal payMoney;//需支付的金额
    private BigDecimal bFlag = BigDecimal.ZERO;

    public static final int REQUEST_CODE_ROLLIN = 1;
    private static final int REQUEST_CODE_REDPACKET = 2;

    public static final int FAIL = 0;
    public static final int SUCCESS = 1;

    private CustomDialog packageTips;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        orderMoney = new BigDecimal(money);
        productInfo = JsonUtil.parseObject(intent.getStringExtra("productInfo"), ProductBaseInfo.class);
        productType = productInfo.getProductType();
        productCode = productInfo.getProductCode();
//        productType = Constants.PRODUCT_TYPE_REGULART_PLAN;
//        productCode = "MQBXSB11707051748001";
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

    public static void startActivity(Context context, String money, ProductBaseInfo productInfo) {
        Intent intent = new Intent(context, CurrentInvestment.class);
        intent.putExtra("money", money);
        intent.putExtra("productInfo", JSON.toJSONString(productInfo));
        context.startActivity(intent);
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

        textOrderMoney.setText(FormatUtil.formatAmount(orderMoney));
        frameRedPackage.setOnClickListener(this);

        if (Constants.PRODUCT_TYPE_MQB == productType) {
            frameExpect.setVisibility(View.GONE);
            frameRedPackage.setVisibility(View.GONE);

            btLaw1.setText("《秒钱宝服务协议》、");
            btLaw2.setText("《秒钱宝债权转让合同》、");
            btLaw3.setText("《秒钱宝收益转让合同》");
            textProjectType.setText(productInfo.getProductName());
            SpannableString spannableString = new SpannableString(productInfo.getProductRate() + "%");
            spannableString.setSpan(new TextAppearanceSpan(this, R.style.F7_R1_V2), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textProjectInfo.setText("");
            textProjectInfo.append("年化收益 ");
            textProjectInfo.append(spannableString);
        } else {
            frameExpect.setVisibility(View.VISIBLE);
            frameRedPackage.setVisibility(View.VISIBLE);
            if (Constants.PRODUCT_TYPE_REGULAR_PROJECT == productType) {
                textProjectType.setText("定期项目");
                btLaw1.setText("《定期项目服务协议》、");
                btLaw2.setText("《定期项目债权转让合同》、");
                btLaw3.setText("《定期项目收益转让合同》");
            } else {
                textProjectType.setText("定期计划");
                btLaw1.setText("《定期计划服务协议》、");
                btLaw2.setText("《定期计划债权转让合同》、");
                btLaw3.setText("《定期计划收益转让合同》");
            }
            SpannableString spannableString = new SpannableString(productInfo.getProductRate() + "%");
            spannableString.setSpan(new TextAppearanceSpan(this, R.style.F7_R1_V2), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textProjectInfo.setText("");
            textProjectInfo.append("年化收益 ");
            textProjectInfo.append(spannableString);
            textProjectInfo.append(" 期限 ");
            SpannableString spannableString2 = new SpannableString(productInfo.getProductTerm() + "天");
            spannableString2.setSpan(new TextAppearanceSpan(this, R.style.F7_R1_V2), 0, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textProjectInfo.append(spannableString2);
        }

        btPay.setOnClickListener(this);
        btRollin.setOnClickListener(this);

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
        if (Constants.PRODUCT_TYPE_MQB == productType) {
            WebActivity.startActivity(mActivity, Urls.web_current_law_server);
        } else if (Constants.PRODUCT_TYPE_REGULAR_PROJECT == productType) {
            WebActivity.startActivity(mActivity, Urls.web_regular_law_server);
        } else if (Constants.PRODUCT_TYPE_REGULART_PLAN == productType) {
            WebActivity.startActivity(mActivity, Urls.web_plan_law_server);
        }
    }

    //债权转让合同
    public void textLaw2(View v) {
        //秒钱宝
        if (Constants.PRODUCT_TYPE_MQB == productType) {
            WebActivity.startActivity(mActivity, Urls.web_current_law_claims);
        }//定期项目
        else if (Constants.PRODUCT_TYPE_REGULAR_PROJECT == productType) {
            WebActivity.startActivity(mActivity, Urls.web_regular_law_claims);
        } //定期计划
        else if (Constants.PRODUCT_TYPE_REGULART_PLAN == productType) {
            WebActivity.startActivity(mActivity, Urls.web_plan_law_claims);
        }
    }

    //收益权转让合同
    public void textLaw3(View v) {
        if (Constants.PRODUCT_TYPE_MQB == productType) {
            WebActivity.startActivity(mActivity, Urls.web_current_law_earnings);
        } else if (Constants.PRODUCT_TYPE_REGULAR_PROJECT == productType) {
            WebActivity.startActivity(mActivity, Urls.web_regular_law_earnings);
        } else if (Constants.PRODUCT_TYPE_REGULART_PLAN == productType) {
            WebActivity.startActivity(mActivity, Urls.web_plan_law_earnings);
        }
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        if (Constants.PRODUCT_TYPE_MQB == productType) {
            mTitle.setTitleText("秒钱宝认购");
        } else if (Constants.PRODUCT_TYPE_REGULAR_PROJECT == productType) {
            mTitle.setTitleText("定期项目认购");
        } else if (Constants.PRODUCT_TYPE_REGULART_PLAN == productType) {
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
                if ((Constants.PRODUCT_TYPE_REGULART_PLAN == productType || Constants.PRODUCT_TYPE_REGULAR_PROJECT == productType) && promList != null && promList.size() > 0 && position < 0) {
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
