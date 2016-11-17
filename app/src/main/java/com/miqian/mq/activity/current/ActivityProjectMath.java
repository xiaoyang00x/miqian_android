package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.MyCurrentAdapter;
import com.miqian.mq.adapter.ProjectMathAdapter;
import com.miqian.mq.entity.CurrentMathProjectData;
import com.miqian.mq.entity.CurrentMathProjectResult;
import com.miqian.mq.entity.UserCurrentData;
import com.miqian.mq.entity.UserCurrentResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

/**
 * Created by wgl on 2016/11/16.
 * 我的秒钱宝-项目匹配
 */
public class ActivityProjectMath extends BaseActivity implements View.OnClickListener  {

    private MySwipeRefresh swipeRefresh;
    private RecyclerView recyclerView;


    private CurrentMathProjectData currentMathProjectData;
    private ProjectMathAdapter myCurrentAdapter;


    private boolean inProcess = false;
    private final Object mLock = new Object();

    @Override
    public void obtainData() {

        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        swipeRefresh.setRefreshing(true);
        begin();
        HttpRequest.getCurrentProjectMath(mActivity, new ICallback<CurrentMathProjectResult>() {
            @Override
            public void onSucceed(CurrentMathProjectResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                if (result == null || result.getData() == null) {
                    return;
                }
                currentMathProjectData = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                refreshView();
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    private void refreshView() {
        if (currentMathProjectData != null) {
            myCurrentAdapter = new ProjectMathAdapter(mContext, currentMathProjectData);
            recyclerView.setAdapter(myCurrentAdapter);
        }

    }

    private void initListener() {
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        initListener();

    }


    private MySwipeRefresh.OnPullRefreshListener mOnPullRefreshListener = new MySwipeRefresh.OnPullRefreshListener() {
        @Override
        public void onRefresh() {
            obtainData();
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.current_project_match;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("项目匹配");
    }

    @Override
    protected String getPageName() {
        return "项目匹配";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}
