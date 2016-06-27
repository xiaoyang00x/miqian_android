package com.miqian.mq.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

//    private RelativeLayout frameTransfer;//转让情况
//    private RelativeLayout frameContract;//合同

    private Button btRepayment;//还款详情

    private UserRegularDetail userRegularDetail;

    private String investId;//投资产品id
    private String clearYn;//Y:已结息  N:未结息
    private String projectType;//Y:已结息  N:未结息
    private String subjectId;//标的id
    private TextView textProject;
    //    private Button btnClick;
    private TextView textInterestRatePresent;
    private UserRegularDetail.RegInvest regInvest;
    private List<Operation> operationList;
    private TextView tvContentFirst;
    private TextView tvDateFirst;
    private TextView tvTimeFirst;
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
//        btnClick = (Button) findViewById(R.id.btn_click);

        textProjectName = (TextView) findViewById(R.id.text_project_name);
        textProject = (TextView) findViewById(R.id.text_project);
        frameProjectMatch = (RelativeLayout) findViewById(R.id.frame_project_match);
        textRepayment = (TextView) findViewById(R.id.text_repayment);

        textEarningType = (TextView) findViewById(R.id.text_earning_type);
        textEarning = (TextView) findViewById(R.id.text_earning);
        textTransferMoney = (TextView) findViewById(R.id.text_transfer_money);

        tvDateFirst = (TextView) findViewById(R.id.tv_date_first);
        tvTimeFirst = (TextView) findViewById(R.id.tv_time_first);
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
//        btnClick.setOnClickListener(this);
        findViewById(R.id.tv_referrecord).setOnClickListener(this);
        layoutPriginproject.setOnClickListener(this);
        layoutTransferDetail.setOnClickListener(this);

    }

    public void refreshView() {
        if (userRegularDetail == null) {
            return;
        }
        textInterestRate.setText(regInvest.getRealInterest());
        String presentInterest = regInvest.getPresentInterest();
        if (TextUtils.isEmpty(presentInterest)) {
            textInterestRatePresent.setText("%");
        } else {
            if (0 == (Double.parseDouble(presentInterest))) {
                textInterestRatePresent.setText("%");
            } else {
                textInterestRatePresent.setText("+" + presentInterest + "%");
            }
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
//        if (regInvest.getTransFlag().equals("Y")) {
//            btnClick.setEnabled(true);
//        } else {
//            btnClick.setEnabled(false);
//            btnClick.setBackgroundColor(ContextCompat.getColor(this, R.color.mq_b4_v2));
//        }
//        btnClick.setText(regInvest.getTransDesc());

        //标的相关记录
        layoutFirst.setVisibility(View.VISIBLE);
        if (operationList.size() > 0) {
            tvContentFirst.setText(operationList.get(0).getOperationContent());
            String operationDt = operationList.get(0).getOperationDt();
            if (!TextUtils.isEmpty(operationDt)) {
                String dt = Uihelper.timestampToDateStr_other(Long.parseLong(operationDt));
                String[] split = dt.split(" ");
                tvDateFirst.setText(split[0]);
                tvTimeFirst.setText(split[1]);
            }
        }
        if (operationList.size() > 1) {
            findViewById(R.id.view_grey).setVisibility(View.VISIBLE);
            for (int i = 1; i < operationList.size(); i++) {
                View itemRecord = LayoutInflater.from(this).inflate(R.layout.item_record, null);
                TextView tvDate = (TextView) itemRecord.findViewById(R.id.tv_date);
                TextView tvTime = (TextView) itemRecord.findViewById(R.id.tv_time);
                TextView tvContent = (TextView) itemRecord.findViewById(R.id.tv_content);
                View view = (View) itemRecord.findViewById(R.id.view);
                String operationDt = operationList.get(i).getOperationDt();
                if (!TextUtils.isEmpty(operationDt)) {
                    String dt = Uihelper.timestampToDateStr_other(Long.parseLong(operationDt));
                    String[] split = dt.split(" ");
                    tvDate.setText(split[0]);
                    tvTime.setText(split[1]);
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
            if ("3".equals(regInvest.getProdId())) {//定期赚
                frameProjectMatch.setVisibility(View.VISIBLE);
                textProject.setText("项目详情");
            } else if ("4".equals(regInvest.getProdId())) {//定期赚转让
                layoutPriginproject.setVisibility(View.VISIBLE);
                tvOriginproject.setVisibility(View.VISIBLE);
                tvOriginprojectName.setText(regInvest.getBdNm());
            } else if ("5".equals(regInvest.getProdId())) {//定期计划
                frameProjectMatch.setVisibility(View.VISIBLE);
                textProject.setText("项目匹配");
            } else if ("6".equals(regInvest.getProdId())) {//定期计划转让
                layoutPriginproject.setVisibility(View.VISIBLE);
                tvOriginproject.setVisibility(View.VISIBLE);
                tvOriginprojectName.setText(regInvest.getBdNm());
            }
        }
        String projectState = regInvest.getProjectState();

        if ("0".equals(projectState) || "1".equals(projectState) || "2".equals(projectState)) {
            layoutTransferDetail.setVisibility(View.VISIBLE);
            ivProjectState.setVisibility(View.VISIBLE);
            tvTransferedMoney.setText("已转让" + regInvest.getTransedAmt());
            if ("0".equals(projectState)) {
                ivProjectState.setImageResource(R.drawable.user_regular_transfering);
            } else if ("1".equals(projectState)) {
                ivProjectState.setImageResource(R.drawable.user_regular_transfer_wjx);
            } else {
                ivProjectState.setImageResource(R.drawable.user_regular_transfered);
            }
        }
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
                    subjectId = regInvest.getBdId();
                    if (RegularBase.REGULAR_03 == Integer.parseInt(regInvest.getProdId())) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3");
                    } else if (RegularBase.REGULAR_05 == Integer.parseInt(regInvest.getProdId())) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/5");
                    }
                }
                break;
//            case R.id.frame_contract://查看合同
//                WebActivity.startActivity(mActivity, "https://www.baidu.com/");
//                break;
//            case R.id.bt_repayment://还款详情
//                MobclickAgent.onEvent(mActivity, "1044");
//                if (userRegularDetail != null) {
//                    Intent intent = new Intent(mActivity, RepaymentActivity.class);
//                    intent.putExtra("investId", userRegularDetail.getId());
//                    startActivity(intent);
//                }
//                break;
//            case R.id.btn_click:
//                Intent intent_launch = new Intent(UserRegularDetailActivity.this, LaunchTransferRegularAcitivity.class);
//                intent_launch.putExtra("investId", investId);
//                intent_launch.putExtra("projectType", projectType);
//                startActivity(intent_launch);
//                break;
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
