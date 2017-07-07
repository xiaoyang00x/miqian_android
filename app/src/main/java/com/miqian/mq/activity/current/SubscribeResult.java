package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jackie on 2015/9/29.
 */
public class SubscribeResult extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.text_status)
    TextView textStatus;
    @BindView(R.id.text_money)
    TextView textMoney;//订单金额
    @BindView(R.id.text_pay_money)
    TextView textPayMoney;//余额支付
    @BindView(R.id.text_promote)
    TextView textPromote;
    @BindView(R.id.trade_number)
    TextView tradeNumber;
    @BindView(R.id.text_error)
    TextView textError;

    @BindView(R.id.text_tip)
    TextView textTip;
    @BindView(R.id.frame_money)
    RelativeLayout frameMoney;
    @BindView(R.id.frame_balance)
    RelativeLayout frameBalance;
    @BindView(R.id.frame_promote)
    RelativeLayout framePromote;
    @BindView(R.id.frame_error)
    RelativeLayout frameError;

    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_my_product)
    Button btMyProduct;

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
        ButterKnife.bind(this);
        btBack.setOnClickListener(this);
        btMyProduct.setOnClickListener(this);
    }

    private void refreshView() {
        if (subscribeOrder == null) return;
        if (status == 1) {
            textStatus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.result_success, 0, 0);
            textStatus.setText("认购申请成功");
            textMoney.setText(FormatUtil.formatAmount(subscribeOrder.getAmt()));
            textPayMoney.setText(FormatUtil.formatAmount(subscribeOrder.getRealPayAmt()));

            if (TextUtils.isEmpty(subscribeOrder.getPromDesc())) {
                framePromote.setVisibility(View.GONE);
            } else {
                textPromote.setText(subscribeOrder.getPromDesc());
            }
            if (CurrentInvestment.PRODID_CURRENT.equals(productType)) {
                textTip.setText("认购成功后，请前往我的秒钱宝查看");
            } else {
                btMyProduct.setText("前往我的定期");
                textTip.setText("认购成功后，请前往我的定期查看");
            }
        } else {
            textStatus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.result_fail, 0, 0);
            textStatus.setText("认购申请失败");
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
                if (CurrentInvestment.PRODID_CURRENT.equals(productType)) {

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
            if (CurrentInvestment.PRODID_CURRENT.equals(productType)) {
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
