package com.miqian.mq.activity.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.adapter.AdapterMyTicket;
import com.miqian.mq.entity.Page;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.entity.RedPaperData;
import com.miqian.mq.entity.Redpaper;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券列表
 * Created by Administrator on 2015/10/8.
 */
public class MyTicketActivity extends BaseActivity implements View.OnClickListener {

    public List<Promote> promList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterMyTicket adapterMyTicket;

    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;
    private boolean isStop;  //activity被finish则停止从网络获取数据的异步操作

    @Override
    public void onCreate(Bundle arg0) {
        String jpushToken = getIntent().getStringExtra("token");
        if (!TextUtils.isEmpty(jpushToken)) {
            //通知进来的情况下，不是当前用户则退出此界面
            String token = UserUtil.getToken(this);
            if (UserUtil.hasLogin(this) && !token.equals(jpushToken)) {
                isStop = true;
                finish();
            }
        }
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        if (isStop) {
            return;
        }
        pageNo = 1;
        begin();
        HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {

            @Override
            public void onSucceed(RedPaperData result) {
                end();
                Redpaper redpaper = result.getData();
                if (redpaper != null) {
                    promList = redpaper.getCustPromotion();
                    page = redpaper.getPage();
                    if (promList != null && promList.size() > 0) {
                        showContentView();
                        refreshView();
                    } else {
                        showEmptyView();
                    }
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
                showErrorView();
            }
        }, "0", String.valueOf(pageNo), pageSize);

    }

    @Override
    protected void showEmptyView() {
        super.showEmptyView();
        mViewnoresult.findViewById(R.id.tv_tips).setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 30;
        mViewnoresult.addView(getView(), params);
    }

    @Override
    public void initView() {
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
            if (promList.size() >= page.getTotalRows()) {
                return;
            }
            isLoading = true;
            ++pageNo;
            HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {


                @Override
                public void onSucceed(RedPaperData result) {
                    List<Promote> tempList = result.getData().getCustPromotion();
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
            }, "0", String.valueOf(pageNo), pageSize);
        }
    }

    private void refreshView() {
        adapterMyTicket = new AdapterMyTicket(mActivity, promList, true);
        adapterMyTicket.setMaxItem(page.getTotalRows());
        recyclerView.setAdapter(adapterMyTicket);
    }

    private View getView() {
        View view = View.inflate(mActivity, R.layout.adapter_loading, null);
        view.findViewById(R.id.frame_load).setVisibility(View.GONE);
        view.findViewById(R.id.frame_none).setVisibility(View.VISIBLE);
        view.findViewById(R.id.bt_overdue).setOnClickListener(this);
        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_myredpaper;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("红包/卡");
        mTitle.setRightImage(R.drawable.ic_ticket_rule);
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mActivity, "1042");
                WebActivity.startActivity(mActivity, Urls.web_promote);
            }
        });
    }

    @Override
    protected String getPageName() {
        return "红包/卡";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_overdue:
                MyTicketInvalidActivity.startActivity(mActivity);
                break;
        }
    }
}
