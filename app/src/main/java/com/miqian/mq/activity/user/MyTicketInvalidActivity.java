package com.miqian.mq.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterMyTicket;
import com.miqian.mq.entity.Page;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.entity.RedPaperData;
import com.miqian.mq.entity.Redpaper;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * 过往(已过期 已使用 已赠送)优惠券列表
 * Created by Administrator on 2015/10/8.
 */
public class MyTicketInvalidActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    public List<Promote> promList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private LinearLayout frameNone;

    private AdapterMyTicket adapterMyTicket;
    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;
    private String type;
    private String expireCount;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyTicketInvalidActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        type = "2";
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        pageNo = 1;
        begin();
        HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {

            @Override
            public void onSucceed(RedPaperData result) {
                end();
                Redpaper redpaper = result.getData();
                promList = redpaper.getPromList();
                page = redpaper.getPageInfo();
                expireCount = redpaper.getCountInfo() != null ? redpaper.getCountInfo().getExpireCount() : "";
                if (redpaper != null) {
                    if (promList != null && promList.size() > 0) {
                        showContentView();
                        showEmptyView(false);
                        refreshView();
                    } else {
                        showEmptyView(true);
                    }
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
                showErrorView();
            }
        }, type, String.valueOf(pageNo), pageSize);
    }

    @Override
    public void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        frameNone = (LinearLayout) findViewById(R.id.frame_none);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).build());
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void loadMore() {
        if (!isLoading) {
            if (promList.size() >= page.getTotalRecord()) {
                return;
            }
            isLoading = true;
            ++pageNo;
            HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {


                @Override
                public void onSucceed(RedPaperData result) {
                    List<Promote> tempList = result.getData().getPromList();
                    if (promList != null && tempList != null && tempList.size() > 0) {
                        promList.addAll(tempList);
                        adapterMyTicket.notifyItemInserted(promList.size());
                    }
                    isLoading = false;
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;
                    --pageNo;
                }
            }, type, String.valueOf(pageNo), pageSize);
        }
    }


    private void refreshView() {
        if (type.equals("2")) {
            adapterMyTicket = new AdapterMyTicket(mActivity, promList, false, true);
        } else {
            adapterMyTicket = new AdapterMyTicket(mActivity, promList, false);
        }
        adapterMyTicket.setMaxItem(page.getTotalRecord());
        recyclerView.setAdapter(adapterMyTicket);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_myredpaper_invalid;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("红包/卡");
    }

    @Override
    protected String getPageName() {
        return "红包/卡";
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.bt_overdue) {
            type = "2";
        } else if (checkedId == R.id.bt_used) {
            type = "1";
        }
        obtainData();
    }

    protected void showEmptyView(boolean flag) {
        if (flag) {
            recyclerView.setVisibility(View.GONE);
            frameNone.setVisibility(View.VISIBLE);
            TextView textTip = (TextView) frameNone.findViewById(R.id.tv_tips);
            TextView overdueTip = (TextView) frameNone.findViewById(R.id.overdue_tip);
            if (type.equals("2") && !"0".equals(expireCount)) {
                textTip.setVisibility(View.GONE);
                overdueTip.setVisibility(View.VISIBLE);
            } else {
                textTip.setVisibility(View.VISIBLE);
                overdueTip.setVisibility(View.GONE);
            }
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            frameNone.setVisibility(View.GONE);
        }
    }
}
