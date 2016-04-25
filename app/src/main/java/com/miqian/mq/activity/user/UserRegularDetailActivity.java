package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.UserRegularDetail;
import com.miqian.mq.entity.UserRegularDetailResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.RoundCornerProgressBar;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

public class UserRegularDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView textCapital;
    private TextView textCapitalMoney;
    private TextView textInterestRate;

    private TextView textLimit;
    private ImageView imageProjectStatus;
    private TextView textDateStart;
    private TextView textDateEnd;

    private RoundCornerProgressBar progressBar;

    private RelativeLayout frameProjectMatch;//项目匹配
    private TextView textProjectName;//项目名称
    private TextView textRepayment;

    private TextView textEarningType;
    private TextView textEarning;
    private TextView textTransferMoney;

    private RelativeLayout frameTransfer;//转让情况
//    private RelativeLayout frameContract;//合同

    private Button btRepayment;//还款详情

    private UserRegularDetail userRegularDetail;

    private String investId;//投资产品id
    private String clearYn;//Y:已结息  N:未结息
    private String projectType;//Y:已结息  N:未结息
    private String subjectId;//标的id

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        investId = intent.getStringExtra("investId");
        clearYn = intent.getStringExtra("clearYn");
        projectType = intent.getStringExtra("projectType");
        super.onCreate(bundle);
    }

    @Override
    protected String getPageName() {
        if (projectType.equals("3")) {
            return "定期赚详情";
        } else {
            return "定期计划详情";
        }
    }

    public void obtainData() {
        begin();
        HttpRequest.getUserRegularDetail(mActivity, new ICallback<UserRegularDetailResult>() {
            @Override
            public void onSucceed(UserRegularDetailResult result) {
                end();
                userRegularDetail = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, investId, clearYn);
    }

    public void initView() {
        textCapital = (TextView) findViewById(R.id.text_capital);
        textCapitalMoney = (TextView) findViewById(R.id.text_capital_money);
        textInterestRate = (TextView) findViewById(R.id.text_interest_rate);

        textLimit = (TextView) findViewById(R.id.text_limit);
        imageProjectStatus = (ImageView) findViewById(R.id.image_project_status);
        textDateStart = (TextView) findViewById(R.id.text_date_start);
        textDateEnd = (TextView) findViewById(R.id.text_date_end);

        progressBar = (RoundCornerProgressBar) findViewById(R.id.progress_bar);

        textProjectName = (TextView) findViewById(R.id.text_project_name);
        frameProjectMatch = (RelativeLayout) findViewById(R.id.frame_project_match);
        textRepayment = (TextView) findViewById(R.id.text_repayment);

        textEarningType = (TextView) findViewById(R.id.text_earning_type);
        textEarning = (TextView) findViewById(R.id.text_earning);

        textTransferMoney = (TextView) findViewById(R.id.text_transfer_money);
        frameTransfer = (RelativeLayout) findViewById(R.id.frame_transfer);
//        frameContract = (RelativeLayout) findViewById(R.id.frame_contract);

        btRepayment = (Button) findViewById(R.id.bt_repayment);

        frameProjectMatch.setOnClickListener(this);
        frameTransfer.setOnClickListener(this);
//        frameContract.setOnClickListener(this);
        btRepayment.setOnClickListener(this);
    }

    public void refreshView() {
        if (userRegularDetail == null) {
            return;
        }
        textInterestRate.setText(userRegularDetail.getRealInterest());

        textLimit.setText("项目期限：" + userRegularDetail.getLimitCnt() + "天");

        if ("Y".equals(userRegularDetail.getBearingStatus())) {
            textCapital.setText("投资本金：");
            textCapitalMoney.setText(userRegularDetail.getPrnAmt());
            frameProjectMatch.setVisibility(View.GONE);
            textEarningType.setText("总收益");
            textEarning.setText(userRegularDetail.getRegAssert());
            if ("2".equals(userRegularDetail.getProjectState())) {
                imageProjectStatus.setImageResource(R.drawable.user_regular_transfer_gray_detail);
            } else if ("3".equals(userRegularDetail.getProjectState())) {
                imageProjectStatus.setImageResource(R.drawable.user_regular_over_detail);
            }
        } else if ("N".equals(userRegularDetail.getBearingStatus())) {
            textCapital.setText("待收本金：");
            textCapitalMoney.setText(userRegularDetail.getRegAmt());
            if ("1".equals(userRegularDetail.getProjectState())) {
                frameProjectMatch.setVisibility(View.VISIBLE);
                imageProjectStatus.setImageResource(R.drawable.user_regular_transfering_detail);
            } else if ("2".equals(userRegularDetail.getProjectState())) {
                frameProjectMatch.setVisibility(View.GONE);
                imageProjectStatus.setImageResource(R.drawable.user_regular_transfer_red_detail);
            } else {
                frameProjectMatch.setVisibility(View.VISIBLE);
                imageProjectStatus.setImageResource(R.color.transparent);
            }
            textEarningType.setText("待收收益");
            textEarning.setText(userRegularDetail.getRegIncome());
        }
        if ("3".equals(userRegularDetail.getProdId())) {
            frameProjectMatch.setVisibility(View.VISIBLE);
            textProjectName.setText(userRegularDetail.getBdNm());
        } else {
            textProjectName.setText("项目匹配");
        }

        if ("N".equals(userRegularDetail.getHasTransOper())) {
            frameTransfer.setVisibility(View.GONE);
        } else if ("Y".equals(userRegularDetail.getHasTransOper())) {
            frameTransfer.setVisibility(View.VISIBLE);
            textTransferMoney.setText("已转让" + userRegularDetail.getTransed());
        }

        textDateStart.setText(userRegularDetail.getCrtDt() + "起");
        textDateEnd.setText(userRegularDetail.getDueDt() + "结束");
        progressBar.setProgress((new Float(userRegularDetail.getTimeProgress())).intValue());
        textRepayment.setText(userRegularDetail.getPayMeansName());
    }

    public int getLayoutId() {
        return R.layout.user_regular_detail;
    }

    public void initTitle(WFYTitle mTitle) {
        if (projectType.equals("3")) {
            mTitle.setTitleText("定期赚详情");
        } else {
            mTitle.setTitleText("定期计划详情");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_project_match:
                MobclickAgent.onEvent(mActivity, "1045");
                if (userRegularDetail != null) {
                    subjectId = userRegularDetail.getBdId();
                    if ("3".equals(userRegularDetail.getProdId())) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3");
                    } else if ("4".equals(userRegularDetail.getProdId())) {
                        //定期计划 项目匹配
                        WebActivity.startActivity(mActivity, Urls.project_match + subjectId);
                    }
                }
                break;
            case R.id.frame_transfer://转让情况
                if (userRegularDetail != null) {
                    Intent intent = new Intent(mActivity, TransferDetailActivity.class);
                    intent.putExtra("investId", userRegularDetail.getId());
                    intent.putExtra("clearYn", userRegularDetail.getBearingStatus());
                    startActivity(intent);
                }
                break;
//            case R.id.frame_contract://查看合同
//                WebActivity.startActivity(mActivity, "https://www.baidu.com/");
//                break;
            case R.id.bt_repayment://还款详情
                MobclickAgent.onEvent(mActivity, "1044");
                if (userRegularDetail != null) {
                    Intent intent = new Intent(mActivity, RepaymentActivity.class);
                    intent.putExtra("investId", userRegularDetail.getId());
                    startActivity(intent);
                }
                break;
        }
    }
}
