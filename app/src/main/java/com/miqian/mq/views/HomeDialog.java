package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.GetHomeActivity;
import com.miqian.mq.entity.GetHomeActivityResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页运动活动对话框
 */
public class HomeDialog extends Dialog {
    private Button btn_confirm;
    private View btn_close;
    private TextView tv_title;
    private TextView tv_content;

    private GetHomeActivity mData;

    public HomeDialog(Context context, GetHomeActivity data) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.home_dialog);
        setCancelable(false);
        mData = data;
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_close = findViewById(R.id.btn_close);

        tv_title.setText(mData.getTitleCase());
        tv_content.setText(mData.getBackgroundCase());
        btn_confirm.setText(mData.getEnterCase());

        btn_confirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                MobclickAgent.onEvent(getContext(), "home_pop_conform");
                WebActivity.startActivity(getContext(), mData.getJumpUrl());
                getActivityFeedback();
                dismiss();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                getActivityFeedback();
                dismiss();
            }
        });
    }

    /**
     * 用户已读反馈
     */
    private void getActivityFeedback() {
        HttpRequest.getActivityFeedback(getContext(), mData.getActivityId(), mData.getActivityPlanId(), null);
    }

}
