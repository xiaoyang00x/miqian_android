package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.Operation;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.UserRegularDetail;
import com.miqian.mq.entity.UserRegularDetailResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.CalculateUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class UserRegularDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView textCapital;
    private TextView textCapitalMoney;
    private TextView textInterestRate;

    private TextView textLimit;
    private TextView textDateStart;
    private TextView textDateEnd;

    private RelativeLayout frameProjectMatch;//项目匹配
    private TextView textProjectName;//项目名称
    private TextView textRepayment;

    private TextView textEarningType;
    private TextView textEarning;
    private TextView textTransferMoney;


    private UserRegularDetail userRegularDetail;

    private String investId;//投资产品id
    private String clearYn;//Y:已结息  N:未结息
    private String projectType;//  1 为定期赚 2 为定期计划
    private String subjectId;//标的id
    private TextView textProject;
    private TextView textInterestRatePresent;
    private UserRegularDetail.RegInvest regInvest;
    private List<Operation> operationList;
    private TextView tvContentFirst;
    private TextView tvDateFirst;
//    private TextView tvTimeFirst;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private LinearLayout linearLayoutRecord;
    private TextView tvOriginproject;
    private TextView tvOriginprojectName;
    private View layoutPriginproject;
    private ImageView ivProjectState;
    private View layoutTransferDetail;
    private TextView tvTransferedMoney;
    private LinearLayout layoutFirst;

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
        if (projectType.equals("1")) {
            return "定期项目详情";
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
                regInvest = userRegularDetail.getRegInvest();
                operationList = userRegularDetail.getOperation();
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
        textInterestRatePresent = (TextView) findViewById(R.id.text_interest_rate_present);

        textLimit = (TextView) findViewById(R.id.text_limit);
        textDateStart = (TextView) findViewById(R.id.text_date_start);
        textDateEnd = (TextView) findViewById(R.id.text_date_end);

        textProjectName = (TextView) findViewById(R.id.text_project_name);
        textProject = (TextView) findViewById(R.id.text_project);
        frameProjectMatch = (RelativeLayout) findViewById(R.id.frame_project_match);
        textRepayment = (TextView) findViewById(R.id.text_repayment);

        textEarningType = (TextView) findViewById(R.id.text_earning_type);
        textEarning = (TextView) findViewById(R.id.text_earning);
        textTransferMoney = (TextView) findViewById(R.id.text_transfer_money);

        tvDateFirst = (TextView) findViewById(R.id.tv_date_first);
//        tvTimeFirst = (TextView) findViewById(R.id.tv_time_first);
        tvContentFirst = (TextView) findViewById(R.id.tv_content_first);
        linearLayoutRecord = (LinearLayout) findViewById(R.id.linear_record);
        layoutFirst = (LinearLayout) findViewById(R.id.layout_first);

        //购买转让的Ui
        layoutPriginproject = findViewById(R.id.layout_originproject);
        tvOriginproject = (TextView) findViewById(R.id.tv_originproject);
        tvOriginprojectName = (TextView) findViewById(R.id.tv_originproject_name);
        ivProjectState = (ImageView) findViewById(R.id.image_project_status);

        //转让记录
        layoutTransferDetail = findViewById(R.id.layout_transfer_detail);
        tvTransferedMoney = (TextView) findViewById(R.id.tv_transfered_money);


        frameProjectMatch.setOnClickListener(this);
        findViewById(R.id.tv_referrecord).setOnClickListener(this);
        layoutPriginproject.setOnClickListener(this);
        layoutTransferDetail.setOnClickListener(this);

    }

    public void refreshView() {
        if (userRegularDetail == null) {
            return;
        }
        textProjectName.setText(regInvest.getBdNm());
        textLimit.setText(regInvest.getLimitCnt());

        if ("Y".equals(regInvest.getBearingStatus())) {
            textCapital.setText("投资本金(元)");
            textCapitalMoney.setText(regInvest.getPrnAmt());
            frameProjectMatch.setVisibility(View.GONE);
            textEarningType.setText("总收益(元)");
            textEarning.setText(regInvest.getPrnIncome());
        } else if ("N".equals(regInvest.getBearingStatus())) {
            textCapital.setText("待收本金(元)");
            textCapitalMoney.setText(regInvest.getRegAmt());
            textEarningType.setText("待收收益(元)");
            textEarning.setText(regInvest.getRegIncome());
        }
        textDateStart.setText("认购日期:" + regInvest.getCrtDt());
        textDateEnd.setText("结束日期:" + regInvest.getDueDt());
        textRepayment.setText(regInvest.getPayMeansName());

        //标的相关记录
        layoutFirst.setVisibility(View.VISIBLE);
        if (operationList.size() > 0) {
            tvContentFirst.setText(operationList.get(0).getOperationContent());
            String operationDt = operationList.get(0).getOperationDt();
            if (!TextUtils.isEmpty(operationDt)) {
                String dt = Uihelper.timestampToDateStr_other(Long.parseLong(operationDt));
                String[] split = dt.split(" ");
                tvDateFirst.setText(split[0]);
//                tvTimeFirst.setText(split[1]);
            }
        }
        if (operationList.size() > 1) {
            findViewById(R.id.view_red).setVisibility(View.VISIBLE);
            for (int i = 1; i < operationList.size(); i++) {
                View itemRecord = LayoutInflater.from(this).inflate(R.layout.item_record, null);
                TextView tvDate = (TextView) itemRecord.findViewById(R.id.tv_date);
//                TextView tvTime = (TextView) itemRecord.findViewById(R.id.tv_time);
                TextView tvContent = (TextView) itemRecord.findViewById(R.id.tv_content);
                View view = itemRecord.findViewById(R.id.view);
                ImageView ivProcess = (ImageView) itemRecord.findViewById(R.id.iv_process);
                String operationDt = operationList.get(i).getOperationDt();
                if (!TextUtils.isEmpty(operationDt)) {
                    String dt = Uihelper.timestampToDateStr_other(Long.parseLong(operationDt));
                    String[] split = dt.split(" ");
                    tvDate.setText(split[0]);
//                    tvTime.setText(split[1]);
                }
                int state = operationList.get(i).getState();
                if (state == 0) {
                    ivProcess.setImageResource(R.drawable.process_grey);
                    view.setBackgroundResource(R.color.mq_b5_v2);
                } else {
                    ivProcess.setImageResource(R.drawable.process_red);
                    view.setBackgroundResource(R.color.mq_r1_v2);
                    tvDate.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
                    tvContent.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
                }
                tvContent.setText(operationList.get(i).getOperationContent());
                if (i == operationList.size() - 1) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Uihelper.dip2px(mContext, 3), Uihelper.dip2px(mContext, 25));
                    view.setLayoutParams(layoutParams);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.CENTER_HORIZONTAL);//addRule参数对应RelativeLayout XML布局的属性
                }
                linearLayoutRecord.addView(itemRecord);
            }
        }
        if ("N".equals(regInvest.getBearingStatus())) {
            if ("5".equals(regInvest.getProdId()) || "6".equals(regInvest.getProdId())) {//定期计划和定期计划转让
                frameProjectMatch.setVisibility(View.VISIBLE);
                textProject.setText("项目匹配");
            }
        }
        if ("3".equals(regInvest.getProdId()) || "4".equals(regInvest.getProdId())) {//定期赚和定期赚转让详情
            frameProjectMatch.setVisibility(View.VISIBLE);
            textProject.setText("项目详情");
        }
        if ("Y".equals(regInvest.getHasTransOper())) {//有转让，N未转让
            layoutTransferDetail.setVisibility(View.VISIBLE);
            tvTransferedMoney.setText("已转让" + regInvest.getTransedAmt());
        }

        String realInterest = regInvest.getRealInterest();
        String presentInterest = regInvest.getPresentInterest();
        textInterestRate.setText(realInterest);
        int showType = CalculateUtil.getShowInterest(regInvest.getProjectState(), regInvest.getSubjectType(), regInvest.getRealInterest()
                , regInvest.getPresentInterest(), regInvest.getTransedAmt());
        String projectState = regInvest.getProjectState();

        switch (showType) {
            case CalculateUtil.INTEREST_SHOWTYPE_ONE:
                showProjectImage(projectState, ivProjectState);
                textInterestRatePresent.setText("%");
                break;
            case CalculateUtil.INTEREST_SHOWTYPE_TWO:
                showProjectImage(projectState, ivProjectState);
                if (!TextUtils.isEmpty(realInterest)) {
                    textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
                }
                textInterestRatePresent.setText("%");
                break;
            case CalculateUtil.INTEREST_SHOWTYPE_THREE:
                showProjectImage(projectState, ivProjectState);
                textInterestRatePresent.setText("+" + presentInterest + "%");
                break;
            case CalculateUtil.INTEREST_SHOWTYPE_FOUR:
                if (!TextUtils.isEmpty(realInterest)) {
                    textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
                }
                textInterestRatePresent.setText("%");
                ivProjectState.setVisibility(View.VISIBLE);
                ivProjectState.setImageResource(R.drawable.double_rate_detail);
                break;
            case CalculateUtil.INTEREST_SHOWTYPE_FIVE:
                textInterestRatePresent.setText("%");
                break;
            case CalculateUtil.INTEREST_SHOWTYPE_SIX:
                if (!TextUtils.isEmpty(realInterest)) {
                    textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
                }
                textInterestRatePresent.setText("%");
                ivProjectState.setVisibility(View.VISIBLE);
                ivProjectState.setImageResource(R.drawable.double_card_detail);
                break;
            case CalculateUtil.INTEREST_SHOWTYPE_SEVEN:
                textInterestRatePresent.setText("+" + presentInterest + "%");
                break;
            default:
                break;
        }

    }

    private void showProjectImage(String projectState, ImageView ivProjectState) {
        ivProjectState.setVisibility(View.VISIBLE);
        if ("2".equals(projectState)) {
            ivProjectState.setImageResource(R.drawable.transfer_detail_ing);
        } else if ("3".equals(projectState)) {
            ivProjectState.setImageResource(R.drawable.transfer_detail_ed);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_regular_detail;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        if (projectType.equals("1")) {
            mTitle.setTitleText("定期项目详情");
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
                    subjectId = regInvest.getBdId();
                    if (RegularBase.REGULAR_PROJECT == Integer.parseInt(regInvest.getProdId())) {//定期项目
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3");
                    } else if (RegularBase.REGULAR_PLAN == Integer.parseInt(regInvest.getProdId())) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/5");//定期计划
                    } else if (RegularBase.REGULAR_04 == Integer.parseInt(regInvest.getProdId())) {//定期项目转让
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + regInvest.getSysbdId() + "/3");
                    } else if (RegularBase.REGULAR_06 == Integer.parseInt(regInvest.getProdId())) {//定期计划转让
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + regInvest.getSysbdId() + "/5");
                    }
                }
                break;
            case R.id.layout_originproject:
                if (userRegularDetail != null) {
                    String subjectId = regInvest.getSysbdId();
                    if (RegularBase.REGULAR_04 == Integer.parseInt(regInvest.getProdId())) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3");
                    } else if (RegularBase.REGULAR_06 == Integer.parseInt(regInvest.getProdId())) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/5");
                    }
                }
                break;
            case R.id.tv_referrecord:  //查看标的操作记录详情

                Intent intent = new Intent(this, OperationRecordAcitivity.class);
                intent.putExtra("investId", investId);
                startActivity(intent);
                break;

            case R.id.layout_transfer_detail:  //查看转让详情

                Intent intentTransfer = new Intent(this, TransferDetailActivity.class);
                intentTransfer.putExtra("investId", investId);
                intentTransfer.putExtra("clearYn", clearYn);
                startActivity(intentTransfer);
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
