package com.miqian.mq.activity.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterUserMqb;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.entity.NewCurrentFoundFlow;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolei_wang on 2017/7/6.
 * Email: gwzheng521@163.com
 * Description: 我的秒钱宝首页
 */

public class UserMqbActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private MySwipeRefresh swipeRefresh;

    private AdapterUserMqb mAdapter;

    private int pageNo = 1;
    private boolean isLoading = false;

    private NewCurrentFoundFlow mData;
    private ArrayList<NewCurrentFoundFlow.InvestInfo> regInvestList;


    @Override
    public void obtainData() {
        pageNo = 1;
        if (!swipeRefresh.isRefreshing()) {
            begin();
        }
        HttpRequest.getUserMqb(mActivity, new ICallback<MqResult<NewCurrentFoundFlow>>() {
            @Override
            public void onSucceed(MqResult<NewCurrentFoundFlow> result) {
                end();
                swipeRefresh.setRefreshing(false);
                mData = result.getData();
                regInvestList = mData.getInvestList();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, String.valueOf(pageNo));
    }

    private void loadMore() {
        if (!isLoading) {
            if (regInvestList.size() >= mData.getPageInfo().getTotalPage()) {
                return;
            }
            isLoading = true;
            pageNo += 1;
            HttpRequest.getUserMqb(mActivity, new ICallback<MqResult<NewCurrentFoundFlow>>() {
                @Override
                public void onSucceed(MqResult<NewCurrentFoundFlow> result) {
                    List<NewCurrentFoundFlow.InvestInfo> tempList = result.getData().getInvestList();
                    if (regInvestList != null && tempList != null && tempList.size() > 0) {
                        regInvestList.addAll(tempList);
                        mAdapter.notifyItemInserted(regInvestList.size() + 1);
                    }
                    isLoading = false;
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;
                    Uihelper.showToast(mActivity, error);
                }
            }, String.valueOf(pageNo));
        }
    }

    @Override
    public void initView() {

        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                obtainData();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 2) {
                    loadMore();
                }
            }
        });
    }

    private void refreshView() {
        mAdapter = new AdapterUserMqb(this, regInvestList, mData.getNewCurrent(), mData.getPageInfo());
        mAdapter.setMaxItem(mData.getPageInfo().getTotalRecord());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_mqb;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("我的秒钱宝");
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.title_left:
//                titleLeft.setTextColor(getResources().getColor(R.color.mq_r1));
//                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left_selected);
//                titleRight.setTextColor(getResources().getColor(R.color.white));
//                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right);
//                isExpiry = "N";
//                mType = 0;
//                obtainData();
//                break;
//            case R.id.title_right:
//
//                titleLeft.setTextColor(getResources().getColor(R.color.white));
//                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left);
//                titleRight.setTextColor(getResources().getColor(R.color.mq_r1));
//                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right_selected);
//                isExpiry = "Y";
//                mType = 1;
//                obtainData();
//                break;
//        }
    }


    @Override
    protected String getPageName() {
        return "我的秒钱宝";
    }

}
