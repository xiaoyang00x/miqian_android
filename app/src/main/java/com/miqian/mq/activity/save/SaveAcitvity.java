package com.miqian.mq.activity.save;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2017/6/15.
 * 江西银行存管
 * 激活资金存管
 */

public class SaveAcitvity extends BaseActivity implements View.OnClickListener {

    private Button btOpen;
    private Button btProduct;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                startActivity(new Intent(this, SaveBindAcitvity.class));
                break;
            case R.id.bt_product:
                break;
        }
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        btOpen = (Button) findViewById(R.id.bt_open);
        btOpen.setOnClickListener(this);
        btProduct = (Button) findViewById(R.id.bt_product);
        btProduct.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_jx_save;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("激活资金存管");
    }

    @Override
    protected String getPageName() {
        return "激活资金存管";
    }
}
