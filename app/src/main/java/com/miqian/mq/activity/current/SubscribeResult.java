package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.entity.SubscribeOrder;
import com.miqian.mq.utils.ActivityStack;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.ExtendOperationController.OperationKey;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/29.
 */
public class SubscribeResult extends BaseActivity implements View.OnClickListener {

    private TextView tvStatus;
    private TextView textMoney;
    private TextView textPayMoney;
    private TextView textPromote;
    private TextView tradeNumber;
    private TextView textError;

    private TextView textTip;
    private RelativeLayout frameMoney;
    private RelativeLayout frameBalance;
    private RelativeLayout framePromote;
    private RelativeLayout frameError;

    private Button btBack;
    private Button btMyProduct;

    private int status;

    private SubscribeOrder subscribeOrder;
    private String productType;
    private String errorReason;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);
        String result = intent.getStringExtra("subscribeOrder");
        productType = intent.getStringExtra("productType");
        errorReason = intent.getStringExtra("errorReason");
        subscribeOrder = JsonUtil.parseObject(result, SubscribeOrder.class);
        super.onCreate(bundle);
    }

    @Override
    protected String getPageName() {
        return "认购结果";
    }

    @Override
    public void obtainData() {
        refreshView();
    }

    @Override
    public void initView() {
        tvStatus = (TextView) findViewById(R.id.text_status);
        textMoney = (TextView) findViewById(R.id.text_money);
        textPayMoney = (TextView) findViewById(R.id.text_pay_money);
        textPromote = (TextView) findViewById(R.id.text_promote);
        textError = (TextView) findViewById(R.id.text_error);
        tradeNumber = (TextView) findViewById(R.id.trade_number);
        textTip = (TextView) findViewById(R.id.text_tip);

        frameMoney = (RelativeLayout) findViewById(R.id.frame_money);
        frameBalance = (RelativeLayout) findViewById(R.id.frame_balance);
        framePromote = (RelativeLayout) findViewById(R.id.frame_promote);
        frameError = (RelativeLayout) findViewById(R.id.frame_error);

        btBack = (Button) findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
        btMyProduct = (Button) findViewById(R.id.bt_my_product);
        btMyProduct.setOnClickListener(this);
    }

    private void refreshView() {
        if (subscribeOrder == null) return;
        if (status == 1) {

            tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.result_success, 0, 0);
            tvStatus.setText("认购申请成功");
            textMoney.setText(FormatUtil.formatAmount(subscribeOrder.getAmt()));
            textPayMoney.setText(FormatUtil.formatAmount(subscribeOrder.getAmt().subtract(subscribeOrder.getUsePromAmt())));

            if (subscribeOrder.getUsePromAmt().compareTo(BigDecimal.ZERO) == 0) {
                framePromote.setVisibility(View.GONE);
            } else {
                textPromote.setText(FormatUtil.formatAmount(subscribeOrder.getUsePromAmt()));
            }
            if (CurrentInvestment.PRODID_CURRENT == productType) {
                textTip.setText("认购成功后，请前往我的秒钱宝查看");
            } else {
                btMyProduct.setText("前往我的定期");
                textTip.setText("认购成功后，请前往我的定期查看");
            }
        } else {
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.result_fail, 0, 0);
            tvStatus.setText("认购申请失败");
            frameMoney.setVisibility(View.GONE);
            frameBalance.setVisibility(View.GONE);
            framePromote.setVisibility(View.GONE);
            frameError.setVisibility(View.VISIBLE);
            textError.setText(errorReason);
            btBack.setText("确定");
            btMyProduct.setVisibility(View.GONE);
        }
        tradeNumber.setText(subscribeOrder.getOrderNo());
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
            case R.id.bt_my_product:
                if (CurrentInvestment.PRODID_CURRENT == productType) {

                } else {
                    ActivityStack.getActivityStack().clearActivity();
                    startActivity(new Intent(SubscribeResult.this, UserRegularActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private void closeActivity() {
        MobclickAgent.onEvent(mContext, "1065");
        if (status == 1) {
            if (CurrentInvestment.PRODID_CURRENT == productType) {
                ExtendOperationController.getInstance().doNotificationExtendOperation(OperationKey.BACK_CURRENT, null);
            } else {
                ExtendOperationController.getInstance().doNotificationExtendOperation(OperationKey.BACK_REGULAR, null);
            }
        } else {
            SubscribeResult.this.finish();
            ActivityStack.getActivityStack().popActivity();
        }
    }
}
