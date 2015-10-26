package com.miqian.mq.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.miqian.mq.views.ProgressDialogView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by guolei_wang on 15/9/16.
 */
public abstract class BasicFragment extends Fragment {

    protected Context mContext;
    protected Context mApplicationContext;
    protected Activity mActivity;
    private Dialog mWaitingDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mContext = activity.getBaseContext();
        mApplicationContext = activity.getApplicationContext();
    }

    protected void begin() {
        if (mWaitingDialog == null) {
            mWaitingDialog = ProgressDialogView.create(getActivity());
        }
        if (!mWaitingDialog.isShowing())
            mWaitingDialog.show();
    }

    protected void end() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing())
            mWaitingDialog.dismiss();
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
