package com.miqian.mq.fragment;

import android.graphics.Bitmap;
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
import com.miqian.mq.entity.GetHomeActivity;
import com.miqian.mq.entity.GetHomeActivityResult;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.HomePageInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.HomeDialog;
import com.miqian.mq.views.MySwipeRefresh;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentHome extends BasicFragment implements ImageLoadingListener{

    private View view;
    private RecyclerView recyclerView;
    private MySwipeRefresh swipeRefresh;
    HomeAdapter adapter;
    HomePageInfo mData;
    GetHomeActivity mHomeActivityData;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private HomeDialog homeDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frame_home, null);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        findView(view);
        setView();
//        getHomeActivity();
        if (mData == null) {
            getHomePageInfo();
        }
        return view;
    }

    private void setView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (mData != null) {
            adapter = new HomeAdapter(mActivity, mData);
            recyclerView.setAdapter(adapter);
        }
    }

    private Timer timer;// 首页焦点图自动滑动timer
    private TimerTask timerTask;// 首页焦点图自动滑动TimerTask

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }

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
    protected String getPageName() {
        return "首页";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        mData = null;
    }

    private void findView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefresh = (MySwipeRefresh) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                getHomePageInfo();
            }
        });
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
        begin();
        swipeRefresh.setRefreshing(true);
        HttpRequest.getHomePageInfo(getActivity(), new ICallback<HomePageInfoResult>() {

            @Override
            public void onSucceed(HomePageInfoResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
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
                end();
                swipeRefresh.setRefreshing(false);
                Uihelper.showToast(getActivity(), error);
            }
        });
    }

    private boolean inActivityProcess = false;
    private final Object mActivityLock = new Object();

    private void getHomeActivity() {
        if (inActivityProcess) {
            return;
        }
        synchronized (mActivityLock) {
            inActivityProcess = true;
        }
        HttpRequest.getHomeActivity(getActivity(), "", new ICallback<GetHomeActivityResult>() {

            @Override
            public void onSucceed(GetHomeActivityResult result) {
                synchronized (mActivityLock) {
                    inActivityProcess = false;
                }
                if (result == null) return;
                mHomeActivityData = result.getData();

                if(mHomeActivityData != null) {
                    showActivityDialog();
//                    imageLoader.loadImage(mHomeActivityData.getBackgroundUrl(), options);
                }
            }

            @Override
            public void onFail(String error) {
                synchronized (mActivityLock) {
                    inActivityProcess = false;
                }
            }
        });
    }

    private void showActivityDialog() {
        homeDialog = new HomeDialog(mActivity, mHomeActivityData);
        homeDialog.show();
    }
}