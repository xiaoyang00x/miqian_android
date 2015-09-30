package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.adapter.HomeAdapter;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.RegularEarn;
import com.miqian.mq.entity.RegularPlan;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolei_wang on 15/9/16.
 */
public class RegularFragment extends BasicFragment {

    private final String TAG = "RegularFragment";
    RegularListAdapter mAdapter;
    private ArrayList<RegularPlan> planList;
    private ArrayList<RegularEarn> regList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regular, container, false);
        findView(view);
        setView();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        getMainRegular();
    }

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView titleText;
    private void findView(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        titleText = (TextView) view.findViewById(R.id.title);
    }

    private void setView() {
        titleText.setText("定期");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                    Log.d("onScrollStateChanged", "lastVisibleItem = " + lastVisibleItem + " ------------- totalItemCount = " + totalItemCount);

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1)) {
//                        Toast.makeText(mActivity, "加载更多", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMainRegular();
            }
        });

        if(regList != null) {
            mAdapter = new RegularListAdapter(regList, planList, mApplicationContext, swipeRefreshLayout);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();
    private void getMainRegular() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        HttpRequest.getMainRegular(getActivity(), new ICallback<GetRegularResult>() {

            @Override
            public void onSucceed(GetRegularResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefreshLayout.setRefreshing(false);
                if(result == null) return;
                GetRegularInfo info = result.getData();
                if(info == null) return;
                regList = info.getRegList();
                planList = info.getPlanList();
//                    if (null == mAdapter) {
                        mAdapter = new RegularListAdapter(info.getRegList(), info.getPlanList(), mApplicationContext, swipeRefreshLayout);
                        recyclerView.setAdapter(mAdapter);
//                    } else {
//                        mAdapter.clear();
//                        mAdapter.addAll(info.getRegList());
//                    }
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefreshLayout.setRefreshing(false);
                Uihelper.showToast(getActivity(), error);
            }
        });
    }
}
