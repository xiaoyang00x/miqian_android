package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.Redeem;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/9/29.
 */
public class RedeemResult extends BaseActivity implements View.OnClickListener {

    private ImageView imageSuccess;
    private TextView textInterest;
    private TextView textBalance;
    private TextView textCapital;
    private TextView tradeNumber;
    private TextView textTime;
    private Button btBackHome;
    private Button btBackUser;
    private LinearLayout frameSuccess;
    private RelativeLayout frameFail;

    private int status;
    private String title;
    private Redeem redeem;
    private String capital;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        status = intent.getIntExtra("state", 0);
        if (status == 1) {
            title = "赎回成功";
            redeem = (Redeem) intent.getSerializableExtra("redeemData");
        } else {
            title = "赎回失败";
            capital = intent.getStringExtra("capital");
        }

        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        refreshView();
    }

    @Override
    public void initView() {
        textBalance = (TextView) findViewById(R.id.text_balance);
        textCapital = (TextView) findViewById(R.id.text_capital);
        textInterest = (TextView) findViewById(R.id.text_interest);
        tradeNumber = (TextView) findViewById(R.id.trade_number);
        textTime = (TextView) findViewById(R.id.text_time);
        btBackHome = (Button) findViewById(R.id.bt_back_home);
        btBackUser = (Button) findViewById(R.id.bt_back_user);
        btBackHome.setOnClickListener(this);
        btBackUser.setOnClickListener(this);

        frameSuccess = (LinearLayout) findViewById(R.id.frame_success);
        frameFail = (RelativeLayout) findViewById(R.id.frame_fail);
    }

    private void refreshView() {
        if (status == 1) {
            frameSuccess.setVisibility(View.VISIBLE);
            frameFail.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(redeem.getOrderNo())) {
                tradeNumber.setText(redeem.getOrderNo());
            }
            if (!TextUtils.isEmpty(redeem.getAddTime())) {
                textTime.setText(Uihelper.timeToString((redeem.getAddTime())));
            }
            if (!TextUtils.isEmpty(redeem.getArriAmt())) {
                textBalance.setText(redeem.getArriAmt() + "元");
            }
            if (!TextUtils.isEmpty(redeem.getInterest())) {
                textInterest.setText(redeem.getInterest() + "元");
            }
            if (!TextUtils.isEmpty(redeem.getAmt())) {
                textCapital.setText(redeem.getAmt() + "元");
            }

        } else {
            findViewById(R.id.view_divider1).setVisibility(View.GONE);
            findViewById(R.id.frame_arriveAmt).setVisibility(View.GONE);
            findViewById(R.id.view_divider2).setVisibility(View.GONE);
            findViewById(R.id.frame_interest).setVisibility(View.GONE);
            frameSuccess.setVisibility(View.GONE);
            frameFail.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(capital)) {
                textCapital.setText(capital + "元");
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.redeem_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText(title);
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back_home:
                RedeemResult.this.finish();
                break;
            case R.id.bt_back_user:
                RedeemResult.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected String getPageName() {
        return title;
    }
}
