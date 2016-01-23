package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.utils.ActivityStack;
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
    private TextView textPromote;
    private TextView tradeNumber;
    private TextView textTime;
    private TextView textPayType;
    private TextView textPayMoney;
    private TextView tvTip;
    private RelativeLayout framePromote;

    private int status;
    private int payModeState;
    private String title;
    private String money;
    private String payMoney;
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
        payMoney = intent.getStringExtra("payMoney");
        payModeState = intent.getIntExtra("payModeState", 0);
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
        textPayType = (TextView) findViewById(R.id.text_pay_type);
        textPayMoney = (TextView) findViewById(R.id.text_pay_money);
        textPromote = (TextView) findViewById(R.id.text_promote);
        tradeNumber = (TextView) findViewById(R.id.trade_number);
        textTime = (TextView) findViewById(R.id.text_time);
        framePromote = (RelativeLayout) findViewById(R.id.frame_promote);

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
        if (payModeState == CurrentInvestment.PAY_MODE_BALANCE) {
            textPayType.setText("余额支付");
        } else if (payModeState == CurrentInvestment.PAY_MODE_BANK) {
            textPayType.setText("银行卡充值支付");
        } else if (payModeState == CurrentInvestment.PAY_MODE_CURRENT) {
            textPayType.setText("活期转定期");
        }
        textPayMoney.setText(payMoney+ "元");
        textMoney.setText(money + "元");
        if (TextUtils.isEmpty(promoteMoney) || "0".equals(promoteMoney)) {
            framePromote.setVisibility(View.GONE);
        } else {
            textPromote.setText(promoteMoney + "元");
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
                if (status == 1) {
                    ExtendOperationController.getInstance().doNotificationExtendOperation(OperationKey.BACK_USER, null);
                } else {
                    SubscribeResult.this.finish();
                    ActivityStack.getActivityStack().popActivity();
                }
                break;
            default:
                break;
        }
    }
}
