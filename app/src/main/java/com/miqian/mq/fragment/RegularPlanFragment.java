package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miqian.mq.R;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.entity.RegularEarn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolei_wang on 15/9/16.
 */
public class RegularPlanFragment extends BasicFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regular_plan, container, false);
        findView(view);
        setView();
        return view;
    }
    private void findView(View view) {
    }

    private void setView() {
    }
}
