package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.adapter.HomeAdapter;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.HomePageInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentHome extends BasicFragment {

    private TextView titleText;
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    HomeAdapter adapter;
    HomePageInfo mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHomePageInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frame_home, null);
        findView(view);
        setView();

        return view;
    }

    private void setView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomePageInfo();
            }
        });

        titleText.setText("首页");
        if(mData != null) {
            adapter = new HomeAdapter(mActivity, mData);
            recyclerView.setAdapter(adapter);
        }
    }

    private Timer timer;// 首页焦点图自动滑动timer
    private TimerTask timerTask;// 首页焦点图自动滑动TimerTask
    private class AutoScrollTimerTask extends TimerTask {
        @Override
        public void run() {
            if (null != adapter) adapter.setAutoScroll();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (timer == null) {// 自动滑动
            timer = new Timer();
            timerTask = new AutoScrollTimerTask();
            timer.schedule(timerTask, 5000, 5000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        mData = null;
    }

    private void findView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        titleText = (TextView) view.findViewById(R.id.title);
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();

    private void getHomePageInfo() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        HttpRequest.getHomePageInfo(getActivity(), new ICallback<HomePageInfoResult>() {

            @Override
            public void onSucceed(HomePageInfoResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                if (result == null) return;
                mData = result.getData();
                if (mData == null) return;
//                if (null == adapter) {
                    adapter = new HomeAdapter(mActivity, mData);
                    recyclerView.setAdapter(adapter);
//                } else {
//                    adapter.notifyDataSetChanged(info);
//                }
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                Uihelper.showToast(getActivity(), error);
            }
        });
    }
}