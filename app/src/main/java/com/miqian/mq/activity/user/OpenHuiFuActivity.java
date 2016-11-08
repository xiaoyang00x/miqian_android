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
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by 小坛 on 2016/10/17.
 * 开通汇付存管界面
 */

public class OpenHuiFuActivity extends BaseActivity {
    private static int mType;
    private Button btnClose;
    private TextView tvLaw;

    @Override
    public void obtainData() {

        Intent intent = getIntent();
        mType = intent.getIntExtra("entertype", 0);
        if (mType == TypeUtil.TYPE_OPENHF_REGISTER) {
            btnClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initView() {

        btnClose = (Button) findViewById(R.id.bt_close);
        tvLaw = (TextView) findViewById(R.id.tv_law);
        tvLaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //了解资金存管明细
                WebActivity.startActivity(mActivity, Urls.web_hf_help);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_openhuifu;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        switch (mType) {
            case TypeUtil.TYPE_OPENHF_REGISTER:
                mTitle.setTitleText("注册");
                break;
            case TypeUtil.TYPE_OPENHF_ROLLIN:
                mTitle.setTitleText("充值");
                break;
            case TypeUtil.TYPE_OPENHF_INVESTMENT:
                mTitle.setTitleText("认购");
                break;
            case TypeUtil.TYPE_OPENHF_ROLLOUT:
                mTitle.setTitleText("提现");
                break;
        }

    }

    @Override
    protected String getPageName() {
        return "开通资金存管";
    }

    /**
     * 进入开通存管界面
     *
     * @param context
     * @param type
     */

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, OpenHuiFuActivity.class);
        intent.putExtra("entertype", type);
        context.startActivity(intent);
    }

    //开通按钮
    public void btn_click(View v) {
        RealNameActivity.startActivity(mActivity, HfUpdateActivity.REQUEST_CODE_REGISTER);
    }

    //暂不开通按钮
    public void btn_close(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HfUpdateActivity.REQUEST_CODE_REGISTER) {
            if (resultCode == CurrentInvestment.SUCCESS) {
                Pref.saveBoolean(UserUtil.getPrefKey(mActivity, Pref.HF_ACCOUNT_STATUS), true, mActivity);
                finish();
            } else {
                Uihelper.showToast(mActivity, "开户失败，请重试");
            }
        }
    }
}
