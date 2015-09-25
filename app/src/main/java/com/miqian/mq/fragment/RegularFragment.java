package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miqian.mq.R;
import com.miqian.mq.adapter.HomeAdapter;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.RegularEarn;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolei_wang on 15/9/16.
 */
public class RegularFragment extends BasicFragment {

    List<RegularEarn> mDatas = null;
    RegularListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regular, container, false);
        findView(view);
        setView();
        getMainRegular();
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
//        mDatas = new ArrayList<>();
//
//        mAdapter = new RegularListAdapter(mDatas, mApplicationContext, swipeRefreshLayout);
//        recyclerView.setAdapter(mAdapter);
    }

    private void getMainRegular() {
        HttpRequest.getMainRegular(getActivity(), new ICallback<GetRegularResult>() {

            @Override
            public void onSucceed(GetRegularResult result) {
                if(result == null) return;
                GetRegularInfo info = result.getData();
                if(info == null) return;
//                HomePageInfo info = result;
//                Uihelper.showToast(getActivity(), "success ");
//                if (null != mContext) {
                    if (null == mAdapter) {
                        mAdapter = new RegularListAdapter(info.getRegList(), info.getPlanList(), mApplicationContext, swipeRefreshLayout);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.addAll(info.getRegList());
                    }
//                    swipeRefresh.setRefreshing(false);
//                }
            }

            @Override
            public void onFail(String error) {
                Uihelper.showToast(getActivity(), error);
            }
        });
    }
}
