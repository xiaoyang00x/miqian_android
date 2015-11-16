package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.entity.RegularPlan;
import com.miqian.mq.entity.RegularPlanResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.RoundCornerProgressBar;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

/**
 * Created by guolei_wang on 15/9/25.
 */
public class RegularPlanActivity extends BaseActivity implements View.OnClickListener {
    public static final String KEY_SUBJECT_ID = "KEY_SUBJECT_ID";
    private String subjectId; //标的 ID
    private RegularPlan mData;

    private DialogPay dialogPay;

    private BigDecimal downLimit = new BigDecimal(100);
    private BigDecimal upLimit = new BigDecimal(9999999999L);
    private BigDecimal leftLimit = new BigDecimal(9999999999L);//标的剩余额度
    private String interestRateString = "";

    public static void startActivity(Context context, String subjectId) {
        Intent intent = new Intent(context, RegularPlanActivity.class);
        intent.putExtra(KEY_SUBJECT_ID, subjectId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        subjectId = getIntent().getStringExtra(KEY_SUBJECT_ID);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void obtainData() {

        getRegularDetails(subjectId);
    }

    Button btn_buy;
    Button btn_des_close;
    TextView tv_description;
    TextView tv_limit;
//    TextView tv_title;
    TextView tv_annurate_interest_rate;
    TextView tv_add_interest;
    TextView tv_lable1,tv_lable2,tv_lable3,tv_lable4;
    TextView tv_project_name,tv_fx,tv_bxbz,tv_hkfs;
    RoundCornerProgressBar progressBar;
    View layout_des;

    public void initView() {
        btn_buy = (Button)findViewById(R.id.btn_buy);
        btn_des_close = (Button)findViewById(R.id.btn_des_close);
        tv_description = (TextView)findViewById(R.id.tv_description);
        tv_limit = (TextView)findViewById(R.id.tv_limit);
//        tv_title = (TextView)findViewById(R.id.title);
        tv_annurate_interest_rate = (TextView)findViewById(R.id.tv_annurate_interest_rate);
        tv_add_interest = (TextView)findViewById(R.id.tv_add_interest);
        tv_lable1 = (TextView)findViewById(R.id.tv_lable1);
        tv_lable2 = (TextView)findViewById(R.id.tv_lable2);
        tv_lable3 = (TextView)findViewById(R.id.tv_lable3);
        tv_lable4 = (TextView)findViewById(R.id.tv_lable4);
        tv_project_name = (TextView)findViewById(R.id.tv_project_name);
        tv_fx = (TextView)findViewById(R.id.tv_fx);
        tv_bxbz = (TextView)findViewById(R.id.tv_bxbz);
        tv_hkfs = (TextView)findViewById(R.id.tv_hkfs);
        progressBar = (RoundCornerProgressBar)findViewById(R.id.progressBar);
        layout_des = findViewById(R.id.layout_des);

        dialogPay = new DialogPay(mActivity) {
            @Override
            public void positionBtnClick(String moneyString) {
                MobclickAgent.onEvent(mContext, "1059");
                if (!TextUtils.isEmpty(moneyString)) {
                    upLimit = leftLimit.compareTo(upLimit) < 0 ? leftLimit : upLimit;
                    BigDecimal money = new BigDecimal(moneyString);
                    BigDecimal remainder = money.remainder(downLimit);
                    if (money.compareTo(downLimit) == -1) {
                        this.setTitle("提示：请输入大于等于" + downLimit + "元");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else if (money.compareTo(upLimit) == 1) {
                        this.setTitle("提示：请输入小于等于" + upLimit + "元");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else if (remainder.compareTo(BigDecimal.ZERO) != 0) {
                        this.setTitle("提示：请输入" + downLimit + "的整数倍");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else {
                        UserUtil.currenPay(mActivity, moneyString, CurrentInvestment.PRODID_REGULAR_PLAN, subjectId, interestRateString);
                        this.setEditMoney("");
                        this.setTitle("认购金额");
                        this.setTitleColor(getResources().getColor(R.color.mq_b1));
                        this.dismiss();
                    }
                } else {
                    this.setTitle("提示：请输入金额");
                    this.setTitleColor(getResources().getColor(R.color.mq_r1));
                }
            }

            @Override
            public void negativeBtnClick() {
                MobclickAgent.onEvent(mContext, "1058");
                this.setEditMoney("");
                this.setTitle("认购金额");
                this.setTitleColor(getResources().getColor(R.color.mq_b1));
            }
        };
        btn_buy.setOnClickListener(this);
        btn_des_close.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regular_plan;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        getmTitle().setTitleText("定期计划");
        mTitle.setRightText("产品特点");
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(mContext, "1011");
                WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail);
            }
        });
    }

    @Override
    protected String getPageName() {
        return "定期计划";
    }

    private void updateUI() {
        if(mData == null) return;
        if(TextUtils.isEmpty(mData.getPromotionDesc())) {
            layout_des.setVisibility(View.GONE);
        }else {
            layout_des.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mData.getPromotionDescUrl())) {
                tv_description.setOnClickListener(this);
                tv_description.setText(mData.getPromotionDesc() + " >>");
            }else {
                tv_description.setText(mData.getPromotionDesc());
            }
        }
        tv_limit.setText("项目期限 " + mData.getLimit() + "天");
        tv_annurate_interest_rate.setText(mData.getYearInterest());
        if("Y".equalsIgnoreCase(mData.getPresentationYesNo())) {
            tv_add_interest.setVisibility(View.VISIBLE);
            tv_add_interest.setText(" +" + mData.getPresentationYearInterest() + "% ");
        }else {
            tv_add_interest.setVisibility(View.GONE);
        }
        tv_project_name.setText(mData.getSubjectName());
        tv_hkfs.setText(mData.getPayMode());
        progressBar.setProgress((new Float(mData.getPurchasePercent())).intValue());
        tv_fx.setText(mData.getDdbzf());
        tv_bxbz.setText(mData.getBxbzf());

        if (mData.getSubjectMaxBuy().compareTo(new BigDecimal(999999999999L)) == 0) {
            tv_lable1.setText("");
        } else {
            tv_lable1.setText("每人限额" + FormatUtil.formatAmount(mData.getSubjectMaxBuy()) + "元");
        }

        //待开标
        if ("00".equals(mData.getSubjectStatus())) {
            tv_lable2.setText(FormatUtil.formatDate(mData.getStartTimestamp(), "dd日 HH:mm:ss开售"));
        } else {
            tv_lable2.setText(Float.valueOf(mData.getPurchasePercent()).intValue() + "%");
        }
        tv_lable3.setText("剩余额度" + FormatUtil.formatAmount(mData.getSubjectTotalPrice().subtract(mData.getPurchasePrice())) + "元");
        tv_lable4.setText("标的总额" + FormatUtil.formatAmount(mData.getSubjectTotalPrice()) + "元");

        //待开标
        if ("00".equals(mData.getSubjectStatus())) {
            btn_buy.setText("待开标");
            btn_buy.setEnabled(false);
        } else if ("01".equals(mData.getSubjectStatus())) {
            //已开标
            btn_buy.setText("认购");
            btn_buy.setEnabled(true);
        } else {
            btn_buy.setText("已售罄");
            btn_buy.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_buy:
                MobclickAgent.onEvent(mContext, "1012");
                dialogPay.setEditMoneyHint(downLimit +"起，" + downLimit + "整数倍");
                UserUtil.loginPay(mActivity, dialogPay);
                break;
            case R.id.btn_des_close:
                layout_des.setVisibility(View.GONE);
                break;
            case R.id.tv_description:
                WebActivity.startActivity(mActivity, mData.getPromotionDescUrl());
                break;
            default:
                break;
        }
    }

    private void getRegularDetails(String subjectId) {
        begin();
        HttpRequest.getRegularDetails(this, "2", subjectId, new ICallback<RegularPlanResult>() {

            @Override
            public void onSucceed(RegularPlanResult result) {
                end();
                if(result != null) {
                    showContentView();
                    mData = result.getData();
                    downLimit = mData.getFromInvestmentAmount();
                    upLimit = mData.getSubjectMaxBuy();
                    leftLimit = mData.getSubjectTotalPrice().subtract(mData.getPurchasePrice());
                    BigDecimal tempInterest = new BigDecimal(mData.getYearInterest()).add(new BigDecimal(mData.getPresentationYearInterest()));
                    interestRateString = "年化收益率：" + tempInterest + "%  期限：" + mData.getLimit() + "天";
                    updateUI();
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(RegularPlanActivity.this, error);
                showErrorView();
            }
        });
    }
}
