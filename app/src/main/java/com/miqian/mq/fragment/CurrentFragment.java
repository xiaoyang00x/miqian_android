package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.adapter.CurrentProjectAdapter;
import com.miqian.mq.entity.CurrentProjectInfo;
import com.miqian.mq.entity.CurrentProjectResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.MyAsyncTask;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.ServerBusyView;

import java.util.ArrayList;

/**
 * 秒钱宝首页
 */
public class CurrentFragment extends BasicFragment {

    private View rootView;
    private MySwipeRefresh swipeRefresh;
    private RecyclerView recyclerView;

    private ServerBusyView serverBusyView; // 服务器繁忙(无网络)页面

    private CurrentProjectAdapter mAdapter;
    private ArrayList<CurrentProjectInfo> mData;

    private static final int STARTPAGE = 1; // 起始页
    private int pageNo = STARTPAGE; // 下一页,从第一页开始
    private final int pageSize = 10;
    private boolean isFinished; // 无更多数据

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = mInflater.inflate(R.layout.fragment_current, null);
            findView();
            initTitle();
            initView();
            initListener();
        }
        if (null == mData) {
            obtainFirstPageData();
        }
        return rootView;
    }

    private void initTitle() {
        ((TextView) rootView.findViewById(R.id.title)).setText("秒钱宝");
    }

    private void findView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeRefresh = (MySwipeRefresh) rootView.findViewById(R.id.swipe_refresh);
        serverBusyView = (ServerBusyView) rootView.findViewById(R.id.serverBusyView);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CurrentProjectAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
        recyclerView.addOnScrollListener(mOnScrollListener);
        serverBusyView.setListener(requestAgainListener);
    }

    private MySwipeRefresh.OnPullRefreshListener mOnPullRefreshListener = new MySwipeRefresh.OnPullRefreshListener() {
        @Override
        public void onRefresh() {
            obtainFirstPageData();
        }
    };

    // 上拉 到 倒数第二个元素开始获取下一页数据
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // 当不滚动时
            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                //获取最后一个完全显示的ItemPosition  无数据则返回 -1
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();
                // 判断是否即将滚动到底部 且 数据未全部加载完
                if (lastVisibleItem == totalItemCount - 1 && totalItemCount != 0 && !isFinished) {
                    obtainNextPageData();
                }
            }
        }

    };

    // 服务器繁忙页面 - 再次请求 - 刷新
    private ServerBusyView.IRequestAgainListener requestAgainListener = new ServerBusyView.IRequestAgainListener() {
        @Override
        public void request() {
            obtainFirstPageData();
        }
    };

    // 获取第一页数据/刷新数据
    private void obtainFirstPageData() {
        pageNo = STARTPAGE;
        swipeRefresh.setRefreshing(true);
        obtainData(new ICallback<CurrentProjectResult>() {

            @Override
            public void onSucceed(CurrentProjectResult result) {
                mData = result.getData().getCurrentList(); // 缓存第一页数据
                mAdapter.clear();
                mAdapter.addAll(mData);
                mAdapter.notifyDataSetChanged();
                serverBusyView.hide();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                if (error.equals(MyAsyncTask.SERVER_ERROR) && mData == null) {
                    serverBusyView.showServerBusy();
                } else if (error.equals(MyAsyncTask.NETWORK_ERROR) && mData == null) {
                    serverBusyView.showNoNetwork();
                }
            }
        });
    }

    // 获取下一页数据
    private void obtainNextPageData() {
        pageNo++;
        obtainData(new ICallback<CurrentProjectResult>() {

            @Override
            public void onSucceed(CurrentProjectResult result) {
                int size = mAdapter.getItemCount() - 2;
                mAdapter.addAll(result.getData().getCurrentList());
                // 刷新当前页面数据 － 从倒数第二条数据开始刷新（倒数第一条是loadingMoreView）
                mAdapter.notifyItemRangeChanged(size, result.getData().getCurrentList().size() + 2);
            }

            @Override
            public void onFail(String error) {
                pageNo--;
            }
        });
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();

    // 获取当前页面数据 - 若无数据
    private void obtainData(final ICallback<CurrentProjectResult> iCallback) {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        HttpRequest.getCurrentProjectList(mContext, new ICallback<CurrentProjectResult>() {

            @Override
            public void onSucceed(CurrentProjectResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                if (result == null || result.getData() == null) {
                    return;
                }
                if (result.getData().getCurrentList() == null) {
                    mAdapter.hasLoadAllData(true);
                } else {
                    isFinished = result.getData().getCurrentList().size() < pageSize;
                    mAdapter.hasLoadAllData(isFinished);
                }
                serverBusyView.hide();
                iCallback.onSucceed(result);
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                iCallback.onFail(error);
            }
        }, pageNo, pageSize);
    }

    @Override
    protected String getPageName() {
        return "首页-秒钱宝";
    }

}
