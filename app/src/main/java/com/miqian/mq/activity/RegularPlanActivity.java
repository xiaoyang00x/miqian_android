package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularPlan;
import com.miqian.mq.entity.RegularPlanResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;

/**
 * Created by guolei_wang on 15/9/25.
 */
public class RegularPlanActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static final String KEY_SUBJECT_ID = "KEY_SUBJECT_ID";
    private String subjectId; //标的 ID

    public static void startActivity(Context context, String subjectId) {
        Intent intent = new Intent(context, RegularPlanActivity.class);
        intent.putExtra(KEY_SUBJECT_ID, subjectId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_plan);
        findView();
        initView();
        subjectId = getIntent().getStringExtra(KEY_SUBJECT_ID);
        getRegularDetails(subjectId);
    }

    Button btn_buy;
    Button btn_des_close;
    TextView tv_description;
    TextView tv_limit;
    TextView tv_title;
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
        tv_title = (TextView)findViewById(R.id.title);
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
        tv_title.setText("定期计划");
    }

    private void updateUI(RegularPlan data) {
        if(data == null) return;
        if(TextUtils.isEmpty(data.getPromotionDesc())) {
            layout_des.setVisibility(View.GONE);
        }else {
            layout_des.setVisibility(View.VISIBLE);
            tv_description.setText(data.getPromotionDesc());
        }
        tv_limit.setText("项目期限 " + data.getLimit() + "天");
        tv_annurate_interest_rate.setText(data.getYearInterest());
        if("Y".equalsIgnoreCase(data.getPresentationYesNo())) {
            tv_add_interest.setVisibility(View.VISIBLE);
            tv_add_interest.setText(data.getPresentationYearInterest());
        }else {
            tv_add_interest.setVisibility(View.GONE);
        }
        tv_project_name.setText(data.getSubjectName());
        tv_hkfs.setText(data.getPayMode());
        progressBar.setProgress((new Float(data.getPurchasePercent())).intValue());
        tv_fx.setText(data.getDdbzf());
        tv_bxbz.setText(data.getBxbzf());
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

    private void getRegularDetails(String subjectId) {
        HttpRequest.getRegularDetails(this, "2", subjectId, new ICallback<RegularPlanResult>() {

            @Override
            public void onSucceed(RegularPlanResult result) {
                if(result != null) {
                    updateUI(result.getData());
                }
            }

            @Override
            public void onFail(String error) {
                Uihelper.showToast(RegularPlanActivity.this, error);
            }
        });
    }
}
