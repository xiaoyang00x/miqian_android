package com.miqian.mq.activity.current;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.IntoActivity;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/9/19.
 */
public class CurrentInvestment extends BaseActivity implements View.OnClickListener {

    private Button btPay;

    @Override
    public void obtainData() {
        HttpRequest.getCurrentOrder(mActivity, new ICallback<CurrentInfoResult>() {
            @Override
            public void onSucceed(CurrentInfoResult result) {

            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    @Override
    public void initView() {
        btPay = (Button) findViewById(R.id.bt_pay);
        btPay.setOnClickListener(this);
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
