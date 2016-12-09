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
 * 首页优惠券过期对话框
 */
public class PromotionDialogOverdue extends Dialog implements View.OnClickListener {
    private TextView tv_description;
    private TextView tv_amount;
    private TextView tv_details;
    private View btn_close;

    private GetHomeActivity mData;

    public PromotionDialogOverdue(Activity context, GetHomeActivity data) {
        super(context, R.style.Dialog);
        setOwnerActivity(context);
        this.setContentView(R.layout.dialog_promotion_overdue);
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
                        UserUtil.loginActivity(getOwnerActivity(), MyTicketActivity.class, initDialogLogin(getOwnerActivity(), MyTicketActivity.class, 2));
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

    Dialog_Login dialog_login = null;

    private Dialog_Login initDialogLogin(final Context context, final Class<?> cls, int type) {
        if (dialog_login == null || dialog_login.type != type) {
            dialog_login = new Dialog_Login(context, type) {
                @Override
                public void loginSuccess() {
                    // TODO: 2015/10/10 Loading
                    if (Pref.getBoolean(Pref.GESTURESTATE, getContext(), true)) {
                        GestureLockSetActivity.startActivity(context, cls);
                    } else if (null != cls) {
                        getOwnerActivity().startActivity(new Intent(context, cls));
                    }
                    dismiss();
                }
            };
        }
        return dialog_login;
    }
}
