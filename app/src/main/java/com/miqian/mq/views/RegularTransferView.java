package com.miqian.mq.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.adapter.RegularTransferAdapter;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.entity.RegularTransferList;
import com.miqian.mq.entity.RegularTransferListResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.MyAsyncTask;

/**
 * @author wangduo
 * @description: 定期转让view
 * @email: cswangduo@163.com
 * @date: 16/5/25
 */
public class RegularTransferView {

    private Context mContext;

    private View mView;
    private MySwipeRefresh swipeRefresh;
    private RecyclerView recyclerView;

    private ServerBusyView serverBusyView;

    private RegularTransferAdapter mAdapter;
    private RegularTransferList mData; // 数据缓存 - 当前页面是否有数据 - 是否从服务器获取数据成功

    private static final int STARTPAGE = 1; // 起始页
    private int nextPage = STARTPAGE; // 下一页,从第一页开始

    public RegularTransferView(Context mContext) {
        this.mContext = mContext;
        mView = LayoutInflater.from(mContext).inflate(R.layout.regular_project, null);
        findView();
        initView();
        initListener();
        obtainFirstPageData();
    }

    private void findView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        swipeRefresh = (MySwipeRefresh) mView.findViewById(R.id.swipe_refresh);
        serverBusyView = (ServerBusyView) mView.findViewById(R.id.serverBusyView);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RegularTransferAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
        recyclerView.addOnScrollListener(mOnScrollListener);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        serverBusyView.setListener(requestAgainListener);
    }

    private RegularTransferAdapter.OnItemClickListener mOnItemClickListener = new RegularTransferAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
        }
    };

    // 下拉刷新
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
                if (lastVisibleItem == totalItemCount - 1 && totalItemCount != 0 && totalItemCount != mAdapter.getTotalCount()) {
                    obtainNextPageData();
                }
            }
        }

    };

    // 服务器繁忙(无网络)页面 - 再次请求 - 刷新
    private ServerBusyView.IRequestAgainListener requestAgainListener = new ServerBusyView.IRequestAgainListener() {
        @Override
        public void request() {
            obtainFirstPageData();
        }
    };

    // 获取第一页数据/刷新数据
    public void obtainFirstPageData() {
        nextPage = STARTPAGE;
        obtainData(new ICallback<RegularTransferListResult>() {

            @Override
            public void onSucceed(RegularTransferListResult result) {
                if (result == null || (mData = result.getData()) == null) { // 缓存第一页数据
                    return;
                }
                mAdapter.setTotalCount(result.getData().getPage().getCount());
                mAdapter.clear();
                mAdapter.addAll(result.getData().getSubjectData());
                mAdapter.notifyDataSetChanged();
                serverBusyView.hide();
            }

            @Override
            public void onFail(String error) {
                if (error.equals(MyAsyncTask.SERVER_ERROR) && mData == null) {
                    serverBusyView.showServerBusy();
                } else if (error.equals(MyAsyncTask.NETWORK_ERROR) && mData == null) {
                    serverBusyView.showNoNetwork();
                }
            }
        });
    }

    // 获取下一页数据
    public void obtainNextPageData() {
        nextPage++;
        obtainData(new ICallback<RegularTransferListResult>() {

            @Override
            public void onSucceed(RegularTransferListResult result) {
                if (result == null || result.getData() == null) {
                    return;
                }
                int size = mAdapter.getItemCount() - 1;
                mAdapter.addAll(result.getData().getSubjectData());
                mAdapter.notifyItemRangeChanged(size, result.getData().getSubjectData().size());
            }

            @Override
            public void onFail(String error) {
                nextPage--;
            }
        });
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();

    // 获取数据:定期项目转让列表
    public void obtainData(final ICallback<RegularTransferListResult> iCallback) {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        if (nextPage == STARTPAGE) { // 加载第一页数据
            swipeRefresh.setRefreshing(true);
        }
        HttpRequest.getRegularTransferList(mContext, new ICallback<RegularTransferListResult>() {

            @Override
            public void onSucceed(RegularTransferListResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                iCallback.onSucceed(result);
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
                iCallback.onFail(error);
            }
        }, nextPage);
    }

    public View getView() {
        return mView;
    }

    public void setVisibility(int visibility) {
        if (null != mView) {
            mView.setVisibility(visibility);
            if (visibility == View.VISIBLE && null == mData) {
                obtainFirstPageData();
            }
        }
    }

}
