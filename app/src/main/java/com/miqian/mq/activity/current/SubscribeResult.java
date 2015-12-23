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
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.ExtendOperationController.OperationKey;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Jackie on 2015/9/29.
 */
public class SubscribeResult extends BaseActivity implements View.OnClickListener {

    private ImageView imageSuccess;
    private TextView textMoney;
    private TextView textBalance;
    private TextView textPromote;
    private TextView tradeNumber;
    private TextView textTime;
    private TextView textCurrent;
    private TextView tvTip;
    private RelativeLayout framePromote;
    private RelativeLayout frameCurrent;

    private int status;
    private String title;
    private String money;
    private String balance;
    private String promoteMoney;
    private String currentMoney;
    private String orderNo;
    private String timeString;
    private Button btBack;
    private TextView tvStatus;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);
        if (status == 1) {
            title = "认购成功";
        } else {
            title = "认购失败";
        }
        money = intent.getStringExtra("money");
        balance = intent.getStringExtra("balance");
        promoteMoney = intent.getStringExtra("promoteMoney");
        currentMoney = intent.getStringExtra("currentMoney");
        orderNo = intent.getStringExtra("orderNo");
        timeString = Uihelper.timeToString(intent.getStringExtra("addTime"));
        super.onCreate(bundle);
    }

    @Override
    protected String getPageName() {
        return title;
    }

    @Override
    public void obtainData() {
        refreshView();
    }

    @Override
    public void initView() {
        imageSuccess = (ImageView) findViewById(R.id.image_success);
        textMoney = (TextView) findViewById(R.id.text_money);
        textBalance = (TextView) findViewById(R.id.text_balance);
        textPromote = (TextView) findViewById(R.id.text_promote);
        tradeNumber = (TextView) findViewById(R.id.trade_number);
        textTime = (TextView) findViewById(R.id.text_time);
        framePromote = (RelativeLayout) findViewById(R.id.frame_promote);
        textCurrent = (TextView) findViewById(R.id.text_current);
        frameCurrent = (RelativeLayout) findViewById(R.id.frame_current);

        btBack = (Button) findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        tvStatus = (TextView) findViewById(R.id.text_status);
    }

    private void refreshView() {
        if (status == 1) {
            imageSuccess.setImageResource(R.drawable.rollin_status_success);
            tradeNumber.setText(orderNo);
            textTime.setText(timeString);
            tvStatus.setText("认购成功");
        } else {
            findViewById(R.id.frame_trade_number).setVisibility(View.GONE);
            findViewById(R.id.frame_time).setVisibility(View.GONE);
            tvTip.setText("如果多次失败，请联系客服400-6656-191");
            imageSuccess.setImageResource(R.drawable.rollin_status_fail);
            tvStatus.setText("认购失败");
        }
        textMoney.setText(money + "元");
        textBalance.setText(balance + "元");
        if (TextUtils.isEmpty(promoteMoney) || "0".equals(promoteMoney)) {
            framePromote.setVisibility(View.GONE);
        } else {
            textPromote.setText(promoteMoney + "元");
        }

        if (TextUtils.isEmpty(currentMoney) || "0".equals(currentMoney)) {
            frameCurrent.setVisibility(View.GONE);
        } else {
            textCurrent.setText(currentMoney + "元");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.subscribe_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("认购");
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                MobclickAgent.onEvent(mContext, "1065");
                SubscribeResult.this.finish();
                ExtendOperationController.getInstance().doNotificationExtendOperation(OperationKey.BACK_USER, null);
                break;
            default:
                break;
        }
    }
}
