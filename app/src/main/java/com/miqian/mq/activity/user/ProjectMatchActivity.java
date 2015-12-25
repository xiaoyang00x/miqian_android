package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterProjectMatch;
import com.miqian.mq.entity.Page;
import com.miqian.mq.entity.ProjectInfo;
import com.miqian.mq.entity.ProjectInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * Created by Jackie on 2015/10/15.
 * 项目匹配
 */
public class ProjectMatchActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private int pageNo = 1;
    private String pageSize = "20";
    private String peerCustId = "";//默认不填,(活期赚不填写) 定期计划的匹配项目填:1372

    private List<ProjectInfo> mList;

    private AdapterProjectMatch mAdapter;
    private Page page;

    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("peerCustId"))) {
            peerCustId = intent.getStringExtra("peerCustId");
        } else {
            peerCustId = "";
        }
        super.onCreate(bundle);
    }

    @Override
    protected String getPageName() {
        return "项目匹配";
    }

    @Override
    public void obtainData() {
        pageNo = 1;
        begin();
        HttpRequest.projectMatch(mActivity, new ICallback<ProjectInfoResult>() {
            @Override
            public void onSucceed(ProjectInfoResult result) {
                end();
                mList = result.getData().getMatchsubList();
                if (mList != null && mList.size() > 0) {
                    showContentView();
                    page = result.getData().getPage();
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
        }, String.valueOf(pageNo), pageSize, peerCustId);
    }

    private void loadMore() {
        if (!isLoading) {
            if (mList.size() >= page.getCount()) {
                return;
            }
            isLoading = true;
            pageNo += 1;
            HttpRequest.projectMatch(mActivity, new ICallback<ProjectInfoResult>() {
                @Override
                public void onSucceed(ProjectInfoResult result) {
                    List<ProjectInfo> tempList = result.getData().getMatchsubList();
                    if (mList != null && tempList != null && tempList.size() > 0) {
                        mList.addAll(tempList);
                        mAdapter.notifyItemInserted(mList.size());
                    }
                    isLoading = false;
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;
                }
            }, String.valueOf(pageNo), pageSize, peerCustId);
        }
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
                if (lastVisibleItem >= totalItemCount - 3) {
                    loadMore();
                }
            }
        });
    }

    private void refreshView() {
        mAdapter = new AdapterProjectMatch(mList);
        mAdapter.setMaxItem(page.getCount());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_project_match;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("项目匹配");
    }
}
