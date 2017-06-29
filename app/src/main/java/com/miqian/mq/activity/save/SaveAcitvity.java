package com.miqian.mq.activity.save;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.SaveInfo;
import com.miqian.mq.entity.SaveInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2017/6/15.
 * 江西银行存管
 * 激活资金存管
 */

public class SaveAcitvity extends BaseActivity implements View.OnClickListener {

    private Button btOpen;
    private Button btProduct;

    private TextView textAccount;
    private TextView textPassword;
    private TextView textAuto;
    private TextView textLine1;
    private TextView textLine2;



    private SaveInfo saveInfo;

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
        begin();
        HttpRequest.openJxPreprocess(this, new ICallback<SaveInfoResult>() {
            @Override
            public void onSucceed(SaveInfoResult saveInfoResult) {
                end();
                saveInfo = saveInfoResult.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(SaveAcitvity.this, error);
            }
        });
    }

    @Override
    public void initView() {
        textAccount = (TextView) findViewById(R.id.text_account);
        textPassword = (TextView) findViewById(R.id.text_password);
        textAuto = (TextView) findViewById(R.id.text_auto);
        textLine1 = (TextView) findViewById(R.id.text_line1);
        textLine2 = (TextView) findViewById(R.id.text_line2);

        btOpen = (Button) findViewById(R.id.bt_open);
        btOpen.setOnClickListener(this);
        btProduct = (Button) findViewById(R.id.bt_product);
        btProduct.setOnClickListener(this);
    }


    private void refreshView() {
        if ("1".equals(saveInfo.getJxAccountStatus())) {
            Drawable drawable=getResources().getDrawable(R.drawable.save_step1_on);
            textAccount.setCompoundDrawables(null, drawable, null, null);
            textAccount.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
            textLine1.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
        }
        if ("1".equals(saveInfo.getJxPayPwdStatus())) {
            Drawable drawable=getResources().getDrawable(R.drawable.save_step1_on);
            textPassword.setCompoundDrawables(null, drawable, null, null);
            textPassword.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
            textLine2.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
        }
        if ("1".equals(saveInfo.getJxAutoClaimsTransferStatus()) && "1".equals(saveInfo.getJxAutoSubscribeStatus())) {
            Drawable drawable=getResources().getDrawable(R.drawable.save_step1_on);
            textAuto.setCompoundDrawables(null, drawable, null, null);
            textAuto.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
        }
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
