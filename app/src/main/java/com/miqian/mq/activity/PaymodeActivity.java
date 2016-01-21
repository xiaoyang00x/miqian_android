package com.miqian.mq.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.views.DialogTip;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Administrator on 2016/1/20.
 */
public class PaymodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvCurrentSate;
    private DialogTip dialogTip;

    @Override
    public void onCreate(Bundle arg0) {

        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

        tvCurrentSate = (TextView) findViewById(R.id.tv_current_state);
        tvCurrentSate.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_paymode;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("选择支付方式");

    }

    @Override
    protected String getPageName() {
        return "选择支付方式";
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_current_state:

                if (dialogTip == null) {
                    dialogTip = new DialogTip(mActivity) {
                    };
                }
                dialogTip.show();

                break;
            default:
                break;
        }
    }
}
