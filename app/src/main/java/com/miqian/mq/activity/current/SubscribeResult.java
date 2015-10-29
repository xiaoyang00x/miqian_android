package com.miqian.mq.activity.current;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    private TextView textTel;
    private Button btBackHome;
    private Button btBackUser;
    private LinearLayout frameSuccess;
    private RelativeLayout frameFail;

    private int status;
    private String title;
    private String money;
    private String balance;
    private String promoteMoney;
    private String orderNo;
    private String timeString;

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
        btBackHome = (Button) findViewById(R.id.bt_back_home);
        btBackUser = (Button) findViewById(R.id.bt_back_user);
        textTel = (TextView) findViewById(R.id.text_tel);
        textTel.setOnClickListener(this);
        btBackHome.setOnClickListener(this);
        btBackUser.setOnClickListener(this);

        frameSuccess = (LinearLayout) findViewById(R.id.frame_success);
        frameFail = (RelativeLayout) findViewById(R.id.frame_fail);
    }

    private void refreshView() {
        if (status == 1) {
            imageSuccess.setVisibility(View.VISIBLE);
            frameSuccess.setVisibility(View.VISIBLE);
            frameFail.setVisibility(View.GONE);
            tradeNumber.setText(orderNo);
            textTime.setText(timeString);
        } else {
            imageSuccess.setVisibility(View.GONE);
            frameSuccess.setVisibility(View.GONE);
            frameFail.setVisibility(View.VISIBLE);
        }
        textMoney.setText(money + "元");
        textBalance.setText(balance + "元");
        textPromote.setText(promoteMoney + "元");
    }

    @Override
    public int getLayoutId() {
        return R.layout.subscribe_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText(title);
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_tel:
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006656191")));
                break;
            case R.id.bt_back_home:
                MobclickAgent.onEvent(mContext, "1064");
                SubscribeResult.this.finish();
                ExtendOperationController.getInstance().doNotificationExtendOperation(OperationKey.BACK_HOME, null);
                break;
            case R.id.bt_back_user:
                MobclickAgent.onEvent(mContext, "1065");
                SubscribeResult.this.finish();
                ExtendOperationController.getInstance().doNotificationExtendOperation(OperationKey.BACK_USER, null);
                break;
            default:
                break;
        }
    }
}
