package com.miqian.mq.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.miqian.mq.views.ProgressDialogView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by guolei_wang on 15/9/16.
 */
public abstract class BasicFragment extends Fragment {

    Context mContext;
    Context mApplicationContext;
    Activity mActivity;
    private ProgressDialogView progressDialogView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mContext = activity.getBaseContext();
        mApplicationContext = activity.getApplicationContext();
    }

    void begin() {
        if (progressDialogView == null) {
            progressDialogView = new ProgressDialogView(getActivity());
        }
        progressDialogView.show();
    }

    void end() {
        if (progressDialogView != null) {
            progressDialogView.dismiss();
        }
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
     *
     * @return
     */
    protected abstract String getPageName();

}
