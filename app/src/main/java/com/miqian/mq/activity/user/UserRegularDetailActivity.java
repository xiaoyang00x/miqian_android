package com.miqian.mq.activity.user;

import android.content.Intent;
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
import com.miqian.mq.entity.Operation;
import com.miqian.mq.entity.RegInvest;
import com.miqian.mq.entity.UserRegularDetail;
import com.miqian.mq.entity.UserRegularDetailResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRegularDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.text_capital)
    public TextView textCapital;

    @BindView(R.id.text_capital_money)
    public TextView textCapitalMoney;

    @BindView(R.id.text_interest_rate)
    public TextView textInterestRate;

    @BindView(R.id.text_interest_rate_present)
    public TextView textInterestRatePresent;

    @BindView(R.id.text_limit)
    public TextView textLimit;

    @BindView(R.id.text_date_start)
    public TextView textDateStart;

    @BindView(R.id.text_date_end)
    public TextView textDateEnd;

    @BindView(R.id.text_project_name)
    public TextView textProjectName;//项目名称

    @BindView(R.id.text_project)
    public TextView textProject;

    @BindView(R.id.text_repayment)
    public TextView textRepayment;

    @BindView(R.id.text_earning_type)
    public TextView textEarningType;

    @BindView(R.id.frame_project_match)
    public RelativeLayout frameProjectMatch;//项目匹配

    @BindView(R.id.text_earning)
    public TextView textEarning;

    @BindView(R.id.tv_date_first)
    public TextView tvDateFirst;

    @BindView(R.id.tv_content_first)
    public TextView tvContentFirst;

    @BindView(R.id.linear_record)
    public LinearLayout linearLayoutRecord;

    @BindView(R.id.layout_first)
    public LinearLayout layoutFirst;


    private String investId;//投资产品id
    private String clearYn;//Y:已结息  N:未结息
    private String subjectId;//标的id
    private List<Operation> operationList;
    //    private TextView tvTimeFirst;
    private boolean inProcess = false;
    private final Object mLock = new Object();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private ImageView ivProjectState;
    private RegInvest reginvest;

    @Override
    protected String getPageName() {
        String pageName = "";
        if (reginvest != null) {
            String productType = reginvest.getProductType();
            if ("1".equals(productType) || "97".equals(productType)) {
                pageName = "定期赚详情";

            } else {
                pageName = "定期赚详情";
            }
        }
        return pageName;
    }

    public void obtainData() {
        //查询标的记录
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        begin();
        HttpRequest.getUserRegularDetail(mActivity, new ICallback<UserRegularDetailResult>() {
            @Override
            public void onSucceed(UserRegularDetailResult result) {
                end();
                UserRegularDetail data = result.getData();
                if (data != null) {
                    operationList = data.getOperation();
                    productRecord();
                    setData(data);
                }
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, reginvest.getPurchaseSeqno());
    }

    public void setData(UserRegularDetail data) {
        String status = data.getStatus();
        if ("4".equals(status)) {//已结清
            textCapital.setText("投资本金(元)");
            textCapitalMoney.setText(data.getYsAmount());
            frameProjectMatch.setVisibility(View.GONE);
            textEarningType.setText("总收益(元)");
            textEarning.setText(data.getYsProfit());
        } else {
            textCapital.setText("待收本金(元)");
            textCapitalMoney.setText(data.getDsAmount());
            textEarningType.setText("待收收益(元)");
            textEarning.setText(data.getDsProfit());
        }

    }

    public void initView() {
        Intent intent = getIntent();
        reginvest = (RegInvest) intent.getSerializableExtra("reginvest");
        ButterKnife.bind(this);
        frameProjectMatch.setOnClickListener(this);
        findViewById(R.id.tv_referrecord).setOnClickListener(this);
        refreshView();
    }

    private void refreshView() {
        if (reginvest != null && reginvest.getProductName() != null) {
            textProjectName.setText(reginvest.getProductName());
        }
        if (reginvest != null && reginvest.getProductTerm() != null) {
            textLimit.setText(reginvest.getProductTerm());
        }
        if (reginvest != null && reginvest.getStartTime() != null) {
            textDateStart.setText("认购日期:" + reginvest.getStartTime());
        }
        if (reginvest != null && reginvest.getEndTime() != null) {
            textDateEnd.setText("结束日期:" + reginvest.getEndTime());
        }
        if (reginvest != null && reginvest.getRepayType() != null) {
            textRepayment.setText(reginvest.getRepayType());
        }

        if (reginvest != null) {
            String status = reginvest.getStatus();
            if (!"4".equals(status)) {
                if ("2".equals(reginvest.getProductType()) || "98".equals(reginvest.getProductType())) {//定期计划和定期计划转让
                    frameProjectMatch.setVisibility(View.VISIBLE);
                    textProject.setText("项目匹配");
                }
            }
            if ("2".equals(reginvest.getProductType()) || "98".equals(reginvest.getProductType())) {//定期赚和定期赚转让详情
                frameProjectMatch.setVisibility(View.VISIBLE);
                textProject.setText("项目详情");
            }
            String realInterest = reginvest.getProductRate();
            String presentInterest = reginvest.getProductPlusRate();
            String bidType = reginvest.getBidType();
            if (Constants.BID_TYPE_SBB.equals(bidType)) {
                if (!TextUtils.isEmpty(realInterest)) {
                    textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
                    textInterestRatePresent.setText("%");
                }
            } else if (Constants.BID_TYPE_SBSYK.equals(bidType)) {
                if (!TextUtils.isEmpty(realInterest) && !TextUtils.isEmpty(presentInterest)) {
                    float realprofit = Float.parseFloat(realInterest) * 2 + Float.parseFloat(presentInterest) * 2;
                    textInterestRate.setText(realprofit + "");
                    textInterestRatePresent.setText("%");
                }


            } else {
                if (!TextUtils.isEmpty(realInterest) && !TextUtils.isEmpty(presentInterest)) {
                    textInterestRate.setText(Float.parseFloat(realInterest) + "");
                    textInterestRatePresent.setText("+" + presentInterest + "%");
                }
            }
        }


//        int showType = CalculateUtil.getShowInterest(regInvest.getProjectState(), regInvest.getSubjectType(), regInvest.getRealInterest()
//                , regInvest.getPresentInterest(), regInvest.getTransedAmt());
//        String projectState = regInvest.getProjectState();
//
//        switch (showType) {
//            case CalculateUtil.INTEREST_SHOWTYPE_ONE:
//                showProjectImage(projectState, ivProjectState);
//                textInterestRatePresent.setText("%");
//                break;
//            case CalculateUtil.INTEREST_SHOWTYPE_TWO:
//                showProjectImage(projectState, ivProjectState);
//                if (!TextUtils.isEmpty(realInterest)) {
//                    textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
//                }
//                textInterestRatePresent.setText("%");
//                break;
//            case CalculateUtil.INTEREST_SHOWTYPE_THREE:
//                showProjectImage(projectState, ivProjectState);
//                textInterestRatePresent.setText("+" + presentInterest + "%");
//                break;
//            case CalculateUtil.INTEREST_SHOWTYPE_FOUR:
//                if (!TextUtils.isEmpty(realInterest)) {
//                    textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
//                }
//                textInterestRatePresent.setText("%");
//                ivProjectState.setVisibility(View.VISIBLE);
//                ivProjectState.setImageResource(R.drawable.double_rate_detail);
//                break;
//            case CalculateUtil.INTEREST_SHOWTYPE_FIVE:
//                textInterestRatePresent.setText("%");
//                break;
//            case CalculateUtil.INTEREST_SHOWTYPE_SIX:
//                if (!TextUtils.isEmpty(realInterest)) {
//                    textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
//                }
//                textInterestRatePresent.setText("%");
//                ivProjectState.setVisibility(View.VISIBLE);
//                ivProjectState.setImageResource(R.drawable.double_card_detail);
//                break;
//            case CalculateUtil.INTEREST_SHOWTYPE_SEVEN:
//                textInterestRatePresent.setText("+" + presentInterest + "%");
//                break;
//            default:
//                break;
//        }
    }

    private void productRecord() {
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
                View view = (View) itemRecord.findViewById(R.id.view);
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_regular_detail;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        if (reginvest != null) {
            String productType = reginvest.getProductType();
            if ("1".equals(productType) || "97".equals(productType)) {
                mTitle.setTitleText("定期赚详情");

            } else {
                mTitle.setTitleText("定期计划详情");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_project_match:
                MobclickAgent.onEvent(mActivity, "1045");
                if (reginvest == null) {
                    return;
                }
                if ("2".equals(reginvest.getProductType()) || "98".equals(reginvest.getProductType())) {//定期计划
                    HttpRequest.toRegularPlanDetail(mActivity, reginvest.getProductCode(), "");
                } else {
                    HttpRequest.toRegularDetail(mActivity, reginvest.getProductCode(), "");
                }

                break;
            case R.id.tv_referrecord:  //查看标的操作记录详情

                Intent intent = new Intent(this, OperationRecordAcitivity.class);
                if (reginvest != null) {
                    intent.putExtra("investId", reginvest.getPurchaseSeqno());
                }
                startActivity(intent);
                break;
//
//            case R.id.layout_transfer_detail:  //查看转让详情
//
//                Intent intentTransfer = new Intent(this, TransferDetailActivity.class);
//                intentTransfer.putExtra("investId", investId);
//                intentTransfer.putExtra("clearYn", clearYn);
//                startActivity(intentTransfer);
//                break;

        }
    }

}
