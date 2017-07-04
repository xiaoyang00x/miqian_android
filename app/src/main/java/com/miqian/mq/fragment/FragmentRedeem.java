package com.miqian.mq.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miqian.mq.R;

/**
 * Description:
 *
 * 赎回
 */

public class FragmentRedeem extends BasicFragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frame_user, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }


    @Override
    protected String getPageName() {
        return "首页-我的";
    }


}