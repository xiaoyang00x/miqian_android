package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.IntoActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/9/19.
 */
public class CurrentInvestment extends BaseActivity implements View.OnClickListener {

    private Button btPay;
    private TextView orderMoney;

    private String money;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        HttpRequest.getCurrentOrder(mActivity, new ICallback<CurrentInfoResult>() {
            @Override
            public void onSucceed(CurrentInfoResult result) {
                refreshView();
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private void refreshView() {
        orderMoney.setText(money + "元");
    }

    @Override
    public void initView() {
        btPay = (Button) findViewById(R.id.bt_pay);
        btPay.setOnClickListener(this);
        orderMoney = (TextView) findViewById(R.id.order_money);
    }

    @Override
    public int getLayoutId() {
        return R.layout.current_investment;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("确认订单");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pay:
                Intent intent = new Intent(CurrentInvestment.this, IntoActivity.class);
                intent.putExtra("rollType", 1);
                intent.putExtra("money", "0.1");
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {

        }
        Log.e("", "requestCode : " + requestCode);
        Log.e("", "resultCode : " + resultCode);
    }


}
