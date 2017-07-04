package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.entity.GetHomeActivity;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;

/**
 * 首页红包对话框
 */
public class PromotionDialog extends Dialog implements View.OnClickListener {
    private TextView tv_description;
    private TextView tv_amount;
    private TextView tv_details;
    private View btn_close;

    private GetHomeActivity mData;

    public PromotionDialog(Activity context, GetHomeActivity data) {
        super(context, R.style.Dialog);
        setOwnerActivity(context);
        this.setContentView(R.layout.dialog_promotion);
        setCancelable(false);
        mData = data;
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_details = (TextView) findViewById(R.id.tv_details);
        btn_close = findViewById(R.id.btn_close);

        tv_amount.setText(mData.getTitleCase());
        tv_details.setText(mData.getEnterCase());
        tv_description.setText(mData.getBackgroundCase());

        btn_close.setOnClickListener(this);
        tv_details.setOnClickListener(this);

    }

    /**
     * 用户已读反馈
     */
    private void getActivityFeedback() {
        HttpRequest.getActivityFeedback(getContext(), mData.getActivityId(), mData.getActivityPlanId(), null);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.tv_details:
                if(TextUtils.isEmpty(mData.getJumpUrl())) {
                    if("3".equals(mData.getJumpNative())) {
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_REGULAR, null);
                    }else if("2".equals(mData.getJumpNative())) {
                     UserUtil.afterLoginActivity(getOwnerActivity(), MyTicketActivity.class);
                    }
                }else {
                    WebActivity.startActivity(getOwnerActivity(), mData.getJumpUrl());
                }
                getActivityFeedback();
                dismiss();
                break;
            case R.id.btn_close:
                getActivityFeedback();
                dismiss();
                break;
            default:
                break;
        }
    }
}
