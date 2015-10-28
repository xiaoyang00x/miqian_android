package com.miqian.mq.activity.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterUserRegular;
import com.miqian.mq.entity.RegInvest;
import com.miqian.mq.entity.UserRegular;
import com.miqian.mq.entity.UserRegularResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

public class UserRegularActivity extends BaseActivity implements View.OnClickListener, AdapterUserRegular.MyItemClickListener {

    private Button titleLeft;
    private Button titleRight;
    private RecyclerView mRecyclerView;
    private ImageButton btLeft;
    private MySwipeRefresh swipeRefresh;

    private AdapterUserRegular mAdapter;

    private int pageNo = 1;
    private String pageSize = "10";
    private String isExpiry = "N";//是否结息 N：计息中 Y：已结息
    private String isForce = "1";//是否强制刷新 1强制刷新 0不强制刷新
    private boolean isLoading = false;
    private int mType = 0;

    private UserRegular userRegular;
    private List<RegInvest> regInvestList;

    @Override
    public void obtainData() {
        pageNo = 1;
        isForce = "1";
        if (!swipeRefresh.isRefreshing()) {
            mWaitingDialog.show();
        }
        HttpRequest.getUserRegular(mActivity, new ICallback<UserRegularResult>() {
            @Override
            public void onSucceed(UserRegularResult result) {
                swipeRefresh.setRefreshing(false);
                mWaitingDialog.dismiss();
                userRegular = result.getData();
                regInvestList = userRegular.getRegInvest();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, String.valueOf(pageNo), pageSize, isExpiry, isForce);
    }

    private void loadMore() {
        if (!isLoading) {
            if (regInvestList.size() >= userRegular.getPage().getCount()) {
//                mAdapter.notifyItemChanged(regInvestList.size() + 1);
                return;
            }
            isLoading = true;
            pageNo += 1;
            isForce = "0";
            HttpRequest.getUserRegular(mActivity, new ICallback<UserRegularResult>() {
                @Override
                public void onSucceed(UserRegularResult result) {
                    List<RegInvest> tempList = result.getData().getRegInvest();
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
            }, String.valueOf(pageNo), pageSize, isExpiry, isForce);
        }
    }

    @Override
    public void initView() {
        btLeft = (ImageButton) findViewById(R.id.bt_left);
        btLeft.setOnClickListener(this);

        titleLeft = (Button) findViewById(R.id.title_left);
        titleRight = (Button) findViewById(R.id.title_right);

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);

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
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 2) {
                    loadMore();
                }
            }
        });
    }

    private void refreshView() {
        mAdapter = new AdapterUserRegular(regInvestList, userRegular.getReg(), mType);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setMaxItem(userRegular.getPage().getCount());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_regualr;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        View parentView = (View) mTitle.getParent();
        parentView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                titleLeft.setTextColor(getResources().getColor(R.color.mq_r1));
                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left_selected);
                titleRight.setTextColor(getResources().getColor(R.color.white));
                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right);
                isExpiry = "N";
                mType = 0;
                obtainData();
                break;
            case R.id.title_right:
                titleLeft.setTextColor(getResources().getColor(R.color.white));
                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left);
                titleRight.setTextColor(getResources().getColor(R.color.mq_r1));
                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right_selected);
                isExpiry = "Y";
                mType = 1;
                obtainData();
                break;
            case R.id.bt_left:
                UserRegularActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int postion) {
        RegInvest regInvest = regInvestList.get(postion);
        Intent intent = new Intent(mActivity, UserRegularDetailActivity.class);
        intent.putExtra("investId", regInvest.getId());
        intent.putExtra("clearYn", regInvest.getBearingStatus());
        intent.putExtra("projectType", regInvest.getProdId());
        startActivity(intent);
    }

    @Override
    protected String getPageName() {
        return "我的定期";
    }
}
