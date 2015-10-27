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

import java.math.BigDecimal;

/**
 * Created by guolei_wang on 15/9/25.
 */
public class RegularPlanActivity extends BaseActivity implements View.OnClickListener {
    public static final String KEY_SUBJECT_ID = "KEY_SUBJECT_ID";
    private String subjectId; //标的 ID

    private DialogPay dialogPay;

    private BigDecimal downLimit = BigDecimal.ONE;
    private BigDecimal upLimit = new BigDecimal(9999999999L);
    private BigDecimal continueLimit = new BigDecimal(100);//递增
    private String interestRateString = "";

    public static void startActivity(Context context, String subjectId) {
        Intent intent = new Intent(context, RegularPlanActivity.class);
        intent.putExtra(KEY_SUBJECT_ID, subjectId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectId = getIntent().getStringExtra(KEY_SUBJECT_ID);
        getRegularDetails(subjectId);
    }

    @Override
    public void obtainData() {

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
                if (!TextUtils.isEmpty(moneyString)) {
                    BigDecimal money = new BigDecimal(moneyString);
                    BigDecimal remainder = money.remainder(continueLimit);
                    if (money.compareTo(downLimit) == -1) {
                        this.setTitle("提示：请输入大于等于" + downLimit + "元");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else if (money.compareTo(upLimit) == 1) {
                        this.setTitle("提示：请输入小于等于" + upLimit + "元");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else if (remainder.compareTo(BigDecimal.ZERO) != 0) {
                        this.setTitle("提示：请输入" + continueLimit + "的整数倍");
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
                WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail);
            }
        });
    }

    @Override
    protected String getPageName() {
        return "定期计划";
    }

    private void updateUI(RegularPlan data) {
        if(data == null) return;
        if(TextUtils.isEmpty(data.getPromotionDesc())) {
            layout_des.setVisibility(View.GONE);
        }else {
            layout_des.setVisibility(View.VISIBLE);
            tv_description.setText(data.getPromotionDesc());
        }
        tv_limit.setText("项目期限 " + data.getLimit() + "天");
        tv_annurate_interest_rate.setText(data.getYearInterest());
        if("Y".equalsIgnoreCase(data.getPresentationYesNo())) {
            tv_add_interest.setVisibility(View.VISIBLE);
            tv_add_interest.setText(" +" + data.getPresentationYearInterest() + "% ");
        }else {
            tv_add_interest.setVisibility(View.GONE);
        }
        tv_project_name.setText(data.getSubjectName());
        tv_hkfs.setText(data.getPayMode());
        progressBar.setProgress((new Float(data.getPurchasePercent())).intValue());
        tv_fx.setText(data.getDdbzf());
        tv_bxbz.setText(data.getBxbzf());

        if (data.getSubjectMaxBuy().compareTo(new BigDecimal(999999999999L)) == 0) {
            tv_lable1.setText("");
        } else {
            tv_lable1.setText("每人限额" + FormatUtil.formatAmount(data.getSubjectMaxBuy()) + "元");
        }

        //待开标
        if ("00".equals(data.getSubjectStatus())) {
            tv_lable2.setText(FormatUtil.formatDate(data.getStartTimestamp(), "dd日 hh:mm:ss开售"));
        } else {
            tv_lable2.setText(Float.valueOf(data.getPurchasePercent()).intValue() + "%");
        }
        tv_lable3.setText("剩余额度" + FormatUtil.formatAmount(data.getSubjectTotalPrice().subtract(data.getPurchasePrice())) + "元");
        tv_lable4.setText("标的总额" + FormatUtil.formatAmount(data.getSubjectTotalPrice()) + "元");

        //待开标
        if ("00".equals(data.getSubjectStatus())) {
            btn_buy.setText("待开标");
            btn_buy.setEnabled(false);
        } else if ("01".equals(data.getSubjectStatus())) {
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
                dialogPay.setEditMoneyHint(downLimit +"起，" + continueLimit + "整数倍");
                UserUtil.loginPay(mActivity, dialogPay);
                break;
            case R.id.btn_des_close:
                layout_des.setVisibility(View.GONE);
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
                    RegularPlan regularPlan = result.getData();
                    downLimit = regularPlan.getFromInvestmentAmount();
                    upLimit = regularPlan.getSubjectMaxBuy();
                    continueLimit = regularPlan.getContinueInvestmentLimit();
                    BigDecimal tempInterest = new BigDecimal(regularPlan.getYearInterest()).add(new BigDecimal(regularPlan.getPresentationYearInterest()));
                    interestRateString = "年化收益率：" + tempInterest + "%  期限：" + regularPlan.getLimit() + "天";
                    updateUI(result.getData());
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(RegularPlanActivity.this, error);
            }
        });
    }
}
