package com.miqian.mq.activity.user;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.UserRegularDetail;
import com.miqian.mq.views.WFYTitle;

import java.io.Serializable;

/**
 * Created by 涂良坛 on 2016/6/6.
 * 发起定期转让
 */
public class LaunchTransferRegularAcitivity extends BaseActivity {

    private UserRegularDetail userRegularDetail;
    private TextView textProject;
    private TextView textCapitalMoney;
    private TextView textLimit;
    private TextView textInterestRate;
    private TextView textDateStart;
    private TextView textDateEnd;
    private TextView textRepayment;
    private TextView textPrnTransSa; //可转让金额
    private TextView textRemindDay;  //剩余期限
    private EditText et_TransferRate;//转让后的年化收益
    private EditText etMoney;  //转让金额
    private TextView textChangeMoney; //加价，减价
    private TextView textDiscountRate;//转让折让比

    @Override
    public void onCreate(Bundle arg0) {
        userRegularDetail = (UserRegularDetail) getIntent().getExtras().getSerializable("userRegularDetail");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

//        if (userRegularDetail != null) {
//            textLimit.setText(userRegularDetail.getLimitCnt());
//            textCapitalMoney.setText(userRegularDetail.getRegAmt());
//            textProject.setText(userRegularDetail.getProdName());
//            textDateStart.setText("认购日期:" + userRegularDetail.getCrtDt());
//            textDateEnd.setText("结束日期:" + userRegularDetail.getDueDt());
//            textInterestRate.setText(userRegularDetail.getRealInterest());
//            textRepayment.setText(userRegularDetail.getPayMeansName());
//            textPrnTransSa.setText(userRegularDetail.getPrnTransSa());
////            textRemindDay.setText(userRegularDetail.get);
//        }
    }

    @Override
    public void initView() {

        textProject = (TextView) findViewById(R.id.text_project);
        textCapitalMoney = (TextView) findViewById(R.id.text_capital_money);
        textLimit = (TextView) findViewById(R.id.text_limit);
        textInterestRate = (TextView) findViewById(R.id.text_interest_rate);
        textDateStart = (TextView) findViewById(R.id.text_date_start);
        textDateEnd = (TextView) findViewById(R.id.text_date_end);
        textRepayment = (TextView) findViewById(R.id.text_repayment);
        textPrnTransSa = (TextView) findViewById(R.id.text_prnTransSa);
        textRemindDay = (TextView) findViewById(R.id.text_remindday);
        textChangeMoney= (TextView) findViewById(R.id.tv_changemoney);
        textDiscountRate = (TextView) findViewById(R.id.tv_discountrate);

        etMoney=(EditText)findViewById(R.id.et_money);
        et_TransferRate=(EditText)findViewById(R.id.et_transfer_rate);
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_launch_transfer;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
    }

    @Override
    protected String getPageName() {
        return "发起转让";
    }
}