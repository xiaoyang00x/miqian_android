package com.miqian.mq.activity.save;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.rollin.IntoActivity;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.entity.SaveInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ActivityStack;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jackie on 2017/6/15.
 * 江西银行存管
 * 设置交易密码 签署协议结果页面
 */

public class SaveResultAcitvity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.frame_account)
    LinearLayout frameAccount;
    @BindView(R.id.frame_agreement)
    LinearLayout frameAgreement;
    @BindView(R.id.frame_step)
    LinearLayout frameStep;
    @BindView(R.id.frame_fish)
    LinearLayout frameFish;

    @BindView(R.id.text_status)
    TextView textStatus;

    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_no)
    TextView textNo;

    @BindView(R.id.text_password)
    TextView textPassword;
    @BindView(R.id.text_line2)
    TextView textLine2;
    @BindView(R.id.text_auto)
    TextView textAuto;

    @BindView(R.id.text_state1)
    TextView textState1;
    @BindView(R.id.text_state2)
    TextView textState2;

    @BindView(R.id.bt_submit)
    Button btSubmit;
    @BindView(R.id.bt_rollin)
    Button btRollin;
    @BindView(R.id.bt_product)
    Button btProduct;

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

            if ("1".equals(saveInfo.getJxAutoClaimsTransferStatus())) {
                textState1.setText("已签署");
                textState1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.save_tick, 0, 0, 0);
                textState1.setBackgroundResource(R.drawable.save_auto_agreement_selector);
            }

            if ("1".equals(saveInfo.getJxAutoSubscribeStatus())) {
                textState2.setText("已签署");
                textState2.setBackgroundResource(R.drawable.save_auto_agreement_selector);
                textState2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.save_tick, 0, 0, 0);
            }

            if ("0".equals(saveInfo.getJxAutoClaimsTransferStatus())) {
                textStatus.setText("设置成功");
                btSubmit.setText("签署自动债权转让协议");
            } else if ("0".equals(saveInfo.getJxAutoSubscribeStatus())) {
                textStatus.setText("签署成功");
                btSubmit.setText("签署自动投标协议");
            } else {
                textStatus.setText("签署成功\n恭喜您，开通存管成功，即刻投资理财吧");
                textAuto.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.save_step3_on, 0, 0);
                textAuto.setTextColor(ContextCompat.getColor(this, R.color.mq_r1_v2));
                frameStep.setVisibility(View.GONE);
                frameFish.setVisibility(View.VISIBLE);
                UserUtil.saveJxSave(SaveResultAcitvity.this, null, true);
            }
        }
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        btSubmit.setOnClickListener(this);
        btRollin.setOnClickListener(this);
        btProduct.setOnClickListener(this);

        if (UserUtil.isSaveBefore(this)) {
            getmTitle().setIvLeftVisiable(View.GONE);
        }
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
            }
        } else if (requestCode == 2) {

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
