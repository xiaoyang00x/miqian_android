package com.miqian.mq.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.miqian.mq.R;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.MyAsyncTask;

/**
 * @author wangduo
 * @description: 定期首页view
 * @email: cswangduo@163.com
 * @date: 16/5/25
 */
public class RegularProjectView {

    private Context mContext;
    private RegularListAdapter mAdapter;
    private GetRegularInfo mData;

    private View mView;
    private MySwipeRefresh swipeRefresh;
    private RecyclerView recyclerView;

    private ServerBusyView serverBusyView; // 服务器繁忙/无网络连接页面

    public RegularProjectView(Context mContext) {
        this.mContext = mContext;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.regular_project, null);
        findView();
        initView();
        initListener();
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
    }

    private void initListener() {
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
        recyclerView.addOnScrollListener(mOnScrollListener);
        serverBusyView.setListener(requestAgain);
    }

    private MySwipeRefresh.OnPullRefreshListener mOnPullRefreshListener = new MySwipeRefresh.OnPullRefreshListener() {
        @Override
        public void onRefresh() {
//                isFirstLoading = true;
            obtainData();
        }
    };

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
//            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
//            boolean isSlidingToLast = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //获取最后一个完全显示的ItemPosition
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部，并且是向右滚动
                if (lastVisibleItem == (totalItemCount - 1)) {
//                        Toast.makeText(mActivity, "加载更多", Toast.LENGTH_SHORT).show();
                }
            }
        }

    };

    private void refreshData() {
        if (mData == null) {
            obtainData();
//            if (isServerBusyPageShow) {
//                serverBusyView.showServerBusy();
//            } else if (isNoNetworkPageShow) {
//                serverBusyView.showNoNetwork();
//            } else {
//                serverBusyView.hide();
//            }
        }
    }

    // 服务器繁忙页面 - 再次请求 - 刷新
    private ServerBusyView.IRequestAgain requestAgain = new ServerBusyView.IRequestAgain() {
        @Override
        public void execute() {
            obtainData();
        }
    };

    private boolean inProcess = false;
    private final Object mLock = new Object();

    // 获取数据:定期项目
    public void obtainData() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        swipeRefresh.setRefreshing(true);
        HttpRequest.getRegularList(mContext, new ICallback<GetRegularResult>() {

            @Override
            public void onSucceed(GetRegularResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                if (result == null || (mData = result.getData()) == null) {
                    return;
                }
                mAdapter = new RegularListAdapter(mData, mContext);
                recyclerView.setAdapter(mAdapter);

                serverBusyView.hide();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
//                Uihelper.showToast(getActivity(), error);
                if (error.equals(MyAsyncTask.SERVER_ERROR) && mData == null) {
                    serverBusyView.showServerBusy();
                } else if (error.equals(MyAsyncTask.NETWORK_ERROR) && mData == null) {
                    serverBusyView.showNoNetwork();
                }
            }
        });
    }

    public View getView() {
        return mView;
    }

    public void setVisibility(int visibility) {
        if (null != mView) {
            mView.setVisibility(visibility);
        }
    }

}
