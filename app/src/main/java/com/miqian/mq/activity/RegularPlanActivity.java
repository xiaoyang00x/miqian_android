package com.miqian.mq.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;

/**
 * Created by guolei_wang on 15/9/25.
 */
public class RegularPlanActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_plan);
        findView();
        initView();
    }

    Button btn_buy;
    Button btn_des_close;
    TextView tv_description;
    TextView tv_limit;
    TextView tv_annurate_interest_rate;
    TextView tv_add_interest;
    TextView tv_lable1,tv_lable2,tv_lable3,tv_lable4;
    TextView tv_project_name,tv_fx,tv_bxbz,tv_hkfs;
    ProgressBar progressBar;
    View layout_des;
    private void findView() {
        btn_buy = (Button)findViewById(R.id.btn_buy);
        btn_des_close = (Button)findViewById(R.id.btn_des_close);
        tv_description = (TextView)findViewById(R.id.tv_description);
        tv_limit = (TextView)findViewById(R.id.tv_limit);
        tv_annurate_interest_rate = (TextView)findViewById(R.id.tv_annurate_interest_rate);
        tv_add_interest = (TextView)findViewById(R.id.tv_add_interest);
        tv_lable1 = (TextView)findViewById(R.id.tv_lable1);
        tv_lable2 = (TextView)findViewById(R.id.tv_lable2);
        tv_lable3 = (TextView)findViewById(R.id.tv_lable3);
        tv_lable4 = (TextView)findViewById(R.id.tv_lable4);
        tv_project_name = (TextView)findViewById(R.id.tv_project_name);
        tv_fx = (TextView)findViewById(R.id.tv_fx);
        tv_bxbz = (TextView)findViewById(R.id.tv_bxbz);
        tv_hkfs = (TextView)findViewById(R.id.tv_hkfs);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        layout_des = findViewById(R.id.layout_des);

    }

    private void initView() {
        btn_buy.setOnClickListener(this);
        btn_des_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_buy:
                break;
            case R.id.btn_des_close:
                layout_des.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void getMainRegular() {
        HttpRequest.getMainRegular(this, new ICallback<GetRegularResult>() {

            @Override
            public void onSucceed(GetRegularResult result) {
            }

            @Override
            public void onFail(String error) {
                Uihelper.showToast(RegularPlanActivity.this, error);
            }
        });
    }
}
