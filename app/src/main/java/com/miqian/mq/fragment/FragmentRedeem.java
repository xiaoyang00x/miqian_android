package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.adapter.CapitalRecordAdapter;
import com.miqian.mq.entity.CapitalItem;
import com.miqian.mq.entity.CapitalRecordResult;
import com.miqian.mq.entity.Page;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Uihelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 赎回
 */
public class FragmentRedeem extends BasicFragment {
    private View view;
    @BindView(R.id.recyclerview_record)
    RecyclerView mRecyclerView;
    @BindView(R.id.frame_no_recorddata)
    View mViewnoresult_data;
    @BindView(R.id.tv_recordtip)
    TextView tvTips;
    @BindView(R.id.iv_record_data)
    ImageView ivData;
    @BindView(R.id.data_view)
    View mContentView;
    @BindView(R.id.tv_refreshdata)
    TextView tVRefreshdata;
    CapitalRecordAdapter adapter;
    List<CapitalItem> list = new ArrayList<>();
    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frame_userrecord, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        initView();
        obtainData();
        return view;
    }

    private void initView() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity).colorResId(R.color.mq_b4).size(1).marginResId(R.dimen.margin_left_right).build());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 3) {
                    loadMore();
                }
            }
        });
    }

    /**
     * 无数据
     */
    protected void showEmptyView() {
        mContentView.setVisibility(View.VISIBLE);
        mViewnoresult_data.setVisibility(View.VISIBLE);
        tVRefreshdata.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        tvTips.setText("暂时没有数据");
        ivData.setBackgroundResource(R.drawable.nodata);
    }

    /**
     * 显示数据
     */
    protected void showContentView() {
        mContentView.setVisibility(View.VISIBLE);
        mViewnoresult_data.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.tv_refreshdata)
    public void reFreshData() {
        showContentView();
        obtainData();
    }

    /**
     * 获取失败，请重新获取
     */
    protected void showErrorView() {

        mContentView.setVisibility(View.VISIBLE);
        mViewnoresult_data.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        tVRefreshdata.setVisibility(View.VISIBLE);
        if (MobileOS.getNetworkType(mContext) == -1) {
            tvTips.setText("暂时没有网络");
            ivData.setBackgroundResource(R.drawable.nonetwork);
        } else {
            tvTips.setText("数据获取失败，请重新获取");
            ivData.setBackgroundResource(R.drawable.error_data);
        }
    }

    private void obtainData() {
        begin();
        pageNo = 1;
        begin();
        //取全部
        HttpRequest.getCapitalRecords(mActivity, new ICallback<CapitalRecordResult>() {

            @Override
            public void onSucceed(CapitalRecordResult result) {
                end();
                list.clear();
                CapitalRecordResult record = result;
                if (record.getData() != null && record.getData().getList().size() > 0) {
                    page = record.getData().getPageInfo();
                    list.addAll(record.getData().getList());
                    refreshView();
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
                showErrorView();
            }
        }, String.valueOf(pageNo), pageSize, "40");

    }

    private void loadMore() {
        if (!isLoading) {
            if (list.size() >= page.getTotalPage()) {
                return;
            }
            isLoading = true;
            pageNo += 1;
            //加载更多
            HttpRequest.getCapitalRecords(mActivity, new ICallback<CapitalRecordResult>() {

                @Override
                public void onSucceed(CapitalRecordResult result) {
                    List<CapitalItem> tempList = result.getData().getList();
                    if (list != null && tempList != null && tempList.size() > 0) {
                        list.addAll(tempList);
                        adapter.notifyItemInserted(list.size());
                    }
                    isLoading = false;
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;
                    Uihelper.showToast(mActivity, error);
                }
            }, String.valueOf(pageNo), pageSize, "10");

        }
    }

    private void refreshView() {
        adapter = new CapitalRecordAdapter(list);
        adapter.setMaxItem(page.getTotalPage());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected String getPageName() {
        return "我的资金记录";
    }


}