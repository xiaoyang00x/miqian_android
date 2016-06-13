package com.miqian.mq.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.adapter.RegularProjectAdapter;
import com.miqian.mq.entity.RegularProjectList;
import com.miqian.mq.entity.RegularProjectListResult;
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
    private RegularProjectAdapter mAdapter;
    private RegularProjectList mData;

    private View mView;
    private MySwipeRefresh swipeRefresh;
    private RecyclerView recyclerView;

    private ServerBusyView serverBusyView; // 服务器繁忙(无网络)页面

    public RegularProjectView(Context mContext) {
        this.mContext = mContext;
        mView = LayoutInflater.from(mContext).inflate(R.layout.regular_home_project, null);
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
        mAdapter = new RegularProjectAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
        serverBusyView.setListener(requestAgainListener);
    }

    private MySwipeRefresh.OnPullRefreshListener mOnPullRefreshListener = new MySwipeRefresh.OnPullRefreshListener() {
        @Override
        public void onRefresh() {
            obtainData();
        }
    };

    // 服务器繁忙页面 - 再次请求 - 刷新
    private ServerBusyView.IRequestAgainListener requestAgainListener = new ServerBusyView.IRequestAgainListener() {
        @Override
        public void request() {
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
        HttpRequest.getRegularProjectList(mContext, new ICallback<RegularProjectListResult>() {

            @Override
            public void onSucceed(RegularProjectListResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                if (result == null || (mData = result.getData()) == null) {
                    return;
                }
                mAdapter.clear();
                mAdapter.addAll(mData);
                mAdapter.notifyDataSetChanged();
                serverBusyView.hide();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                if (error.equals(MyAsyncTask.SERVER_ERROR) && mData == null) {
                    serverBusyView.showServerBusy();
                } else if (error.equals(MyAsyncTask.NETWORK_ERROR) && mData == null) {
                    serverBusyView.showNoNetwork();
                } else {
                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
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
            if (visibility == View.VISIBLE && null == mData) {
                obtainData();
            }
        }
    }

}
