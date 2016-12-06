package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.RegTransDetail;
import com.miqian.mq.entity.RegTransDetailResult;
import com.miqian.mq.entity.TransFer;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.DialogTransferTip;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涂良坛 on 2016/6/6.
 * 发起定期转让
 */
public class LaunchTransferRegularAcitivity extends BaseActivity implements View.OnLayoutChangeListener, View.OnClickListener {

    private TextView textProject;
    private TextView textCapitalMoney;
    private TextView textLimit;
    private TextView textInterestRate;
    private TextView textDateStart;
    private TextView textDateEnd;
    private TextView textRepayment;
    private TextView textPrnTransSa; //可转让金额
    private TextView textRemindDay;  //剩余期限
    private EditText etTransferRate;//转让后的年化收益
    private EditText etMoney;  //转让金额
    private TextView textChangeMoney; //加价，减价
    private TextView textChangeMoneyRight;
    private RegTransDetail regTransDetail;
    private String investId;
    private int screenHeight;
    private int keyHeight;
    private View activityRootView;
    private TextView btnClick;
    private ScrollView scrollview;
    private String projectType;
    private String investmentAmt;//起投金额
    private TextView tvPresentProfit;
    private BigDecimal profit;
    private String stringDiscountRate;
    private BigDecimal disCountMoney;
    private String mMoney;
    private String transferRate;
    private TextView textFinalMoney;//转让成功后收入
    private DecimalFormat fnum;
    private BigDecimal discountRate;//折让比
    private BigDecimal leftCnt;//剩余期限

    @Override
    public void onCreate(Bundle arg0) {

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        Intent intent = getIntent();
        investId = intent.getStringExtra("investId");
        projectType = intent.getStringExtra("projectType");
        super.onCreate(arg0);
    }

    @Override
    protected void onResume() {
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
        super.onResume();
    }

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getRegTransDetail(this, investId, "N", new ICallback<RegTransDetailResult>() {
            @Override
            public void onSucceed(RegTransDetailResult result) {
                end();
                regTransDetail = result.getData();
                reRfeshView();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(LaunchTransferRegularAcitivity.this, error);
            }
        });
    }

    private void reRfeshView() {
        if (regTransDetail != null) {

            leftCnt = regTransDetail.getLeftCnt();

            textLimit.setText(regTransDetail.getLimitCnt());
            textCapitalMoney.setText(regTransDetail.getRegAmt());
            textProject.setText(regTransDetail.getBdNm());
            textDateStart.setText(regTransDetail.getCrtDt() + "认购");
            textDateEnd.setText(regTransDetail.getDueDt() + "结束");

            String realInterest = regTransDetail.getRealInterest();
            textInterestRate.setText(realInterest);
            String presentInterest = regTransDetail.getPresentInterest();

            BigDecimal floatRealInterst = new BigDecimal(realInterest);
            BigDecimal floatPresentInterst = BigDecimal.ZERO;
            if (!TextUtils.isEmpty(presentInterest)) {
                tvPresentProfit.setText("+" + presentInterest + "%");
                floatPresentInterst = new BigDecimal(presentInterest);
            }
            //实际年化收益
            profit = floatRealInterst.add(floatPresentInterst);

            textRepayment.setText(regTransDetail.getPayMeansName());
            textPrnTransSa.setText(regTransDetail.getPrnTransSa());
            textRemindDay.setText(leftCnt + "");
            investmentAmt = regTransDetail.getInvestmentAmt();
            etMoney.setHint(investmentAmt + "元起转");
        }

    }

    @Override
    public void initView() {

        textProject = (TextView) findViewById(R.id.text_project);
        textCapitalMoney = (TextView) findViewById(R.id.text_capital_money);
        textLimit = (TextView) findViewById(R.id.text_limit);
        textInterestRate = (TextView) findViewById(R.id.text_interest_rate);
        tvPresentProfit = (TextView) findViewById(R.id.tv_present_profit);
        textDateStart = (TextView) findViewById(R.id.text_date_start);
        textDateEnd = (TextView) findViewById(R.id.text_date_end);
        textRepayment = (TextView) findViewById(R.id.text_repayment);
        textPrnTransSa = (TextView) findViewById(R.id.text_prnTransSa);
        textRemindDay = (TextView) findViewById(R.id.text_remindday);
        textChangeMoney = (TextView) findViewById(R.id.tv_changemoney);
        textFinalMoney = (TextView) findViewById(R.id.tv_final_money);

        textChangeMoneyRight = (TextView) findViewById(R.id.tv_changemoney_right);
        btnClick = (TextView) findViewById(R.id.btn_click);

        etMoney = (EditText) findViewById(R.id.et_money);
        etTransferRate = (EditText) findViewById(R.id.et_transfer_rate);

        activityRootView = findViewById(R.id.layout_main);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        btnClick.setOnClickListener(this);
        findViewById(R.id.tv_question).setOnClickListener(this);
        etMoney.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void myAfterTextChanged(Editable s) {
                String money = s.toString();
                try {
                    if (money.matches(FormatUtil.PATTERN_MONEY)) {
                        String inputProfit = etTransferRate.getText().toString();
                        if (!TextUtils.isEmpty(money) && !TextUtils.isEmpty(inputProfit)) {
                            handleData(money, inputProfit);
                        } else {
                            textChangeMoney.setText("--.--");
                            textFinalMoney.setText("--.--");
                        }
                        return;
                    }
                    s.delete(money.length() - 1, money.length());

                } catch (Exception ignored) {
                }
            }
        });
        etTransferRate.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void myAfterTextChanged(Editable s) {
                String money = etMoney.getText().toString();
                String inputProfit = s.toString();
                if (!TextUtils.isEmpty(money) && !TextUtils.isEmpty(inputProfit)) {
                    handleData(money, inputProfit);
                } else {
                    textChangeMoney.setText("--.--");
                    textFinalMoney.setText("--.--");
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_launch_transfer;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        if (projectType.equals("3")) {
            mTitle.setTitleText("定期赚转让");
        } else {
            mTitle.setTitleText("定期计划转让");
        }
    }

    @Override
    protected String getPageName() {
        if (projectType.equals("3")) {
            return "定期赚转让";
        } else {
            return "定期计划转让";
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            btnClick.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollview.scrollTo(0, Uihelper.dip2px(mContext, 280));
                }
            }, 500);
        }
    }

    private void handleData(String money, String finalprofit) {
        //计算折让比
        BigDecimal f_transferrate = new BigDecimal(finalprofit);
        BigDecimal temp = new BigDecimal(365).divide(leftCnt, 8, BigDecimal.ROUND_HALF_EVEN).add(f_transferrate.divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_EVEN));
        discountRate = ((f_transferrate.subtract(profit))).divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_EVEN).divide(temp, 8, BigDecimal.ROUND_HALF_EVEN);
        fnum = new DecimalFormat("##0.00");
//        stringDiscountRate = fnum.format(discountRate);
        //计算折让金
        BigDecimal tempMoney = new BigDecimal(money);
        disCountMoney = discountRate.multiply(tempMoney);
        if (disCountMoney.compareTo(BigDecimal.ZERO) > 0) {
            textChangeMoney.setTextColor(ContextCompat.getColor(this, R.color.mq_b4_v2));
            textChangeMoneyRight.setTextColor(ContextCompat.getColor(this, R.color.mq_b4_v2));
        } else {
            textChangeMoney.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
            textChangeMoneyRight.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
        }

        textChangeMoney.setText(fnum.format(disCountMoney.abs()) + "");

        //转让后收入
        BigDecimal finalMoney = tempMoney.subtract(disCountMoney);
        textFinalMoney.setText(fnum.format(finalMoney) + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_question:
                //可转让金额说明
                WebActivity.startActivity(mContext, Urls.web_transferamt_expaint);
                break;
            case R.id.btn_click:

                mMoney = etMoney.getText().toString();
                transferRate = etTransferRate.getText().toString();
                if (TextUtils.isEmpty(mMoney)) {
                    Uihelper.showToast(mActivity, "转让金额不能为空");
                    return;
                }
                BigDecimal tempMoney = new BigDecimal(mMoney);
                BigDecimal minMoney = new BigDecimal(regTransDetail.getInvestmentAmt());
                if (minMoney.compareTo(tempMoney) > 0) {
                    Uihelper.showToast(mActivity, "转让金额不能小于" + minMoney + "元");
                    return;
                }
                //可转让金额
                BigDecimal prnTransSa = new BigDecimal(regTransDetail.getPrnTransSa());
                if (tempMoney.compareTo(prnTransSa) > 0) {
                    Uihelper.showToast(mActivity, "转让金额不能大于" + minMoney + "元");
                    return;
                }

                if (TextUtils.isEmpty(transferRate)) {
                    Uihelper.showToast(mActivity, "转让后年化收益不能为空");
                    return;
                }
                BigDecimal inputProfit = new BigDecimal(transferRate);
                BigDecimal transferOverLow = regTransDetail.getTransferOverLow();
                BigDecimal transferOverTop = regTransDetail.getTransferOverTop();
                BigDecimal subtractProfit = inputProfit.subtract(profit);


                BigDecimal disCount = new BigDecimal(fnum.format(disCountMoney));

                int result = subtractProfit.compareTo(transferOverTop);
                if (result == 0) {
                    //输入交易密码
//                    showTradeDialog();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("您设定转让后年化收益为")
                        .append(transferRate)
                        .append("%。")
                        .append("让利金额")
                        .append(disCount.abs());
                if (subtractProfit.compareTo(transferOverTop) > 0) {
                    sb.append("元, 让利金额过高,可能造成本金损失过多,是否继续？");
                } else if (subtractProfit.compareTo(transferOverLow) < 0) {
                    sb.append("元, 让利金额过低,可能影响您的转让结果,是否继续？");
                }
                DialogTransferTip dialogTips = new DialogTransferTip(LaunchTransferRegularAcitivity.this, sb.toString()) {
                    @Override
                    public void positionBtnClick() {
                        //输入交易密码
                        dismiss();

                    }
                };
                dialogTips.show();
                break;
            default:
                break;
        }

    }


    private void rollOut(String password) {

        DecimalFormat fnumFour = new DecimalFormat("##0.0000");
        //拼接transferList
        TransFer tranfer = new TransFer();
        tranfer.setDiscountRate(fnumFour.format(discountRate));
        tranfer.setInvestId(investId);
        BigDecimal bdTransferRate = new BigDecimal(transferRate);
        BigDecimal tempBigdecimal = bdTransferRate.divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_EVEN);
        tranfer.setPredictRate(fnumFour.format(tempBigdecimal) + "");

        BigDecimal temp = new BigDecimal(mMoney);
        tranfer.setPrinciple(fnum.format(temp) + "");

        tranfer.setSubjectId(regTransDetail.getBdId());
        tranfer.setProdId(regTransDetail.getProdId());
        List<TransFer> list = new ArrayList<>();
        list.add(tranfer);
        String transferList = JSON.toJSONString(list);
        begin();
        HttpRequest.launchTransfer(this, fnum.format(temp) + "", password, transferList, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                end();
                Intent intent = new Intent(LaunchTransferRegularAcitivity.this, UserRegularActivity.class);
                intent.putExtra("launchSuccess", true);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(LaunchTransferRegularAcitivity.this, error);
            }
        });
    }
}
