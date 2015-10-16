package com.miqian.mq.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by guolei_wang on 15/9/16.
 */
public abstract class BasicFragment extends Fragment {

    protected Context mContext;
    protected Context mApplicationContext;
    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mContext = activity.getBaseContext();
        mApplicationContext = activity.getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageName());
    }

    /**
     * 获取页面名称，用于友盟页面统计
     * @return
     */
    protected abstract String getPageName();

}
