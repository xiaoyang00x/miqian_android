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
public class RegularFragment extends BasicFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regular, container, false);
        findView(view);
        setView();
        return view;
    }

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private void findView(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
    }

    private void setView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        List<RegularEarn> datas = new ArrayList<>();

        for(int i = 0; i < 6; i++) {
            RegularEarn earn = new RegularEarn("永兴医院医疗设备项目" , " 项目总额8000万", "15%");
            datas.add(i, earn);
        }


        RegularListAdapter adapter = new RegularListAdapter(datas, mApplicationContext, swipeRefreshLayout);
        recyclerView.setAdapter(adapter);
    }
}
