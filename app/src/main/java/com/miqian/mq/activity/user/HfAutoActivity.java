package com.miqian.mq.activity.user;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by 小坛 on 2016/10/17.
 * 汇付开通自动投标页面
 */

public class HfAutoActivity extends BaseActivity {

    private TextView tvLaw;
    private TextView hfTitle;
    private TextView hfContent;
    private TextView hfTip;
    private Button btOpen;

    @Override
    public void obtainData() {
    }

    @Override
    public void initView() {
        tvLaw = (TextView) findViewById(R.id.tv_law);
        tvLaw.setText("什么是自动投标");
        tvLaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //了解自动投标
                WebActivity.startActivity(mActivity, Urls.web_hf_help);
            }
        });

        hfTitle = (TextView) findViewById(R.id.hf_title);
        hfTitle.setText("开通自动投标");
        hfContent = (TextView) findViewById(R.id.hf_content);
        hfContent.setText("认购需开通自动投标功能");
        hfTip = (TextView) findViewById(R.id.hf_tip);
        hfTip.setText("开通自动投标功能，帮您自动匹配标的");
        btOpen = (Button) findViewById(R.id.bt_open);
        btOpen.setText("开通自动投标");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_openhuifu;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("自动投标");
    }

    @Override
    protected String getPageName() {
        return "开通自动投标";
    }

    /**
     * 进入开通存管界面
     *
     * @param context
     */

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HfAutoActivity.class);
        context.startActivity(intent);
    }

    //点击按钮
    public void btn_click(View v) {
        HttpRequest.autoHf(mActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HfUpdateActivity.REQUEST_CODE_AUTO) {
            if (resultCode == CurrentInvestment.SUCCESS) {
                Pref.saveBoolean(UserUtil.getPrefKey(mActivity, Pref.HF_AUTO_STATUS), true, mActivity);
                finish();
            } else {
                Uihelper.showToast(mActivity, "开通自动投标失败，请重试");
            }
        }
    }
}
