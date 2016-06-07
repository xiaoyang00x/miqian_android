package com.miqian.mq.activity.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.RollOut;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by TuLiangTan on 2015/9/28.
 */

public class RollOutResultActivity extends BaseActivity implements View.OnClickListener {

    //    private TextView textTel;
    private Button btBack;
    private TextView tvMoney, tvOrderNum, tvCardNum;
    private RollOut rollOut;
    private ImageView imageStatus;
    private TextView tvTip;
    private TextView textState;
    private String state; //0为失败，1为成功

    @Override
    public void onCreate(Bundle arg0) {
        rollOut = (RollOut) getIntent().getSerializableExtra("rollOutResult");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

        state = rollOut.getState();
        if (rollOut == null) {
            return;
        }
        if (!TextUtils.isEmpty(rollOut.getMoneyOrder())) {
            tvMoney.setText(rollOut.getMoneyOrder() + "元");
        }
        String cardNum = rollOut.getCardNum();
        if (!TextUtils.isEmpty(rollOut.getBankName()) && !TextUtils.isEmpty(cardNum)) {
            tvCardNum.setText(rollOut.getBankName() + "（****" + cardNum.substring(cardNum.length() - 4, cardNum.length()) + ")");
        }

        if ("1".equals(state)) {
            if (!TextUtils.isEmpty(rollOut.getOrderNo())) {
                tvOrderNum.setText(rollOut.getOrderNo());
            }
        } else {
            imageStatus.setImageResource(R.drawable.result_fail);
            tvTip.setText("");
            textState.setText("提现失败");
            findViewById(R.id.divider1).setVisibility(View.GONE);
            findViewById(R.id.frame_ordernum).setVisibility(View.GONE);
        }
    }

    @Override
    public void initView() {

        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvOrderNum = (TextView) findViewById(R.id.tv_ordernum);
        tvCardNum = (TextView) findViewById(R.id.tv_cardnum);
        textState = (TextView) findViewById(R.id.text_status);

        btBack = (Button) findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
        imageStatus = (ImageView) findViewById(R.id.image_status);
        tvTip = (TextView) findViewById(R.id.text_tip);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rollout_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("提现");
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    protected String getPageName() {
        return "提现结果";
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_back:
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_USER, null);
                break;
        }
    }
}
