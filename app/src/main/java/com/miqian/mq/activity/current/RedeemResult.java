package com.miqian.mq.activity.current;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.Redeem;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/9/29.
 */
public class RedeemResult extends BaseActivity implements View.OnClickListener {

    private TextView textInterest;
    private TextView textBalance;
    private TextView textCapital;
    private TextView tradeNumber;
    private TextView textState;
    private Button btBack;

    private int status;
    private String title;
    private Redeem redeem;
    private String capital;
    private String errormessage;
    private TextView tvTip;
    private ImageView imageStatus;

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
            errormessage = intent.getStringExtra("errormessage");
        }

        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        refreshView();
    }

    @Override
    public void initView() {
        textState = (TextView) findViewById(R.id.text_status);
        textBalance = (TextView) findViewById(R.id.text_balance);
        textCapital = (TextView) findViewById(R.id.text_capital);
        textInterest = (TextView) findViewById(R.id.text_interest);
        tradeNumber = (TextView) findViewById(R.id.trade_number);
        btBack = (Button) findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
        imageStatus = (ImageView) findViewById(R.id.image_status);
        tvTip = (TextView) findViewById(R.id.tv_tip);

    }

    private void refreshView() {
        if (status == 1) {
            if (!TextUtils.isEmpty(redeem.getOrderNo())) {
                tradeNumber.setText(redeem.getOrderNo());
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
            imageStatus.setImageResource(R.drawable.rollin_status_success);

        } else {
            findViewById(R.id.view_divider3).setVisibility(View.GONE);
            findViewById(R.id.frame_interest).setVisibility(View.GONE);
            findViewById(R.id.view_divider1).setVisibility(View.GONE);
            findViewById(R.id.frame_arriveAmt).setVisibility(View.GONE);
            findViewById(R.id.view_divider2).setVisibility(View.GONE);
            findViewById(R.id.frame_casecode).setVisibility(View.GONE);
            tvTip.setText("如果多次失败，请联系客服400-6656-191");
            imageStatus.setImageResource(R.drawable.rollin_status_fail);
            textState.setText("赎回失败");
            textCapital.setText(capital + "元");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.redeem_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("赎回");
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                mActivity.finish();
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
