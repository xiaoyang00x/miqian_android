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
import com.miqian.mq.adapter.CapitalRecordAdapter;
import com.miqian.mq.entity.CapitalItem;
import com.miqian.mq.entity.CapitalRecordResult;
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
 * Created by Administrator on 2017/6/30.
 */

public class UserRecordActivity extends BaseActivity {

    @BindView(R.id.tv_charge)
    TextView tvcharge;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_expired)
    TextView tvExpired;
    @BindView(R.id.tv_redeem)
    TextView tvRedeem;
    @BindView(R.id.tv_withraw)
    TextView tvWithraw;
    @BindView(R.id.id_tab_line_iv1)
    ImageView tab1;
    @BindView(R.id.id_tab_line_iv2)
    ImageView tab2;
    @BindView(R.id.id_tab_line_iv3)
    ImageView tab3;
    @BindView(R.id.id_tab_line_iv4)
    ImageView tab4;
    @BindView(R.id.id_tab_line_iv5)
    ImageView tab5;
    @BindView(R.id.recyclerview_record)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.iv_data)
    ImageView ivData;

    private int type = 10;  //10 提现 ， 20 购买 ，30 到期 ，40 赎回 ，50 提现
    CapitalRecordAdapter adapter;
    List<CapitalItem> list = new ArrayList<>();
    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;

    @Override
    public void obtainData() {
        begin();
        pageNo = 1;
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
                    showEmptyData();
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
                showErrorData();
            }
        }, String.valueOf(pageNo), pageSize, type + "");
    }

    private void refreshView() {
        adapter = new CapitalRecordAdapter(list);
        adapter.setMaxItem(page.getTotalRecord());
        mRecyclerView.setAdapter(adapter);
    }

    private void loadMore() {
        if (!isLoading) {
            if (page == null || list.size() >= page.getTotalRecord()) {
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
            }, String.valueOf(pageNo), pageSize, type + "");

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

    }

    /**
     * 重置颜色
     */
    private void resetView(int type) {

        switch (type) {
            case 10:
                tvcharge.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvExpired.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvWithraw.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tab1.setVisibility(View.VISIBLE);
                tab2.setVisibility(View.GONE);
                tab3.setVisibility(View.GONE);
                tab4.setVisibility(View.GONE);
                tab5.setVisibility(View.GONE);

                break;
            case 20:
                tvcharge.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                tvExpired.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvWithraw.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.VISIBLE);
                tab3.setVisibility(View.GONE);
                tab4.setVisibility(View.GONE);
                tab5.setVisibility(View.GONE);
                break;
            case 30:
                tvcharge.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvExpired.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvWithraw.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);
                tab3.setVisibility(View.VISIBLE);
                tab4.setVisibility(View.GONE);
                tab5.setVisibility(View.GONE);
                break;
            case 40:
                tvcharge.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvExpired.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                tvWithraw.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);
                tab3.setVisibility(View.GONE);
                tab4.setVisibility(View.VISIBLE);
                tab5.setVisibility(View.GONE);
                break;
            case 50:
                tvcharge.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvExpired.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
                tvWithraw.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);
                tab3.setVisibility(View.GONE);
                tab4.setVisibility(View.GONE);
                tab5.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.tv_charge)
    public void charge() {
        type = 10;
        resetView(type);
        switchTab(type);
    }

    @OnClick(R.id.tv_buy)
    public void buy() {
        type = 20;
        resetView(type);
        switchTab(type);

    }

    private void switchTab(int type) {
        resetView(type);
        page = null;
        if (list != null && list.size() > 0) {
            list.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        showContentData();
        obtainData();
    }

    @OnClick(R.id.tv_expired)
    public void expired() {
        type = 30;
        resetView(type);
        switchTab(type);
    }

    @OnClick(R.id.tv_redeem)
    public void redeem() {
        type = 40;
        resetView(type);
        switchTab(type);
    }

    @OnClick(R.id.tv_withraw)
    public void withraw() {
        type = 50;
        resetView(type);
        switchTab(type);
    }

    public int getLayoutId() {
        return R.layout.activity_userrecord;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("资金记录");

    }

    @Override
    protected String getPageName() {
        return "资金记录";
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
        findViewById(R.id.tv_reFresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtainData();
            }
        });
        if (MobileOS.getNetworkType(mContext) == -1) {
            tvTips.setText("暂时没有网络");
            ivData.setBackgroundResource(R.drawable.nonetwork);
        } else {
            tvTips.setText("数据获取失败，请重新获取");
            ivData.setBackgroundResource(R.drawable.error_data);
        }
    }
}
