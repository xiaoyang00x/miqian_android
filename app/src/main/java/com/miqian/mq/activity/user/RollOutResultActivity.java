package com.miqian.mq.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.RollOut;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by TuLiangTan on 2015/9/28.
 */

public class RollOutResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView textTel;
    private Button btBack;
    private TextView tvMoney, tvOrderNum, tvCardNum;
    private RollOut rollOut;

    @Override
    public void obtainData() {

        rollOut = (RollOut) getIntent().getSerializableExtra("rollOutResult");
        if (rollOut == null) {
            finish();
        }
        if (!TextUtils.isEmpty(rollOut.getMoneyOrder())) {
            tvMoney.setText(rollOut.getMoneyOrder() + "元");
        }
        String cardNum = rollOut.getCardNum();
        if (!TextUtils.isEmpty(rollOut.getBankName()) && !TextUtils.isEmpty(cardNum)) {
            tvCardNum.setText("银行卡号：" + rollOut.getBankName() + "（****" + cardNum.substring(cardNum.length() - 4, cardNum.length()) + ")");
        }
        if (!TextUtils.isEmpty(rollOut.getOrderNo())) {
            tvOrderNum.setText("资金编号:" + rollOut.getOrderNo());
        }


    }

    @Override
    public void initView() {

        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvOrderNum = (TextView) findViewById(R.id.tv_ordernum);
        tvCardNum = (TextView) findViewById(R.id.tv_cardnum);
        textTel = (TextView) findViewById(R.id.text_tel);

        btBack = (Button) findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
        textTel.setOnClickListener(this);
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
                finish();
                break;
            case R.id.text_tel:
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006656191")));
                break;
        }


    }
}
