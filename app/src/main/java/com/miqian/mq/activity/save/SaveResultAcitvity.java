package com.miqian.mq.activity.save;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.rollin.IntoActivity;
import com.miqian.mq.entity.SaveInfo;
import com.miqian.mq.entity.SaveInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ActivityStack;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2017/6/15.
 * 江西银行存管
 * 设置交易密码 签署协议结果页面
 */

public class SaveResultAcitvity extends BaseActivity implements View.OnClickListener {

    private LinearLayout frameAccount;
    private LinearLayout frameAgreement;
    private LinearLayout frameStep;
    private LinearLayout frameFish;

    private TextView textStatus;

    private TextView textName;
    private TextView textNo;

    private TextView textPassword;
    private TextView textLine2;
    private TextView textAuto;

    private TextView textState1;
    private TextView textState2;

    private Button btSubmit;
    private Button btRollin;
    private Button btProduct;

    private SaveInfo saveInfo;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                startActivity();
                break;
            case R.id.bt_product:
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_REGULAR, null);
                break;
            case R.id.bt_rollin:
                ActivityStack.getActivityStack().clearActivity();
                startActivity(new Intent(this, IntoActivity.class));
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
                Uihelper.showToast(SaveResultAcitvity.this, error);
            }
        });
    }

    private void refreshView() {
        if ("0".equals(saveInfo.getJxPayPwdStatus())) {
            frameAccount.setVisibility(View.VISIBLE);
            frameAgreement.setVisibility(View.GONE);
            textStatus.setText("开户成功");
            textName.setText(saveInfo.getUserName());
            textNo.setText(saveInfo.getJxId());
            btSubmit.setText("设置交易密码");
        } else {
            frameAccount.setVisibility(View.GONE);
            frameAgreement.setVisibility(View.VISIBLE);
            textPassword.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.save_step2_on, 0, 0);
            textPassword.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
            textLine2.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
            if ("0".equals(saveInfo.getJxAutoClaimsTransferStatus())) {
                textStatus.setText("设置成功");
                btSubmit.setText("签署自动债权转让协议");
            } else if ("0".equals(saveInfo.getJxAutoSubscribeStatus())) {
                textStatus.setText("签署成功");
                btSubmit.setText("签署自动投标协议");
                textState1.setText("已签署");
                textState1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.save_tick, 0, 0, 0);
                textState1.setBackgroundResource(R.drawable.save_auto_agreement_selector);
            } else {
                textStatus.setText("签署成功\n恭喜您，开通存管成功，即刻投资理财吧");
                textAuto.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.save_step3_on, 0, 0);
                textAuto.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
                frameStep.setVisibility(View.GONE);
                frameFish.setVisibility(View.VISIBLE);
                textState1.setText("已签署");
                textState1.setBackgroundResource(R.drawable.save_auto_agreement_selector);
                textState1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.save_tick, 0, 0, 0);
                textState2.setText("已签署");
                textState2.setBackgroundResource(R.drawable.save_auto_agreement_selector);
                textState2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.save_tick, 0, 0, 0);
            }
        }
    }

//    private void initState() {
//        if ("0".equals(saveInfo.getJxPayPwdStatus())) {
//            jxState = 2;
//        } else if ("0".equals(saveInfo.getJxAutoClaimsTransferStatus()) || "0".equals(saveInfo.getJxAutoSubscribeStatus())) {
//            jxState = 3;
//        } else {
//            jxState = 4;
//        }
//    }

    @Override
    public void initView() {
        frameAccount = (LinearLayout) findViewById(R.id.frame_account);
        frameAgreement = (LinearLayout) findViewById(R.id.frame_agreement);
        frameStep = (LinearLayout) findViewById(R.id.frame_step);
        frameFish = (LinearLayout) findViewById(R.id.frame_fish);

        textStatus = (TextView) findViewById(R.id.text_status);

        textPassword = (TextView) findViewById(R.id.text_password);
        textLine2 = (TextView) findViewById(R.id.text_line2);
        textAuto = (TextView) findViewById(R.id.text_auto);

        textName = (TextView) findViewById(R.id.text_name);
        textNo = (TextView) findViewById(R.id.text_no);

        textState1 = (TextView) findViewById(R.id.text_state1);
        textState2 = (TextView) findViewById(R.id.text_state2);

        btSubmit = (Button) findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(this);
        btRollin = (Button) findViewById(R.id.bt_rollin);
        btRollin.setOnClickListener(this);
        btProduct = (Button) findViewById(R.id.bt_product);
        btProduct.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_jx_result;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("开通存管账户");
    }

    @Override
    protected String getPageName() {
        return "开通存管账户";
    }

    private void startActivity() {
        if ("0".equals(saveInfo.getJxPayPwdStatus())) {
            HttpRequest.setJxPassword(SaveResultAcitvity.this);
        } else if ("0".equals(saveInfo.getJxAutoClaimsTransferStatus())) {
            HttpRequest.autoClaims(SaveResultAcitvity.this);
        } else if ("0".equals(saveInfo.getJxAutoSubscribeStatus())) {
            HttpRequest.autoSubscribe(SaveResultAcitvity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                obtainData();
            } else if (resultCode == 1) {

            }
        } else if (requestCode == 2) {

        }
    }
}
