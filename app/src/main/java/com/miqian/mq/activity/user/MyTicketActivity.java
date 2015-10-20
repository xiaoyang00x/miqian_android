package com.miqian.mq.activity.user;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterMyRedPaper;
import com.miqian.mq.adapter.AdapterMyTicket;
import com.miqian.mq.adapter.AdapterProjectMatch;
import com.miqian.mq.entity.Page;
import com.miqian.mq.entity.ProjectInfo;
import com.miqian.mq.entity.ProjectInfoResult;
import com.miqian.mq.entity.RedPaperData;
import com.miqian.mq.entity.Redpaper;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class MyTicketActivity extends BaseActivity {
    public List<Redpaper.CustPromotion> promList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterMyTicket adapterMyTicket;


    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;


    @Override
    public void obtainData() {
        mWaitingDialog.show();
        HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {


            @Override
            public void onSucceed(RedPaperData result) {
                mWaitingDialog.dismiss();
                Redpaper redpaper = result.getData();
                page = result.getData().getPage();
                if (redpaper != null) {
                    promList = redpaper.getCustPromotion();
                    refreshView();
                }
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);

            }
        }, "SC", "", "1", pageSize);

    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).marginResId(R.dimen.margin_left_right).build());
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void loadMore() {
        if (!isLoading) {
            if (promList.size() >= page.getCount()) {
                return;
            }
            isLoading = true;
            pageNo += 1;
            HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {


                @Override
                public void onSucceed(RedPaperData result) {
                    Redpaper redpaper = result.getData();
                    page = result.getData().getPage();
                    if (redpaper != null) {
                        if (promList != null && promList != null && promList.size() > 0){
                            promList.addAll(redpaper.getCustPromotion());
                            adapterMyTicket.notifyItemInserted(promList.size());
                        }
                        isLoading = false;
                    }
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;

                }
            }, "SC", "", String.valueOf(pageNo), pageSize);
        }
    }




    private void refreshView() {
        adapterMyTicket = new AdapterMyTicket(promList);
        adapterMyTicket.setMaxItem(page.getCount());
        recyclerView.setAdapter(adapterMyTicket);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_myredpaper;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("拾财券");
        mTitle.setRightText("使用规则");
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected String getPageName() {
        return "拾财券";
    }
}
