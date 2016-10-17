package com.miqian.mq.activity.user;

import android.content.Context;
import android.content.Intent;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.GestureLockVerifyActivity;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by 小坛 on 2016/10/17.
 * 开通汇付存管界面
 */

public class OpenHuiFuActivity extends BaseActivity {
    private static int mType;

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_openhuifu;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

    }

    @Override
    protected String getPageName() {
        return "";
    }

    /**
     * 进入开通存管界面
     * @param context
     * @param type
     */
    
    public static void startActivity(Context context,int type) {
        mType=type;
        Intent intent = new Intent(context, OpenHuiFuActivity.class);
        context.startActivity(intent);
    }

}
