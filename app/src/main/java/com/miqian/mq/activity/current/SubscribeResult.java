package com.miqian.mq.activity.current;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.SubscribeOrder;
import com.miqian.mq.listener.JsShareListener;
import com.miqian.mq.utils.ActivityStack;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.ExtendOperationController.OperationKey;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.ShareUtils;
import com.miqian.mq.views.DialogShare;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Jackie on 2015/9/29.
 */
public class SubscribeResult extends BaseActivity implements View.OnClickListener, JsShareListener {

    private ImageView imageStatus;
    private TextView textMoney;
    private TextView textPromote;
    private TextView tradeNumber;
    private TextView textPayType;
    private TextView textPayMoney;
    private TextView tvTip;
    private TextView textGold;
    private RelativeLayout framePromote;
    private RelativeLayout frameGold;

    private int status;
    private int payModeState;
    private String title;
    private String money;
    private String payMoney;
    private String promoteMoney;
    private String goldCoin;
    private String orderNo;
    private Button btBack;
    private TextView tvStatus;

    private SubscribeOrder subscribeOrder;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);
        if (status == 1) {
            title = "认购申请成功";
            String result = intent.getStringExtra("subscribeOrder");
            subscribeOrder =  JsonUtil.parseObject(result, SubscribeOrder.class);
        } else {
            title = "认购失败";
        }
        money = intent.getStringExtra("money");
        payMoney = intent.getStringExtra("payMoney");
        payModeState = intent.getIntExtra("payModeState", 0);
        promoteMoney = intent.getStringExtra("promoteMoney");
        if (subscribeOrder != null) {
            orderNo = subscribeOrder.getOrderNo();
            goldCoin = subscribeOrder.getGoldCoin();
        }

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
        imageStatus = (ImageView) findViewById(R.id.image_status);
        textMoney = (TextView) findViewById(R.id.text_money);
        textPayType = (TextView) findViewById(R.id.text_pay_type);
        textPayMoney = (TextView) findViewById(R.id.text_pay_money);
        textPromote = (TextView) findViewById(R.id.text_promote);
        tradeNumber = (TextView) findViewById(R.id.trade_number);
        framePromote = (RelativeLayout) findViewById(R.id.frame_promote);
        frameGold = (RelativeLayout) findViewById(R.id.frame_gold);
        textGold = (TextView) findViewById(R.id.text_gold);
        textGold.setOnClickListener(this);

        btBack = (Button) findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        tvStatus = (TextView) findViewById(R.id.text_status);
    }

    private void refreshView() {
        if (status == 1) {
            imageStatus.setImageResource(R.drawable.result_success);
            tradeNumber.setText(orderNo);
            tvStatus.setText("认购申请成功");
        } else {
            findViewById(R.id.frame_trade_number).setVisibility(View.GONE);
            tvTip.setText("如果多次失败，请联系客服400-6656-191");
            imageStatus.setImageResource(R.drawable.result_fail);
            tvStatus.setText("认购失败");
        }
        if (payModeState == CurrentInvestment.PAY_MODE_BALANCE) {
            textPayType.setText("余额支付");
//        } else if (payModeState == CurrentInvestment.PAY_MODE_BANK) {
//            textPayType.setText("银行卡充值支付");
//        } else if (payModeState == CurrentInvestment.PAY_MODE_LIAN) {
//            textPayType.setText("连连快捷支付");
        }
        textPayMoney.setText(payMoney+ "元");
        textMoney.setText(money + "元");
        if (TextUtils.isEmpty(promoteMoney) || "0".equals(promoteMoney)) {
            framePromote.setVisibility(View.GONE);
        } else {
            textPromote.setText(promoteMoney + "元");
        }
        if (!TextUtils.isEmpty(goldCoin)) {
            frameGold.setVisibility(View.VISIBLE);
            textGold.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
            textGold.getPaint().setAntiAlias(true);//抗锯齿
            textGold.setText(goldCoin);
        }
        if (subscribeOrder != null && subscribeOrder.getShareLink() != null) {
            showShare();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.subscribe_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("认购结果");
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                closeActivity();
                break;
            case R.id.text_gold:
                if (subscribeOrder != null) {
                    WebActivity.startActivity(this, subscribeOrder.getGoldCoin_url());
                }
                break;
            default:
                break;
        }
    }

    private void closeActivity() {
        MobclickAgent.onEvent(mContext, "1065");
        if (status == 1) {
            ExtendOperationController.getInstance().doNotificationExtendOperation(OperationKey.BACK_MAIN, null);
        } else {
            SubscribeResult.this.finish();
            ActivityStack.getActivityStack().popActivity();
        }
    }

    @Override
    public void shareLog(String json) {

    }

    private void showShare() {
        DialogShare dialogShare = new DialogShare(SubscribeResult.this, subscribeOrder.getPopup()) {
            @Override
            public void share() {
                shareChannel();
            }
        };
        dialogShare.show();
    }

    private void shareChannel() {
        ShareUtils.share(SubscribeResult.this, subscribeOrder.getShareLink(), SubscribeResult.this);
    }
}
