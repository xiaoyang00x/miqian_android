package com.miqian.mq.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

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

}
