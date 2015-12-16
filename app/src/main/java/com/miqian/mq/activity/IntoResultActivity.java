package com.miqian.mq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/9/23.
 */
public class IntoResultActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageStatus;
//    private TextView textProcessing;
    private TextView textOrderMoney;
    private TextView textOrderNo;
//    private RelativeLayout frameFail;
    private TextView textStatus;
//    private TextView textTel;
    private Button btBack;
    private LinearLayout frameTip;
    private TextView textTip;

    private int status;
    private String money;
    private String orderNo;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        status = intent.getIntExtra("status", CurrentInvestment.SUCCESS);
        money = intent.getStringExtra("money");
        orderNo = intent.getStringExtra("orderNo");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        imageStatus = (ImageView) findViewById(R.id.image_status);
//        textProcessing = (TextView) findViewById(R.id.text_processing);
        textOrderMoney = (TextView) findViewById(R.id.text_order_money);
        textOrderNo = (TextView) findViewById(R.id.text_order_no);
//        frameFail = (RelativeLayout) findViewById(R.id.frame_fail);
        textStatus = (TextView) findViewById(R.id.text_status);
//        textTel = (TextView) findViewById(R.id.text_tel);
        frameTip = (LinearLayout) findViewById(R.id.frame_tip);
        textTip = (TextView) findViewById(R.id.text_tip);
        btBack = (Button) findViewById(R.id.bt_back);
//        textTel.setOnClickListener(this);
        btBack.setOnClickListener(this);
        refreshView();
    }

    private void refreshView() {
        textOrderMoney.setText(money + "元");
        textOrderNo.setText(orderNo);
        if (status == CurrentInvestment.SUCCESS) {
            imageStatus.setImageResource(R.drawable.rollin_status_success);
        } else if (status == CurrentInvestment.PROCESSING) {
            textStatus.setText("充值处理中");
//            textProcessing.setVisibility(View.VISIBLE);
            imageStatus.setImageResource(R.drawable.rollin_status_processing);
            frameTip.setVisibility(View.VISIBLE);
            textTip.setText("请在 “我的” 资金记录中查看充值结果");
        } else if (status == CurrentInvestment.FAIL) {
            textStatus.setText("充值失败");
            imageStatus.setImageResource(R.drawable.rollin_status_fail);
//            frameFail.setVisibility(View.VISIBLE);
            frameTip.setVisibility(View.VISIBLE);
            textTip.setText("可能是网银支付出现问题，建议您稍后重试。如果一直失败，请在官网在线支付，官网地址：www.shicaidai.com");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("充值结果");
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                IntoResultActivity.this.finish();
                break;
//            case R.id.text_tel:
//                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006656191")));
//                break;
            default:
                break;
        }
    }

    @Override
    protected String getPageName() {
        return "充值结果";
    }
}
