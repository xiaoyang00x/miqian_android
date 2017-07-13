package com.miqian.mq.activity.user;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterMqbSubScripRecords;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.entity.NewCurrentFundFlow;
import com.miqian.mq.entity.Page;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guolei_wang on 2017/7/12.
 * Email: gwzheng521@163.com
 * Description: 我的秒钱宝认购记录
 */

public class ActivitySubscriptionRecords extends BaseActivity {

    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_redeem)
    TextView tvRedeem;
    @BindView(R.id.id_tab_line_iv2)
    ImageView tab2;
    @BindView(R.id.id_tab_line_iv4)
    ImageView tab4;
    @BindView(R.id.recyclerview_record)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.iv_data)
    ImageView ivData;

    private int type = 20;  //20 认购，40 赎回
    AdapterMqbSubScripRecords adapter;
    List<NewCurrentFundFlow.NewCurrentRecords> list = new ArrayList<>();
    private int pageNo = 1;
    private Page page;
    private boolean isLoading = false;

    @Override
    public void obtainData() {
        begin();
        pageNo = 1;
        //取全部
        HttpRequest.newCurrentFundFlow(mActivity, new ICallback<MqResult<NewCurrentFundFlow>>() {

            @Override
            public void onSucceed(MqResult<NewCurrentFundFlow> result) {
                end();
                list.clear();
                MqResult<NewCurrentFundFlow> record = result;
                if (record.getData() != null && record.getData().getList().size() > 0) {
                    page = record.getData().getPageInfo();
                    list.addAll(record.getData().getList());
                    refreshView();
                } else {
                    showEmptyData();
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
                showErrorData();
            }
        }, String.valueOf(pageNo), type + "");
    }

    private void refreshView() {
        adapter = new AdapterMqbSubScripRecords(list, type);
        adapter.setMaxItem(page.getTotalRecord());
        mRecyclerView.setAdapter(adapter);
    }

    private void loadMore() {
        if (!isLoading) {
            if (list.size() >= page.getTotalRecord()) {
                return;
            }
            isLoading = true;
            pageNo += 1;
            //加载更多
            HttpRequest.newCurrentFundFlow(mActivity, new ICallback<MqResult<NewCurrentFundFlow>>() {

                @Override
                public void onSucceed(MqResult<NewCurrentFundFlow> result) {
                    List<NewCurrentFundFlow.NewCurrentRecords> tempList = result.getData().getList();
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
            }, String.valueOf(pageNo), type + "");

        }
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
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

        resetView(type);

    }

    /**
     * 重置颜色
     */
    private void resetView(int type) {

        switch (type) {
            case 20:
                tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tab2.setVisibility(View.VISIBLE);
                tab4.setVisibility(View.GONE);
                break;
            default:
                tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                tab2.setVisibility(View.GONE);
                tab4.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.id_tab_buy)
    public void buy() {
        type = 20;
        resetView(type);
        switchTab(type);

    }

    private void switchTab(int type) {
        resetView(type);
        if (list != null && list.size() > 0) {
            list.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        showContentData();
        obtainData();
    }

    @OnClick(R.id.id_tab_redeem)
    public void redeem() {
        type = 40;
        resetView(type);
        switchTab(type);
    }

    public int getLayoutId() {
        return R.layout.activity_subscription_records;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("交易记录");

    }

    @Override
    protected String getPageName() {
        return "我的秒钱宝-交易记录";
    }

    /**
     * 无数据
     */
    protected void showEmptyData() {
        mViewnoresult.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_reFresh).setVisibility(View.GONE);
        tvTips.setText("暂时没有数据");
        ivData.setBackgroundResource(R.drawable.nodata);
    }
    /**
     * 显示数据
     */
    protected void showContentData() {
        mViewnoresult.setVisibility(View.GONE);
    }

    /**
     * 获取失败，请重新获取
     */
    protected void showErrorData() {
        mViewnoresult.setVisibility(View.VISIBLE);
        if (MobileOS.getNetworkType(mContext) == -1) {
            tvTips.setText("暂时没有网络");
            ivData.setBackgroundResource(R.drawable.nonetwork);
        } else {
            tvTips.setText("数据获取失败，请重新获取");
            ivData.setBackgroundResource(R.drawable.error_data);
        }
    }
}

