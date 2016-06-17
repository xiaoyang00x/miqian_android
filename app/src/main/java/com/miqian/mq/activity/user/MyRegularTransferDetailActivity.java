package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.RegTransFerredDetail;
import com.miqian.mq.entity.RegTransFerredDetailResult;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by TLT on 2016/6/16.
 * <p/>
 * 我的定期转让详情
 */
public class MyRegularTransferDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_begin_countdown; // 开标倒计时
    private TextView tv_name; // 标的名称
    private TextView tv_description; // 标的描述
    private TextView tv_profit_rate; // 标的年利率
    private TextView tv_profit_rate_unit; // 标的年利率 单位
    private TextView tv_time_limit; // 标的期限
    private TextView tv_remain_amount; // 标的剩余金额
    private RegTransFerredDetail regTransFerredDetail;
    private View layoutRecord;
    private String investId;
    private String projectType;
    private TextView tvOriginProfit;
    private TextView tvBidamt;
    private TextView tvRealbidAmt;
    private TextView tvReginncome;
    private TextView tvOriginprojectName;
    private TextView tvState;
    private View layoutOriginProject;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        investId = intent.getStringExtra("investId");
        projectType = intent.getStringExtra("projectType");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {

        begin();
        HttpRequest.getRegTransFerredDetail(MyRegularTransferDetailActivity.this, "", new ICallback<RegTransFerredDetailResult>() {
            @Override
            public void onSucceed(RegTransFerredDetailResult result) {
                end();
                regTransFerredDetail = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(MyRegularTransferDetailActivity.this, error);
            }
        });
    }

    public void refreshView() {
        if (regTransFerredDetail == null) {
            return;
        }
        tv_name.setText(regTransFerredDetail.getBdNm());
        // 标的描述
        StringBuilder sb = new StringBuilder();
        sb.append(regTransFerredDetail.getChangeMod()
                + " | " + regTransFerredDetail.getBidLmtminAmt() + "元起投"
                + " | " + regTransFerredDetail.getBdCiMod());
        tv_description.setText(sb.toString());
        tv_profit_rate.setText(regTransFerredDetail.getPredictRate());
        tv_time_limit.setText(regTransFerredDetail.getLimitCnt());
        tvOriginProfit.setText(regTransFerredDetail.getOldYrt() + "%");
        tv_remain_amount.setText(regTransFerredDetail.getBidAmt() + "元");
        tvBidamt.setText(regTransFerredDetail.getBidAmt());
        tvRealbidAmt.setText(regTransFerredDetail.getRealBidAmt());
        tvReginncome.setText(regTransFerredDetail.getRegIncome());
        tvOriginprojectName.setText(regTransFerredDetail.getOriginalSubjectNm());

        String projectState = regTransFerredDetail.getProjectState();
        if (!TextUtils.isEmpty(projectState)) {
            if (projectState.equals("1")) {
                tvState.setText("转让中");
            }
            if (projectState.equals("2")) {
                tvState.setText("已转让");
            }
            if (projectState.equals("3")) {
                tvState.setText("转让未结息");
            }
        }
        updateProjectStatus();
    }

    @Override
    public void initView() {
        tv_begin_countdown = (TextView) findViewById(R.id.tv_begin_countdown);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_profit_rate = (TextView) findViewById(R.id.tv_profit_rate);
        tv_profit_rate_unit = (TextView) findViewById(R.id.tv_profit_rate_unit);
        tv_time_limit = (TextView) findViewById(R.id.tv_time_limit);
        tv_remain_amount = (TextView) findViewById(R.id.tv_remain_amount);
        tvOriginProfit = (TextView) findViewById(R.id.tv_origin_profit);

        tvBidamt = (TextView) findViewById(R.id.tv_bidamt);
        tvRealbidAmt = (TextView) findViewById(R.id.tv_realbidAmt);
        tvReginncome = (TextView) findViewById(R.id.tv_reginncome);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvOriginprojectName = (TextView) findViewById(R.id.tv_originproject_name);

        layoutRecord = findViewById(R.id.rlyt_buy_record);
        layoutOriginProject = findViewById(R.id.layout_originproject);
        layoutRecord.setOnClickListener(this);
        layoutOriginProject.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_myregular_transfer_detail;
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
    public void onClick(View view) {

        switch (view.getId()) {
            //查看认购记录
            case R.id.rlyt_buy_record:
                String prodId = regTransFerredDetail.getProdId();
                int subjectId = 0;
                if (!TextUtils.isEmpty(prodId)) {
                    subjectId = Integer.parseInt(prodId);
                }
                if (subjectId == 3) {
                    WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/" + prodId);
                } else if (subjectId == 4) {
                    WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/" + prodId);
                }
                break;
            case R.id.layout_originproject:

                if (regTransFerredDetail != null) {
                    String originId = regTransFerredDetail.getOriginalSubjectId();
                    if ("3".equals(regTransFerredDetail.getProdId())) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + originId + "/3");
                    } else if ("4".equals(regTransFerredDetail.getProdId())) {
                        //定期计划 项目匹配
                        WebActivity.startActivity(mActivity, Urls.project_match + originId);
                    }
                }

                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler();

    private long maxTime;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (maxTime <= 0) {
                // 倒计时结束(转让已结束)
                regTransFerredDetail.setProjectState("4");
                updateProjectStatus();
                obtainData();
            }
            tv_begin_countdown.setText(timeToString(maxTime));
            maxTime -= 1000L;
            mHandler.postDelayed(this, 1000L);
        }
    };


    public void updateProjectStatus() {
        //转让中
        if (regTransFerredDetail.getProjectState().equals("3")) {
            tv_begin_countdown.setVisibility(View.VISIBLE);
            setMaxTime(regTransFerredDetail.getBdEndTm() - System.currentTimeMillis());
            mHandler.post(mRunnable);
        } else {
            tv_begin_countdown.setVisibility(View.GONE);
        }
    }

    private void setMaxTime(long time) {
        maxTime = time;
    }

    // 时间差转换为*天*时*分*秒
    private StringBuilder timeToString(long between) {
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between - day * 24 * 60 * 60 * 1000) / (60 * 60 * 1000);
        long minute = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        return new StringBuilder("结束时间:").
                append(0 == day ? "" : day).append(0 == day ? "" : "天").
                append(0 == hour ? "" : hour).append(0 == hour ? "" : "时").
                append(0 == minute ? "" : minute).append(0 == minute ? "" : "分").
                append(0 == second ? "" : second).append(0 == second ? "" : "秒");
    }
}
