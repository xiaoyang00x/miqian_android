package com.miqian.mq.activity.rollin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.fragment.FragmentUser;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/9/23.
 */
public class IntoResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView textOrderMoney;
    private TextView textOrderNo;
    private TextView textBank;
    private Button btBack;

    private String status;
    private String money;
    private String orderNo;
    private String bankNo;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        status = intent.getStringExtra("status");
        money = intent.getStringExtra("money");
        orderNo = intent.getStringExtra("orderNo");
        bankNo = intent.getStringExtra("bankNo");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        textOrderMoney = (TextView) findViewById(R.id.text_order_money);
        textOrderNo = (TextView) findViewById(R.id.text_order_no);
        textBank = (TextView) findViewById(R.id.text_bank);
        btBack = (Button) findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
        refreshView();
    }

    private void refreshView() {
        textOrderMoney.setText(FormatUtil.formatAmountStr(money));
        textOrderNo.setText(orderNo);
        textBank.setText(bankNo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("充值");
        mTitle.setIvLeftVisiable(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                if ("1".equals(status)) {
                    FragmentUser.refresh = true;
                }
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_USER, null);
                break;
            default:
                break;
        }
    }

    @Override
    protected String getPageName() {
        return "充值结果";
    }
}
