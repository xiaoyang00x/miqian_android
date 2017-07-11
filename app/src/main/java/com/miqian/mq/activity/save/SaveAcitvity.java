package com.miqian.mq.activity.save;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.entity.SaveInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jackie on 2017/6/15.
 * 江西银行存管
 * 激活资金存管
 */

public class SaveAcitvity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.bt_open)
    Button btOpen;
    @BindView(R.id.bt_product)
    Button btProduct;

    @BindView(R.id.text_account)
    TextView textAccount;
    @BindView(R.id.text_password)
    TextView textPassword;
    @BindView(R.id.text_auto)
    TextView textAuto;
    @BindView(R.id.text_line1)
    TextView textLine1;
    @BindView(R.id.text_line2)
    TextView textLine2;

    private SaveInfo saveInfo;
    private int jxState;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                startActivity();
                break;
            case R.id.bt_product:
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_REGULAR, null);
                break;
        }
    }

    @Override
    public void obtainData() {
        begin();
        HttpRequest.openJxPreprocess(this, new ICallback<MqResult<SaveInfo>>() {
            @Override
            public void onSucceed(MqResult<SaveInfo> result) {
                end();
                saveInfo = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(SaveAcitvity.this, error);
            }
        });
    }


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, SaveAcitvity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        btOpen.setOnClickListener(this);
        btProduct.setOnClickListener(this);

        if (UserUtil.isSaveBefore(this)) {
            getmTitle().setIvLeftVisiable(View.GONE);
            btProduct.setVisibility(View.GONE);
        }
    }

    private void refreshView() {
        initState();
        if ("1".equals(saveInfo.getJxAccountStatus())) {
            textAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.save_step1_on, 0, 0);
            textAccount.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
            textLine1.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
        }
        if ("1".equals(saveInfo.getJxPayPwdStatus())) {
            textPassword.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.save_step2_on, 0, 0);
            textPassword.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
            textLine2.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
        }
        if ("1".equals(saveInfo.getJxAutoClaimsTransferStatus()) && "1".equals(saveInfo.getJxAutoSubscribeStatus())) {
            textAuto.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.save_step3_on, 0, 0);
            textAuto.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
        }
        refreshButtonView();
    }

    private void refreshButtonView() {
        switch (jxState) {
            case 4:
                UserUtil.saveJxSave(SaveAcitvity.this, null, true);
                btOpen.setText("已完成存管");
                break;
            case 3:
                btOpen.setText("签署自动投标");
                break;
            case 2:
                btOpen.setText("设置交易密码");
                break;
            case 1:
                btOpen.setText("绑定银行卡");
                break;
            case 0:
                btOpen.setText("激活资金存管账户");
                break;
        }
    }

    @Override
    protected void onResume() {
        obtainData();
        super.onResume();
    }

    private void initState() {
        if ("0".equals(saveInfo.getJxAccountStatus())) {
            jxState = 0;
        } else if ("0".equals(saveInfo.getBindCardStatus())) {
            jxState = 1;
        } else if ("0".equals(saveInfo.getJxPayPwdStatus())) {
            jxState = 2;
        } else if ("0".equals(saveInfo.getJxAutoClaimsTransferStatus()) || "0".equals(saveInfo.getJxAutoSubscribeStatus())) {
            jxState = 3;
        } else {
            jxState = 4;
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

    private void startActivity() {
        switch (jxState) {
            case 0:
            case 1:
                startActivity(new Intent(this, SaveBindAcitvity.class));
                break;
            case 2:
            case 3:
                startActivity(new Intent(this, SaveResultAcitvity.class));
                break;
            case 4:
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_USER, null);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (UserUtil.isSaveBefore(this) && keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
