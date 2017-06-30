package com.miqian.mq.activity.rollin;

import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.DialogTipSave;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2017/6/15.
 * 充值方式选择页面
 */

public class IntoCheckAcitvity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Button btConfirm;
    private RadioGroup radioGroup;

    private DialogTipSave dialogTipInto;

    private String timeType = "1";
    private String transferType = "1";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                checkOrder();
                break;
        }
    }

    private void checkOrder() {
        begin();
        HttpRequest.rollInResult(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta meta) {
                end();
                showDialog();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, timeType, transferType);
    }

    @Override
    public void obtainData() {

    }

    private void showDialog() {
        if (dialogTipInto == null) {
            dialogTipInto = new DialogTipSave(this,"充值结果确认","充值结果确认查询已提交\\n请关注您的账户余额及资产") {
                @Override
                public void positionBtnClick() {
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_USER, null);
                }
            };
        }
        dialogTipInto.show();
    }

    @Override
    public void initView() {
        btConfirm = (Button) findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.radio_time);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.radio_channel);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into_check;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("充值结果确认");
    }

    @Override
    protected String getPageName() {
        return "充值结果确认";
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.radio_time:
                if (checkedId == R.id.bt_today) {
                    timeType = "1";
                } else if (checkedId == R.id.bt_yesterday) {
                    timeType = "0";
                }
                break;
            case R.id.radio_channel:
                if (checkedId == R.id.bt_bank) {
                    transferType = "1";
                } else if (checkedId == R.id.bt_alipay) {
                    transferType = "0";
                }
                break;
        }
    }
}
