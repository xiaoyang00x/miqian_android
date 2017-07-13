package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.user.UserMqbActivity;
import com.miqian.mq.entity.RedeemResultInfo;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.views.WFYTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jackie on 2015/9/29.
 */
public class RedeemMqbResult extends BaseActivity {

    @BindView(R.id.text_status)
    TextView textStatus;
    @BindView(R.id.text_money)
    TextView textMoney;//订单金额
    @BindView(R.id.text_interest)
    TextView textInterest;//对应利息
    @BindView(R.id.text_expect)
    TextView textExpect;//预期到账时间
    @BindView(R.id.trade_number)
    TextView tradeNumber;//业务编号
    @BindView(R.id.text_error)
    TextView textError;//失败原因
    @BindView(R.id.text_tip1)
    TextView textTip1;//提示
    @BindView(R.id.text_tip2)
    TextView textTip2;//提示

    @BindView(R.id.frame_expect)
    RelativeLayout frameExpect;
    @BindView(R.id.frame_error)
    RelativeLayout frameError;

    @BindView(R.id.bt_back)
    Button btBack;

    private int status;

    private RedeemResultInfo redeemResultInfo;
    private String errorReason;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);
        String result = intent.getStringExtra("redeemResultInfo");
        errorReason = intent.getStringExtra("errorReason");
        redeemResultInfo = JsonUtil.parseObject(result, RedeemResultInfo.class);
        super.onCreate(bundle);
    }

    @Override
    protected String getPageName() {
        return "赎回结果";
    }

    @Override
    public void obtainData() {
        refreshView();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    private void refreshView() {
        if (redeemResultInfo != null) {
            textMoney.setText(FormatUtil.formatAmount(redeemResultInfo.getAmt()));
            textInterest.setText(FormatUtil.formatAmount(redeemResultInfo.getInterest()));
            tradeNumber.setText(redeemResultInfo.getOrderNo());
        }
        if (status == 1) {
            textStatus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.result_success, 0, 0);
            textStatus.setText("赎回成功");
            textExpect.setText(redeemResultInfo.getArriveDesc());
            textTip1.setText(redeemResultInfo.getTips());
        } else {
            textStatus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.result_fail, 0, 0);
            textStatus.setText("赎回失败");
            frameExpect.setVisibility(View.GONE);
            frameError.setVisibility(View.VISIBLE);
            textError.setText(errorReason);
            textTip1.setVisibility(View.VISIBLE);
            textTip2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_redeem_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("赎回结果");
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
        super.onBackPressed();
    }

    @OnClick(R.id.bt_back)
    public void closeActivity() {
        startActivity(new Intent(RedeemMqbResult.this, UserMqbActivity.class));
        RedeemMqbResult.this.finish();
    }
}
